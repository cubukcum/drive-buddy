package com.example.drive_buddy

object Constants {
    const val MODEL_PATH = "vehicle_detection.tflite"
    const val LABELS_PATH = "vehicle_label.txt"
    const val WEAPON_MODEL_PATH = "weapon_detection.tflite"
    const val WEAPON_LABELS_PATH = "weapon_label.txt"
    const val TRAFFIC_MODEL_PATH = "sign_detection.tflite"
    const val TRAFFIC_LABELS_PATH = "sign_label.txt"
    const val DROWSINESS_MODEL_PATH = "best_int8.tflite"
    const val DROWSINESS_LABELS_PATH = "drowsiness_label.txt"
//    const val DROWSINESS_MODEL_PATH = "mobilenet_v1_1.0_224_quant.tflite"
//    const val DROWSINESS_LABELS_PATH = "labels_mobilenet_quant_v1_224.txt"

    const val APPLICATION_ID = "com.os.cvCamera"
    const val BUILD_TYPE = "debug"
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0.1"

    // Field from default config.
    const val GIT_HASH = "a76d351"

}