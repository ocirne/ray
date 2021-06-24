package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.math.Point3
import kotlin.math.sin

interface texture {
    fun value(u: Double, v: Double, p: Point3) : RgbColor
}

class solidColor(private val colorValue: RgbColor) : texture {

    constructor(red: Double, green: Double, blue: Double): this(RgbColor(red, green, blue))

    override fun value(u: Double, v: Double, p: Point3): RgbColor {
        return colorValue
    }
}

class checker_texture(val even: texture, val odd: texture): texture {

    constructor(c1: RgbColor, c2: RgbColor): this(solidColor(c1), solidColor(c2))

    override fun value(u: Double, v: Double, p: Point3): RgbColor {
        val sines = sin(10*p.x)*sin(10*p.y)*sin(10*p.z)
        return if (sines < 0) odd.value(u, v, p) else even.value(u, v, p)
    }
}

class noise_texture(val scale: Double): texture {

    val noise: perlin = perlin()

    override fun value(u: Double, v: Double, p: Point3): RgbColor {
//        return Color(1.0,1.0,1.0) * noise.turb(scale * p)
        return RgbColor(1.0,1.0,1.0) * 0.5 * (1.0 + sin(scale * p.z + 10.0 * noise.turb(p)))
    }
}
