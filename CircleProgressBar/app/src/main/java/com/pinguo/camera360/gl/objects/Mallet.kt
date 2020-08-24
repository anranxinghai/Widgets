package com.pinguo.camera360.gl.objects

import android.opengl.GLES20.GL_POINTS
import android.opengl.GLES20.glDrawArrays
import com.pinguo.camera360.gl.constant.Constants.BYTES_PER_FLOAT
import com.pinguo.camera360.gl.data.VertexArray
import com.pinguo.camera360.gl.programs.ColorShaderProgram

class Mallet {
    companion object {
        const val POSITION_COMPONENT_COUNT = 2
        const val COLOR_COMPONENT_COUNT = /*4*/3
        const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT


        val VERTEX_DATA = floatArrayOf(
                //Order of coordinates:X,Y,A,R,G,B
                0.0f, -0.4f,/*1.0f,*/ 0f, 0f,1f,
                0.0f, 0.4f,/*1.0f,*/ 1.0f, 0f,0f
        )
    }

    private var vertexArray: VertexArray

    constructor() {
        vertexArray = VertexArray(VERTEX_DATA)
    }

    fun bindData(colorProgram: ColorShaderProgram){
        vertexArray.setVertexAttribPointer(0,colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE)
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT,colorProgram.getColorAttributeLocation(), COLOR_COMPONENT_COUNT, STRIDE)
    }

    fun draw(){
        glDrawArrays(GL_POINTS,0,2)
    }
}