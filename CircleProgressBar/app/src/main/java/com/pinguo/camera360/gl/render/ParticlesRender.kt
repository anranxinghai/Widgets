package com.pinguo.camera360.gl.render

import android.content.Context
import android.graphics.Color
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix.*
import com.pinguo.camera360.gl.objects.ParticleShooter
import com.pinguo.camera360.gl.objects.ParticleSystem
import com.pinguo.camera360.gl.programs.ParticleShaderProgram
import com.pinguo.camera360.gl.util.Geometry
import com.pinguo.camera360.gl.util.MatrixHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ParticlesRender : GLSurfaceView.Renderer {

    //用来在本地内存中存储数据
    private val context: Context
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)

    lateinit var particleProgram: ParticleShaderProgram
    lateinit var particleSystem: ParticleSystem
    lateinit var redParticleShooter: ParticleShooter
    lateinit var greenParticleShooter: ParticleShooter
    lateinit var blueParticleShooter: ParticleShooter
    private var globalStartTime = 0L

    constructor(context: Context) {
        this.context = context
    }

    //Surface创建时的回调，从其他Activity切换回来也可能被调用
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0f, 0f, 0f, 0f)
        particleProgram = ParticleShaderProgram(context)
        particleSystem = ParticleSystem(10000)

        globalStartTime = System.nanoTime()

        val particleDirection: Geometry.Vector = Geometry.Vector(0f, 0.5f, 0f)

        redParticleShooter = ParticleShooter(Geometry.Point(-1f, 0f, 0f), particleDirection, Color.rgb(255, 50, 0))
        greenParticleShooter = ParticleShooter(Geometry.Point(0f, 0f, 0f), particleDirection, Color.rgb(25, 255, 25))
        blueParticleShooter = ParticleShooter(Geometry.Point(1f, 0f, 0f), particleDirection, Color.rgb(5, 50, 255))
    }

    //Surface尺寸变化时被调用，比如横竖屏切换
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //设置OpenGL 视点填充满整个surface
        glViewport(0, 0, width, height)

        MatrixHelper.perspectiveM(projectionMatrix, 45f, width / height.toFloat(), 1f, 10f)
        setIdentityM(viewMatrix, 0)
        translateM(viewMatrix, 0, 0f, -1.5f, -5f)
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
    }

    //每一帧绘制，都会回调
    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)
        val currentTime = (System.nanoTime() - globalStartTime) / 1000000000f

        redParticleShooter.addParticles(particleSystem, currentTime, 5)
        greenParticleShooter.addParticles(particleSystem, currentTime, 5)
        blueParticleShooter.addParticles(particleSystem, currentTime, 5)

        particleProgram.useProgram()
        particleProgram.setUniforms(viewProjectionMatrix, currentTime)
        particleSystem.bindData(particleProgram)
        particleSystem.draw()
    }
}