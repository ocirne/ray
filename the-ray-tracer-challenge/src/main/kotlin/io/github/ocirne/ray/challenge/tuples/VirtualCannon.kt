package io.github.ocirne.ray.challenge.tuples

class Projectile(val position: Point, var velocity: Vector)

class Environment(val gravity: Vector, val wind: Vector)


fun tick(env: Environment, proj: Projectile): Projectile {
    val position = proj.position + proj.velocity
    val velocity = proj.velocity + env.gravity + env.wind
    return Projectile(position, velocity)
}

fun main() {
    // projectile starts one unit above the origin.
    // velocity is normalized to 1 unit/tick.
    var p = Projectile(point(0, 1, 0), vector(1, 1, 0).normalize())
    // gravity -0.1 unit/tick, and wind is -0.01 unit/tick.
    val e = Environment(vector(0.0, -0.1, 0.0), vector(-0.01, 0.0, 0.0))
    var ticks = 0
    while (p.position.y > 0) {
        println("Position: ${p.position.x}, ${p.position.y}")
        p = tick(e, p)
        ticks++
    }
    println("Needed $ticks ticks.")
}
