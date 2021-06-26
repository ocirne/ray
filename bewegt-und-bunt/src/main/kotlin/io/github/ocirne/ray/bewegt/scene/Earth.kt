package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.hittable.hittable_list
import io.github.ocirne.ray.bewegt.texture.ImageTexture
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.hittable.sphere

class Earth : Scene(
    background = RGBColor(0.7, 0.8, 1.0),
    vfov = 20.0
) {

    override fun buildWorld(): hittable_list {
        val objects = hittable_list.builder()

        val earthTexture = ImageTexture("earthmap.jpg")
        val earthSurface = Lambertian(earthTexture)
        val globe = sphere(Point3(0, 0, 0), 2, earthSurface)
        objects.add(globe)

        return objects.build()
    }
}
