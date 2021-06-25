package io.github.ocirne.ray.bewegt.scene

import io.github.ocirne.ray.bewegt.canvas.BLACK
import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.hittable_list
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3

/** Default values for a scene */
abstract class Scene(
    val aspect_ratio: Double = 16.0 / 9.0,
    val image_width: Int = 600,
    val samples_per_pixel: Int = 100,
    val maxDepth: Int = 50,
    val lookFrom: Point3 = Point3(13, 2, 3),
    val lookAt: Point3 = Point3(0, 0, 0),
    val vup: Vector3 = Vector3(0, 1, 0),
    val distanceToFocus: Double = 10.0,
    val vfov: Double = 40.0,
    val aperture: Double = 0.0,
    val background: RgbColor = BLACK,
    val time0: Double = 0.0,
    val time1: Double = 1.0
) {
    abstract fun world(): hittable_list
}
