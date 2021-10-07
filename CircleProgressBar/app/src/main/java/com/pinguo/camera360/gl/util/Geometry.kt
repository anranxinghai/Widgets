package com.pinguo.camera360.gl.util

import kotlin.math.sqrt

/**
 * 几何
 */
object Geometry {

    /**
     * 点
     */
    class Point(val x: Float, val y: Float, val z: Float) {
        fun translateY(distance: Float): Point {
            return Point(x, y + distance, z)
        }

        fun translate(vector: Vector): Point {
            return Point(x + vector.x, y + vector.y, z + vector.z)
        }
    }

    /**
     * 圆
     */
    class Circle(val center: Point, val radius: Float) {
        fun scale(scale: Float): Circle {
            return Circle(center, radius * scale)
        }
    }

    /**
     * 圆柱
     */
    class Cylinder(val center: Point, val radius: Float, val height: Float)

    /**
     * 射线
     */
    class Ray(val point: Point, val vector: Vector)

    /**
     * 向量
     */
    class Vector(val x: Float, val y: Float, val z: Float) {
        companion object {
            fun vectorBetween(from: Point, to: Point): Vector {
                return Vector(to.x - from.x, to.y - from.y, to.z - from.z)
            }
        }

        fun crossProduct(other: Vector): Vector {
            return Vector(
                    y * other.z - z * other.y,
                    z * other.x - x * other.z,
                    x * other.y - y * other.x
            )
        }

        fun length(): Float {
            return sqrt(x * x + y * y + z * z)
        }

        fun dotProduct(other: Vector): Float {
            return x * other.x + y * other.y + z * other.z
        }

        fun scale(scaleFactor: Float): Vector {
            return Vector(x * scaleFactor, y * scaleFactor, z * scaleFactor)
        }
    }

    /**
     * 球体
     */
    class Sphere(val center: Point, val radius: Float)

    class Plane(val point: Point,
                /**法向量**/
                val normal: Vector)

    fun intersects(sphere: Sphere, ray: Ray): Boolean {
        return distanceBetween(sphere.center, ray) < sphere.radius
    }

    fun intersectionPoint(ray: Ray, plane: Plane): Point {
        val rayToPlaneVector = Vector.vectorBetween(ray.point, plane.point)
        val scaleFactor = rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal)
        val intersectionPoint = ray.point.translate(ray.vector.scale(scaleFactor))
        return intersectionPoint
    }

    private fun distanceBetween(point: Point, ray: Ray): Float {
        val p1ToPoint = Vector.vectorBetween(ray.point, point)
        val p2ToPoint = Vector.vectorBetween(ray.point.translate(ray.vector), point)
        val areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length()
        val lengthOfBase = ray.vector.length()
        return areaOfTriangleTimesTwo / lengthOfBase
    }
}