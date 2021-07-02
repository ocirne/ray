package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.transformations.rotationZ
import io.github.ocirne.ray.challenge.transformations.scaling
import io.github.ocirne.ray.challenge.transformations.shearing
import io.github.ocirne.ray.challenge.tuples.*
import java.io.File
import kotlin.math.PI


fun silhouette(): Canvas {
    val wallSize = 4.0
    val canvasSize = 600
    val middle = wallSize / 2
    val canvas = Canvas(canvasSize, canvasSize)

    val eye = point(0, 0, -20)
    val spheres = listOf(
        Sphere(),
        Sphere(scaling(1.0, 0.5, 1.0).translate(-2.0, -2.0, 0.0)),
        Sphere(scaling(0.5, 1.0, 1.0).translate(-2.0, 2.0, 0.0)),
        Sphere(rotationZ(PI / 4).scale(0.5, 1.0, 1.0).translate(2.0, -2.0, 0.0)),
        Sphere(shearing(1, 0, 0, 0, 0, 0).scale(0.5, 1.0, 1.0).translate(2.0, 2.0, 0.0))
    )

    val pixelSize = wallSize / canvasSize

    for (x in 0 until canvasSize) {
        for (y in 0 until canvasSize) {
            val direction = vector(x * pixelSize - middle, y * pixelSize - middle, 10.0)
            val ray = Ray(eye, direction)
            val hit = spheres.map { sphere -> sphere.intersect(ray).isNotEmpty() }.any { it }
            if (hit) {
                canvas.writePixel(x, y, RED)
            } else {
                canvas.writePixel(x, y, BLACK)
            }
        }
    }
    return canvas
}
