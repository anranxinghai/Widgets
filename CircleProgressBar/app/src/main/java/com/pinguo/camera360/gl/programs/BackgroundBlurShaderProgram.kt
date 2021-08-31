package com.pinguo.camera360.gl.programs

import android.content.Context
import android.opengl.GLES20.*
import com.pinguo.camera360.R

class BackgroundBlurShaderProgram : ShaderProgram {

    companion object {
        private const val VET4_SIZE = 1

        private const val NORMALIZED_TRANSFORM_CENTER_POINT = "_NormalizedTransformCenterPoint"
        private const val MAIN_TEX = "_MainTex"
        private const val SHAPE_TEX = "_ShapeTex"
        private const val IMAGE_SIZE = "_ImageSize"
        private const val SHAPE_SIZE = "_ShapeSize"
        private const val BOKEH_MASK_TEX = "_BokehMaskTex"
        private const val BOKEH_MASK_EXTERN_TEX = "_BokehMaskExternTex"
        private const val HALO = "_Halo"
        private const val SWIRLY = "_Swirly"
        private const val RADIAL = "_Radial"
        private const val CORROSION = "_Corrosion"
        private const val HIGHLIGHT = "_HighLight"
        private const val VIVID = "_Vivid"
        private const val COLOR_SHIFT = "_ColorShift"
    }

    private var normalizedTransformCenterPointPosition = 0
    private var mainTexPosition = 0
    private var shapeTexPosition = 0
    private var imageSizePosition = 0
    private var shapeSizePosition = 0
    private var bokehMaskTexPosition = 0
    private var bokehMaskExternTexPosition = 0
    private var haloPosition = 0
    private var swirlyPosition = 0
    private var radialPosition = 0
    private var corrosionPosition = 0
    private var highLightPosition = 0
    private var vividPosition = 0
    private var colorShiftPosition = 0
    private var uMatrixLocation = 0
    private var uTextureUnitLocation = 0
    private var aPositionLocation = 0
    private var aTextureCoordinatesLocation:Int = 0
    constructor(context: Context) : super(
        context,
        R.raw.background_blur_vertex_shader,
        R.raw.background_blur_fragment_shader
    ) {
        normalizedTransformCenterPointPosition = glGetUniformLocation(program, NORMALIZED_TRANSFORM_CENTER_POINT)
        mainTexPosition = glGetUniformLocation(program, MAIN_TEX)
        shapeTexPosition = glGetUniformLocation(program, SHAPE_TEX)
        imageSizePosition = glGetUniformLocation(program, IMAGE_SIZE)
        shapeSizePosition = glGetUniformLocation(program, SHAPE_SIZE)
        bokehMaskTexPosition = glGetUniformLocation(program, BOKEH_MASK_TEX)
        bokehMaskExternTexPosition = glGetUniformLocation(program, BOKEH_MASK_EXTERN_TEX)

        haloPosition = glGetUniformLocation(program, HALO)
        swirlyPosition = glGetUniformLocation(program, SWIRLY)
        radialPosition = glGetUniformLocation(program, RADIAL)
        corrosionPosition = glGetUniformLocation(program, CORROSION)
        highLightPosition = glGetUniformLocation(program, HIGHLIGHT)
        vividPosition = glGetUniformLocation(program, VIVID)
        colorShiftPosition = glGetUniformLocation(program, COLOR_SHIFT)

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT)
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES)
    }

    fun setUniforms(
        normalizedTransformCenterPoint: FloatArray,
        mainTexId: Int,
        shapeTexId: Int,
        mainSize: FloatArray,
        shapeSize: FloatArray,
        bokehMaskTexId: Int,
        bokehMaskExternTexId: Int,
        halo: Float = 1.0f,
        swirly: Float = 1.0f,
        radial: Float = 1.0f,
        corrosion: Float = 1.0f,
        highLight:Float = 1.0f,
        vivid:Float = 1.0f,
        colorShift:Float = 1.0f
    ) {
        glUniform4fv(normalizedTransformCenterPointPosition,VET4_SIZE,normalizedTransformCenterPoint,0)

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, mainTexId)
        glUniform1i(mainTexPosition,0)
        glUniform4fv(imageSizePosition,VET4_SIZE,mainSize,0)

        glActiveTexture(GL_TEXTURE1)
        glBindTexture(GL_TEXTURE_2D, shapeTexId)
        glUniform1i(shapeTexPosition,1)
        glUniform4fv(shapeSizePosition,VET4_SIZE,shapeSize,0)

        glActiveTexture(GL_TEXTURE2)
        glBindTexture(GL_TEXTURE_2D, bokehMaskTexId)
        glUniform1i(bokehMaskTexPosition,2)

        glActiveTexture(GL_TEXTURE3)
        glBindTexture(GL_TEXTURE_2D, bokehMaskExternTexId)
        glUniform1i(bokehMaskExternTexPosition,3)



        glUniform1f(haloPosition,halo)
        glUniform1f(swirlyPosition,swirly)
        glUniform1f(radialPosition,radial)
        glUniform1f(corrosionPosition,corrosion)
        glUniform1f(highLightPosition,highLight)
        glUniform1f(vividPosition,vivid)
        glUniform1f(colorShiftPosition,colorShift)

    }
    fun getPositionAttributeLocation():Int{
        return aPositionLocation
    }

    fun getTextureCoordinatesAttributeLocation():Int{
        return aTextureCoordinatesLocation
    }
}