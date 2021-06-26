package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.hittable.HittableList
import io.github.ocirne.ray.bewegt.hittable.MovingSphere
import io.github.ocirne.ray.bewegt.hittable.Sphere
import io.github.ocirne.ray.bewegt.material.Dielectric
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.material.Metal
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.texture.CheckerTexture
import kotlin.random.Random

class RandomSceneWeekend : Scene(
    background = RGBColor(0.7, 0.8, 1.0),
    vfov = 20.0,
    aperture = 0.1
) {

    override fun buildWorld(): HittableList {
        val builder = HittableList.Builder()

        val checker = CheckerTexture(RGBColor(0.2, 0.3, 0.1), RGBColor(0.9, 0.9, 0.9))
        builder.add(Sphere(Point3(0, -1000, 0), 1000, Lambertian(checker)))

        for (a in -11..10) {
            for (b in -11..10) {
                val chooseMaterial = Random.nextDouble()
                val center = Point3(a + 0.9 * Random.nextDouble(), 0.2, b + 0.9 * Random.nextDouble())

                if ((center - Point3(4.0, 0.2, 0.0)).length() > 0.9) {
                    when {
                        chooseMaterial < 0.8 -> {
                            // diffuse
                            val albedo = RGBColor.random() * RGBColor.random()
                            val sphereMaterial = Lambertian(albedo)
                            val center2 = center + Vector3(0.0, Random.nextDouble(0.0, 0.5), 0.0)
                            builder.add(MovingSphere(center, center2, 0.0, 1.0, 0.2, sphereMaterial))
                        }
                        chooseMaterial < 0.95 -> {
                            // metal
                            val albedo = RGBColor.random(0.5, 1.0)
                            val fuzz = Random.nextDouble(0.5)
                            val sphereMaterial = Metal(albedo, fuzz)
                            builder.add(Sphere(center, 0.2, sphereMaterial))
                        }
                        else -> {
                            // glass
                            val sphereMaterial = Dielectric(1.5)
                            builder.add(Sphere(center, 0.2, sphereMaterial))
                        }
                    }
                }
            }
        }

        val material1 = Dielectric(1.5)
        builder.add(Sphere(Point3(0, 1, 0), 1, material1))

        val material2 = Lambertian(RGBColor(0.4, 0.2, 0.1))
        builder.add(Sphere(Point3(-4, 1, 0), 1, material2))

        val material3 = Metal(RGBColor(0.7, 0.6, 0.5), 0)
        builder.add(Sphere(Point3(4, 1, 0), 1, material3))

        return builder.build()
    }
}
