package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.material.Isotropic
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.math.Ray
import io.github.ocirne.ray.bewegt.math.infinity
import io.github.ocirne.ray.bewegt.texture.SolidColor
import io.github.ocirne.ray.bewegt.texture.Texture
import kotlin.math.ln
import kotlin.random.Random

class ConstantMedium(private val boundary: Hittable, density: Double, a: Texture): Hittable() {

    val neg_inv_density = -1.0/density
    val phase_function = Isotropic(a)

    constructor(boundary: Hittable, density: Double, c: RGBColor): this(boundary, density, SolidColor(c))

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        // Print occasional samples when debugging. To enable, set enableDebug true.
        val enableDebug = false
        val debugging = enableDebug && Random.nextDouble() < 0.00001

        val rec1 = boundary.hit(r, -infinity, infinity) ?: return null
        val rec2 = boundary.hit(r, rec1.t+0.0001, infinity) ?: return null

        if (debugging)
            println("t_min=${rec1.t}, t_max=${rec2.t}")

        if (rec1.t < tMin) rec1.t = tMin
        if (rec2.t > tMax) rec2.t = tMax

        if (rec1.t >= rec2.t)
            return null

        if (rec1.t < 0.0)
            rec1.t = 0.0

        val ray_length = r.direction.length()
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

        return HitRecord(p, normal, phase_function, t, u ,v, front_face)
    }

    override fun boundingBox(time0: Double, time1: Double): aabb? {
        return boundary.boundingBox(time0, time1)
    }
}
