package io.github.ocirne.ray.challenge.shapes

import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Operation.*
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector

enum class Operation {
    UNION,
    INTERSECTION,
    DIFFERENCE
}

class CSG(val operation: Operation, val left: Shape, val right: Shape,
          transform: Matrix = identityMatrix): Shape(transform) {

    init {
        left.parent = this
        right.parent = this
    }

    override fun localIntersect(localRay: Ray): List<Intersection> {
        val leftxs = left.intersect(localRay)
        val rightxs = right.intersect(localRay)
        val xs = (leftxs + rightxs).sortedBy { it.t }
        return filterIntersections(xs)
    }

    override fun localNormalAt(localPoint: Point, hit: Intersection?): Vector {
        TODO("Should never be called")
    }

    override fun bounds(): Bounds {
        TODO("Not yet implemented")
    }

    override fun contains(shape: Shape): Boolean {
        return left.contains(shape) || right.contains(shape)
    }

    fun filterIntersections(xs: List<Intersection>): List<Intersection> {
        // begin outside of both children
        var inl = false
        var inr = false

        // prepare a list to receive the filtered intersections
        val result = mutableListOf<Intersection>()

        for (i in xs) {
            // if i.obj is part of the "left" child, then lhit is true
            var lhit = left.contains(i.shape)

            if (intersectionAllowed(operation, lhit, inl, inr)) {
                result.add(i)
            }
            // depending on which object was hit, toggle either inl or inr
            if (lhit) {
                inl = !inl
            } else {
                inr = !inr
            }
         }
        return result
    }
}

fun intersectionAllowed(op: Operation, lhit: Boolean, inl: Boolean, inr: Boolean): Boolean {
    return when (op) {
        UNION -> (lhit && !inr) || (!lhit && !inl)
        INTERSECTION -> (lhit && inr) || (!lhit && inl)
        DIFFERENCE -> (lhit && !inr) || (!lhit && inl)
    }
}