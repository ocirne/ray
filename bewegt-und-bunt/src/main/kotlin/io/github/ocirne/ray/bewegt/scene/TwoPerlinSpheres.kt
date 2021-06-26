package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.hittable.hittable_list
import io.github.ocirne.ray.bewegt.hittable.sphere
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.texture.NoiseTexture

class TwoPerlinSpheres : Scene(
    background = RGBColor(0.7, 0.8, 1.0),
    vfov = 20.0
) {

    override fun buildWorld(): hittable_list {
        val objects = hittable_list.builder()

        val perlinTexture = NoiseTexture(4.0)

        objects.add(sphere(Point3(0, -1000, 0), 1000, Lambertian(perlinTexture)))
        objects.add(sphere(Point3(0, 2, 0), 2, Lambertian(perlinTexture)))

        return objects.build()
    }
}
