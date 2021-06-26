package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.hittable.*
import io.github.ocirne.ray.bewegt.material.*
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3

class AnimatableCornellBox(private val angle: Double) : Scene(
    aspectRatio = 1.0,
    imageWidth = 200,
    samplesPerPixel = 10,
    lookFrom = Point3(278, 278, -800),
    lookAt = Point3(278, 278, 0)
) {

    override fun buildWorld(): hittable_list {
        val objects = hittable_list.builder()

        val red = Lambertian(RGBColor(.65, .05, .05))
        val white = Lambertian(RGBColor(.73, .73, .73))
        val green = Lambertian(RGBColor(.12, .45, .15))
        val orange = Lambertian(RGBColor(.55, .85, .12))
        val blue = Lambertian(RGBColor(.12, .25, .76))
        val light = DiffuseLight(RGBColor(15, 15, 15))
        val aluminium = Metal(RGBColor(0.8, 0.85, 0.88), 0.0)
        val glass = Dielectric(1.5)
        val diamond = Dielectric(2.14)

        objects.add(yz_rect(0, 555, 0, 555, 555, green))
        objects.add(yz_rect(0, 555, 0, 555, 0, red))
        objects.add(FlipFace(xz_rect(213, 343, 227, 332, 554, light)))
        objects.add(xz_rect(0, 555, 0, 555, 555, white))
        objects.add(xz_rect(0, 555, 0, 555, 0, white))
        objects.add(xy_rect(0, 555, 0, 555, 555, white))

        var box1: Hittable = box(Point3(0, 5, 0), Point3(164, 440, 164), aluminium)
        box1 = Translate(box1, Vector3(-82, 0, -82))
        box1 = rotate_y(box1, angle)
        box1 = Translate(box1, Vector3(265+82, 0, 295+82))
        objects.add(box1)

        var box2: Hittable = box(Point3(0, 440, 0), Point3(164, 441, 164), white)
        box2 = Translate(box2, Vector3(-82, 0, -82))
        box2 = rotate_y(box2, angle)
        box2 = Translate(box2, Vector3(265+82, 0, 295+82))
        objects.add(box2)

        objects.add(sphere(Point3(190, 90, 190), 90, glass))
        objects.add(sphere(Point3(450, 30, 100), 30, diamond))
        objects.add(sphere(Point3(330, 40, 60), 40, orange))
        objects.add(sphere(Point3(50, 20, 80), 20, blue))

        return objects.build()
    }

    override fun buildLights() = hittable_list.builder()
        .add(xz_rect(213, 343, 227, 332, 554, Material()))
        .add(sphere(Point3(190, 90, 190), 90, Material()))
        .add(sphere(Point3(400, 30, 100), 30, Material()))
        .build()
}
