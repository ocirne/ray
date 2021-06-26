package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.hittable.HittableList
import io.github.ocirne.ray.bewegt.hittable.Sphere
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.texture.CheckerTexture


class TwoSpheres : Scene(
    background = RGBColor(0.7, 0.8, 1.0),
    vfov = 20.0
) {

    override fun buildWorld(): HittableList {
        val objects = HittableList.Builder()

        val checker = CheckerTexture(RGBColor(0.2, 0.3, 0.1), RGBColor(0.9, 0.9, 0.9))

        objects.add(Sphere(Point3(0, -10, 0), 10, Lambertian(checker)))
        objects.add(Sphere(Point3(0, 10, 0), 10, Lambertian(checker)))

        return objects.build()
    }
}
