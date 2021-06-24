package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.math.Color
import java.io.PrintWriter
import kotlin.math.sqrt

fun writeColor(out: PrintWriter, pixelColor: Color, samples_per_pixel: Int) {
    val r0 = pixelColor.x
    val g0 =pixelColor.y
    val b0 = pixelColor.z

    // Replace NaN components with zero. See explanation in Ray Tracing: The Rest of Your Life.
    val r1 = if (r0 != r0) 0.0 else r0
    val g1 = if (g0 != g0) 0.0 else g0
    val b1 = if (b0 != b0) 0.0 else b0

    // Divide the Color by the number of samples.
    val scale = 1.0 / samples_per_pixel
    val r2 = sqrt(r1 * scale)
    val g2 = sqrt(g1 * scale)
    val b2 = sqrt(b1 * scale)

    // Write the translated [0,255] value of each Color component.
    out.println("""${r2.clamp_and_stretch()} ${g2.clamp_and_stretch()} ${b2.clamp_and_stretch()}""")
}

