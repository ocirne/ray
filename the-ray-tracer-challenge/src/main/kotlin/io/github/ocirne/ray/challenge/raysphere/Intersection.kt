package io.github.ocirne.ray.challenge.raysphere

import io.github.ocirne.ray.challenge.math.epsilon
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector

data class Computation(
    val t: Double,
    val obj: Sphere,
    val point: Point,
    val eyeV: Vector,
    val preNormalV: Vector) {

    val normalV: Vector
    val inside: Boolean
    val overPoint: Point

    init {
        if (preNormalV.dot(eyeV) < 0) {
            inside = true
            normalV = -preNormalV
        } else {
            inside = false
            normalV = preNormalV
        }
        overPoint = point + normalV * epsilon
    }
}

class Intersection(val t: Double, val obj: Sphere) {

    constructor(t: Int, obj: Sphere) : this(t.toDouble(), obj)

    fun prepareComputations(ray: Ray): Computation {
        val point = ray.position(t)
        return Computation(
            t = t,
            obj = obj,
            point = point,
            eyeV = -ray.direction,
            preNormalV = obj.normalAt(point),
        )
    }
}

class Intersections(vararg intersections: Intersection) {

    val count = intersections.size
    private val elements = intersections

    operator fun get(index: Int): Intersection {
        return elements[index]
    }

    fun hit(): Intersection? {
        return elements.filter { i -> i.t > 0 }.minByOrNull { it.t }
    }
}