package com.pinguo.camera360.gl.render

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix.*
import com.pinguo.camera360.R
import com.pinguo.camera360.gl.objects.Mallet
import com.pinguo.camera360.gl.objects.Puck
import com.pinguo.camera360.gl.objects.Table
import com.pinguo.camera360.gl.programs.ColorShaderProgram
import com.pinguo.camera360.gl.programs.TextureShaderProgram
import com.pinguo.camera360.gl.util.Geometry
import com.pinguo.camera360.gl.util.MatrixHelper
import com.pinguo.camera360.gl.util.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRender : GLSurfaceView.Renderer {
    //用来在本地内存中存储数据
    private val context: Context
    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)
    private val invertedViewProjectMatrix = FloatArray(16)

    private lateinit var table: Table
    private lateinit var mallet: Mallet
    private lateinit var puck: Puck
    private lateinit var textureProgram: TextureShaderProgram
    private lateinit var colorProgram: ColorShaderProgram
    private var texture: Int = 0

    private var blueMalletPressed = false
    private var redMalletPressed = false
    private lateinit var blueMalletPosition: Geometry.Point
    private lateinit var redMalletPosition: Geometry.Point

    constructor(context: Context) {
        this.context = context
    }

    //Surface创建时的回调，从其他Activity切换回来也可能被调用
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0f, 0f, 0f, 0f)
        table = Table()
        mallet = Mallet(0.08f, 0.15f, 32)
        puck = Puck(0.06f, 0.02f, 32)
        textureProgram = TextureShaderProgram(context)
        colorProgram = ColorShaderProgram(context)
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)
        blueMalletPosition = Geometry.Point(0f, mallet.height / 2f, 0.4f)
        redMalletPosition = Geometry.Point(0f, mallet.height / 2f, -0.4f)

    }
    //Surface尺寸变化时被调用，比如横竖屏切换
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //设置OpenGL 视点填充满整个surface
        glViewport(0, 0, width, height)
        val aspectRatio = if (width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
        /*if (width > height) orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f)
        else orthoM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f)*/
        /*Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2f)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -1.5f)
        Matrix.rotateM(modelMatrix,0,-60f,1f,0f,0f)*/
        MatrixHelper.perspectiveM(projectionMatrix, 30f, aspectRatio, 1f, 10f)
        /**
         * rm 目标数组
         * rmOffset setLookAtm会把结果从rm的这个偏移值看是存入
         * eyeX eyeY eyeZ 眼睛所在位置
         * centerX centerY centerZ 眼睛在看的位置
         * upX upY upZ 头所指的方向，upY 为1代表头笔直指正上方
         */
        setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f)
//        perspectiveM(projectionMatrix,0,45f,width/height.toFloat(),1f,10f)
//        orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f)


        val temp = FloatArray(16)
        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)
    }
    //每一帧绘制，都会回调
    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        invertM(invertedViewProjectMatrix, 0, viewProjectionMatrix, 0)
        positionTableInScene()
        textureProgram.useProgram()
        textureProgram.setUniforms(modelViewProjectionMatrix, texture)
        table.bindData(textureProgram)
        table.draw()

        positionObjectInScene(redMalletPosition.x, redMalletPosition.y, redMalletPosition.z)
        colorProgram.useProgram()
        colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f)
        mallet.bindData(colorProgram)
        mallet.draw()

        positionObjectInScene(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z)
//        colorProgram.useProgram()
        colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f)
        mallet.bindData(colorProgram)
        mallet.draw()

        positionObjectInScene(0f, puck.height / 2f, 0f)
//        colorProgram.useProgram()
        colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f)
        puck.bindData(colorProgram)
        puck.draw()
    }


    private fun positionTableInScene() {
        setIdentityM(modelMatrix, 0)
        rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f)
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        setIdentityM(modelMatrix, 0)
        translateM(modelMatrix, 0, x, y, z)
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    fun handleTouchPress(normalizedX: Float, normalizedY: Float) {
        val ray = convertNormalized2DPointToRay(normalizedX, normalizedY)
        val blueMalletBoundingSphere = Geometry.Sphere(Geometry.Point(blueMalletPosition.x, blueMalletPosition.y, blueMalletPosition.z), mallet.height / 2f)
        val redMalletBoundingSphere = Geometry.Sphere(Geometry.Point(redMalletPosition.x, redMalletPosition.y, redMalletPosition.z), mallet.height / 2f)
        blueMalletPressed = Geometry.intersects(blueMalletBoundingSphere, ray)
        redMalletPressed = Geometry.intersects(redMalletBoundingSphere, ray)
    }

    fun handleTouchDrag(normalizedX: Float, normalizedY: Float) {
        if (blueMalletPressed) {
            val ray = convertNormalized2DPointToRay(normalizedX, normalizedY)
            val plane = Geometry.Plane(Geometry.Point(0f, 0f, 0f), Geometry.Vector(0f, 1f, 0f))
            val touchPoint = Geometry.intersectionPoint(ray, plane)
            blueMalletPosition = Geometry.Point(touchPoint.x, mallet.height / 2, touchPoint.z)
        } else if (redMalletPressed) {
            val ray = convertNormalized2DPointToRay(normalizedX, normalizedY)
            val plane = Geometry.Plane(Geometry.Point(0f, 0f, 0f), Geometry.Vector(0f, 1f, 0f))
            val touchPoint = Geometry.intersectionPoint(ray, plane)
            redMalletPosition = Geometry.Point(touchPoint.x, mallet.height / 2, touchPoint.z)

        }
    }


    private fun convertNormalized2DPointToRay(normalizedX: Float, normalizedY: Float): Geometry.Ray {
        val nearPointNdc = floatArrayOf(normalizedX, normalizedY, -1f, 1f)
        val farPointNdc = floatArrayOf(normalizedX, normalizedY, 1f, 1f)
        val nearPointWorld = FloatArray(4)
        val farPointWorld = FloatArray(4)
        multiplyMV(nearPointWorld, 0, invertedViewProjectMatrix, 0, nearPointNdc, 0)
        multiplyMV(farPointWorld, 0, invertedViewProjectMatrix, 0, farPointNdc, 0)
        divideByW(nearPointWorld)
        divideByW(farPointWorld)
        val nearPointRay = Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2])
        val farPointRay = Geometry.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2])
        return Geometry.Ray(nearPointRay, Geometry.Vector.vectorBetween(nearPointRay, farPointRay))
    }

    private fun divideByW(vector: FloatArray) {
        vector[0] /= vector[3]
        vector[1] /= vector[3]
        vector[2] /= vector[3]
    }
}