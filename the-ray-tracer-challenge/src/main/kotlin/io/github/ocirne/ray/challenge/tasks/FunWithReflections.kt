package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.patterns.*
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.Plane
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import java.io.File
import kotlin.math.PI

fun funWithReflection1(): Canvas {
    val floorMaterial = Material(pattern = CheckersPattern(BLACK, WHITE), specular = 0.0, reflective = 0.0)
    val leftWallMaterial = Material(color = GREEN, reflective = 1.0)
    val rightWallMaterial = Material(color = RED, specular = 1.0, reflective = 0.5)
    val roofMaterial = Material(color = WHITE)
    val backWallMaterial = Material(color = WHITE)

    val floor = Plane(transform = translation(0, -4, 0), material = floorMaterial)
    val leftWall = Plane(transform = translation(-4, 0, 0) * rotationZ(-PI / 2), material = leftWallMaterial)
    val rightWall = Plane(transform = translation(4, 0, 0) * rotationZ(PI / 2), material = rightWallMaterial)
    val roof = Plane(transform = translation(0, 4, 0), material = roofMaterial)
    val backWall = Plane(transform = translation(0, 0, 4) * rotationX(PI / 2), material = backWallMaterial)

    val sphere1 = Sphere(translation(2.0, -2.5, -2.0) * scaling(1.5, 1.5, 1.5),
        material = Material(color=WHITE, reflective = 1.0))

    val sphere2 = Sphere(translation(-2.0, -2.5, -2.0) * scaling(1.5, 1.5, 1.5),
        material = Material(color=BLACK, reflective = 0.5))

    val lightSource = PointLight(point(0.0, 3.5, -3.0), WHITE)
    val world = World(listOf(floor, roof, leftWall, rightWall, backWall, sphere1, sphere2), listOf(lightSource))

    val transform = viewTransform(point(0.0, 0.0, -8.0), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(600, 600, PI / 2, transform)
    // render the result to a canvas.
    return camera.render(world)
}
