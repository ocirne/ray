package io.github.ocirne.ray.bewegt

import io.github.ocirne.ray.bewegt.math.Color
import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import io.github.ocirne.ray.bewegt.math.Vector3.Companion.times
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

data class scatter_record(
    val specular_ray: ray?,
    val is_specular: Boolean,
    val attenuation: Color,
    val pdf_ptr: pdf?
)

open class material {

    open fun scatter(r_in: ray, rec: hit_record): scatter_record? {
        return null
    }

    open fun scattering_pdf(r_in: ray, rec: hit_record, scattered: ray): Double {
        return 0.0
    }

    open fun emitted(r_in: ray, rec: hit_record, u: Double, v: Double, p: Point3): Color {
        return Color(0, 0, 0)
    }
}

class lambertian(val albedo: texture): material() {

    constructor(c: Color): this(solid_Color(c))

    override fun scatter(r_in: ray, rec: hit_record): scatter_record {
        return scatter_record(
            specular_ray = null,
            is_specular = false,
            attenuation = albedo.value(rec.u, rec.v, rec.p),
            pdf_ptr = cosine_pdf(rec.normal))
    }

    override fun scattering_pdf(r_in: ray, rec: hit_record, scattered: ray): Double {
        val cosine = rec.normal.dot(scattered.direction().unitVector())
        return if (cosine < 0.0) 0.0 else cosine/PI
    }
}

class metal(val albedo: Color, f: Double): material() {

    constructor(albedo: Color, f: Int): this(albedo, f.toDouble())

    val fuzz = if (f < 1.0) f else 1.0

    override fun scatter(r_in: ray, rec: hit_record): scatter_record {
        val reflected = r_in.direction.unitVector().reflect(rec.normal)
        return scatter_record(
            specular_ray = ray(rec.p, reflected + fuzz * Vector3.randomInUnitSphere()),
            attenuation = albedo,
            is_specular = true,
            pdf_ptr = null)
    }
}

class dielectric(val index_of_refraction: Double): material() {

    override fun scatter(r_in: ray, rec: hit_record): scatter_record {
        val attenuation = Color(1, 1, 1)
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
        return scatter_record(
            specular_ray = scattered,
            attenuation = attenuation,
            is_specular = true,
            pdf_ptr = null)
    }

    private fun reflectance(cosine: Double, ref_idx: Double): Double {
        // Use Schlick's approximation for reflectance.
        var r0 = (1 - ref_idx) / (1 + ref_idx)
        r0 *= r0
        return r0 + (1 - r0) * (1 - cosine).pow(5.0)
    }
}

class diffuse_light(val emit: texture): material() {

    constructor(c: Color) : this(solid_Color(c))

    override fun scatter(r_in: ray, rec: hit_record): scatter_record? {
        return null
    }

    override fun emitted(r_in: ray, rec: hit_record, u: Double, v: Double, p: Point3): Color {
        return if (rec.front_face) emit.value(u, v, p) else Color(0, 0, 0)
    }
}

class isotropic(val albedo: texture): material() {

    constructor(c: Color): this(solid_Color(c))

    override fun scatter(r_in: ray, rec: hit_record): scatter_record? {
        val scattered = ray(rec.p, Vector3.randomInUnitSphere(), r_in.time())
        val attenuation = albedo.value(rec.u, rec.v, rec.p)
        throw NotImplementedError()
/*        return scatter_record(
            specular_ray = scattered,
            attenuation = attenuation,
            is_specular = true,
            pdf_ptr = null)  // never called, see Issue #669 */
    }
}
