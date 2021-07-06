package io.github.ocirne.ray.challenge.triangles

import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Bounds
import io.github.ocirne.ray.challenge.shapes.Shape
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector

class Triangle(val p1: Point, val p2: Point, val p3: Point): Shape() {

    val e1: Vector = p2 - p1
    val e2: Vector = p3 - p1
    val normal: Vector = e2.cross(e1).normalize()

    override fun localIntersect(localRay: Ray): List<Intersection> {
        TODO("Not yet implemented")
    }

    override fun localNormalAt(localPoint: Point): Vector {
        TODO("Not yet implemented")
    }

    override fun bounds(): Bounds {
        TODO("Not yet implemented")
    }
}