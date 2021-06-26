package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.hittable.*
import io.github.ocirne.ray.bewegt.material.Dielectric
import io.github.ocirne.ray.bewegt.material.DiffuseLight
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.material.Metal
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3

class CornellBoxBook3 : Scene(
    aspectRatio = 1.0,
    samplesPerPixel = 5,
    lookFrom = Point3(278, 278, -800),
    lookAt = Point3(278, 278, 0)
) {

    override fun buildWorld(): hittable_list {
        val objects = hittable_list.builder()

        val red = Lambertian(RGBColor(.65, .05, .05))
        val white = Lambertian(RGBColor(.73, .73, .73))
        val green = Lambertian(RGBColor(.12, .45, .15))
        val light = DiffuseLight(RGBColor(15, 15, 15))
        val aluminium = Metal(RGBColor(0.8, 0.85, 0.88), 0.0)
        val glass = Dielectric(1.5)

        objects.add(yz_rect(0, 555, 0, 555, 555, green))
        objects.add(yz_rect(0, 555, 0, 555, 0, red))
        objects.add(flip_face(xz_rect(213, 343, 227, 332, 554, light)))
        objects.add(xz_rect(0, 555, 0, 555, 555, white))
        objects.add(xz_rect(0, 555, 0, 555, 0, white))
        objects.add(xy_rect(0, 555, 0, 555, 555, white))

        var box1: hittable = box(Point3(0, 0, 0), Point3(165, 330, 165), white)
        box1 = rotate_y(box1, 15.0)
        box1 = translate(box1, Vector3(265, 0, 295))
        objects.add(box1)

        objects.add(sphere(Point3(190, 90, 190), 90, glass))

        return objects.build()
    }
}
