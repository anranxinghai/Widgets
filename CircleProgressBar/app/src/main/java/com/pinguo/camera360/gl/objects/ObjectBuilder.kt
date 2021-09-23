package com.pinguo.camera360.gl.objects

import android.opengl.GLES20.*
import com.pinguo.camera360.gl.util.Geometry
import com.pinguo.camera360.gl.util.Geometry.Circle
import com.pinguo.camera360.gl.util.Geometry.Cylinder
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ObjectBuilder {
    companion object {
        private const val FLOATS_PER_VERTEX = 3

        fun sizeOfCircleInVertices(numPoints: Int): Int {
            return 1 + (numPoints + 1)
        }


        fun sizeOfOpenCylinderInVertices(numPoints: Int): Int {
            return (numPoints + 1) * 2
        }

        fun createPuck(puck: Cylinder, numPoints: Int): GenerateData {
            val size = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints)
            var builder = ObjectBuilder(size)
            val puckTop = Circle(
                    puck.center.translateY(puck.height / 2f),
                    puck.radius
            )
            builder.appendCircle(puckTop, numPoints)
            builder.appendOpenCylinder(puck, numPoints)
            return builder.build()
        }

        fun createMallet(center: Geometry.Point, radius: Float, height: Float, numPoints: Int): GenerateData {
            val size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints) * 2
            val builder = ObjectBuilder(size)
            val baseHeight = height * 0.25f
            val baseCircle = Circle(center.translateY(-baseHeight), radius)
            val baseCylinder = Cylinder(baseCircle.center.translateY(-baseHeight / 2f), radius, baseHeight)
            builder.appendCircle(baseCircle, numPoints)
            builder.appendOpenCylinder(baseCylinder, numPoints)

            val handleHeight = height * 0.75f
            val handleRadius = radius / 3f
            val handleCircle = Circle(center.translateY(handleHeight * 0.5f), handleRadius)
            val handleCylinder = Cylinder(handleCircle.center.translateY(-handleHeight / 2f), handleRadius, handleHeight)
            builder.appendCircle(handleCircle, numPoints)
            builder.appendOpenCylinder(handleCylinder, numPoints)
            return builder.build()
        }

        interface DrawCommand {
            fun draw()
        }

        class GenerateData(val vertexData: FloatArray, val drawList: List<DrawCommand>)
    }

    private val vertexData: FloatArray
    private val drawList = ArrayList<DrawCommand>()
    private var offset = 0

    constructor(sizeInVertices: Int) {
        vertexData = FloatArray(sizeInVertices * FLOATS_PER_VERTEX)
    }

    private fun appendCircle(circle: Circle, numPoints: Int) {
        val startVertex = offset / FLOATS_PER_VERTEX
        val numVertices = sizeOfCircleInVertices(numPoints)
        vertexData[offset++] = circle.center.x
        vertexData[offset++] = circle.center.y
        vertexData[offset++] = circle.center.z
        for (i in 0..numPoints) {
            val angleInRadians = i / numPoints.toFloat() * Math.PI * 2f
            vertexData[offset++] = circle.center.x + circle.radius * cos(angleInRadians).toFloat()
            vertexData[offset++] = circle.center.y
            vertexData[offset++] = circle.center.z + circle.radius * sin(angleInRadians).toFloat()
        }
        drawList.add(object : DrawCommand {
            override fun draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices)
            }
        })
    }

    private fun appendOpenCylinder(cylinder: Cylinder, numPoints: Int) {
        val startVertex = offset / FLOATS_PER_VERTEX
        val numVertices = sizeOfOpenCylinderInVertices(numPoints)
        val yStart = cylinder.center.y - cylinder.height / 2
        val yEnd = cylinder.center.y + cylinder.height / 2
        for (i in 0..numPoints) {
            val angleInRadians = i / numPoints.toFloat() * PI * 2f
            val xPosition = cylinder.center.x + cylinder.radius * cos(angleInRadians).toFloat()
            val zPosition = cylinder.center.z + cylinder.radius * sin(angleInRadians).toFloat()
            vertexData[offset++] = xPosition
            vertexData[offset++] = yStart
            vertexData[offset++] = zPosition
            vertexData[offset++] = xPosition
            vertexData[offset++] = yEnd
            vertexData[offset++] = zPosition
        }
        drawList.add(object : DrawCommand {
            override fun draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices)
            }

        })
    }

    private fun build(): GenerateData {
        return GenerateData(vertexData, drawList)
    }
}