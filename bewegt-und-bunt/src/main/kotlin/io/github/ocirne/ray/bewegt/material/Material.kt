package io.github.ocirne.ray.bewegt.material

import io.github.ocirne.ray.bewegt.canvas.RGBColor
import io.github.ocirne.ray.bewegt.canvas.NO_COLOR
import io.github.ocirne.ray.bewegt.canvas.WHITE
import io.github.ocirne.ray.bewegt.hittable.HitRecord
import io.github.ocirne.ray.bewegt.math.*
import io.github.ocirne.ray.bewegt.math.Vector3.Companion.times
import io.github.ocirne.ray.bewegt.texture.SolidColor
import io.github.ocirne.ray.bewegt.texture.Texture
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

data class ScatterRecord(
    val specularRay: Ray?,
    val attenuation: RGBColor,
    val pdf: PDF?
) {
    init {
        require((specularRay == null) xor (pdf == null))
    }
}

/**
 * Default Material:
 * - no scatter
 * - no colors are emitted (is not a light)
 */
open class Material {

    open fun scatter(rayIn: Ray, rec: HitRecord): ScatterRecord? {
        return null
    }

    open fun scatteringPdf(rayIn: Ray, rec: HitRecord, scattered: Ray): Double {
        return 0.0
    }

    open fun emitted(rayIn: Ray, rec: HitRecord, u: Double, v: Double, p: Point3): RGBColor {
        return NO_COLOR
    }
}

/** "Matte" material */
class Lambertian(private val albedo: Texture): Material() {

    constructor(c: RGBColor): this(SolidColor(c))

    override fun scatter(rayIn: Ray, rec: HitRecord): ScatterRecord {
        return ScatterRecord(
            specularRay = null,
            attenuation = albedo.value(rec.u, rec.v, rec.p),
            pdf = CosinePDF(rec.normal)
        )
    }

    override fun scatteringPdf(rayIn: Ray, rec: HitRecord, scattered: Ray): Double {
        val cosine = rec.normal.dot(scattered.direction.unitVector())
        return if (cosine < 0.0) 0.0 else cosine/PI
    }
}

class Metal(private val albedo: RGBColor, fuzz: Double): Material() {

    constructor(albedo: RGBColor, fuzz: Int): this(albedo, fuzz.toDouble())

    private val fuzz = min(fuzz, 1.0)

    override fun scatter(rayIn: Ray, rec: HitRecord): ScatterRecord {
        val reflected = rayIn.direction.unitVector().reflect(rec.normal)
        return ScatterRecord(
            specularRay = Ray(rec.p, reflected + fuzz * Vector3.randomInUnitSphere()),
            attenuation = albedo,
            pdf = null)
    }
}

/** Glass, Diamonds, ... */
class Dielectric(private val indexOfRefraction: Double): Material() {

    override fun scatter(rayIn: Ray, rec: HitRecord): ScatterRecord {
        val attenuation = WHITE
        val refractionRatio = if (rec.frontFace) 1.0/indexOfRefraction else indexOfRefraction
        val unitDirection = rayIn.direction.unitVector()
        val cosTheta: Double = min((-unitDirection).dot(rec.normal), 1.0)
        val sinTheta: Double = sqrt(1.0 - cosTheta * cosTheta)
        val cannotRefract = refractionRatio * sinTheta > 1.0
        val direction = if (cannotRefract || reflectance(cosTheta, refractionRatio) > Random.nextDouble())
            unitDirection.reflect(rec.normal)
        else
            unitDirection.refract(rec.normal, refractionRatio)
        val scattered = Ray(rec.p, direction, rayIn.time)
        return ScatterRecord(
            specularRay = scattered,
            attenuation = attenuation,
            pdf = null)
    }

    private fun reflectance(cosine: Double, reflectanceIndex: Double): Double {
        // Use Schlick's approximation for reflectance.
        val r0 = ((1 - reflectanceIndex) / (1 + reflectanceIndex)).pow(2)
        return r0 + (1 - r0) * (1 - cosine).pow(5.0)
    }
}

class DiffuseLight(private val emit: Texture): Material() {

    constructor(c: RGBColor) : this(SolidColor(c))

    override fun scatter(rayIn: Ray, rec: HitRecord): ScatterRecord? {
        return null
    }

    override fun emitted(rayIn: Ray, rec: HitRecord, u: Double, v: Double, p: Point3): RGBColor {
        return if (rec.frontFace) emit.value(u, v, p) else NO_COLOR
    }
}

class Isotropic(private val albedo: Texture): Material() {

    constructor(c: RGBColor): this(SolidColor(c))

    override fun scatter(rayIn: Ray, rec: HitRecord): ScatterRecord? {
        val scattered = Ray(rec.p, Vector3.randomInUnitSphere(), rayIn.time)
        val attenuation = albedo.value(rec.u, rec.v, rec.p)
        TODO("Issue #669")
/*        return ScatterRecord(
            specular_ray = scattered,
            attenuation = attenuation,
            is_specular = true,
            pdf_ptr = null)  // never called, see Issue #669 */
    }
}
