package com.pinguo.camera360.gl.render

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.opengl.Matrix.multiplyMM
import com.pinguo.camera360.R
import com.pinguo.camera360.gl.objects.Mallet
import com.pinguo.camera360.gl.objects.Table
import com.pinguo.camera360.gl.programs.ColorShaderProgram
import com.pinguo.camera360.gl.programs.TextureShaderProgram
import com.pinguo.camera360.gl.util.MatrixHelper
import com.pinguo.camera360.gl.util.TextureHelper
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRender : GLSurfaceView.Renderer {
    //用来在本地内存中存储数据
    private var vertexData:FloatBuffer? = null
    private var context:Context
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)

    private lateinit var table:Table
    private lateinit var mallet:Mallet
    private lateinit var textureProgram:TextureShaderProgram
    private lateinit var colorProgram:ColorShaderProgram
    private var texture:Int = 0

    constructor(context: Context) {
        this.context = context
    }

    //Surface创建时的回调，从其他Activity切换回来也可能被调用
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0f,0f,0f,0f)
        table = Table()
        mallet = Mallet()
        textureProgram = TextureShaderProgram(context)
        colorProgram = ColorShaderProgram(context)
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)
    }
    //Surface尺寸变化时被调用，比如横竖屏切换
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //设置OpenGL 视点填充满整个surface
        glViewport(0, 0, width, height)
        val aspectRatio = if(width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
        /*if (width > height) orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f)
        else orthoM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f)*/
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2f)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -1.5f)
        Matrix.rotateM(modelMatrix,0,-60f,1f,0f,0f)
        MatrixHelper.perspectiveM(projectionMatrix,45f,aspectRatio,1f,10f)
//        perspectiveM(projectionMatrix,0,45f,width/height.toFloat(),1f,10f)
//        orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f)
        val temp = FloatArray(16)
        multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0)
        System.arraycopy(temp,0,projectionMatrix,0,temp.size)
    }
    //每一帧绘制，都会回调
    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        textureProgram.useProgram()
        textureProgram.setUniforms(projectionMatrix,texture)
        table.bindData(textureProgram)
        table.draw()

        colorProgram.useProgram()
        colorProgram.setUniforms(projectionMatrix)
        mallet.bindData(colorProgram)
        mallet.draw()
    }
}