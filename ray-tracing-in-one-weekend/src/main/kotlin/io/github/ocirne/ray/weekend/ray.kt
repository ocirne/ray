package io.github.ocirne.ray.weekend

class ray(val origin: point3, val direction: vec3, val time: Double = 0.0) {

    fun origin(): point3 { return origin }
    fun direction(): vec3 { return direction }
    fun time(): Double { return time }

    fun at(t: Double): point3 {
        return origin + direction * t
    }
}
