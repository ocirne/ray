package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.transformations.identity
import io.github.ocirne.ray.challenge.tuples.WHITE
import io.github.ocirne.ray.challenge.tuples.point
import java.io.File
import kotlin.math.PI

fun main() {
    val canvas = Canvas(600, 600)

    for (c in 0..11) {
        val m = point(0, 0, 0)
        val transform = identity()
            .translate(0.0, 200.0, 0.0)
            .rotateZ(c * PI/6)
        val p = transform * m
        println("Position: ${p.x}, ${p.y}")
        canvas.writePixel(300 + p.x.toInt(), 300 + p.y.toInt(), WHITE)
    }
    File("output/clock.ppm").printWriter().use(canvas::toPPM)
}
