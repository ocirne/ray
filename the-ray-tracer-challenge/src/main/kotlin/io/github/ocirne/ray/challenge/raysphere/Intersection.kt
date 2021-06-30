package io.github.ocirne.ray.challenge.raysphere

import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector

data class Computation(
    val t: Double,
    val obj: Sphere,
    val point: Point,
    val eyev: Vector,
    val preNormalv: Vector) {

    val normalv: Vector
    val inside: Boolean

    init {
        if (preNormalv.dot(eyev) < 0) {
            inside = true
            normalv = -preNormalv
        } else {
            inside = false
            normalv = preNormalv
        }
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
            eyev = -ray.direction,
            preNormalv = obj.normalAt(point),
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