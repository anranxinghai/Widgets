package com.pinguo.camera360.gl.objects

import com.pinguo.camera360.gl.data.VertexArray
import com.pinguo.camera360.gl.programs.ColorShaderProgram
import com.pinguo.camera360.gl.util.Geometry

/**
 * 冰球
 */
class Puck {
    companion object {
        const val POSITION_COMPONENT_COUNT = 3
    }

    val radius: Float
    val height: Float

    private var vertexArray: VertexArray
    private val drawList: List<ObjectBuilder.Companion.DrawCommand>

    constructor(radius: Float, height: Float, numPointsAroundPuck: Int) {
        val generatedData = ObjectBuilder.createPuck(Geometry.Cylinder(Geometry.Point(0f, 0f, 0f), radius, height), numPointsAroundPuck)
        this.radius = radius
        this.height = height
        vertexArray = VertexArray(generatedData.vertexData)
        drawList = generatedData.drawList
    }

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, 0)
    }

    fun draw() {
        drawList.forEach {
            it.draw()
        }
    }
}