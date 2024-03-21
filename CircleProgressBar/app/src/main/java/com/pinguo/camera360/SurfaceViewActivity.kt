package com.pinguo.camera360

import android.app.Activity
import android.hardware.Camera
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView

class SurfaceViewActivity : Activity(),SurfaceHolder.Callback {

    private var surfaceView: SurfaceView? = null
    private var camera: Camera? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surface_view)
        surfaceView = findViewById(R.id.surfaceView)
        val holder = surfaceView?.holder
        holder?.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        camera = Camera.open(0)
        camera?.setPreviewDisplay(holder)
        camera?.startPreview()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        camera?.release()

    }
    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        camera?.stopPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}