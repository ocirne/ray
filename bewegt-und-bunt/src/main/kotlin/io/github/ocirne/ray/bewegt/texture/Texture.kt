package io.github.ocirne.ray.bewegt.texture

import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.canvas.WHITE
import io.github.ocirne.ray.bewegt.math.Point3
import kotlin.math.sin

interface Texture {
    fun value(u: Double, v: Double, p: Point3): RgbColor
}

class SolidColor(private val colorValue: RgbColor) : Texture {

    constructor(red: Double, green: Double, blue: Double) : this(RgbColor(red, green, blue))

    override fun value(u: Double, v: Double, p: Point3): RgbColor {
        return colorValue
    }
}

class CheckerTexture(private val even: Texture, private val odd: Texture) : Texture {

    constructor(evenColor: RgbColor, oddColor: RgbColor) : this(SolidColor(evenColor), SolidColor(oddColor))

    override fun value(u: Double, v: Double, p: Point3): RgbColor {
        val sines = sin(10 * p.x) * sin(10 * p.y) * sin(10 * p.z)
        return if (sines < 0) odd.value(u, v, p) else even.value(u, v, p)
    }
}

class NoiseTexture(private val scale: Double) : Texture {

    private val noise: Perlin = Perlin()

    override fun value(u: Double, v: Double, p: Point3): RgbColor {
        return WHITE * 0.5 * (1.0 + sin(scale * p.z + 10.0 * noise.turbulence(p)))
    }
}
