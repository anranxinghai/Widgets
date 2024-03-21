package com.pinguo.camera360

import android.app.Activity
import android.hardware.Camera
import android.os.Bundle
import android.view.TextureView
import android.graphics.SurfaceTexture

class TextureViewActivity : Activity(),TextureView.SurfaceTextureListener {

    private var textureView: TextureView? = null
    private var camera: Camera? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_texture_view)
        textureView = findViewById(R.id.textureView)
        textureView?.surfaceTextureListener = this
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        camera = Camera.open(0)
        camera?.setPreviewTexture(surface)
        camera?.startPreview()
    }
    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
    }
    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        camera?.stopPreview()
        camera?.release()
        return true
    }


    override fun onResume() {
        super.onResume()
        camera?.startPreview()
    }

    override fun onPause() {
        super.onPause()
        camera?.stopPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}