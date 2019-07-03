package com.pinguo.camera360

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import com.pinguo.camera360.helper.ShaderHelper
import com.pinguo.camera360.util.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRender : GLSurfaceView.Renderer {
    private var vertexData:FloatBuffer? = null
    private var context:Context? = null
    private var program:Int = 0
    private val U_COLOR = "u_Color"
    private var uColorLocation:Int = 0
    private val A_POSITION = "a_Position"
    private var aPositionLocation:Int = 0
    companion object {
        val POSITION_COMPONENT_COUNT = 2
        val BYTES_PER_FLOAT = 4

    }

    constructor(context: Context) {
        this.context = context
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
        glClear(GLES20.GL_COLOR_BUFFER_BIT)
        glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f)
        glDrawArrays(GL_TRIANGLES,0,6)

        glUniform4f(uColorLocation,1.0f,0f,0f,1f)
        glDrawArrays(GL_LINES,6,2)

        glUniform4f(uColorLocation,0f,0f,1f,1f)
        glDrawArrays(GL_POINTS,8,1)

        glUniform4f(uColorLocation,1.0f,0f,0f,1f)
        glDrawArrays(GL_POINTS,9,1)

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0f, 0f, 0f, 0f)
        val vertexShaderSource = TextResourceReader.readTextFileFromResource(context!!,R.raw.simple_vertex_shader)
        val fragmentShaderSource = TextResourceReader.readTextFileFromResource(context!!,R.raw.simple_fragment_shader)
        val vertexShader = ShaderHelper.compilVertexShader(vertexShaderSource)
        val fragmentShader = ShaderHelper.compilVertexShader(fragmentShaderSource)
        program = ShaderHelper.linkProgram(vertexShader,fragmentShader)
        ShaderHelper.validatePrograme(program)
        glUseProgram(program)
        uColorLocation = glGetUniformLocation(program,U_COLOR)
        aPositionLocation = glGetAttribLocation(program,A_POSITION)
        vertexData?.position(0)
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,false,0,vertexData)
        glEnableVertexAttribArray(aPositionLocation)


    }

}