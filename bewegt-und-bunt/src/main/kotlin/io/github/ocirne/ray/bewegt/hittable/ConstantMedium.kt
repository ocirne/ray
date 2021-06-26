package io.github.ocirne.ray.bewegt.hittable

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.material.Isotropic
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.math.Ray
import io.github.ocirne.ray.bewegt.math.infinity
import io.github.ocirne.ray.bewegt.texture.SolidColor
import io.github.ocirne.ray.bewegt.texture.Texture
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class ConstantMedium(private val boundary: Hittable, density: Double, texture: Texture): Hittable() {

    private val negInvDensity = -1.0/density
    private val phaseFunction = Isotropic(texture)

    constructor(boundary: Hittable, density: Double, color: RGBColor): this(boundary, density, SolidColor(color))

    override fun hit(r: Ray, tMin: Double, tMax: Double): HitRecord? {
        val rec1 = boundary.hit(r, -infinity, infinity) ?: return null
        val rec2 = boundary.hit(r, rec1.t+0.0001, infinity) ?: return null
        val preT1 = max(rec1.t, tMin)
        val t2 = min(rec2.t, tMax)
        if (preT1 >= t2) {
            return null
        }
        val t1 = max(preT1, 0.0)
        val rayLength = r.direction.length()
        val distanceInsideBoundary = (t2 - t1) * rayLength
        val hitDistance = negInvDensity * ln(Random.nextDouble())
        if (hitDistance > distanceInsideBoundary) {
            return null
        }
        val t = t1 + hitDistance / rayLength
        val p = r.at(t)
        val u = 0.0
        val v = 0.0
        val normal = Vector3(1,0,0)  // arbitrary
        val frontFace = true     // also arbitrary

        return HitRecord(p, normal, phaseFunction, t, u ,v, frontFace)
    }

    override fun boundingBox(time0: Double, time1: Double): AABB? {
        return boundary.boundingBox(time0, time1)
    }
}
