package com.example.drive_buddy

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.drive_buddy.databinding.ActivityLaneDetectionBinding
import org.apache.commons.math3.fitting.PolynomialCurveFitter
import org.apache.commons.math3.fitting.WeightedObservedPoints
import org.opencv.android.OpenCVLoader
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LaneDetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaneDetectionBinding
    private val isFrontCamera = false

    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        OpenCVLoader.initDebug()
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
        super.onCreate(savedInstanceState)
        binding = ActivityLaneDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider  = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {
        val cameraProvider = cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        val rotation = binding.viewFinder.display.rotation

        val cameraSelector = CameraSelector
            .Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview =  Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(rotation)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()

        imageAnalyzer?.setAnalyzer(cameraExecutor) { imageProxy ->
            val bitmapBuffer =
                Bitmap.createBitmap(
                    imageProxy.width,
                    imageProxy.height,
                    Bitmap.Config.ARGB_8888
                )
            imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer) }
            imageProxy.close()

            val matrix = Matrix().apply {
                postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())

                if (isFrontCamera) {
                    postScale(
                        -1f,
                        1f,
                        imageProxy.width.toFloat(),
                        imageProxy.height.toFloat()
                    )
                }
            }

            val rotatedBitmap = Bitmap.createBitmap(
                bitmapBuffer, 0, 0, bitmapBuffer.width, bitmapBuffer.height,
                matrix, true
            )

            // Apply OpenCV processing here
            val mat = Mat()

            val canny = getEdges(mat)
            val slice = getSlice(canny)
            val lines = getLines(slice)
            val visualized = visualize(mat, lines)

            // Convert the visualized Mat back to Bitmap
            val processedBitmap = Bitmap.createBitmap(visualized.cols(), visualized.rows(), Bitmap.Config.ARGB_8888)

            // Now you can use the processedBitmap for display or further processing
        }


        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalyzer
            )

            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        } catch(exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {
        if (it[Manifest.permission.CAMERA] == true) { startCamera() }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onResume() {
        super.onResume()
        if (allPermissionsGranted()){
            startCamera()
        } else {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    companion object {
        private const val TAG = "Camera"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf (
            Manifest.permission.CAMERA
        ).toTypedArray()
    }




    //below ---> opencv part
    private fun getEdges(source: Mat): Mat {
        val gray = Mat()
        Imgproc.cvtColor(source, gray, Imgproc.COLOR_RGB2GRAY)

        val blur = Mat()
        Imgproc.GaussianBlur(gray, blur, Size(5.0, 5.0), 0.0)

        val dest = Mat()
        Imgproc.Canny(blur, dest, 50.0, 150.0)

        return dest
    }

    private fun getSlice(source: Mat): Mat {
        val height = source.height().toDouble()
        val width = source.width().toDouble()

        val polygons: List<MatOfPoint> = listOf(
            MatOfPoint(
                Point(175.0, height),   // bottom left
                Point(450.0, 400.0), // top left
                Point(900.0, 400.0), // top right
                Point(width, height)       // bottom right
            )
        )

        val mask = Mat.zeros(source.rows(), source.cols(), 0)
        Imgproc.fillPoly(mask, polygons, Scalar(255.0))

        val dest = Mat()
        Core.bitwise_and(source, mask, dest)

        return dest
    }

    private fun getLines(source: Mat): Pair<HoughLine, HoughLine> {
        val lines = Mat()
        Imgproc.HoughLinesP(source, lines, 2.0, Math.PI / 180, 100, 100.0, 50.0)

        val left = HoughLine(source)
        val right = HoughLine(source)

        for (row in 0 until lines.rows()) {
            val points: DoubleArray = lines.get(row, 0)
            val weighted = WeightedObservedPoints()
            val fitter = PolynomialCurveFitter.create(1)

            weighted.add(points[0], points[1])
            weighted.add(points[2], points[3])

            val fitted = fitter.fit(weighted.toList())
            val slope = fitted[1]

            if (slope < 0) {
                left.add(fitted)
            } else {
                right.add(fitted)
            }
        }

        return Pair(left, right)
    }

    private fun visualize(source: Mat, lines: Pair<HoughLine, HoughLine>): Mat {
        val grey = Mat.zeros(source.rows(), source.cols(), 0)
        val dest = Mat()
        Imgproc.cvtColor(grey, dest, Imgproc.COLOR_GRAY2RGB)

        val color = Scalar(0.0, 255.0, 0.0)
        Imgproc.line(
            dest,
            lines.first.coordinates.first,
            lines.first.coordinates.second,
            color,
            Imgproc.LINE_8
        )
        Imgproc.line(
            dest,
            lines.second.coordinates.first,
            lines.second.coordinates.second,
            color,
            Imgproc.LINE_8
        )

        val done = Mat()
        Core.addWeighted(source, 0.9, dest, 1.0, 1.0, done)

        return done
    }


}