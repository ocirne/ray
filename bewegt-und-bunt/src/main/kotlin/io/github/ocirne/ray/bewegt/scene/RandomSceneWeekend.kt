package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.*
import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import kotlin.random.Random

class RandomSceneWeekend : Scene(
    background = RgbColor(0.7, 0.8, 1.0),
    vfov = 20.0,
    aperture = 0.1
) {

    override fun world(): hittable_list {
        val builder = hittable_list.builder()

        val checker = checker_texture(RgbColor(0.2, 0.3, 0.1), RgbColor(0.9, 0.9, 0.9))
        builder.add(sphere(Point3(0, -1000, 0), 1000, lambertian(checker)))

        for (a in -11..10) {
            for (b in -11..10) {
                val chooseMaterial = Random.nextDouble()
                val center = Point3(a + 0.9 * Random.nextDouble(), 0.2, b + 0.9 * Random.nextDouble())

                if ((center - Point3(4.0, 0.2, 0.0)).length() > 0.9) {
                    when {
                        chooseMaterial < 0.8 -> {
                            // diffuse
                            val albedo = RgbColor.random() * RgbColor.random()
                            val sphereMaterial = lambertian(albedo)
                            val center2 = center + Vector3(0.0, Random.nextDouble(0.0, 0.5), 0.0)
                            builder.add(moving_sphere(center, center2, 0.0, 1.0, 0.2, sphereMaterial))
                        }
                        chooseMaterial < 0.95 -> {
                            // metal
                            val albedo = RgbColor.random(0.5, 1.0)
                            val fuzz = Random.nextDouble(0.5)
                            val sphereMaterial = metal(albedo, fuzz)
                            builder.add(sphere(center, 0.2, sphereMaterial))
                        }
                        else -> {
                            // glass
                            val sphereMaterial = dielectric(1.5)
                            builder.add(sphere(center, 0.2, sphereMaterial))
                        }
                    }
                }
            }
        }

        val material1 = dielectric(1.5)
        builder.add(sphere(Point3(0, 1, 0), 1, material1))

        val material2 = lambertian(RgbColor(0.4, 0.2, 0.1))
        builder.add(sphere(Point3(-4, 1, 0), 1, material2))

        val material3 = metal(RgbColor(0.7, 0.6, 0.5), 0)
        builder.add(sphere(Point3(4, 1, 0), 1, material3))

        return builder.build()
    }
}
