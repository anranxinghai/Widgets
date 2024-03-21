package com.pinguo.camera360

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.pinguo.camera360.fragment.MainFragment
import com.pinguo.camera360.fragment.MainFragment.Companion.BUNDLE_KEY
import com.pinguo.camera360.fragment.MainFragmentFactory
import com.pinguo.camera360.fragment.MainFragmentRepository
import com.pinguo.camera360.gl.AirHockeyActivity
import com.pinguo.camera360.gl.ParticlesActivity
import us.pinguo.foundation.utils.Util

class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        val mainFragmentRepository = MainFragmentRepository()
        val fm = supportFragmentManager
        fm.fragmentFactory = MainFragmentFactory(mainFragmentRepository)
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        Util.initialize(this)
        if (savedInstanceState == null) {
             val bundle = bundleOf(BUNDLE_KEY to "MainFragment")
             fm.commit {
                 setReorderingAllowed(true)
                 add<MainFragment>(R.id.fragment_container_view,args = bundle)
             }

            val fragment = MainFragment(/*mainFragmentRepository.constructorString*/)
            fragment.arguments = bundle
            fm.beginTransaction().add(R.id.fragment_container_view,fragment,"null").commit()

            /*val mainFragmentRepository = MainFragmentRepository()
            val fm = supportFragmentManager
            val bundle = bundleOf(BUNDLE_KEY to "MainFragment")
            val fragment = MainFragment(mainFragmentRepository.constructorString)
            fragment.arguments = bundle
            fm.beginTransaction().add(R.id.fragment_container_view,fragment,"null").commit()*/
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        /*val mainFragmentRepository = MainFragmentRepository()
        val fm = supportFragmentManager
        val bundle = bundleOf(BUNDLE_KEY to "MainFragment")
        val fragment = MainFragment(mainFragmentRepository.constructorString)
        fragment.arguments = bundle
        fm.beginTransaction().add(R.id.fragment_container_view,fragment,"null").commitAllowingStateLoss()*/
    }

    override fun onClick(v: View?) {
        val intent = Intent()
        when (v?.id) {
            R.id.camerax -> {
                intent.setClass(this, CameraXActivity::class.java)
            }
            R.id.widgets -> {
                intent.setClass(this, WidgetsActivity::class.java)
            }
            R.id.jni -> {
                intent.setClass(this, JNIActivity::class.java)
            }
            R.id.opengl -> {
                intent.setClass(this, AirHockeyActivity::class.java)
            }
            R.id.surfaceView -> {
                intent.setClass(this, SurfaceViewActivity::class.java)
            }
            R.id.textureView -> {
                intent.setClass(this, TextureViewActivity::class.java)
            }
        }
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }


}
