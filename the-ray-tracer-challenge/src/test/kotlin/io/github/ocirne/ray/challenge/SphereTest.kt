package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.raysphere.Sphere
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class SphereTest {

    @Test
    fun `Scenario A ray intersects a sphere at two points`() {
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val s = Sphere()
        val xs = s.intersect(r)
        xs.size shouldBe 2
        xs[0] shouldBe 4.0
        xs[1] shouldBe 6.0
    }

    @Test
    fun `Scenario A ray intersects a sphere at a tangent`() {
        val r = Ray(point(0, 1, -5), vector(0, 0, 1))
        val s = Sphere()
        val xs = s.intersect(r)
        xs.size shouldBe 2
        xs[0] shouldBe 5.0
        xs[1] shouldBe 5.0
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
        xs[0] shouldBe -1.0
        xs[1] shouldBe 1.0
    }

    @Test
    fun `Scenario A sphere is behind a ray`() {
        val r = Ray(point(0, 0, 5), vector(0, 0, 1))
        val s = Sphere()
        val xs = s.intersect(r)
        xs.size shouldBe 2
        xs[0] shouldBe -6.0
        xs[1] shouldBe -4.0
    }

    /*
  @Test
  fun `Scenario Intersect sets the object on the intersection`() {
  val r = Ray(point(0, 0, -5), vector(0, 0, 1))
    val s = Sphere()
  val xs = s.intersect(r)
   xs.size shouldBe 2
    xs[0].object shouldBe s
    xs[1].object shouldBe s
  }

  @Test
  fun `Scenario A sphere's default transformation`() {
  val s = Sphere()
   s.transform shouldBe identity_matrix
  }

  @Test
  fun `Scenario Changing a sphere's transformation`() {
  val s = Sphere()
    And t = translation(2, 3, 4)
  val set_transform(s, t)
   s.transform shouldBe t
  }

  @Test
  fun `Scenario Intersecting a scaled sphere with a ray`() {
  val r = Ray(point(0, 0, -5), vector(0, 0, 1))
    And s = Sphere()
  val set_transform(s, scaling(2, 2, 2))
    And xs = intersect(s, r)
   xs.count shouldBe 2
    And xs[0].t shouldBe 3
    And xs[1].t shouldBe 7
  }

  @Test
  fun `Scenario Intersecting a translated sphere with a ray`() {
  val r = Ray(point(0, 0, -5), vector(0, 0, 1))
    And s = Sphere()
  val set_transform(s, translation(5, 0, 0))
    And xs = intersect(s, r)
   xs.count shouldBe 0
  }

  @Test
  fun `Scenario The normal on a sphere at a point on the x axis`() {
  val s = Sphere()
  val n = normal_at(s, point(1, 0, 0))
   n shouldBe vector(1, 0, 0)
  }

  @Test
  fun `Scenario The normal on a sphere at a point on the y axis`() {
  val s = Sphere()
  val n = normal_at(s, point(0, 1, 0))
   n shouldBe vector(0, 1, 0)
  }

  @Test
  fun `Scenario The normal on a sphere at a point on the z axis`() {
  val s = Sphere()
  val n = normal_at(s, point(0, 0, 1))
   n shouldBe vector(0, 0, 1)
  }

  @Test
  fun `Scenario The normal on a sphere at a nonaxial point`() {
  val s = Sphere()
  val n = normal_at(s, point(√3/3, √3/3, √3/3))
   n shouldBe vector(√3/3, √3/3, √3/3)
  }

  @Test
  fun `Scenario The normal is a normalized vector`() {
  val s = Sphere()
  val n = normal_at(s, point(√3/3, √3/3, √3/3))
   n shouldBe normalize(n)
  }

  @Test
  fun `Scenario Computing the normal on a translated sphere`() {
  val s = Sphere()
    And set_transform(s, translation(0, 1, 0))
  val n = normal_at(s, point(0, 1.70711, -0.70711))
   n shouldBe vector(0, 0.70711, -0.70711)
  }

  @Test
  fun `Scenario Computing the normal on a transformed sphere`() {
  val s = Sphere()
    And m = scaling(1, 0.5, 1) * rotation_z(PI/5)
    And set_transform(s, m)
  val n = normal_at(s, point(0, √2/2, -√2/2))
   n shouldBe vector(0, 0.97014, -0.24254)
  }

  @Test
  fun `Scenario A sphere has a default material`() {
  val s = Sphere()
  val m = s.material
   m shouldBe material()
  }

  @Test
  fun `Scenario A sphere may be assigned a material`() {
  val s = Sphere()
    val m = material()
    val m.ambient = 1
  val s.material = m
   s.material shouldBe m
  }

  @Test
  fun `Scenario A helper for producing a sphere with a glassy material`() {
  val s = glass_sphere()
   s.transform shouldBe identity_matrix
    s.material.transparency shouldBe 1.0
    s.material.refractive_index shouldBe 1.5
  }
    */
}
