package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.patterns.CheckersPattern
import io.github.ocirne.ray.challenge.patterns.GradientPattern
import io.github.ocirne.ray.challenge.patterns.RingPattern
import io.github.ocirne.ray.challenge.patterns.StripePattern
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.Plane
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import java.io.File
import kotlin.math.PI

fun funWithPatterns1(): Canvas {
    val floorMaterial = Material(color = WHITE, specular = 0.0, pattern = CheckersPattern(BLACK, WHITE))
    val leftWallMaterial = Material(color = GREEN, specular = 0.0, pattern = StripePattern(BLACK, WHITE, rotationY(PI/2)))
    val rightWallMaterial = Material(color = RED, specular = 0.0, pattern = StripePattern(BLACK, WHITE))
    val roofMaterial = Material(color = RED, specular = 0.0, pattern = GradientPattern(BLACK, WHITE, rotationY(PI/2) * translation(4, 0, 0) * scaling(8, 8, 8)))
    val backWallMaterial = Material(color = RED, specular = 0.0, pattern = RingPattern(BLACK, WHITE, scaling(0.4, 0.4, 0.4)))

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

fun main() {
    val canvas = funWithPatterns1()
    File("output/funWithPatterns1.ppm").printWriter().use(canvas::toPPM)
}
