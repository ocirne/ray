package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.*
import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.material.DiffuseLight
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3

class CornellBox : Scene(
    aspectRatio = 1.0,
    samplesPerPixel = 200,
    lookFrom = Point3(278, 278, -800),
    lookAt = Point3(278, 278, 0),
) {

    override fun world(): hittable_list {
        val objects = hittable_list.builder()

        val red = Lambertian(RgbColor(.65, .05, .05))
        val white = Lambertian(RgbColor(.73, .73, .73))
        val green = Lambertian(RgbColor(.12, .45, .15))
        val light = DiffuseLight(RgbColor(15, 15, 15))

        objects.add(yz_rect(0, 555, 0, 555, 555, green))
        objects.add(yz_rect(0, 555, 0, 555, 0, red))
        objects.add(xz_rect(213, 343, 227, 332, 554, light))
        objects.add(xz_rect(0, 555, 0, 555, 0, white))
        objects.add(xz_rect(0, 555, 0, 555, 555, white))
        objects.add(xy_rect(0, 555, 0, 555, 555, white))

        var box1: hittable = box(Point3(0, 0, 0), Point3(165, 330, 165), white)
        box1 = rotate_y(box1, 15.0)
        box1 = translate(box1, Vector3(265, 0, 295))
        objects.add(box1)

        var box2: hittable = box(Point3(0, 0, 0), Point3(165, 165, 165), white)
        box2 = rotate_y(box2, -18.0)
        box2 = translate(box2, Vector3(130, 0, 65))
        objects.add(box2)

        return objects.build()
    }
}