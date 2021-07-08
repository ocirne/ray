package io.github.ocirne.ray.challenge.shapes

import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector

class CSG(val operation: String, val left: Shape, val right: Shape): Shape() {

    override fun localIntersect(localRay: Ray): List<Intersection> {
        TODO("Not yet implemented")
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection?): Vector {
        TODO("Not yet implemented")
    }

    override fun bounds(): Bounds {
        TODO("Not yet implemented")
    }

    fun filterIntersections(xs: List<Intersection>): List<Intersection> {
        TODO("Not yet implemented")
    }
}

fun intersectionAllowed(op: String, lhit: Boolean, inl: Boolean, inr: Boolean): Boolean {
    TODO("Not yet implemented")
}