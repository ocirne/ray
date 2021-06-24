package io.github.ocirne.ray.weekend

import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

data class hit_record(
    var p: point3,
    var normal: vec3,
    val mat: material,
    var t: Double,
    val u: Double,
    val v: Double,
    var front_face: Boolean) {

    fun set_face_normal(r: ray, outward_normal: vec3) {
        front_face = r.direction().dot(outward_normal) < 0
        normal = if (front_face) outward_normal else -outward_normal
    }
}

interface hittable {

    fun hit(r: ray, t_min: Double, t_max: Double): hit_record?

    fun bounding_box(time0: Double, time1: Double): aabb?

    fun pdf_value(origin: point3, v: vec3): Double {
        return 0.0
    }

    fun random(origin: vec3): vec3 {
        return vec3(1, 0, 0)
    }
}

class translate(val ptr: hittable, val offset: vec3): hittable {

    override fun hit(r: ray, t_min: Double, t_max: Double): hit_record? {
        val moved_r = ray(r.origin() - offset, r.direction(), r.time())
        val rec = ptr.hit(moved_r, t_min, t_max) ?: return null
        rec.p += offset
        rec.set_face_normal(moved_r, rec.normal)
        return rec
    }

    override fun bounding_box(time0: Double, time1: Double): aabb? {
        val output_box = bounding_box(time0, time1) ?: return null
        return aabb(output_box.min() + offset, output_box.max() + offset)
    }
}

class rotate_y(val ptr: hittable, angle: Double): hittable {

    var sin_theta = 0.0
    var cos_theta = 0.0
    var bbox: aabb?

    init {
        val radians = angle.degrees_to_radians()
        sin_theta = sin(radians)
        cos_theta = cos(radians)
        bbox = ptr.bounding_box(0.0, 1.0)
        if (bbox != null) {

            var min = point3(infinity, infinity, infinity)
            var max = point3(-infinity, -infinity, -infinity)

            for (i in 0..1) {
                for (j in 0..1) {
                    for (k in 0..1) {
                        val x = i * bbox!!.max().x() + (1 - i) * bbox!!.min().x()
                        val y = j * bbox!!.max().y() + (1 - j) * bbox!!.min().y()
                        val z = k * bbox!!.max().z() + (1 - k) * bbox!!.min().z()

                        val newx = cos_theta * x + sin_theta * z
                        val newz = -sin_theta * x + cos_theta * z

                        val tester = vec3(newx, y, newz)

                        min = point3(
                            min(min.x, tester.x),
                            min(min.y, tester.y),
                            min(min.z, tester.z)
                        )
                        max = point3(
                            max(max.x, tester.x),
                            max(max.y, tester.y),
                            max(max.z, tester.z)
                        )
                    }
                }
            }
            bbox = aabb(min, max)
        }
    }

    override fun hit(r: ray, t_min: Double, t_max: Double): hit_record? {
        val origin = point3(
            cos_theta * r.origin().x - sin_theta * r.origin().z,
            r.origin.y,
            sin_theta * r.origin().x + cos_theta * r.origin().z
        )
        val direction = vec3(
            cos_theta * r.direction().x - sin_theta * r.direction().z,
            r.direction.y,
            sin_theta * r.direction().x + cos_theta * r.direction().z
        )
        val rotated_r = ray(origin, direction, r.time())
        val rec = ptr.hit(rotated_r, t_min, t_max) ?: return null
        val p = point3(
            cos_theta * rec.p.x + sin_theta * rec.p.z,
            rec.p.y,
            -sin_theta * rec.p.x + cos_theta * rec.p.z
        )
        val normal = vec3(
            cos_theta * rec.normal.x + sin_theta * rec.normal.z,
            rec.normal.y,
            -sin_theta * rec.normal.x + cos_theta * rec.normal.z
        )
        rec.p = p
        rec.set_face_normal(rotated_r, normal)
        return rec
    }

    override fun bounding_box(time0: Double, time1: Double): aabb? {
        return bbox
    }
}

class flip_face(val ptr: hittable): hittable {

    override fun hit(r: ray, t_min: Double, t_max: Double): hit_record? {
        val rec = ptr.hit(r, t_min, t_max) ?: return null
        rec.front_face = !rec.front_face
        return rec
    }

    override fun bounding_box(time0: Double, time1: Double): aabb? {
        return ptr.bounding_box(time0, time1)
    }
}
