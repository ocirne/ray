package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.hittable.HittableList
import io.github.ocirne.ray.bewegt.hittable.Sphere
import io.github.ocirne.ray.bewegt.hittable.XYRect
import io.github.ocirne.ray.bewegt.material.DiffuseLight
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.texture.NoiseTexture

class SimpleLight : Scene(
    lookFrom = Point3(26, 3, 6),
    lookAt = Point3(0, 2, 0),
    vfov = 20.0
) {

    override fun buildWorld(): HittableList {
        val objects = HittableList.Builder()

        val perlinTexture = NoiseTexture(4.0)
        objects.add(Sphere(Point3(0, -1000, 0), 1000, Lambertian(perlinTexture)))
        objects.add(Sphere(Point3(0, 2, 0), 2, Lambertian(perlinTexture)))

        val diffuseLight = DiffuseLight(RGBColor(4, 4, 4))
        objects.add(XYRect(3, 5, 1, 3, -2, diffuseLight))

        return objects.build()
    }
}
