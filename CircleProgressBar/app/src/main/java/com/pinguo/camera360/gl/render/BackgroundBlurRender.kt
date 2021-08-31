package com.pinguo.camera360.gl.render

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.os.Environment
import com.pinguo.camera360.gl.objects.Screen
import com.pinguo.camera360.gl.programs.BackgroundBlurShaderProgram
import com.pinguo.camera360.gl.util.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class BackgroundBlurRender(val context: Context) : GLSurfaceView.Renderer {
    //用来在本地内存中存储数据
    private val centerPoint = floatArrayOf(0.5f, 0.5f, 0f, 0f)

    private lateinit var backgroundBlurShaderProgram: BackgroundBlurShaderProgram

    private val mainImage = "${Environment.getExternalStorageDirectory()}/test/input.png"
    private var originImageTexture: Int = 0
    private val shapeImage = "${Environment.getExternalStorageDirectory()}/test/shape.png"
    private var shapeImageTexture: Int = 0
    private val maskImage = "${Environment.getExternalStorageDirectory()}/test/mask.jpg"
    private var maskImageTexture: Int = 0
    private val maskExternImage = "${Environment.getExternalStorageDirectory()}/test/mask.jpg"
    private var maskImageExternTexture: Int = 0


    private lateinit var screen: Screen
    //Surface创建时的回调，从其他Activity切换回来也可能被调用
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0f, 0f, 0f, 0f)
        screen = Screen()
        backgroundBlurShaderProgram = BackgroundBlurShaderProgram(context)
        originImageTexture = TextureHelper.loadTexture(mainImage)
        shapeImageTexture = TextureHelper.loadTexture(shapeImage)
        maskImageTexture = TextureHelper.loadTexture(maskImage)
        maskImageExternTexture = TextureHelper.loadTexture(maskExternImage)
    }

    //Surface尺寸变化时被调用，比如横竖屏切换
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //设置OpenGL 视点填充满整个surface
        glViewport(0, 0, width, height)
    }

    //每一帧绘制，都会回调
    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        backgroundBlurShaderProgram.useProgram()
        screen.bindData(backgroundBlurShaderProgram)
        backgroundBlurShaderProgram.setUniforms(
            centerPoint,
            originImageTexture,
            shapeImageTexture,
            getImageSize(mainImage),
            getImageSize(shapeImage),
            maskImageTexture,
            maskImageExternTexture
        )
        screen.draw()
    }

    private fun getImageSize(imagePath: String): FloatArray {
        val bitmap = BitmapFactory.decodeFile(imagePath)
        return if (bitmap != null) {
            val width = bitmap.width.toFloat()
            val height = bitmap.height.toFloat()
            floatArrayOf(1 / width, 1 / height, width, height)
        } else {
            floatArrayOf(1f, 1f, 1f, 1f)
        }
    }
}