package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.hittable.hittable_list
import io.github.ocirne.ray.bewegt.hittable.sphere
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.texture.CheckerTexture


class TwoSpheres : Scene(
    background = RgbColor(0.7, 0.8, 1.0),
    vfov = 20.0
) {

    override fun buildWorld(): hittable_list {
        val objects = hittable_list.builder()

        val checker = CheckerTexture(RgbColor(0.2, 0.3, 0.1), RgbColor(0.9, 0.9, 0.9))

        objects.add(sphere(Point3(0, -10, 0), 10, Lambertian(checker)))
        objects.add(sphere(Point3(0, 10, 0), 10, Lambertian(checker)))

        return objects.build()
    }
}
