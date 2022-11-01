package com.example.ugd89_d_10932_project1

import android.content.Context
import android.hardware.Camera
import android.hardware.camera2.CameraDevice
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

class CameraView(context: Context?, private val mCamera: Camera): SurfaceView(context), SurfaceHolder.Callback {
    private val mHolder: SurfaceHolder

    init {
        mCamera.setDisplayOrientation(90)
        mHolder = holder
        mHolder.addCallback(this)
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        try {
            mCamera.setPreviewDisplay(p0)
            mCamera.startPreview()
        } catch (e: Exception) {
            Log.d("CameraView", "Camera error on SurfaceCreated: ${e.message}")
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        if (mHolder.surface == null) {
            return
        }

        try {
            mCamera.stopPreview()
        } catch (e: Exception) {
            Log.d("CameraView", "Camera error on SurfaceChanged: ${e.message}")
        }

        try {
            mCamera.setPreviewDisplay(mHolder)
            mCamera.startPreview()
        } catch (e: Exception) {
            Log.d("CameraView", "Camera error on SurfaceChanged: ${e.message}")
        }
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        mCamera.stopPreview()
        mCamera.release()
    }
}