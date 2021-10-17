package com.pinguo.camera360.gl

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pinguo.camera360.R
import com.pinguo.camera360.gl.render.ParticlesRender

class ParticlesActivity : AppCompatActivity() {

    private var glSurfaceView: GLSurfaceView? = null
    private var renderSet = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val airHockeyRender = ParticlesRender(this)
        glSurfaceView = GLSurfaceView(this)
        glSurfaceView?.id = R.id.gl_surface_view

        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportES2 = configurationInfo.reqGlEsVersion >= 0x20000
        Log.i("OpenGL", "GLES version:${configurationInfo.glEsVersion} $supportES2")
        if (supportES2) {
            glSurfaceView?.setEGLContextClientVersion(2)
            glSurfaceView?.setRenderer(airHockeyRender)
            renderSet = true
        }
        //ContentView 整个设置为 glSurfaceView
        setContentView(glSurfaceView)
    }

    override fun onPause() {
        super.onPause()
        if (renderSet) {
            glSurfaceView?.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (renderSet) {
            glSurfaceView?.onResume()
        }
    }

}
