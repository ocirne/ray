package io.github.ocirne.ray.challenge.triangles

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.math.epsilon
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Bounds
import io.github.ocirne.ray.challenge.shapes.Shape
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector
import kotlin.math.abs

class Triangle(val p1: Point, val p2: Point, val p3: Point,
               override val material: Material
): Shape(material = material) {

    val e1: Vector = p2 - p1
    val e2: Vector = p3 - p1
    val normal: Vector = e2.cross(e1).normalize()

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val dirCrossE2 = localRay.direction.cross(e2)
        val det = e1.dot(dirCrossE2)
        if (abs(det) < epsilon) {
            return listOf()
        }
        val f = 1.0 / det
        val p1ToOrigin = localRay.origin - p1
        val u = f * p1ToOrigin.dot(dirCrossE2)
        if (u < 0 || u > 1) {
            return listOf()
        }
        val originCrossE1 = p1ToOrigin.cross(e1)
        val v = f * localRay.direction.dot(originCrossE1)
        if (v < 0 || (u + v) > 1) {
            return listOf()
        }
        val t = f * e2.dot(originCrossE1)
        return listOf(Intersection(t, this))
    }

    override fun localNormalAt(localPoint: Point): Vector {
        return normal
    }

    override fun bounds(): Bounds {
        TODO("Not yet implemented")
    }
}