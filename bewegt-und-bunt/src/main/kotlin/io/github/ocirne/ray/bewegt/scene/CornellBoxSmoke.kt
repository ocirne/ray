package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.BLACK
import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.canvas.WHITE
import io.github.ocirne.ray.bewegt.hittable.*
import io.github.ocirne.ray.bewegt.material.DiffuseLight
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.material.Material
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3

class CornellBoxSmoke : Scene(
    aspectRatio = 1.0,
    imageWidth = 200, // 600
    samplesPerPixel = 100,
    lookFrom = Point3(278, 278, -800),
    lookAt = Point3(278, 278, 0),
) {

    override fun buildWorld(): HittableList {
        val objects = HittableList.Builder()

        val red = Lambertian(RGBColor(.65, .05, .05))
        val white = Lambertian(RGBColor(.73, .73, .73))
        val green = Lambertian(RGBColor(.12, .45, .15))
        val light = DiffuseLight(RGBColor(15, 15, 15))

        objects.add(yz_rect(0, 555, 0, 555, 555, green))
        objects.add(yz_rect(0, 555, 0, 555, 0, red))
        objects.add(xz_rect(213, 343, 227, 332, 554, light).flipFace())
        objects.add(xz_rect(0, 555, 0, 555, 555, white))
        objects.add(xz_rect(0, 555, 0, 555, 0, white))
        objects.add(xy_rect(0, 555, 0, 555, 555, white))

        objects.add(box(Point3(0, 0, 0), Point3(165, 330, 165), white)
            .rotate(1, 15.0)
            .translate(Vector3(265, 0, 295))
            .toConstantMedium(0.01, BLACK))

        objects.add(box(Point3(0, 0, 0), Point3(165, 165, 165), white)
            .rotate(1, -18.0)
            .translate(Vector3(130, 0, 65))
            .toConstantMedium(0.01, WHITE))

        return objects.build()
    }

    override fun buildLights() = HittableList.Builder()
        .add(xz_rect(213, 343, 227, 332, 554, Material()))
        .build()
}