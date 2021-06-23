package io.github.ocirne.ray

import java.io.PrintWriter
import kotlin.math.sqrt

fun write_color(out: PrintWriter, pixel_color: color, samples_per_pixel: Int) {
    // Divide the color by the number of samples.
    val scale = 1.0 / samples_per_pixel
    val r = sqrt(pixel_color.x() * scale)
    val g = sqrt(pixel_color.y() * scale)
    val b = sqrt(pixel_color.z() * scale)

    // Write the translated [0,255] value of each color component.
    out.println("""${r.clamp_and_stretch()} ${g.clamp_and_stretch()} ${b.clamp_and_stretch()}""")
}

