package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Ray
import kotlin.math.max
import kotlin.math.min

// TODO try optimized version
class AABB(val minimum: Point3, val maximum: Point3) {

    fun hit(r: Ray, t_min: Double, t_max: Double): Boolean {
        val tMin2x = max(min(minT0X(r), maxT0X(r)), t_min)
        val tMax2x = min(max(minT0X(r), maxT0X(r)), t_max)
        if (tMax2x <= tMin2x) {
            return false
        }
        val tMin2y = max(min(minT0Y(r), maxT0Y(r)), t_min)
        val tMax2y = min(max(minT0Y(r), maxT0Y(r)), t_max)
        if (tMax2y <= tMin2y) {
            return false
        }
        val tMin2z = max(min(minT0Z(r), maxT0Z(r)), t_min)
        val tMax2z = min(max(minT0Z(r), maxT0Z(r)), t_max)
        if (tMax2z <= tMin2z) {
            return false
        }
        return true
    }

    private fun minT0X(r: Ray): Double {
        return (minimum.x - r.origin.x) / r.direction.x
    }

    private fun minT0Y(r: Ray): Double {
        return (minimum.y - r.origin.y) / r.direction.y
    }

    private fun minT0Z(r: Ray): Double {
        return (minimum.z - r.origin.z) / r.direction.z
    }

    private fun maxT0X(r: Ray): Double {
        return (maximum.x - r.origin.x) / r.direction.x
    }

    private fun maxT0Y(r: Ray): Double {
        return (maximum.y - r.origin.y) / r.direction.y
    }

    private fun maxT0Z(r: Ray): Double {
        return (maximum.z - r.origin.z) / r.direction.z
    }
}

fun surroundingBox(box0: AABB, box1: AABB): AABB {
    val small = Point3(min(box0.minimum.x, box1.minimum.x),
                       min(box0.minimum.y, box1.minimum.y),
                       min(box0.minimum.z, box1.minimum.z))
    val big = Point3(max(box0.maximum.x, box1.maximum.x),
                     max(box0.maximum.y, box1.maximum.y),
                     max(box0.maximum.z, box1.maximum.z))
    return AABB(small, big)
}
