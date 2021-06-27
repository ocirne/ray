package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.hittable.HittableList
import io.github.ocirne.ray.bewegt.texture.ImageTexture
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.hittable.Sphere

class Earth(private val angle: Double=0.0) : Scene(
    background = RGBColor(0.7, 0.8, 1.0),
    vfov = 20.0,
    samplesPerPixel = 10
) {

    override fun buildWorld(): HittableList {
        val objects = HittableList.Builder()

        val earthTexture = ImageTexture("textures/earthmap.jpg")
        val earthSurface = Lambertian(earthTexture)
        val globe = Sphere(Point3(0, 0, 0), 2, earthSurface).rotate(1, angle)
        objects.add(globe)

        return objects.build()
    }
}
