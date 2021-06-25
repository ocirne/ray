package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.*
import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.math.Point3

class TwoPerlinSpheres : Scene(
    background = RgbColor(0.7, 0.8, 1.0),
    vfov = 20.0
) {

    override fun world(): hittable_list {
        val objects = hittable_list.builder()

        val perlinTexture = noise_texture(4.0)

        objects.add(sphere(Point3(0, -1000, 0), 1000, lambertian(perlinTexture)))
        objects.add(sphere(Point3(0, 2, 0), 2, lambertian(perlinTexture)))

        return objects.build()
    }
}
