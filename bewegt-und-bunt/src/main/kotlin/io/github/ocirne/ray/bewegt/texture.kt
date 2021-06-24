package io.github.ocirne.ray.bewegt

import kotlin.math.sin

interface texture {
    fun value(u: Double, v: Double, p: point3) : color
}

class solid_color(private val color_value: color) : texture {

    constructor(red: Double, green: Double, blue: Double): this(color(red, green, blue))

    override fun value(u: Double, v: Double, p: point3): color {
        return color_value
    }
}

class checker_texture(val even: texture, val odd: texture): texture {

    constructor(c1: color, c2: color): this(solid_color(c1), solid_color(c2))

    override fun value(u: Double, v: Double, p: point3): color {
        val sines = sin(10*p.x())*sin(10*p.y())*sin(10*p.z())
        return if (sines < 0) odd.value(u, v, p) else even.value(u, v, p)
    }
}

class noise_texture(val scale: Double): texture {

    val noise: perlin = perlin()

    override fun value(u: Double, v: Double, p: point3): color {
//        return color(1.0,1.0,1.0) * noise.turb(scale * p)
        return color(1.0,1.0,1.0) * 0.5 * (1.0 + sin(scale * p.z() + 10.0 * noise.turb(p)))
    }
}
