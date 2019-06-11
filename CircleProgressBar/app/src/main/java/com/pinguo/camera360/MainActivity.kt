package com.pinguo.camera360

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import us.pinguo.foundation.utils.Util

class MainActivity : AppCompatActivity() ,View.OnClickListener{

    override fun onClick(v: View?) {
        val intent = Intent()
        when(v?.id){
            R.id.camerax->{
                intent.setClass(this, CameraXActivity::class.java)
            }
            R.id.widgets -> {
                intent.setClass(this, WidgetsActivity::class.java)
            }
            R.id.jni -> {
                intent.setClass(this, JNIActivity::class.java)
            }
            R.id.opengl -> {
                intent.setClass(this, OpenGLActivity::class.java)
            }
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        Util.initialize(this)
        camerax.setOnClickListener(this)
        widgets.setOnClickListener(this)
        jni.setOnClickListener(this)
        opengl.setOnClickListener(this)
    }


}
