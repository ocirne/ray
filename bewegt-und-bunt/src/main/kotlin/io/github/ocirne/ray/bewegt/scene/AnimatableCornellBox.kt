package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.*
import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3

class AnimatableCornellBox(val angle: Double) : Scene(
    aspectRatio = 1.0,
    imageWidth = 800,
    samplesPerPixel = 5,
    lookFrom = Point3(278, 278, -800),
    lookAt = Point3(278, 278, 0)
) {

    override fun world(): hittable_list {
        val objects = hittable_list.builder()

        val red = lambertian(RgbColor(.65, .05, .05))
        val white = lambertian(RgbColor(.73, .73, .73))
        val green = lambertian(RgbColor(.12, .45, .15))
        val light = diffuse_light(RgbColor(15, 15, 15))
        val aluminium = metal(RgbColor(0.8, 0.85, 0.88), 0.0)
        val glass = dielectric(1.5)
        val diamond = dielectric(2.14)

        objects.add(yz_rect(0, 555, 0, 555, 555, green))
        objects.add(yz_rect(0, 555, 0, 555, 0, red))
        objects.add(flip_face(xz_rect(213, 343, 227, 332, 554, light)))
        objects.add(xz_rect(0, 555, 0, 555, 555, white))
        objects.add(xz_rect(0, 555, 0, 555, 0, white))
        objects.add(xy_rect(0, 555, 0, 555, 555, white))

        var box1: hittable = box(Point3(0, 0, 0), Point3(165, 330, 165), aluminium)
        box1 = rotate_y(box1, angle)
        box1 = translate(box1, Vector3(265, 0, 295))
        objects.add(box1)

        objects.add(sphere(Point3(190, 90, 190), 90, glass))

        objects.add(sphere(Point3(400, 30, 100), 30, diamond))

        return objects.build()
    }
}
