package io.github.ocirne.ray.challenge.raysphere

import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.tuples.Point

import io.github.ocirne.ray.challenge.tuples.Vector

class Ray(val origin: Point, val direction: Vector) {

    fun position(t: Double): Point {
        return origin + direction * t
    }

    fun transform(m: Matrix): Ray {
        return Ray(m * origin, m * direction)
    }
}