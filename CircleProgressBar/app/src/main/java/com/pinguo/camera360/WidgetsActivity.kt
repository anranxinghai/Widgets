package com.pinguo.camera360

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.pinguo.camera360.camera.view.focusView.PGSeekBar
import kotlinx.android.synthetic.main.activity_widgets.*

class WidgetsActivity : AppCompatActivity() ,View.OnClickListener{
    var isExtended = false
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.dot->{
                dot.setExtended(isExtended)
                isExtended = !isExtended
            }
            R.id.fvSquare->{
                fvSquare.startDrawAnimation()
            }
            R.id.fvCircle->{
                fvCircle.startDrawAnimation()
            }
            R.id.fvRect->{
                fvRect.startDrawAnimation()
            }
            R.id.rvImage->{
                rvImage.startReveal()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widgets)
        Log.i("MainActivity","hello.visiblity:${hello.visibility}")
        seekBar.setOnSeekBarChangeListener(object : PGSeekBar.OnSeekBarChangeListener{

            override fun onSeekRateChanged(seekBar: PGSeekBar, process: Float) {
                circleProgressBar.setProgress((process * 100).toInt())
            }

            override fun onActionUp() {

            }

        })
        dot.setOnClickListener(this)
        fvSquare.setOnClickListener(this)
        fvCircle.setOnClickListener(this)
        fvRect.setOnClickListener(this)
        rvImage.setOnClickListener(this)
        rvImage.setBitmap(BitmapFactory.decodeResource(resources, R.drawable.input))
        distanceSeekBar.setOnSeekBarChangeListener(object : PGSeekBar.OnSeekBarChangeListener{

            override fun onSeekRateChanged(seekBar: PGSeekBar, rate: Float) {
                fvBlur.setScale(rate)
                fvBlur.updatePosition(200*rate,200*rate)
            }

            override fun onActionUp() {

            }

        })
        degreeSeekBar.setOnSeekBarChangeListener(object : PGSeekBar.OnSeekBarChangeListener{

            override fun onSeekRateChanged(seekBar: PGSeekBar, rate: Float) {
                fvBlur.setRotate(rate*360)
            }

            override fun onActionUp() {

            }

        })
    }
}
