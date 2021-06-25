package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.math.Vector3.Companion.times
import io.github.ocirne.ray.bewegt.math.Ray
import io.github.ocirne.ray.bewegt.scene.Scene
import kotlin.math.tan
import kotlin.random.Random

class Camera(val scene: Scene) {

    private val theta = scene.vfov.degrees_to_radians()
    private val h = tan(theta / 2.0)
    private val viewportHeight = 2.0 * h
    private val viewportWidth = scene.aspectRatio * viewportHeight

    private val w = scene.lookFrom.minus(scene.lookAt).unitVector()
    private val u = scene.vup.cross(w).unitVector()
    private val v = w.cross(u)

    private val origin = scene.lookFrom
    private val horizontal = scene.distanceToFocus * viewportWidth * u
    private val vertical = scene.distanceToFocus * viewportHeight * v
    private val lowerLeftCorner = origin - horizontal / 2.0 - vertical / 2.0 - scene.distanceToFocus * w

    private val lensRadius = scene.aperture / 2

    fun getRay(s: Double, t: Double): Ray {
        val rd = lensRadius * Vector3.randomInUnitDisk()
        val offset = u * rd.x + v * rd.y
        return Ray(
            origin + offset,
            lowerLeftCorner + s * horizontal + t * vertical - origin - offset,
            Random.nextDouble(scene.time0, scene.time1)
        )
    }
}
