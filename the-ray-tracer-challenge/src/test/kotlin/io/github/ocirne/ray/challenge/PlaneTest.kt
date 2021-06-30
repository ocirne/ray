package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Plane
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class PlaneTest {

    @Test
    fun `Scenario The normal of a plane is constant everywhere`() {
        val p = Plane()
        val n1 = p.localNormalAt(point(0, 0, 0))
        val n2 = p.localNormalAt(point(10, 0, -10))
        val n3 = p.localNormalAt(point(-5, 0, 150))
        n1 shouldBe vector(0, 1, 0)
        n2 shouldBe vector(0, 1, 0)
        n3 shouldBe vector(0, 1, 0)
    }

    @Test
    fun `Scenario Intersect with a ray parallel to the plane`() {
        val p = Plane()
        val r = Ray(point(0, 10, 0), vector(0, 0, 1))
        val xs = p.localIntersect(r)
        xs.size shouldBe 0
    }

    @Test
    fun `Scenario Intersect with a coplanar ray`() {
        val p = Plane()
        val r = Ray(point(0, 0, 0), vector(0, 0, 1))
        val xs = p.localIntersect(r)
        xs.size shouldBe 0
    }

    @Test
    fun `Scenario A ray intersecting a plane from above`() {
        val p = Plane()
        val r = Ray(point(0, 1, 0), vector(0, -1, 0))
        val xs = p.localIntersect(r)
        xs.size shouldBe 1
        xs[0].t shouldBe 1
        xs[0].obj shouldBe p
    }

    @Test
    fun `Scenario A ray intersecting a plane from below`() {
        val p = Plane()
        val r = Ray(point(0, -1, 0), vector(0, 1, 0))
        val xs = p.localIntersect(r)
        xs.size shouldBe 1
        xs[0].t shouldBe 1
        xs[0].obj shouldBe p
    }
}