package com.pinguo.camera360.gl.programs

import android.content.Context
import android.opengl.GLES20.glUseProgram
import com.pinguo.camera360.gl.helper.ShaderHelper
import com.pinguo.camera360.gl.util.TextResourceReader

open class ShaderProgram {
    companion object {
        const val U_MATRIX = "u_Matrix"
        const val U_TEXTURE_UNIT = "u_TextureUnit"
        const val A_POSITION = "a_Position"
        const val U_COLOR = "u_Color"
        const val A_COLOR = "a_Color"
        const val A_TEXTURE_COORDINATES = "a_TextureCoordinates"
        const val A_DIRECTION_VECTOR = "a_DirectionVector"
        const val U_TIME = "u_Time"
        const val A_PARTICLE_START_TIME = "a_ParticleStartTime"
    }

    open var program: Int

    constructor(context: Context, vertexShaderResourceId: Int, fragmentShaderResourceId: Int) {
        program = ShaderHelper.buildProgram(TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId), TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId))
    }

    fun useProgram() {
        glUseProgram(program)
    }

}