package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.math.epsilon
import io.github.ocirne.ray.challenge.math.equalsDelta
import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Intersections
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Plane
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.shapes.glassSphere
import io.github.ocirne.ray.challenge.transformations.scaling
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

internal class IntersectionTest {

    @Test
    fun `Scenario An intersection encapsulates t and object`() {
        val s = Sphere()
        val i = Intersection(3.5, s)
        i.t shouldBe 3.5
        i.shape shouldBe s
    }

    @Test
    fun `Scenario Aggregating intersections`() {
        val s = Sphere()
        val i1 = Intersection(1, s)
        val i2 = Intersection(2, s)
        val xs = Intersections(i1, i2)
        xs.count shouldBe 2
        xs[0].t shouldBe 1
        xs[1].t shouldBe 2
    }

    @Test
    fun `Scenario The hit, when all intersections have positive t`() {
        val s = Sphere()
        val i1 = Intersection(1, s)
        val i2 = Intersection(2, s)
        val xs = Intersections(i2, i1)
        val i = xs.hit()
        i shouldBe i1
    }

    @Test
    fun `Scenario The hit, when some intersections have negative t`() {
        val s = Sphere()
        val i1 = Intersection(-1, s)
        val i2 = Intersection(1, s)
        val xs = Intersections(i2, i1)
        val i = xs.hit()
        i shouldBe i2
    }

    @Test
    fun `Scenario The hit, when all intersections have negative t`() {
        val s = Sphere()
        val i1 = Intersection(-2, s)
        val i2 = Intersection(-1, s)
        val xs = Intersections(i2, i1)
        val i = xs.hit()
        i shouldBe null
    }

    @Test
    fun `Scenario The hit is always the lowest nonnegative intersection`() {
        val s = Sphere()
        val i1 = Intersection(5, s)
        val i2 = Intersection(7, s)
        val i3 = Intersection(-3, s)
        val i4 = Intersection(2, s)
        val xs = Intersections(i1, i2, i3, i4)
        val i = xs.hit()
        i shouldBe i4
    }

    @Test
    fun `Scenario Precomputing the state of an intersection`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val shape = Sphere()
        val i = Intersection(4, shape)
        val comps = i.prepareComputations(r)
        comps.t shouldBe i.t
        comps.shape shouldBe i.shape
        comps.point shouldBe point(0, 0, -1)
        comps.eyeV shouldBe vector(0, 0, -1)
        comps.normalV shouldBe vector(0, 0, -1)
    }

    @Test
    fun `Scenario The hit, when an intersection occurs on the outside`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val shape = Sphere()
        val i = Intersection(4, shape)
        val comps = i.prepareComputations(r)
        comps.inside shouldBe false
    }

    @Test
    fun `Scenario The hit, when an intersection occurs on the inside`() {
        val r = Ray(point(0, 0, 0), vector(0, 0, 1))
        val shape = Sphere()
        val i = Intersection(1, shape)
        val comps = i.prepareComputations(r)
        comps.point shouldBe point(0, 0, 1)
        comps.eyeV shouldBe vector(0, 0, -1)
        comps.inside shouldBe true
        // normal would have been (0, 0, 1), but is inverted!
        comps.normalV shouldBe vector(0, 0, -1)
    }

    @Test
    fun `Scenario The hit should offset the point`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val shape = Sphere(translation(0, 0, 1))
        val i = Intersection(5, shape)
        val comps = i.prepareComputations(r)
        comps.overPoint.z shouldBeLessThan -epsilon / 2
        comps.point.z shouldBeGreaterThan comps.overPoint.z
    }

    @Test
    fun `Scenario Precomputing the reflection vector`() {
        val shape = Plane()
        val r = Ray(point(0, 1, -1), vector(0.0, -magic2, magic2))
        val i = Intersection(sqrt(2.0), shape)
        val comps = i.prepareComputations(r)
        comps.reflectV shouldBe vector(0.0, magic2, magic2)
    }

    data class Example(val index: Int, val n1: Double, val n2: Double)

    @Test
    fun `Scenario Outline Finding n1 and n2 at various intersections`() {
        for (example in listOf(
            Example(0, 1.0, 1.5),
            Example(1, 1.5, 2.0),
            Example(2, 2.0, 2.5),
            Example(3, 2.5, 2.5),
            Example(4, 2.5, 1.5),
            Example(5, 1.5, 1.0)
        )) {
            verifyIntersectionExample(example.index, example.n1, example.n2)
        }
    }

    private fun verifyIntersectionExample(index: Int, n1: Double, n2: Double) {
        val a = glassSphere(scaling(2, 2, 2), refractiveIndex = 1.5)
        val b = glassSphere(translation(0.0, 0.0, -0.25), refractiveIndex = 2.0)
        val c = glassSphere(translation(0.0, 0.0, 0.25), refractiveIndex = 2.5)
        val r = Ray(point(0, 0, -4), vector(0, 0, 1))
        val xs = listOf(
            Intersection(2, a),
            Intersection(2.75, b),
            Intersection(3.25, c),
            Intersection(4.75, b),
            Intersection(5.25, c),
            Intersection(6, a)
        )
        val comps = xs[index].prepareComputations(r, xs)
        comps.n1 shouldBe n1
        comps.n2 shouldBe n2
    }

    @Test
    fun `Scenario The under point is offset below the surface`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val shape = glassSphere(translation(0, 0, 1))
        val i = Intersection(5, shape)
        val xs = listOf(i)
        val comps = i.prepareComputations(r, xs)
        comps.underPoint.z shouldBeGreaterThan epsilon / 2
        comps.point.z shouldBeLessThan comps.underPoint.z
    }

    @Test
    fun `Scenario The Schlick approximation under total internal reflection`() {
        val shape = glassSphere()
        val r = Ray(point(0.0, 0.0, magic2), vector(0, 1, 0))
        val xs = listOf(Intersection(-magic2, shape), Intersection(magic2, shape))
        val comps = xs[1].prepareComputations(r, xs)
        val reflectance = comps.schlick()
        reflectance shouldBe 1.0
    }

    @Test
    fun `Scenario The Schlick approximation with a perpendicular viewing angle`() {
        val shape = glassSphere()
        val r = Ray(point(0, 0, 0), vector(0, 1, 0))
        val xs = listOf(Intersection(-1, shape), Intersection(1, shape))
        val comps = xs[1].prepareComputations(r, xs)
        val reflectance = comps.schlick()
        reflectance.equalsDelta(0.04) shouldBe true
    }

    @Test
    fun `Scenario The Schlick approximation with small angle and n2 gt n1`() {
        val shape = glassSphere()
        val r = Ray(point(0.0, 0.99, -2.0), vector(0, 0, 1))
        val xs = listOf(Intersection(1.8589, shape))
        val comps = xs[0].prepareComputations(r, xs)
        val reflectance = comps.schlick()
        reflectance.equalsDelta(0.48873) shouldBe true
    }
/*
        @Test
        fun `Scenario An intersection can encapsulate `u` and `v``() {
          val s = triangle (point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0))
          val i = intersection_with_uv (3.5, s, 0.2, 0.4)
           i . u shouldBe 0.2
          val i . v shouldBe 0.4
        }
        
 */
}