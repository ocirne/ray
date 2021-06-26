package io.github.ocirne.ray.bewegt.math

import io.github.ocirne.ray.bewegt.math.Vector3.Companion.times
import kotlin.math.abs

class ONB(val u: Vector3, val v: Vector3, val w: Vector3) {

    fun local(a: Double, b: Double, c: Double): Vector3 {
        return a * u + b * v + c * w
    }

    fun local(a: Vector3): Vector3 {
        return a.x * u + a.y * v + a.z * w
    }

    companion object {
        fun buildFromW(n: Vector3): ONB {
            val w = n.unitVector()
            val a = if (abs(w.x) > 0.9) Vector3(0, 1, 0) else Vector3(1, 0, 0)
            val v = w.cross(a).unitVector()
            val u = w.cross(v)
            return ONB(u, v, w)
        }
    }
}
