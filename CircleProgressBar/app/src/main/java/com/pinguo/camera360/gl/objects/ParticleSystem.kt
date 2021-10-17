package com.pinguo.camera360.gl.objects

import android.graphics.Color
import android.opengl.GLES20.GL_POINTS
import android.opengl.GLES20.glDrawArrays
import com.pinguo.camera360.gl.constant.Constants.BYTES_PER_FLOAT
import com.pinguo.camera360.gl.data.VertexArray
import com.pinguo.camera360.gl.programs.ParticleShaderProgram
import com.pinguo.camera360.gl.util.Geometry

class ParticleSystem {
    companion object {
        private const val POSITION_COMPONENT_COUNT = 3
        private const val COLOR_COMPONENT_COUNT = 3
        private const val VECTOR_COMPONENT_COUNT = 3
        private const val PARTICLE_START_TIME_COMPONENT_COUNT = 1
        private const val TOTAL_COMPONENT_COUNT =
                POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT +
                        VECTOR_COMPONENT_COUNT + PARTICLE_START_TIME_COMPONENT_COUNT
        private const val STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT
    }

    private val particles: FloatArray
    private val vertexArray: VertexArray
    private val maxParticleCount: Int
    private var currentParticleCount = 0
    private var nextParticle = 0

    constructor(maxParticleCount: Int) {
        particles = FloatArray(maxParticleCount * TOTAL_COMPONENT_COUNT)
        vertexArray = VertexArray(particles)
        this.maxParticleCount = maxParticleCount
    }

    fun addParticle(position: Geometry.Point, color: Int, direction: Geometry.Vector, particleStartTime: Float) {
        val particleOffset = nextParticle * TOTAL_COMPONENT_COUNT
        var currentOffset = particleOffset
        nextParticle++
        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++
        }
        if (nextParticle == maxParticleCount) {
            nextParticle = 0
        }
        particles[currentOffset++] = position.x
        particles[currentOffset++] = position.y
        particles[currentOffset++] = position.z

        particles[currentOffset++] = Color.red(color) / 255f
        particles[currentOffset++] = Color.green(color) / 255f
        particles[currentOffset++] = Color.blue(color) / 255f

        particles[currentOffset++] = direction.x
        particles[currentOffset++] = direction.y
        particles[currentOffset++] = direction.z

        particles[currentOffset++] = particleStartTime
        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT)

    }

    fun bindData(particleShaderProgram: ParticleShaderProgram) {
        var dataOffset = 0
        vertexArray.setVertexAttribPointer(dataOffset, particleShaderProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, STRIDE)
        dataOffset += POSITION_COMPONENT_COUNT
        vertexArray.setVertexAttribPointer(dataOffset, particleShaderProgram.getColorAttributeLocation(), COLOR_COMPONENT_COUNT, STRIDE)
        dataOffset += COLOR_COMPONENT_COUNT
        vertexArray.setVertexAttribPointer(dataOffset, particleShaderProgram.getDirectionAttributeLocation(), VECTOR_COMPONENT_COUNT, STRIDE)
        dataOffset += VECTOR_COMPONENT_COUNT
        vertexArray.setVertexAttribPointer(dataOffset, particleShaderProgram.getParticleStartTimeAttributeLocation(), PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE)
    }

    fun draw() {
        glDrawArrays(GL_POINTS, 0, currentParticleCount)
    }
}