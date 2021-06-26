package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.*
import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.hittable.hittable_list
import io.github.ocirne.ray.bewegt.hittable.sphere
import io.github.ocirne.ray.bewegt.material.DiffuseLight
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.texture.noise_texture

class SimpleLight : Scene(
    lookFrom = Point3(26, 3, 6),
    lookAt = Point3(0, 2, 0),
    vfov = 20.0
) {

    override fun world(): hittable_list {
        val objects = hittable_list.builder()

        val perlinTexture = noise_texture(4.0)
        objects.add(sphere(Point3(0, -1000, 0), 1000, Lambertian(perlinTexture)))
        objects.add(sphere(Point3(0, 2, 0), 2, Lambertian(perlinTexture)))

        val diffuseLight = DiffuseLight(RgbColor(4, 4, 4))
        objects.add(xy_rect(3, 5, 1, 3, -2, diffuseLight))

        return objects.build()
    }
}
