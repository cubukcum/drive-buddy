package com.os.cvCamera


import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.TypedValue
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.get
import com.os.cvCamera.BuildConfig.GIT_HASH
import com.os.cvCamera.BuildConfig.VERSION_NAME
import com.os.cvCamera.databinding.ActivityMainBinding
import
import org.opencv.android.CameraActivity
import org.opencv.android.CameraBridgeViewBase.CAMERA_ID_BACK
import org.opencv.android.CameraBridgeViewBase.CAMERA_ID_FRONT
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
import org.opencv.android.OpenCVLoader.OPENCV_VERSION
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import timber.log.Timber
import org.apache.commons.math3.fitting.PolynomialCurveFitter
import org.apache.commons.math3.fitting.WeightedObservedPoints


class MainActivity : CameraActivity(), CvCameraViewListener2 {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mRGBA: Mat
    private lateinit var mRGBAT: Mat
    private var mCameraId: Int = CAMERA_ID_BACK
    private var mTorchCameraId: String = ""
    private var mTorchState = false
    private lateinit var mCameraManager: CameraManager

    // Filters id
    private var mFilterId = -1


    companion object {
        init {
            System.loadLibrary("opencv_java4")
            System.loadLibrary("cvcamera")
        }
    }

    private external fun openCVVersion(): String?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("OpenCV Version: $OPENCV_VERSION")


        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mCameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        //
        loadOpenCVConfigs()

        // Find the flashlight
        findFlashLight()

        // Load buttonConfigs
        configButtons()

        // Load button colors
        setButtonColors()

    }

    private fun setButtonColors() {
        for (i in 0..<binding.bottomAppBar.menu.size()) {
            val item = binding.bottomAppBar.menu[i]
            val typedValue = TypedValue()
            theme.resolveAttribute(android.R.attr.colorAccent, typedValue, true)
            item.icon?.colorFilter =
                PorterDuffColorFilter(typedValue.data, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun configButtons() {
        binding.cvCameraChangeFab.setOnClickListener {
            cameraSwitch()
        }

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.about -> {
                    // Get app version and githash from BuildConfig
                    val cvVer = openCVVersion() // Get OpenCV version from native code
                    val toast: Toast = Toast.makeText(
                        this,
                        "CvCamera-Mobile - Version $VERSION_NAME-$GIT_HASH - OpenCV $cvVer ",
                        Toast.LENGTH_SHORT,
                    )
                    toast.show()

                    true
                }

                R.id.filters -> {
                    // Toggle between grayscale,toSepia,toPencilSketch,toSobel,toCanny
                    mFilterId = when (mFilterId) {
                        -1 -> {
                            Toast.makeText(this, getString(R.string.grayscale_filter), Toast.LENGTH_SHORT).show()
                            0
                        }

                        0 -> {
                            Toast.makeText(this, getString(R.string.sepia_filter), Toast.LENGTH_SHORT).show()
                            1
                        }

                        1 -> {
                            Toast.makeText(this, getString(R.string.sobel_filter), Toast.LENGTH_SHORT).show()
                            2
                        }

                        2 -> {
                            Toast.makeText(this, getString(R.string.canny_filter), Toast.LENGTH_SHORT).show()
                            3
                        }

                        3 -> {
                            -1
                        }

                        else -> {
                            -1
                        }
                    }


                    true
                }

                R.id.resizeCanvas -> {
                    binding.CvCamera.disableView()
                    binding.CvCamera.setFitToCanvas(!binding.CvCamera.getFitToCanvas())
                    binding.CvCamera.enableView()
                    true
                }

                else -> {
                    false
                }
            }

        }
    }

    private fun cameraSwitch() {
        mCameraId = if (mCameraId == CAMERA_ID_BACK) {
            CAMERA_ID_FRONT
        } else {
            CAMERA_ID_BACK
        }

        binding.CvCamera.disableView()
        binding.CvCamera.setCameraIndex(mCameraId)
        binding.CvCamera.enableView()

    }


    private fun loadOpenCVConfigs() {
        binding.CvCamera.setCameraIndex(mCameraId)
        binding.CvCamera.setCvCameraViewListener(this)
        binding.CvCamera.setCameraPermissionGranted()
        Timber.d("OpenCV Camera Loaded")
        binding.CvCamera.enableView()
        binding.CvCamera.getCameraDevice()
    }


    private fun enableFlashLight() {
        mTorchState = true
        mCameraManager.setTorchMode(mTorchCameraId, true)
        Timber.d("Torch is on")
    }

    private fun findFlashLight() {
        for (cameraId in mCameraManager.cameraIdList) {
            try {
                // Check if the camera has a torchlight
                val hasTorch = mCameraManager.getCameraCharacteristics(cameraId)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) ?: false

                if (hasTorch) {
                    // Find the ID of the camera that has a torchlight and store it in mTorchCameraId
                    Timber.d("Torch is available")
                    Timber.d("Camera Id: $cameraId")
                    mTorchCameraId = cameraId
                    mTorchState = false
                    break
                } else {
                    Timber.d("Torch is not available")
                }
            } catch (e: CameraAccessException) {
                // Handle any errors that occur while trying to access the camera
                Timber.e("CameraAccessException ${e.message}")
            }
        }
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mRGBA = Mat(height, width, CvType.CV_8UC4)
        mRGBAT = Mat()
    }

    override fun onCameraViewStopped() {
        mRGBA.release()
        mRGBAT.release()
    }

    override fun onCameraFrame(inputFrame: CvCameraViewFrame?): Mat {
        return if (inputFrame != null) {
            val frame = inputFrame.rgba()
            val filteredFrame = cvFilters(frame)
            frame.release()
            filteredFrame
        } else {
            // return last or empty frame
            mRGBA
        }
    }


    private fun cvFilters(frame: Mat): Mat {
//        return when (mFilterId) {
//            0 -> {
//                frame.getSlice(frame.getEdges())
//            }
//
//            1 -> {
//                frame.getSlice(frame.getEdges())            }
//
//            2 -> {
//                frame.getSlice(frame.getEdges())            }
//
//            3 -> {
//                frame.getSlice(frame.getEdges())            }
//
//            else -> frame
//        }
        if (mCameraId == CAMERA_ID_FRONT) {
            Core.flip(frame, frame, -1)
        }
        Timber.i("Size of input frame: ${frame.width()}x${frame.height()}")

        // Get edges
        val canny = getEdges(frame)
        Timber.i("Size of edges: ${canny.width()}x${canny.height()}")

        // Get slice
        val slice = getSlice(canny)
        Timber.i("Size of slice: ${slice.width()}x${slice.height()}")

        // Get lines
        val lines = getLines(slice)

        // Visualize
        val visualized = visualize(frame, lines)
        Timber.i("Size of visualized: ${visualized.width()}x${visualized.height()}")

        return visualized

    }

    override fun onDestroy() {
        Timber.d("onDestroy")
        super.onDestroy()
        binding.CvCamera.disableView()
    }

    override fun onPause() {
        Timber.d("onPause")
        super.onPause()
        binding.CvCamera.disableView()
    }

    override fun onResume() {
        Timber.d("onResume")
        super.onResume()
    }
    private fun getEdges(source: Mat): Mat {
        val gray = Mat()
//        Imgproc.cvtColor(source, gray, Imgproc.COLOR_RGB2GRAY)
        Imgproc.cvtColor(source, gray, Imgproc.COLOR_BGR2GRAY)

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
//            MatOfPoint(
//                Point(400.0, 300.0),   //
//                Point(400.0, 600.0),
//                Point(height, 750.0), //
//                Point(height, 150.0)       //
//            )
//        )
//            MatOfPoint(
//                Point(width * 0.0, height * 1.0), // bottom left
//                Point(width * 0.4, height * 0.4),  // top left
//                Point(width * 0.6, height * 0.4),  // top right
//                Point(width * 0.8, height * 1.0)  // bottom right
//            )
            MatOfPoint(

                Point(width * 0.4, height * 0.4),  // top left
                Point(width * 0.6, height * 0.4),  // top right
                Point(width * 0.8, height * 1.0) , // bottom right
                Point(width * 0.0, height * 1.0) // bottom left
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
        Imgproc.HoughLinesP(source, lines,1.0, Math.PI/180, 20, 20.0, 500.0)

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
        println(left.coordinates)
        println(right.coordinates)

        return Pair(left, right)
    }

    private fun visualize(source: Mat, lines: Pair<HoughLine, HoughLine>): Mat {
        println(source.size())
        println("printed size of source")
        val grey = Mat.zeros(source.rows(), source.cols(), 0)
        println("printed size of dest")
//        Imgproc.cvtColor(grey, dest, Imgproc.COLOR_GRAY2RGB)
        val dest = Mat(source.rows(), source.cols(), CvType.CV_8UC4)


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
        Core.addWeighted(source, 0.5, dest, 1.0, 1.0, done)


        return done
    }
}
