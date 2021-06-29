package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Intersections
import io.github.ocirne.ray.challenge.raysphere.Sphere
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

internal class IntersectionTest  {

      @Test
      fun `Scenario An intersection encapsulates t and object`() {
        val s = Sphere()
        val i = Intersection(3.5, s)
        i.t shouldBe 3.5
        i.obj shouldBe s
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

/*
      @Test
      fun `Scenario Precomputing the state of an intersection`() {
  Given r = Ray(point(0, 0, -5), vector(0, 0, 1))
    And shape = sphere()
    And i = intersection(4, shape)
  When comps = prepare_computations(i, r)
  Then comps.t shouldBe i.t
    And comps.obj shouldBe i.obj
    And comps.point shouldBe point(0, 0, -1)
    And comps.eyev shouldBe vector(0, 0, -1)
    And comps.normalv shouldBe vector(0, 0, -1)
      }

      @Test
      fun `Scenario Precomputing the reflection vector`() {
  Given shape = plane()
    And r = Ray(point(0, 1, -1), vector(0, -√2/2, √2/2))
    And i = intersection(√2, shape)
  When comps = prepare_computations(i, r)
  Then comps.reflectv shouldBe vector(0, √2/2, √2/2)
      }

      @Test
      fun `Scenario The hit, when an intersection occurs on the outside`() {
  Given r = Ray(point(0, 0, -5), vector(0, 0, 1))
    And shape = sphere()
    And i = intersection(4, shape)
  When comps = prepare_computations(i, r)
  Then comps.inside shouldBe false
      }

      @Test
      fun `Scenario The hit, when an intersection occurs on the inside`() {
  Given r = Ray(point(0, 0, 0), vector(0, 0, 1))
    And shape = sphere()
    And i = intersection(1, shape)
  When comps = prepare_computations(i, r)
  Then comps.point shouldBe point(0, 0, 1)
    And comps.eyev shouldBe vector(0, 0, -1)
    And comps.inside shouldBe true
      # normal would have been (0, 0, 1), but is inverted!
    And comps.normalv shouldBe vector(0, 0, -1)
      }

      @Test
      fun `Scenario The hit should offset the point`() {
  Given r = Ray(point(0, 0, -5), vector(0, 0, 1))
    And shape = sphere() with:
      | transform | translation(0, 0, 1) |
    And i = intersection(5, shape)
  When comps = prepare_computations(i, r)
  Then comps.over_point.z < -EPSILON/2
    And comps.point.z > comps.over_point.z
      }

            @Test
            fun `Scenario The under point is offset below the surface`() {
  Given r = Ray(point(0, 0, -5), vector(0, 0, 1))
    And shape = glass_sphere() with:
      | transform | translation(0, 0, 1) |
    And i = intersection(5, shape)
    And xs = intersections(i)
  When comps = prepare_computations(i, r, xs)
  Then comps.under_point.z > EPSILON/2
    And comps.point.z < comps.under_point.z
            }

      @Test
      fun `Scenario The hit, when all intersections have positive t`() {
  Given s = sphere()
    And i1 = intersection(1, s)
    And i2 = intersection(2, s)
    And xs = intersections(i2, i1)
  When i = hit(xs)
  Then i shouldBe i1
      }

          @Test
          fun `Scenario The hit, when some intersections have negative t`() {
  Given s = sphere()
    And i1 = intersection(-1, s)
    And i2 = intersection(1, s)
    And xs = intersections(i2, i1)
  When i = hit(xs)
  Then i shouldBe i2
          }

          @Test
          fun `Scenario The hit, when all intersections have negative t`() {
  Given s = sphere()
    And i1 = intersection(-2, s)
    And i2 = intersection(-1, s)
    And xs = intersections(i2, i1)
  When i = hit(xs)
  Then i is nothing
          }

          @Test
          fun `Scenario The hit is always the lowest nonnegative intersection`() {
  Given s = sphere()
  And i1 = intersection(5, s)
  And i2 = intersection(7, s)
  And i3 = intersection(-3, s)
  And i4 = intersection(2, s)
  And xs = intersections(i1, i2, i3, i4)
When i = hit(xs)
Then i shouldBe i4
          }

        @Test
        fun `Scenario Outline: Finding n1 and n2 at various intersections`() {
  Given A = glass_sphere() with:
      | transform                 | scaling(2, 2, 2) |
      | material.refractive_index | 1.5              |
    And B = glass_sphere() with:
      | transform                 | translation(0, 0, -0.25) |
      | material.refractive_index | 2.0                      |
    And C = glass_sphere() with:
      | transform                 | translation(0, 0, 0.25) |
      | material.refractive_index | 2.5                     |
    And r = Ray(point(0, 0, -4), vector(0, 0, 1))
    And xs = intersections(2:A, 2.75:B, 3.25:C, 4.75:B, 5.25:C, 6:A)
  When comps = prepare_computations(xs[<index>], r, xs)
  Then comps.n1 shouldBe <n1>
    And comps.n2 shouldBe <n2>

  Examples:
    | index | n1  | n2  |
    | 0     | 1.0 | 1.5 |
    | 1     | 1.5 | 2.0 |
    | 2     | 2.0 | 2.5 |
    | 3     | 2.5 | 2.5 |
    | 4     | 2.5 | 1.5 |
    | 5     | 1.5 | 1.0 |
        }

      @Test
      fun `Scenario The Schlick approximation under total internal reflection`() {
  Given shape = glass_sphere()
    And r = Ray(point(0, 0, √2/2), vector(0, 1, 0))
    And xs = intersections(-√2/2:shape, √2/2:shape)
  When comps = prepare_computations(xs[1], r, xs)
    And reflectance = schlick(comps)
  Then reflectance shouldBe 1.0
      }

      @Test
      fun `Scenario The Schlick approximation with a perpendicular viewing angle`() {
        Given shape = glass_sphere ()
        And r = Ray (point(0, 0, 0), vector(0, 1, 0))
        And xs = intersections (-1:shape, 1:shape)
        When comps = prepare_computations (xs[1], r, xs)
        And reflectance = schlick (comps)
        Then reflectance shouldBe 0.04
      }

        @Test
        fun `Scenario The Schlick approximation with small angle and n2 > n1`() {
          Given shape = glass_sphere ()
          And r = Ray (point(0, 0.99, -2), vector(0, 0, 1))
          And xs = intersections (1.8589:shape)
          When comps = prepare_computations (xs[0], r, xs)
          And reflectance = schlick (comps)
          Then reflectance shouldBe 0.48873
        }

        @Test
        fun `Scenario An intersection can encapsulate `u` and `v``() {
          Given s = triangle (point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0))
          When i = intersection_with_uv (3.5, s, 0.2, 0.4)
          Then i . u shouldBe 0.2
          And i . v shouldBe 0.4
        }
        
 */
      }