package com.pinguo.camera360

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class OpenGLActivity : AppCompatActivity() {

    private var glSurfaceView:GLSurfaceView? = null
    private var renderSet = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glSurfaceView = GLSurfaceView(this)
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportES2 = configurationInfo.reqGlEsVersion >= 0x20000
        Log.i("OpenGL","GLES version:${configurationInfo.glEsVersion} $supportES2")
        if(supportES2){
            glSurfaceView!!.setEGLContextClientVersion(2)
            glSurfaceView?.setRenderer(FirstOpenGLProjectRender())
        }
        setContentView(glSurfaceView)
    }

    override fun onPause() {
        super.onPause()
        if (renderSet){
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
