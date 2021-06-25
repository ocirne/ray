package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.*
import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.canvas.WHITE
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import kotlin.random.Random

class FinalSceneNextWeek : Scene(
    aspectRatio = 1.0,
    imageWidth = 200, // 800
    samplesPerPixel = 20,  // 10000
    lookFrom = Point3(478, 278, -600),
    lookAt = Point3(278, 278, 0)
) {

    override fun world(): hittable_list {
        val boxes1 = hittable_list.builder()
        val ground = lambertian(RgbColor(0.48, 0.83, 0.53))

        val boxesPerSide = 20
        for (i in 0 until boxesPerSide) {
            for (j in 0 until boxesPerSide) {
                val w = 100.0
                val x0 = -1000.0 + i * w
                val z0 = -1000.0 + j * w
                val y0 = 0.0
                val x1 = x0 + w
                val y1 = Random.nextDouble(1.0, 101.0)
                val z1 = z0 + w

                boxes1.add(box(Point3(x0, y0, z0), Point3(x1, y1, z1), ground))
            }
        }
        val objects = hittable_list.builder()

        objects.add(bvh_node(boxes1.build(), time0 = 0.0, time1 = 1.0))

        val light = diffuse_light(RgbColor(7, 7, 7))
        objects.add(xz_rect(123, 423, 147, 412, 554, light))

        val center1 = Point3(400, 400, 200)
        val center2 = center1 + Vector3(30, 0, 0)
        val movingSphereMaterial = lambertian(RgbColor(0.7, 0.3, 0.1))
        objects.add(moving_sphere(center1, center2, 0.0, 1.0, 50.0, movingSphereMaterial))

        objects.add(sphere(Point3(260, 150, 45), 50, dielectric(1.5)))
        objects.add(
            sphere(
                Point3(0, 150, 145), 50, metal(RgbColor(0.8, 0.8, 0.9), 1.0)
            )
        )

        val boundary1 = sphere(Point3(360, 150, 145), 70, dielectric(1.5))
        objects.add(boundary1)
        objects.add(constant_medium(boundary1, 0.2, RgbColor(0.2, 0.4, 0.9)))
        val boundary2 = sphere(Point3(0, 0, 0), 5000, dielectric(1.5))
        objects.add(constant_medium(boundary2, .0001, WHITE))

        val earthMaterial = lambertian(image_texture("earthmap.jpg"))
        objects.add(sphere(Point3(400, 200, 400), 100, earthMaterial))
        val perlinTexture = noise_texture(0.1)
        objects.add(sphere(Point3(220, 280, 300), 80, lambertian(perlinTexture)))

        val boxes2 = hittable_list.builder()
        val white = lambertian(RgbColor(.73, .73, .73))
        val ns = 1000
        for (j in 0 until ns) {
            boxes2.add(sphere(Point3.random(0.0, 165.0), 10, white))
        }

        objects.add(
            translate(
                rotate_y(bvh_node(boxes2.build(), time0 = 0.0, time1 = 1.0), 15.0),
                Vector3(-100, 270, 395)
            )
        )

        return objects.build()
    }
}
