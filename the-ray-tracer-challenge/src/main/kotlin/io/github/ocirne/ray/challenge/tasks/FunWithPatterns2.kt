package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.patterns.*
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.Plane
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.PI

fun funWithPatterns2(): Canvas {
    val floorMaterial = Material(color = WHITE, specular = 0.0,
        pattern = SolidPattern(RED))
    val leftWallMaterial = Material(color = GREEN, specular = 0.0,
        pattern = BlendedPattern(
            StripePattern(WHITE, DARK_GREEN, rotationY(PI / 2)),
            StripePattern(DARK_GREEN, WHITE),
            transform = rotationY(PI / 4)))
    val rightWallMaterial = Material(color = RED, specular = 0.0,
        pattern = StripePattern(BLACK, WHITE, rotationY(PI / 3)))
    val roofMaterial = Material(color = RED, specular = 0.0,
        pattern = NestedPattern(
            StripePattern(RED, WHITE, rotationY(PI / 10) * scaling(0.1, 0.1, 0.1)),
            StripePattern(GREEN, BLACK, rotationY(-PI / 10) * scaling(0.1, 0.1, 0.1)),
            scaling(2, 2, 2)))
    val backWallMaterial = Material(color = RED, specular = 0.0,
        pattern = RadialGradientPattern(WHITE, RED, scaling(2, 2, 2)))

    val floor = Plane(transform = translation(0, -4, 0), material = floorMaterial)
    val leftWall = Plane(transform = translation(-4, 0, 0) * rotationZ(-PI / 2), material = leftWallMaterial)
    val rightWall = Plane(transform = translation(4, 0, 0) * rotationZ(PI / 2), material = rightWallMaterial)
    val roof = Plane(transform = translation(0, 4, 0), material = roofMaterial)
    val backWall = Plane(transform = translation(0, 0, 4) * rotationX(PI / 2), material = backWallMaterial)

    val lightSource = PointLight(point(0, 0, 0), WHITE)
    val world = World(listOf(floor, roof, leftWall, rightWall, backWall), listOf(lightSource))

    val transform = viewTransform(point(0.0, 0.0, -8.0), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(600, 600, PI / 2, transform)
    // render the result to a canvas.
    return camera.render(world)
}
