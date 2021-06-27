package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.hittable.HittableList
import io.github.ocirne.ray.bewegt.texture.ImageTexture
import io.github.ocirne.ray.bewegt.material.Lambertian
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.hittable.Sphere
import io.github.ocirne.ray.bewegt.hittable.XYRect
import io.github.ocirne.ray.bewegt.material.DiffuseLight
import io.github.ocirne.ray.bewegt.material.Material

/** TODO Größenverhältnisse prüfen */
class SolarEclipse : Scene(
    aspectRatio = 2.0,
    lookFrom = Point3(0, 0, -200),
    lookAt = Point3(0, 0, 0),
    samplesPerPixel = 1000,
    vfov = 12.0
) {

    private val dx = 15
    private val dy = 10
    private val dz = 0

    override fun buildWorld(): HittableList {
        val objects = HittableList.Builder()

        val starTexture = ImageTexture("textures/2k_stars_milky_way.jpg")
        val earthTexture = ImageTexture("textures/2k_earth_daymap.jpg")
        val moonTexture = ImageTexture("textures/2k_moon.jpg")

        val stars = Lambertian(starTexture)
        val sunLight = DiffuseLight(RGBColor(200, 200, 200))
        val earthSurface = Lambertian(earthTexture)
        val moonSurface = Lambertian(moonTexture)

        // Rotation of Spheres doesn't work as expected, Issue #845
        objects.add(XYRect(-100, 100, -50, 50, 100, stars))
        objects.add(Sphere(Point3(200 + dx, 100 + dy, -200 + dz), 10.0, sunLight))
        objects.add(Sphere(Point3(0 + dx, 0 + dy, 0 + dz), 2.5, moonSurface))
        objects.add(Sphere(Point3(-40 + dx, -20 + dy, 40 + dz), 9.1, earthSurface))

        return objects.build()
    }

    override fun buildLights() = HittableList.Builder()
        .add(Sphere(Point3(200 + dx, 100 + dy, -200 + dz), 10.0, Material()))
        .build()
}
