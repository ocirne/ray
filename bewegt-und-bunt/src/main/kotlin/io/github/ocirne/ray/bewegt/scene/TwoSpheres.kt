package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.*
import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.math.Point3


class TwoSpheres : Scene(
    background = RgbColor(0.7, 0.8, 1.0),
    vfov = 20.0
) {

    override fun world(): hittable_list {
        val objects = hittable_list.builder()

        val checker = checker_texture(RgbColor(0.2, 0.3, 0.1), RgbColor(0.9, 0.9, 0.9))

        objects.add(sphere(Point3(0, -10, 0), 10, lambertian(checker)))
        objects.add(sphere(Point3(0, 10, 0), 10, lambertian(checker)))

        return objects.build()
    }
}