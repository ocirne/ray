package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.transformations.rotationZ
import io.github.ocirne.ray.challenge.transformations.scaling
import io.github.ocirne.ray.challenge.transformations.shearing
import io.github.ocirne.ray.challenge.tuples.*
import java.io.File
import kotlin.math.PI


fun main() {
    val wallSize = 15.0
    val canvasSize = 600
    val middle = wallSize / 2
    val canvas = Canvas(canvasSize, canvasSize)

    val rayOrigin = point(0, 0, -10)
    val material = Material(Color(1.0, 0.2, 1.0))
    val spheres = listOf(
        Sphere(material = material),
        Sphere(scaling(1.0, 0.5, 1.0).translate(-2.0, -2.0, 0.0), material),
        Sphere(scaling(0.5, 1.0, 1.0).translate(-2.0, 2.0, 0.0), material),
        Sphere(rotationZ(PI / 4).scale(0.5, 1.0, 1.0).translate(2.0, -2.0, 0.0), material),
        Sphere(shearing(1, 0, 0, 0, 0, 0).scale(0.5, 1.0, 1.0).translate(2.0, 2.0, 0.0), material)
    )

    val lightPosition = point(-10, -10, -10)
    val lightColor = WHITE
    val light = PointLight(lightPosition, lightColor)

    val pixelSize = wallSize / canvasSize

    for (x in 0 until canvasSize) {
        for (y in 0 until canvasSize) {
            val position = point(x * pixelSize - middle, y * pixelSize - middle, 10.0)
            val ray = Ray(rayOrigin, (position - rayOrigin).normalize())
            val intersections = spheres
                .map { sphere -> sphere.intersect(ray) }
                .filter { hit -> hit.isNotEmpty() }
                .firstOrNull()
            if (intersections != null) {
                val hit = intersections[0]
                val point = ray.position(hit.t)
                val normal = hit.obj.normalAt(point)
                val eye = -ray.direction
                val color = hit.obj.material.lighting(Sphere(), light, point, eye, normal)
                canvas.writePixel(x, y, color)
            } else {
                canvas.writePixel(x, y, BLACK)
            }
        }
    }

    File("output/silhouette3D.ppm").printWriter().use(canvas::toPPM)
}
