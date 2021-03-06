package io.github.ocirne.ray.weekend

class box(val p0: point3, val p1: point3, mat: material) : hittable {

    val sides: hittable_list

    init {
        val objects = hittable_list.builder()
        objects.add(xy_rect(p0.x(), p1.x(), p0.y(), p1.y(), p1.z(), mat))
        objects.add(xy_rect(p0.x(), p1.x(), p0.y(), p1.y(), p0.z(), mat))
        objects.add(xz_rect(p0.x(), p1.x(), p0.z(), p1.z(), p1.y(), mat))
        objects.add(xz_rect(p0.x(), p1.x(), p0.z(), p1.z(), p0.y(), mat))
        objects.add(yz_rect(p0.y(), p1.y(), p0.z(), p1.z(), p1.x(), mat))
        objects.add(yz_rect(p0.y(), p1.y(), p0.z(), p1.z(), p0.x(), mat))
        sides = objects.build()
    }

    override fun hit(r: ray, t_min: Double, t_max: Double): hit_record? {
        return sides.hit(r, t_min, t_max)
    }

    override fun bounding_box(time0: Double, time1: Double): aabb {
        return aabb(p0, p1)
    }
}
