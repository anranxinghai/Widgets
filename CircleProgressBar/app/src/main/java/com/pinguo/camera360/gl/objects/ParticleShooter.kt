package com.pinguo.camera360.gl.objects

import com.pinguo.camera360.gl.util.Geometry

class ParticleShooter(val position: Geometry.Point, val direction: Geometry.Vector, val color: Int) {
    fun addParticles(particleSystem: ParticleSystem, currentTime: Float, count: Int) {
        for (i in 0 until count) {
            particleSystem.addParticle(position, color, direction, currentTime)
        }
    }
}