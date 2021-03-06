package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.canvas.WHITE
import io.github.ocirne.ray.bewegt.hittable.*
import io.github.ocirne.ray.bewegt.material.*
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.texture.ImageTexture
import io.github.ocirne.ray.bewegt.texture.NoiseTexture
import kotlin.random.Random

class FinalSceneNextWeek : Scene(
    aspectRatio = 1.0,
    imageWidth = 200, // 800
    lookFrom = Point3(478, 278, -600),
    lookAt = Point3(278, 278, 0)
) {

    override fun buildWorld(): HittableList {
        val boxes1 = HittableList.Builder()
        val ground = Lambertian(RGBColor(0.48, 0.83, 0.53))

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

                boxes1.add(Box(Point3(x0, y0, z0), Point3(x1, y1, z1), ground))
            }
        }
        val objects = HittableList.Builder()

        objects.add(BVHNode(boxes1.build(), time0 = 0.0, time1 = 1.0))

        val light = DiffuseLight(RGBColor(7, 7, 7))
        objects.add(XZRect(123, 423, 147, 412, 554, light).flipFace())

        val center1 = Point3(400, 400, 200)
        val center2 = center1 + Vector3(30, 0, 0)
        val movingSphereMaterial = Lambertian(RGBColor(0.7, 0.3, 0.1))
        objects.add(MovingSphere(center1, center2, 0.0, 1.0, 50.0, movingSphereMaterial))

        objects.add(Sphere(Point3(260, 150, 45), 50, Dielectric(1.5)))
        objects.add(
            Sphere(
                Point3(0, 150, 145), 50, Metal(RGBColor(0.8, 0.8, 0.9), 1.0)
            )
        )

        val boundary1 = Sphere(Point3(360, 150, 145), 70, Dielectric(1.5))
        objects.add(boundary1)
        objects.add(ConstantMedium(boundary1, 0.2, RGBColor(0.2, 0.4, 0.9)))
        val boundary2 = Sphere(Point3(0, 0, 0), 5000, Dielectric(1.5))
        objects.add(ConstantMedium(boundary2, .0001, WHITE))

        val earthMaterial = Lambertian(ImageTexture("earthmap.jpg"))
        objects.add(Sphere(Point3(400, 200, 400), 100, earthMaterial))
        val perlinTexture = NoiseTexture(0.1)
        objects.add(Sphere(Point3(220, 280, 300), 80, Lambertian(perlinTexture)))

        val boxes2 = HittableList.Builder()
        val white = Lambertian(RGBColor(.73, .73, .73))
        val ns = 1000
        for (j in 0 until ns) {
            boxes2.add(Sphere(Point3.random(0.0, 165.0), 10, white))
        }

        objects.add(BVHNode(boxes2.build(), time0 = 0.0, time1 = 1.0)
            .rotate(1, 15.0)
            .translate(Vector3(-100, 270, 395)))

        return objects.build()
    }

    override fun buildLights() = HittableList.Builder()
        .add(XZRect(123, 423, 147, 412, 554, Material()))
        .build()
}
