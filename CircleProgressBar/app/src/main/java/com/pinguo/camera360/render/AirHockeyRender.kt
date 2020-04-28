package com.pinguo.camera360

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix.orthoM
import com.pinguo.camera360.helper.ShaderHelper
import com.pinguo.camera360.util.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRender : GLSurfaceView.Renderer {
    //用来在本地内存中存储数据
    private var vertexData:FloatBuffer? = null
    private var context:Context? = null
    private var program:Int = 0
    private val A_COLOR = "a_Color"
    private val U_MATRIX = "u_Matrix"
    private var aColorLocation:Int = 0
    private val A_POSITION = "a_Position"
    private var aPositionLocation:Int = 0
    private val projectionMatrix = FloatArray(16)
    private var uMatrixLocation:Int = 0
    companion object {
        const val POSITION_COMPONENT_COUNT = 4
        const val COLOR_COMPONENT_COUNT = 3
        const val BYTES_PER_FLOAT = 4 //一个float 32位精度，4个字节
        const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

    }

    constructor(context: Context) {
        this.context = context
        val tableVerticesWithTriangles = floatArrayOf(
                 0.0f,  0.0f, 0.0f, 1.5f, 1.0f, 1.0f, 1.0f,
                -0.5f, -0.8f, 0.0f, 1.0f, 0.7f, 0.7f, 0.7f,
                 0.5f, -0.8f, 0.0f, 1.0f, 0.7f, 0.7f, 0.7f,

                 0.5f,  0.8f, 0.0f, 2.0f, 0.7f, 0.7f, 0.7f,
                -0.5f,  0.8f, 0.0f, 2.0f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.0f, 1.0f, 0.7f, 0.7f, 0.7f,

                -0.5f,  0.0f, 0.0f, 1.5f, 1.0f, 0.0f, 0.0f,
                 0.5f,  0.0f, 0.0f, 1.5f, 1.0f, 0.0f, 0.0f,

                 0.0f, -0.4f,  0.0f, 1.25f, 0.0f, 0.0f,  1.0f,
                 0.0f,  0.4f,  0.0f, 1.75f, 1.0f, 0.0f,  0.0f
        )
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)//分配Native空间
                .order(ByteOrder.nativeOrder())//保证顺序一致
                .asFloatBuffer()//分配的内存作为float类型的Buffer返回。
        vertexData?.put(tableVerticesWithTriangles)//将float数据复制存入native内存
    }

    //Surface创建时的回调，从其他Activity切换回来也可能被调用
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClear(GL_COLOR_BUFFER_BIT)
        glClearColor(0f, 0f, 0f, 0f)
        val vertexShaderSource = TextResourceReader.readTextFileFromResource(context!!,R.raw.simple_vertex_shader)
        val fragmentShaderSource = TextResourceReader.readTextFileFromResource(context!!,R.raw.simple_fragment_shader)
        val vertexShader = ShaderHelper.compilVertexShader(vertexShaderSource)
        val fragmentShader = ShaderHelper.compilFragmentShader(fragmentShaderSource)
        program = ShaderHelper.linkProgram(vertexShader,fragmentShader)
        ShaderHelper.validatePrograme(program)
        glUseProgram(program)
        aColorLocation = glGetAttribLocation(program,A_COLOR)//获取a_Color 的位置
        aPositionLocation = glGetAttribLocation(program,A_POSITION)//获取a_Position 的位置
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX)//获取a_Position 的位置

        vertexData?.position(0) // 确保从0开始读取
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,false, STRIDE,vertexData)
        glEnableVertexAttribArray(aPositionLocation)

        vertexData?.position(COLOR_COMPONENT_COUNT)
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT,false, STRIDE,vertexData)
        glEnableVertexAttribArray(aColorLocation)

    }
    //Surface尺寸变化时被调用，比如横竖屏切换
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //设置OpenGL 视点填充满整个surface
        glViewport(0, 0, width, height)
        val aspectRatio = if(width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
        if (width > height) orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f)
        else orthoM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f)



    }
    //每一帧绘制，都会回调
    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        glUniformMatrix4fv(uMatrixLocation,1,false,projectionMatrix,0)
        glDrawArrays(GL_TRIANGLE_FAN,0,6)

        glDrawArrays(GL_LINES,6,2)

        glDrawArrays(GL_POINTS,8,1)

        glDrawArrays(GL_POINTS,9,1)

    }
}