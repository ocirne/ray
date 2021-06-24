package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3

class ray(val origin: Point3, val direction: Vector3, val time: Double = 0.0) {

    fun origin(): Point3 { return origin }
    fun direction(): Vector3 { return direction }
    fun time(): Double { return time }

    fun at(t: Double): Point3 {
        return origin + direction * t
    }
}
