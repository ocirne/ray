package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.patterns.*
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.Group
import io.github.ocirne.ray.challenge.shapes.Plane
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.PI

fun funWithPatterns3(): Canvas {
    val g1 = Group()
    val g2 = Group(rotationZ(PI/2))
    val leftWallMaterial = Material(color = GREEN, specular = 0.0,
        pattern = BlendedPattern(
            StripePattern(WHITE, DARK_GREEN, rotationY(PI / 2)),
            StripePattern(DARK_GREEN, WHITE),
            transform = rotationY(PI / 4)))
    val leftWall = Plane(transform = rotationZ(PI/2), material = leftWallMaterial)
    g2.addChild(leftWall)
    g1.addChild(g2)

    val lightSource = PointLight(point(0, 2, -8), WHITE)
    val world = World(listOf(g1), listOf(lightSource))

    val transform = viewTransform(point(0.0, 2.0, -8.0), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(600, 600, PI / 2, transform)
    // render the result to a canvas.
    return camera.render(world)
}
