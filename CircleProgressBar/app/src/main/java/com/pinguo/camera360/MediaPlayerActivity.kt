package com.pinguo.camera360

import android.app.Activity
import android.hardware.Camera
import android.os.Bundle
import android.view.TextureView
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.view.Surface

class MediaPlayerActivity : Activity(),TextureView.SurfaceTextureListener {

    private var textureView: TextureView? = null
    private var camera: Camera? = null
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_texture_view)
        textureView = findViewById(R.id.textureView)
        textureView?.surfaceTextureListener = this
        mediaPlayer = MediaPlayer()
        val assetFileDescriptor = assets.openFd("Fire.mp4")
        mediaPlayer?.setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset,assetFileDescriptor.length)

    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        mediaPlayer?.setSurface(Surface(textureView?.surfaceTexture))
        mediaPlayer?.prepare()
        mediaPlayer?.start()
    }
    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
    }
    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        return true
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