package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.raysphere.Sphere
import io.github.ocirne.ray.challenge.transformations.rotationZ
import io.github.ocirne.ray.challenge.transformations.scaling
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

internal class SphereTest {

    private val magic2 = sqrt(2.0) / 2.0
    private val magic3 = sqrt(3.0) / 3.0

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
        xs.size shouldBe 0
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
        xs[0].obj shouldBe s
        xs[1].obj shouldBe s
    }

    @Test
    fun `Scenario A sphere's default transformation`() {
        val s = Sphere()
        s.transform shouldBe identityMatrix
    }

    @Test
    fun `Scenario Changing a sphere's transformation`() {
        val s = Sphere()
        val t = translation(2, 3, 4)
        val sm = s.withTransform(t)
        sm.transform shouldBe t
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
        xs.size shouldBe 0
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
    fun `Scenario A sphere has a default material`() {
        val s = Sphere()
        s.material shouldBe Material()
    }

    @Test
    fun `Scenario A sphere may be assigned a material`() {
        val s = Sphere()
        val m = Material(ambient = 1.0)
        val sm = s.withMaterial(m)
        sm.material shouldBe m
    }

/*
  @Test
  fun `Scenario A helper for producing a sphere with a glassy material`() {
  val s = glass_Sphere()
   s.transform shouldBe identity_matrix
    s.material.transparency shouldBe 1.0
    s.material.refractive_index shouldBe 1.5
  }
    */
}
