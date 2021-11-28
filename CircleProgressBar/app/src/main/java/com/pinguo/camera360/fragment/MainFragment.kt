package com.pinguo.camera360.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.pinguo.camera360.CameraXActivity
import com.pinguo.camera360.JNIActivity
import com.pinguo.camera360.R
import com.pinguo.camera360.WidgetsActivity
import com.pinguo.camera360.gl.AirHockeyActivity
import com.pinguo.camera360.gl.ParticlesActivity
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment/*(R.layout.fragment_main)*/, View.OnClickListener {
    private var constructorString:String = ""

    private lateinit var mainViewModel:MainViewModel
    constructor(){
        this.constructorString = "default"
    }
    constructor( constructorString:String){
        this.constructorString = constructorString
    }
    // TODO: Rename and change types of parameters
    private var bundleString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bundleString = it.getString(BUNDLE_KEY)
        }

        mainViewModel = ViewModelProvider(
            viewModelStore, MainViewModelFactory(requireActivity().application!!)
        )[MainViewModel::class.java]

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main,container,false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        camerax.setOnClickListener(this)
        widgets.setOnClickListener(this)
        jni.setOnClickListener(this)
        opengl.setOnClickListener(this)
        particles.setOnClickListener(this)
        fragmentBack.text = "${bundleString}-${constructorString}"
        viewLifecycleOwner.lifecycle.addObserver(mainViewModel)
    }

    override fun onClick(v: View?) {
        val intent = Intent()
        val activity = activity ?: return
        when (v?.id) {
            R.id.camerax -> {
                intent.setClass(activity, CameraXActivity::class.java)
            }
            R.id.widgets -> {
                intent.setClass(activity, WidgetsActivity::class.java)
            }
            R.id.jni -> {
                intent.setClass(activity, JNIActivity::class.java)
            }
            R.id.opengl -> {
                intent.setClass(activity, AirHockeyActivity::class.java)
            }
            R.id.particles -> {
                intent.setClass(activity, ParticlesActivity::class.java)
            }
        }
        startActivity(intent)
    }

    companion object {
        const val BUNDLE_KEY = "bundle_key"
    }
}