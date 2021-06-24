package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.canvas.RgbColor
import io.github.ocirne.ray.bewegt.math.Vector3
import kotlin.math.ln
import kotlin.random.Random

class constant_medium(val boundary: hittable, density: Double, a: texture): hittable {

    val neg_inv_density = -1.0/density
    val phase_function = isotropic(a)

    constructor(boundary: hittable, density: Double, c: RgbColor): this(boundary, density, solidColor(c))

    override fun hit(r: ray, t_min: Double, t_max: Double): hit_record? {
        // Print occasional samples when debugging. To enable, set enableDebug true.
        val enableDebug = false
        val debugging = enableDebug && Random.nextDouble() < 0.00001

        val rec1 = boundary.hit(r, -infinity, infinity) ?: return null
        val rec2 = boundary.hit(r, rec1.t+0.0001, infinity) ?: return null

        if (debugging)
            println("t_min=${rec1.t}, t_max=${rec2.t}")

        if (rec1.t < t_min) rec1.t = t_min
        if (rec2.t > t_max) rec2.t = t_max

        if (rec1.t >= rec2.t)
            return null

        if (rec1.t < 0.0)
            rec1.t = 0.0

        val ray_length = r.direction().length()
        val distance_inside_boundary = (rec2.t - rec1.t) * ray_length
        val hit_distance = neg_inv_density * ln(Random.nextDouble())

        if (hit_distance > distance_inside_boundary)
            return null

        val t = rec1.t + hit_distance / ray_length
        val p = r.at(t)
        val u = 0.0
        val v = 0.0
        if (debugging) {
            println("hit_distance = $hit_distance\nrec.t = $t\nrec.p = $p")
        }
        val normal = Vector3(1,0,0)  // arbitrary
        val front_face = true     // also arbitrary

        return hit_record(p, normal, phase_function, t, u ,v, front_face)
    }

    override fun bounding_box(time0: Double, time1: Double): aabb? {
        return boundary.bounding_box(time0, time1)
    }
}
