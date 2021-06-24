package io.github.ocirne.ray.bewegt

import kotlin.math.abs

class onb(val u: vec3, val v: vec3, val w: vec3) {

    fun local(a: Double, b: Double, c: Double) : vec3 {
        return a*u + b*v + c*w
    }

    fun local(a: vec3) : vec3 {
        return a.x()*u + a.y()*v + a.z()*w
    }

    companion object {
        fun build_from_w(n: vec3): onb {
            val w = n.unitVector()
            val a = if (abs(w.x()) > 0.9) vec3(0, 1, 0) else vec3(1, 0, 0)
            val v = w.cross(a).unitVector()
            val u = w.cross(v)
            return onb(u, v, w)
        }
    }
}
