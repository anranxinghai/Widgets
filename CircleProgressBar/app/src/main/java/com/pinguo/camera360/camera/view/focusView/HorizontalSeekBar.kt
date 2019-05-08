package com.pinguo.camera360.camera.view.focusView

import android.content.Context
import android.util.AttributeSet

class HorizontalSeekBar : PGSeekBar {
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){
        setIsVertial(false)
    }

}