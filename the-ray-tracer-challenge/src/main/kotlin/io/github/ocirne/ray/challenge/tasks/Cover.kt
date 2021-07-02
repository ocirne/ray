package io.github.ocirne.ray.challenge.tasks

import io.github.ocirne.ray.challenge.canvas.Camera
import io.github.ocirne.ray.challenge.canvas.Canvas
import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.lights.PointLight
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.shapes.Cube
import io.github.ocirne.ray.challenge.shapes.Plane
import io.github.ocirne.ray.challenge.transformations.*
import io.github.ocirne.ray.challenge.tuples.*
import java.io.File
import kotlin.math.PI

fun cover(): Canvas {

    // Camera
    val transform = viewTransform(point(-6, 6, -10), point(6, 0, 6), vector(-0.45, 1.0, 0.0))
    // 1680 / 1050
    val camera = Camera(100, 100, 0.785, transform)

    // light sources

    val light1 = PointLight(point(50, 100, -50), WHITE)
//    val light2 = PointLight(point(-400, 50, -10), color(0.2, 0.2, 0.2))

    val lights = listOf(light1)

    // define some constants to avoid duplication

    val whiteMaterial = Material(WHITE, diffuse = 0.7, specular = 0.0, reflective = 0.1)
    val blueMaterial = Material(color(0.537, 0.831, 0.914), diffuse = 0.7, specular = 0.0, reflective = 0.1)
    val redMaterial = Material(color(0.941, 0.322, 0.388), diffuse = 0.7, specular = 0.0, reflective = 0.1)
    val purpleMaterial = Material(color(0.373, 0.404, 0.550), diffuse = 0.7, specular = 0.0, reflective = 0.1)

    val standardTransform = translation(1.0, -1.0, 1.0).scale(0.5, 0.5, 0.5)

    val largeObject = standardTransform.scale(3.5, 3.5, 3.5)
    val mediumObject = standardTransform.scale(3.0, 3.0, 3.0)
    val smallObject = standardTransform.scale(2.0, 2.0, 2.0)

    // a white backdrop for the scene

    val backdrop = Plane(
        translation(0, 0, 500) * rotationX(PI / 2),
        Material(ambient = 1.0, diffuse = 0.0, specular = 0.0)
    )

    // describe the elements of the scene

    val sphere = Sphere(
        largeObject,
        Material(
            color = color(0.373, 0.404, 0.550),
            diffuse = 0.2,
            ambient = 0.0,
            specular = 1.0,
            shininess = 200.0,
            reflective = 0.7,
            transparency = 0.7,
            refractiveIndex = 1.5
        )
    )

    val shapes = listOf(
        backdrop,
        sphere,
        Cube(mediumObject.translate(4.0, 0.0, 0.0), whiteMaterial),
        Cube(largeObject.translate(8.5, 1.5, -0.5), blueMaterial),
        Cube(largeObject.translate(0.0, 0.0, 4.0), redMaterial),
        Cube(smallObject.translate(4.0, 0.0, 4.0), whiteMaterial),
        Cube(mediumObject.translate(7.5, 0.5, 4.0), purpleMaterial),
        Cube(mediumObject.translate(-0.25, 0.25, 8.0), whiteMaterial),
        Cube(largeObject.translate(4.0, 1.0, 7.5), blueMaterial),
        Cube(mediumObject.translate(10.0, 2.0, 7.5), redMaterial),
        Cube(smallObject.translate(8.0, 2.0, 12.0), whiteMaterial),
        Cube(smallObject.translate(20.0, 1.0, 9.0), whiteMaterial),
        Cube(largeObject.translate(-0.5, -5.0, 0.25), blueMaterial),
        Cube(largeObject.translate(4.0, -4.0, 0.0), redMaterial),
        Cube(largeObject.translate(8.5, -4.0, 0.0), whiteMaterial),
        Cube(largeObject.translate(0.0, -4.0, 4.0), whiteMaterial),
        Cube(largeObject.translate(-0.5, -4.5, 8.0), purpleMaterial),
        Cube(largeObject.translate(0.0, -8.0, 4.0), whiteMaterial),
        Cube(largeObject.translate(-0.5, -8.5, 8.0), whiteMaterial)
    )

    val world = World(shapes, lights)
    return camera.render(world)
}

fun main() {
    val canvas = cover()
    File("output/cover.ppm").printWriter().use(canvas::toPPM)
}
