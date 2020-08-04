package com.pinguo.camera360.gl.programs

import android.content.Context
import android.opengl.GLES20.*
import com.pinguo.camera360.R

class ColorShaderProgram : ShaderProgram {
    private var uMatrixLocation = 0
    private var aPositionLocation = 0
    private var aColorLocation: Int = 0

    constructor(context: Context) : super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader) {
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        aColorLocation = glGetAttribLocation(program, A_COLOR)

    }

    fun setUniforms(matrix: FloatArray) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getColorAttributeLocation(): Int {
        return aColorLocation
    }
}