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
    private val A_COLOR = "a_Color"
    private var aColorLocation:Int = 0
    private val A_POSITION = "a_Position"
    private var aPositionLocation:Int = 0
    companion object {
        val POSITION_COMPONENT_COUNT = 2
        val COLOR_COMPONENT_COUNT = 3
        val BYTES_PER_FLOAT = 4
        val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

    }

    constructor(context: Context) {
        this.context = context
        val tableVerticesWithTriangles = floatArrayOf(
                   0f,    0f,  1f,  1f,  1f,
                -0.5f, -0.5f,0.7f,0.7f,0.7f,
                 0.5f, -0.5f,0.7f,0.7f,0.7f,

                 0.5f,  0.5f,0.7f,0.7f,0.7f,
                -0.5f,  0.5f,0.7f,0.7f,0.7f,
                -0.5f, -0.5f,0.7f,0.7f,0.7f,

                -0.5f,    0f,  1f,  0f,  0f,
                 0.5f,    0f,  1f,  0f,  0f,

                   0f,-0.25f,  0f,  0f,  1f,
                   0f, 0.25f,  1f,  0f,  0f
        )
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer()
        vertexData?.put(tableVerticesWithTriangles)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GLES20.GL_COLOR_BUFFER_BIT)
        glDrawArrays(GL_TRIANGLES,0,6)

        glDrawArrays(GL_LINES,6,2)

        glDrawArrays(GL_POINTS,8,1)

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
        val fragmentShader = ShaderHelper.compilFragmentShader(fragmentShaderSource)
        program = ShaderHelper.linkProgram(vertexShader,fragmentShader)
        ShaderHelper.validatePrograme(program)
        glUseProgram(program)
        aColorLocation = glGetUniformLocation(program,A_COLOR)
        aPositionLocation = glGetAttribLocation(program,A_POSITION)

        vertexData?.position(0)
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,false, STRIDE,vertexData)
        glEnableVertexAttribArray(aPositionLocation)

        vertexData?.position(COLOR_COMPONENT_COUNT)
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT,false, STRIDE,vertexData)
        glEnableVertexAttribArray(aColorLocation)

    }

}