package io.github.ocirne.ray.challenge.raysphere

import io.github.ocirne.ray.challenge.math.epsilon
import io.github.ocirne.ray.challenge.shapes.Shape
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.Vector
import kotlin.math.sqrt
import kotlin.math.pow

data class Computation(
    val t: Double,
    val shape: Shape,
    val point: Point,
    private val direction: Vector,
    val preNormalV: Vector,
    val n1: Double,
    val n2: Double
) {
    val eyeV: Vector = -direction
    val normalV: Vector
    val reflectV: Vector
    val inside: Boolean
    val overPoint: Point
    val underPoint: Point

    init {
        if (preNormalV.dot(eyeV) < 0) {
            inside = true
            normalV = -preNormalV
        } else {
            inside = false
            normalV = preNormalV
        }
        overPoint = point + normalV * epsilon
        underPoint = point - normalV * epsilon
        reflectV = direction.reflect(normalV)
    }

    fun schlick(): Double {
        // find the cosine of the angle between the eye and normal vectors
        var cos = eyeV.dot(normalV)
        // total internal reflection can only occur if n1 > n2
        if (n1 > n2) {
            val n = n1 / n2
            val sin2_t = n * n * (1.0 - cos * cos)
            if (sin2_t > 1.0) {
                return 1.0
            }
            // compute cosine of theta_t using trig identity
            val cos_t = sqrt(1.0 - sin2_t)
            // when n1 > n2, use cos(theta_t) instead
            cos = cos_t
        }
        val r0 = ((n1 - n2) / (n1 + n2)).pow(2)
        return r0 + (1 - r0) * (1 - cos).pow(5)
    }
}

class Intersection(val t: Double, val shape: Shape) {

    constructor(t: Int, obj: Shape) : this(t.toDouble(), obj)

    fun prepareComputations(ray: Ray, xs: List<Intersection> = listOf()): Computation {

        val containers = mutableListOf<Shape>()
        var n1 = 0.0
        var n2 = 0.0

        for (i in xs) {
            if (i == this) {
                n1 = if (containers.isEmpty()) 1.0 else containers.last().material.refractiveIndex
            }
            if (containers.contains(i.shape)) {
                containers.remove(i.shape)
            } else {
                containers.add(i.shape)
            }
            if (i == this) {
                n2 = if (containers.isEmpty()) 1.0 else containers.last().material.refractiveIndex
                break
            }
        }

        val point = ray.position(t)
        return Computation(
            t = t,
            shape = shape,
            point = point,
            direction = ray.direction,
            preNormalV = shape.normalAt(point),
            n1 = n1,
            n2 = n2
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