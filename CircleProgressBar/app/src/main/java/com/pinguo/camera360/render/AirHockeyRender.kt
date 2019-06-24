package com.pinguo.camera360

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRender : GLSurfaceView.Renderer {
    private var vertexData:FloatBuffer? = null
    companion object {
        val POSITION_COMPONENT_COUNT = 2
        val BYTES_PER_FLOAT = 4

    }

    constructor() {
        val tableVertices = floatArrayOf(
                0f, 0f,
                0f, 14f,
                9f, 14f,
                9f, 0f
        )

        val tableVerticesWithTriangles = floatArrayOf(
                0f, 0f,
                9f, 14f,
                0f, 14f,

                0f, 0f,
                9f, 0f,
                9f, 14f,

                0f,7f,
                9f,7f,

                4.5f,2f,
                4.5f,12f
        )
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer()
        vertexData?.put(tableVerticesWithTriangles)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.6f, 1.0f, 1.0f, 0.8f)
    }

}