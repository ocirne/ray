package io.github.ocirne.ray.bewegt.canvas

import io.github.ocirne.ray.bewegt.clamp_and_stretch
import kotlin.math.sqrt

class RgbDataFrame(val width: Int, val height: Int) {

    private val pixelData: Array<Array<RgbColor>> = Array(height) { Array(width) { NO_COLOR } }

    private var samplesPerPixel: Int = 0

    fun plus(x: Int, y: Int, color: RgbColor) {
        pixelData[y][x] += color
    }

    fun incSamples() {
        samplesPerPixel++
    }

    fun atAsString(x: Int, y: Int): String {
        val (r, g, b) = scaledColorTriple(pixelData[y][x])
        return "$r $g $b"
    }

    fun atAsInt(x: Int, y: Int): Int {
        val (r, g, b) = scaledColorTriple(pixelData[y][x])
        return (r shl 16) + (g shl 8) + b
    }

    private fun scaledColorTriple(pixelColor: RgbColor): Triple<Int, Int, Int> {
        val scale = 1.0 / samplesPerPixel
        val r = scaled(pixelColor.x, scale)
        val g = scaled(pixelColor.y, scale)
        val b = scaled(pixelColor.z, scale)
        return Triple(r, g, b)
    }

    private fun scaled(channel: Double, scale: Double): Int {
        // Replace NaN components with zero. See explanation in Ray Tracing: The Rest of Your Life.
        val c = if (channel != channel) 0.0 else channel
        return sqrt(c * scale).clamp_and_stretch()
    }
}
