package com.pinguo.camera360.gl.util

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
}