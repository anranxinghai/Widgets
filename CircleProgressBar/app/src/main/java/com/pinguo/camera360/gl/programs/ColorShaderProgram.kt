package com.pinguo.camera360.gl.programs

import android.content.Context
import android.opengl.GLES20.*
import com.pinguo.camera360.R

class ColorShaderProgram : ShaderProgram {
    private var uMatrixLocation = 0
    private var aPositionLocation = 0
    private var uColorLocation: Int = 0

    constructor(context: Context) : super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader) {
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        uColorLocation = glGetUniformLocation(program, U_COLOR)

    }

    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        glUniform4f(uColorLocation, r, g, b, 1f)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

}