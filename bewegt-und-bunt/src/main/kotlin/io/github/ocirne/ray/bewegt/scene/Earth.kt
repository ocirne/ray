package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.hittable_list
import io.github.ocirne.ray.bewegt.image_texture
import io.github.ocirne.ray.bewegt.lambertian
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.sphere

class Earth : Scene(
    background = RgbColor(0.7, 0.8, 1.0),
    vfov = 20.0
) {

    override fun world(): hittable_list {
        val objects = hittable_list.builder()

        val earthTexture = image_texture("earthmap.jpg")
        val earthSurface = lambertian(earthTexture)
        val globe = sphere(Point3(0, 0, 0), 2, earthSurface)
        objects.add(globe)

        return objects.build()
    }
}
