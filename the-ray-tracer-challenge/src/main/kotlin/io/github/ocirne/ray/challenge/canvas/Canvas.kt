package io.github.ocirne.ray.challenge.canvas

import io.github.ocirne.ray.challenge.tuples.BLACK
import io.github.ocirne.ray.challenge.tuples.Color
import java.io.PrintWriter

class Canvas(val width: Int, val height: Int, initialColor: Color=BLACK) {

    private val scale = 256

    private val pixels = Array(width) { Array(height) { initialColor } }

    fun pixelAt(x: Int, y: Int): Color {
        return pixels[x][y]
    }

    fun writePixel(x: Int, y: Int, color: Color) {
        pixels[x][y] = color
    }

    fun toPPM(out: PrintWriter) {
        out.println("P3")
        out.println("$width $height")
        out.println("255")
        for (y in 0 until height) {
            for (x in 0 until width) {
                out.println(pixelAsString(pixels[x][y]))
            }
        }
    }

    private fun pixelAsString(pixel: Color): String {
        val r = scaleAndClamp(pixel.red)
        val g = scaleAndClamp(pixel.green)
        val b = scaleAndClamp(pixel.blue)
        return "$r $g $b"
    }

    private fun scaleAndClamp(channel: Double): Int {
        return clamp((channel * scale).toInt())
    }

    private fun clamp(value: Int, min: Int=0, max: Int=255): Int =
        if (value < min) min else if (value > max) max else value
}
