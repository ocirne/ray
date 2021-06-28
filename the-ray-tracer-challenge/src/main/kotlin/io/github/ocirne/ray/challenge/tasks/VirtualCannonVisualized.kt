package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.tuples.RED
import io.github.ocirne.ray.challenge.tuples.point
import io.github.ocirne.ray.challenge.tuples.vector
import java.io.File

fun main() {
    val start = point(0, 1, 0)
    val velocity = vector(1.0, 1.8, 0.0).normalize() * 11.25
    var p = Projectile(start, velocity)

    // gravity -0.1 unit/tick, and wind is -0.01 unit/tick.
    val gravity = vector(0.0, -0.1, 0.0)
    val wind = vector(-0.01, 0.0, 0.0)
    val e = Environment(gravity, wind)

    val c = Canvas(900, 550)

    var ticks = 0
    while (p.position.y > 0) {
        println("Position: ${p.position.x}, ${p.position.y}")
        c.writePixel(p.position.x.toInt(), 550 - p.position.y.toInt(), RED)
        p = tick(e, p)
        ticks++
    }
    println("Needed $ticks ticks.")
    File("output/cannon.ppm").printWriter().use(c::toPPM)
}
