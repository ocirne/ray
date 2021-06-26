package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Ray
import kotlin.math.min as fmin
import kotlin.math.max as fmax

// TODO try optimized version
class AABB(val minimum: Point3, val maximum: Point3) {

    fun hit(r: Ray, t_min: Double, t_max: Double): Boolean {
        val t_min2x = fmax(fmin(min_t0_x(r), max_t0_x(r)), t_min)
        val t_max2x = fmin(fmax(min_t0_x(r), max_t0_x(r)), t_max)
        if (t_max2x <= t_min2x) {
            return false
        }
        val t_min2y = fmax(fmin(min_t0_y(r), max_t0_y(r)), t_min)
        val t_max2y = fmin(fmax(min_t0_y(r), max_t0_y(r)), t_max)
        if (t_max2y <= t_min2y) {
            return false
        }
        val t_min2z = fmax(fmin(min_t0_z(r), max_t0_z(r)), t_min)
        val t_max2z = fmin(fmax(min_t0_z(r), max_t0_z(r)), t_max)
        if (t_max2z <= t_min2z) {
            return false
        }
        return true
    }

    private fun min_t0_x(r: Ray): Double {
        return (minimum.x - r.origin.x) / r.direction.x
    }

    private fun min_t0_y(r: Ray): Double {
        return (minimum.y - r.origin.y) / r.direction.y
    }

    private fun min_t0_z(r: Ray): Double {
        return (minimum.z - r.origin.z) / r.direction.z
    }

    private fun max_t0_x(r: Ray): Double {
        return (maximum.x - r.origin.x) / r.direction.x
    }

    private fun max_t0_y(r: Ray): Double {
        return (maximum.y - r.origin.y) / r.direction.y
    }

    private fun max_t0_z(r: Ray): Double {
        return (maximum.z - r.origin.z) / r.direction.z
    }

}

fun surroundingBox(box0: AABB, box1: AABB): AABB {
    val small = Point3(fmin(box0.minimum.x, box1.minimum.x),
                       fmin(box0.minimum.y, box1.minimum.y),
                       fmin(box0.minimum.z, box1.minimum.z))
    val big = Point3(fmax(box0.maximum.x, box1.maximum.x),
                     fmax(box0.maximum.y, box1.maximum.y),
                     fmax(box0.maximum.z, box1.maximum.z))
    return AABB(small, big)
}
