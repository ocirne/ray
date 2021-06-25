package io.github.ocirne.ray.bewegt.canvas

import io.github.ocirne.ray.bewegt.*
import java.io.File
import java.io.PrintWriter
import kotlin.math.sqrt

class PPM(val width: Int, val height: Int) {

    private val pixelData: Array<Array<RgbColor>> = Array(height) { Array(width) { NO_COLOR } }

    private var samplesPerPixel: Int = 0

    fun set(x: Int, y: Int, color: RgbColor) {
        pixelData[y][x] = color
    }

    fun add(x: Int, y: Int, color: RgbColor) {
        pixelData[y][x] += color
    }

    fun incSamples() {
        samplesPerPixel++
    }

    fun writeToFile(filename: String) {
        File(filename).printWriter().use { out ->
            out.println("P3")
            out.println("$width $height")
            out.println("255")
            for (y in height - 1 downTo 0) {
                for (x in 0 until width) {
                    writeColor(out, pixelData[y][x], samplesPerPixel)
                }
            }
        }
    }

    private fun writeColor(out: PrintWriter, pixelColor: RgbColor, samplesPerPixel: Int) {
        val scale = 1.0 / samplesPerPixel
        val r = ppmRepresentation(pixelColor.x, scale)
        val g = ppmRepresentation(pixelColor.y, scale)
        val b = ppmRepresentation(pixelColor.z, scale)
        // Write the translated [0,255] value of each Color component.
        out.println("$r $g $b")
    }

    private fun ppmRepresentation(channel: Double, scale: Double): Int {
        // Replace NaN components with zero. See explanation in Ray Tracing: The Rest of Your Life.
        val c = if (channel != channel) 0.0 else channel
        return sqrt(c * scale).clamp_and_stretch()
    }
}
