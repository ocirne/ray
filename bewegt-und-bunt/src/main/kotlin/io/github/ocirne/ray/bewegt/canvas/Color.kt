package io.github.ocirne.ray.bewegt.canvas

import io.github.ocirne.ray.bewegt.clamp_and_stretch
import io.github.ocirne.ray.bewegt.math.Vector3
import java.io.PrintWriter
import kotlin.math.sqrt

typealias RgbColor = Vector3

val BLACK = RgbColor(0, 0, 0)
val NO_COLOR = RgbColor(0, 0, 0)
val WHITE = RgbColor(1, 1, 1)
val CYAN = RgbColor (0, 1, 1)

fun writeColor(out: PrintWriter, pixelColor: RgbColor, samplesPerPixel: Int) {
    val scale = 1.0 / samplesPerPixel
    val r = ppmRepresentation(pixelColor.x, scale)
    val g = ppmRepresentation(pixelColor.y, scale)
    val b = ppmRepresentation(pixelColor.z, scale)
    // Write the translated [0,255] value of each Color component.
    out.println("""$r $g $b""")
}

private fun ppmRepresentation(channel: Double, scale: Double): Int {
    // Replace NaN components with zero. See explanation in Ray Tracing: The Rest of Your Life.
    val c = if (channel != channel) 0.0 else channel
    return sqrt(c * scale).clamp_and_stretch()
}