package com.pinguo.camera360.gl.objects

import android.opengl.GLES20.GL_TRIANGLE_FAN
import android.opengl.GLES20.glDrawArrays
import com.pinguo.camera360.gl.constant.Constants.BYTES_PER_FLOAT
import com.pinguo.camera360.gl.data.VertexArray
import com.pinguo.camera360.gl.programs.BackgroundBlurShaderProgram

class Screen {
    companion object {
        const val POSITION_COMPONENT_COUNT = 2
        const val TEXTURE_COORDINATES_COMPONENT_COUNT = 2
        const val STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT


        val VERTEX_DATA = floatArrayOf(
            //Order of coordinates:X,Y,S,T
            0.0f, 0.0f, 0.5f, 0.5f,
            -1f, -1.0f, 0.0f, 0.9f,
            1f, -1.0f, 1.0f, 0.9f,

            1f, 1.0f, 1.0f, 0.1f,
            -1f, 1.0f, 0.0f, 0.1f,
            -1f, -1.0f, 0.0f, 0.9f
        )
    }

    private var vertexArray: VertexArray

    constructor() {
        vertexArray = VertexArray(VERTEX_DATA)
    }

    fun bindData(backgroundBlurShaderProgram: BackgroundBlurShaderProgram){
        vertexArray.setVertexAttribPointer(0,backgroundBlurShaderProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT,
            STRIDE
        )
        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,backgroundBlurShaderProgram.getTextureCoordinatesAttributeLocation(),
            TEXTURE_COORDINATES_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw(){
        glDrawArrays(GL_TRIANGLE_FAN,0,6)
    }
}