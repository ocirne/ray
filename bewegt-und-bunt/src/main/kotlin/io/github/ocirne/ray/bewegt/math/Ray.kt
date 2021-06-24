package io.github.ocirne.ray.bewegt.math

class Ray(val origin: Point3, val direction: Vector3, val time: Double = 0.0) {

    fun at(t: Double): Point3 {
        return origin + direction * t
    }
}
