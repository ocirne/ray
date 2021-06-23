package io.github.ocirne.ray.weekend

import kotlin.math.PI
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random


interface material {

    fun scatter(r_in: ray, rec: hit_record): Triple<color, ray, Double>? {
        return null
    }

    fun scattering_pdf(r_in: ray, rec: hit_record, scattered: ray): Double {
        return 0.0
    }

    fun emitted(u: Double, v: Double, p: point3): color {
        return color(0, 0, 0)
    }
}

class lambertian(val albedo: texture): material {

    val random_hemisphere_sampling = false

    constructor(c: color): this(solid_color(c))

    override fun scatter(r_in: ray, rec: hit_record): Triple<color, ray, Double> {
        if (random_hemisphere_sampling) {
            val direction = vec3.random_in_unit_sphere()
            val scattered = ray(rec.p, direction.unitVector(), r_in.time())
            val alb = albedo.value(rec.u, rec.v, rec.p)
            val pdf = 0.5 / PI
            return Triple(alb, scattered, pdf)
        }
        var scatter_direction = rec.normal + vec3.random_unit_vector()
        if (scatter_direction.near_zero()) {
            scatter_direction = rec.normal
        }
        val scattered = ray(rec.p, scatter_direction.unitVector(), r_in.time())
        val alb = albedo.value(rec.u, rec.v, rec.p)
        val pdf = rec.normal.dot(scattered.direction()) / PI
        return Triple(alb, scattered, pdf)
    }

    override fun scattering_pdf(r_in: ray, rec: hit_record, scattered: ray): Double {
        val cosine = rec.normal.dot(scattered.direction().unitVector())
        return if (cosine < 0.0) 0.0 else cosine/PI
    }
}

class metal(val albedo: color, f: Double): material {

    constructor(albedo: color, f: Int): this(albedo, f.toDouble())

    val fuzz = if (f < 1.0) f else 1.0

    override fun scatter(r_in: ray, rec: hit_record): Triple<color, ray, Double>? {
        val reflected = r_in.direction.unitVector().reflect(rec.normal)
        val scattered = ray(rec.p, reflected + fuzz * vec3.random_in_unit_sphere(), r_in.time())
        return if (scattered.direction().dot(rec.normal) > 0) Triple(albedo, scattered, 0.0) else null
    }
}

class dielectric(val index_of_refraction: Double): material {

    override fun scatter(r_in: ray, rec: hit_record): Triple<color, ray, Double>? {
        val attenuation = color(1, 1, 1)
        val refraction_ratio = if (rec.front_face) 1.0/index_of_refraction else index_of_refraction

        val unit_direction = r_in.direction().unitVector()
        val cos_theta: Double = min((-unit_direction).dot(rec.normal), 1.0)
        val sin_theta: Double = sqrt(1.0 - cos_theta * cos_theta)
        val cannot_refract = refraction_ratio * sin_theta > 1.0
        val direction = if (cannot_refract || reflectance(cos_theta, refraction_ratio) > Random.nextDouble())
            unit_direction.reflect(rec.normal)
        else
            unit_direction.refract(rec.normal, refraction_ratio)
        val scattered = ray(rec.p, direction, r_in.time())
        return Triple(attenuation, scattered, 0.0)
    }

    private fun reflectance(cosine: Double, ref_idx: Double): Double {
        // Use Schlick's approximation for reflectance.
        var r0 = (1 - ref_idx) / (1 + ref_idx)
        r0 *= r0
        return r0 + (1 - r0) * (1 - cosine).pow(5.0)
    }
}

class diffuse_light(val emit: texture): material {

    constructor(c: color) : this(solid_color(c))

    override fun scatter(r_in: ray, rec: hit_record): Triple<color, ray, Double>? {
        return null
    }

    override fun emitted(u: Double, v: Double, p: point3): color {
        return emit.value(u, v, p)
    }
}

class isotropic(val albedo: texture): material {

    constructor(c: color): this(solid_color(c))

    override fun scatter(r_in: ray, rec: hit_record): Triple<color, ray, Double>? {
        val scattered = ray(rec.p, vec3.random_in_unit_sphere(), r_in.time())
        val attenuation = albedo.value(rec.u, rec.v, rec.p)
        return Triple(attenuation, scattered, 0.0)
    }
}
