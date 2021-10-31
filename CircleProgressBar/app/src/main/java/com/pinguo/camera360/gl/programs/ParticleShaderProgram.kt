package com.pinguo.camera360.gl.programs

import android.content.Context
import android.opengl.GLES20.*
import com.pinguo.camera360.R

class ParticleShaderProgram : ShaderProgram {
    private var uMatrixLocation = 0
    private var uTimeLocation = 0
    private var uTextureUnitLocation = 0;

    private var aPositionLocation = 0
    private var aColorLocation: Int = 0
    private var aDirectionVectorLocation: Int = 0
    private var aParticleStartTimeLocation: Int = 0

    constructor(context: Context) : super(context, R.raw.particle_vertext_shader, R.raw.particle_fragment_shader) {
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        uTimeLocation = glGetUniformLocation(program, U_TIME)
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT)

        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        aColorLocation = glGetAttribLocation(program, A_COLOR)
        aDirectionVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR)
        aParticleStartTimeLocation = glGetAttribLocation(program, A_PARTICLE_START_TIME)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getColorAttributeLocation(): Int {
        return aColorLocation
    }

    fun getDirectionAttributeLocation(): Int {
        return aDirectionVectorLocation
    }

    fun getParticleStartTimeAttributeLocation(): Int {
        return aParticleStartTimeLocation
    }

    fun setUniforms(matrix: FloatArray, elapsedTime: Float, textureId: Int) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        glUniform1f(uTimeLocation, elapsedTime)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glUniform1i(uTextureUnitLocation, 0)
    }
}