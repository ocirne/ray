package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.Plane
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.PI

fun box(): Canvas {
    val floorMaterial = Material(color = WHITE, specular = 0.0)
    val leftWallMaterial = Material(color = GREEN, specular = 0.0)
    val rightWallMaterial = Material(color = RED, specular = 0.0)

    val floor = Plane(transform = translation(0, -5, 0), material = floorMaterial)

    val leftWall = Plane(
        transform = translation(-5, 0, 0) * rotationZ(-PI / 2),
        material = leftWallMaterial
    )

    val rightWall = Plane(
        transform = translation(5, 0, 0) * rotationZ(PI / 2),
        material = rightWallMaterial
    )

    val roof = Plane(transform = translation(0, 5, 0), material = floorMaterial)
    val backWall = Plane(transform = translation(0, 0, 5) * rotationX(PI / 2), material = floorMaterial)

    val right = Sphere(
        transform = translation(2.0, -4.0, 1.5) * scaling(1.0, 1.0, 1.0),
        material = Material(
            color = color(0.5, 1.0, 0.1),
            diffuse = 0.7,
            specular = 0.3
        )
    )

    val left = Sphere(
        transform = translation(-1.5, -3.0, 1.5) * scaling(1.5, 1.5, 1.5),
        material = Material(
            color = color(1.0, 0.8, 0.1),
            diffuse = 0.7,
            specular = 0.3
        )
    )

    val bulb = Sphere(
        transform = translation(0.0, 4.0, -0.2) * scaling(0.1, 0.1, 0.1),
        material = Material(
            color = WHITE,
            diffuse = 0.0,
            specular = 0.0
        )
    )

    val lightSource = PointLight(point(0, 4, 0), WHITE)
    val world = World(listOf(floor, leftWall, rightWall, roof, backWall, right, left, bulb), listOf(lightSource))

    val transform = viewTransform(point(0.0, 0.0, -5.0), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(400, 400, PI / 2, transform)
    // render the result to a canvas.
    return camera.render(world)
}
