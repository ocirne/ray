package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.shapes.glassSphere
import io.github.ocirne.ray.challenge.transformations.rotationZ
import io.github.ocirne.ray.challenge.transformations.scaling
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.PI

internal class SphereTest {

    @Test
    fun `Scenario A ray intersects a sphere at two points`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val s = Sphere()
        val xs = s.intersect(r)
        xs.size shouldBe 2
        xs[0].t shouldBe 4.0
        xs[1].t shouldBe 6.0
    }

    @Test
    fun `Scenario A ray intersects a sphere at a tangent`() {
        val r = Ray(point(0, 1, -5), vector(0, 0, 1))
        val s = Sphere()
        val xs = s.intersect(r)
        xs.size shouldBe 2
        xs[0].t shouldBe 5.0
        xs[1].t shouldBe 5.0
    }

    @Test
    fun `Scenario A ray misses a sphere`() {
        val r = Ray(point(0, 2, -5), vector(0, 0, 1))
        val s = Sphere()
        val xs = s.intersect(r)
        xs should beEmpty()
    }

    @Test
    fun `Scenario A ray originates inside a sphere`() {
        val r = Ray(point(0, 0, 0), vector(0, 0, 1))
        val s = Sphere()
        val xs = s.intersect(r)
        xs.size shouldBe 2
        xs[0].t shouldBe -1.0
        xs[1].t shouldBe 1.0
    }

    @Test
    fun `Scenario A sphere is behind a ray`() {
        val r = Ray(point(0, 0, 5), vector(0, 0, 1))
        val s = Sphere()
        val xs = s.intersect(r)
        xs.size shouldBe 2
        xs[0].t shouldBe -6.0
        xs[1].t shouldBe -4.0
    }

    @Test
    fun `Scenario Intersect sets the object on the intersection`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val s = Sphere()
        val xs = s.intersect(r)
        xs.size shouldBe 2
        xs[0].shape shouldBe s
        xs[1].shape shouldBe s
    }

    @Test
    fun `Scenario Intersecting a scaled sphere with a ray`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val s = Sphere()
        val sm = s.withTransform(scaling(2, 2, 2))
        val xs = sm.intersect(r)
        xs.size shouldBe 2
        xs[0].t shouldBe 3
        xs[1].t shouldBe 7
    }

    @Test
    fun `Scenario Intersecting a translated sphere with a ray`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val s = Sphere()
        val sm = s.withTransform(translation(5, 0, 0))
        val xs = sm.intersect(r)
        xs should beEmpty()
    }

    @Test
    fun `Scenario The normal on a sphere at a point on the x axis`() {
        val s = Sphere()
        val n = s.normalAt(point(1, 0, 0))
        n shouldBe vector(1, 0, 0)
    }

    @Test
    fun `Scenario The normal on a sphere at a point on the y axis`() {
        val s = Sphere()
        val n = s.normalAt(point(0, 1, 0))
        n shouldBe vector(0, 1, 0)
    }

    @Test
    fun `Scenario The normal on a sphere at a point on the z axis`() {
        val s = Sphere()
        val n = s.normalAt(point(0, 0, 1))
        n shouldBe vector(0, 0, 1)
    }

    @Test
    fun `Scenario The normal on a sphere at a nonaxial point`() {
        val s = Sphere()
        val n = s.normalAt(point(magic3, magic3, magic3))
        n shouldBe vector(magic3, magic3, magic3)
    }

    @Test
    fun `Scenario The normal is a normalized vector`() {
        val s = Sphere()
        val n = s.normalAt(point(magic3, magic3, magic3))
        n shouldBe n.normalize()
    }

    @Test
    fun `Scenario Computing the normal on a translated sphere`() {
        val s = Sphere(translation(0, 1, 0))
        val n = s.normalAt(point(0.0, 1.70711, -0.70711))
        n shouldBe vector(0.0, 0.70711, -0.70711)
    }

    @Test
    fun `Scenario Computing the normal on a transformed sphere`() {
        val s = Sphere(rotationZ(PI / 5).scale(1.0, 0.5, 1.0))
        val n = s.normalAt(point(0.0, magic2, -magic2))
        n shouldBe vector(0.0, 0.97014, -0.24254)
    }

    @Test
    fun `Scenario A helper for producing a sphere with a glassy material`() {
        val s = glassSphere()
        s.transform shouldBe identityMatrix
        s.material.transparency shouldBe 1.0
        s.material.refractiveIndex shouldBe 1.5
    }
}
