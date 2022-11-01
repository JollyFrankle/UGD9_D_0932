package com.example.ugd89_d_10932_project1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null
    private var camFacing = 0
    private lateinit var frameCamera: FrameLayout
    lateinit var proximitySensor: Sensor
    lateinit var sensorManager: SensorManager

    var proximitySensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // TODO Auto-generated method stub
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (event!!.values[0] < proximitySensor.maximumRange) {
                mCamera?.release()
                if(camFacing == 0) {
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
                    camFacing = 1
                } else {
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
                    camFacing = 0
                }

                mCameraView = CameraView(this@MainActivity, mCamera!!)
                frameCamera.removeAllViews()
                frameCamera.addView(mCameraView)
            }
//            else {
//                sensorStatusTV.text = "Far"
//            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Camera
        try {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
        } catch (e: Exception) {
            Log.d("MainActivity", "Camera error on open: ${e.message}")
            // alert builder
            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Camera error on open: ${e.message}")
                .setPositiveButton("OK") { dialog, which ->
                    finish()
                }
                .show()
        }

        if (mCamera != null) {
            mCameraView = CameraView(this, mCamera!!)
//            setContentView(mCameraView)
            frameCamera = findViewById(R.id.frameCamera)
            frameCamera.addView(mCameraView)
        }

        val imageClose = findViewById<ImageButton>(R.id.btnClose)
        imageClose.setOnClickListener {
            finish()
            // system exit
            System.exit(0)
        }

        // Proximity Sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor == null) {
            // on below line we are displaying a toast if no sensor is available
            Toast.makeText(this, "No proximity sensor found in device", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            // on below line we are registering
            // our sensor with sensor manager
            sensorManager.registerListener(
                proximitySensorEventListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }
}