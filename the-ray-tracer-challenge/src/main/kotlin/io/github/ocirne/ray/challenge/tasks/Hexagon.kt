package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.*
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.PI
import kotlin.random.Random

fun hexagonCorner(): Shape {
    val corner = Sphere(translation(0, 0, -1) * scaling(0.25, 0.25, 0.25))
    return corner
}

fun hexagonEdge(): Shape {
    val edge = Cylinder(
        minimum = 0.0, maximum = 1.0, transform = translation(0, 0, -1) *
                rotationY(-PI / 6) *
                rotationZ(-PI / 2) *
                scaling(0.25, 1.0, 0.25)
    )
    return edge
}

fun hexagonSide(transform: Matrix): Shape {
    val side = Group(transform)
    side.addChild(hexagonCorner())
    side.addChild(hexagonEdge())

    return side
}

fun hexagon(): Shape {
    val hex = Group()

    for (n in 0..5) {
        val side = hexagonSide(rotationY(n * PI / 3))
        hex.addChild(side)
    }
    return hex
}

fun hexagonMain(): Canvas {
    val lightSource = PointLight(point(0, 4, -4), WHITE)
    val world = World(listOf(hexagon()), listOf(lightSource))

    val transform = viewTransform(point(1.0, 2.0, -2.0), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(600, 600, PI / 4, transform)
    // render the result to a canvas.
    return camera.render(world)
}
