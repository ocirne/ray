package io.github.ocirne.ray.weekend

import kotlin.math.tan
import kotlin.random.Random

class camera(lookfrom: point3,
             lookat: point3,
             vup: vec3,
             vfov: Double,
             aspect_ratio: Double,
             aperture: Double,
             focus_dist: Double,
             val time0: Double = 0.0,
             val time1: Double = 0.0) {

    val theta = vfov.degrees_to_radians()
    val h = tan(theta/2.0)
    val viewport_height = 2.0 * h
    val viewport_width = aspect_ratio * viewport_height

    val w = lookfrom.minus(lookat).unitVector()
    val u = vup.cross(w).unitVector()
    val v = w.cross(u)

    val origin = lookfrom
    val horizontal = focus_dist * viewport_width * u
    val vertical = focus_dist * viewport_height * v
    val lower_left_corner = origin - horizontal/2.0 - vertical/2.0 - focus_dist * w

    val lens_radius = aperture / 2

    fun get_ray(s: Double, t: Double): ray {
        val rd = lens_radius * vec3.random_in_unit_disk()
        val offset = u * rd.x() + v * rd.y()
        return ray(origin + offset,
            lower_left_corner + s*horizontal + t*vertical - origin - offset,
                    Random.nextDouble(time0, time1))
    }
}
