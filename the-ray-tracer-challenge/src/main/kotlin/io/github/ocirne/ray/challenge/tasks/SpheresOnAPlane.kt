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

fun spheresOnAPlane(): Canvas {
    val floorMaterial = Material(
        color = color(1.0, 0.9, 0.9),
        specular = 0.0
    )

    val floor = Plane(
        material = floorMaterial
    )

    val middle = Sphere(
        transform = translation(-0.5, 1.0, 0.5),
        Material(
            color = color(0.1, 1.0, 0.5),
            diffuse = 0.7,
            specular = 0.3
        )
    )

    val right = Sphere(
        transform = translation(1.5, 0.5, -0.5) * scaling(0.5, 0.5, 0.5),
        material = Material(
            color = color(0.5, 1.0, 0.1),
            diffuse = 0.7,
            specular = 0.3
        )
    )

    val left = Sphere(
        transform = translation(-1.5, 0.33, -0.75) * scaling(0.33, 0.33, 0.33),
        material = Material(
            color = color(1.0, 0.8, 0.1),
            diffuse = 0.7,
            specular = 0.3
        )
    )

    val lightSource = PointLight(point(-10, 10, -10), WHITE)
    val world = World(listOf(floor, middle, right, left), listOf(lightSource))

    val transform = viewTransform(point(0.0, 1.5, -5.0), point(0, 1, 0), vector(0, 1, 0))
    val camera = Camera(100, 50, PI / 3, transform)
    // render the result to a canvas.
    return camera.render(world)
}
