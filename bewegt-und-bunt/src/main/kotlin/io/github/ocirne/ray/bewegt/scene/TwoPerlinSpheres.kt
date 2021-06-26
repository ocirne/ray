package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.hittable.HittableList
import io.github.ocirne.ray.bewegt.hittable.Sphere
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.texture.NoiseTexture

class TwoPerlinSpheres : Scene(
    background = RGBColor(0.7, 0.8, 1.0),
    vfov = 20.0
) {

    override fun buildWorld(): HittableList {
        val objects = HittableList.Builder()

        val perlinTexture = NoiseTexture(4.0)

        objects.add(Sphere(Point3(0, -1000, 0), 1000, Lambertian(perlinTexture)))
        objects.add(Sphere(Point3(0, 2, 0), 2, Lambertian(perlinTexture)))

        return objects.build()
    }
}
