package io.github.ocirne.ray.bewegt

import kotlin.math.min as fmin
import kotlin.math.max as fmax

// TODO try optimized version
class aabb(val minimum: point3, val maximum: point3) {

    fun min(): point3 { return minimum }
    fun max(): point3 { return maximum }

    fun hit(r: ray, t_min: Double, t_max: Double): Boolean {
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

    private fun min_t0_x(r: ray): Double {
        return (minimum.x - r.origin().x) / r.direction().x
    }

    private fun min_t0_y(r: ray): Double {
        return (minimum.y - r.origin().y) / r.direction().y
    }

    private fun min_t0_z(r: ray): Double {
        return (minimum.z - r.origin().z) / r.direction().z
    }

    private fun max_t0_x(r: ray): Double {
        return (maximum.x - r.origin().x) / r.direction().x
    }

    private fun max_t0_y(r: ray): Double {
        return (maximum.y - r.origin().y) / r.direction().y
    }

    private fun max_t0_z(r: ray): Double {
        return (maximum.z - r.origin().z) / r.direction().z
    }

}

fun surrounding_box(box0: aabb, box1: aabb): aabb {
    val small = point3(fmin(box0.min().x(), box1.min().x()),
                       fmin(box0.min().y(), box1.min().y()),
                       fmin(box0.min().z(), box1.min().z()))
    val big = point3(fmax(box0.max().x(), box1.max().x()),
                     fmax(box0.max().y(), box1.max().y()),
                     fmax(box0.max().z(), box1.max().z()))
    return aabb(small, big)
}