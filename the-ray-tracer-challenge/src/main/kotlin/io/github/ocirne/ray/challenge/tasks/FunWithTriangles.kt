package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.*
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.triangles.Triangle
import io.github.ocirne.ray.challenge.tuples.*
import kotlin.math.PI
import kotlin.math.sqrt

fun funWithTriangles1(): Canvas {

    val greenMaterial = Material(color= GREEN)
    val redMaterial = Material(color = RED)

    val fx = sqrt(3.0)
    val fy = 2.0

    val shapes = ArrayList<Shape>()
    for (x in 0..10) {
        for (y in 0..10) {
            val bx = x * fx - 10
            val by = y * fy - (x % 2) - 10
            val p1 = getPoint(bx, by)
            val p2 = getPoint(bx+fx, by+fy*0.5)
            val p3 = getPoint(bx, by+fy)
            shapes.add(Triangle(p1, p2, p3, greenMaterial))

            val p4 = getPoint(bx+fx, by+fy*1.5)
            shapes.add(Triangle(p2, p3, p4, redMaterial))
        }
    }

    val lightSource = PointLight(point(0, 0, 20), WHITE)
    val world = World(shapes.toList(), listOf(lightSource))

    val transform = viewTransform(point(-10, 10, 20), point(0, 0, 0), vector(0, 1, 0))
    val camera = Camera(600, 600, PI / 2, transform)

    return camera.render(world)
}

fun getPoint(bx: Double, by: Double): Point {
    val a = sqrt(bx*bx + by*by)
    val bz = sqrt(200.0 - a*a)
    return point(bx, by, bz)
}