package com.pinguo.camera360.gl.data

import android.opengl.GLES20.*
import com.pinguo.camera360.gl.constant.Constants.BYTES_PER_FLOAT
import java.nio.ByteBuffer
import java.nio.ByteOrder

class VertexArray(val vertexData: FloatArray) {

    private val floatBuffer = ByteBuffer.allocateDirect(vertexData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexData)

    fun setVertexAttribPointer(dataOffset: Int, attributeLocation: Int, componentCount: Int, stride: Int) {
        floatBuffer.position(dataOffset)
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, floatBuffer)
        glEnableVertexAttribArray(attributeLocation)
        floatBuffer.position(0)

    }
}