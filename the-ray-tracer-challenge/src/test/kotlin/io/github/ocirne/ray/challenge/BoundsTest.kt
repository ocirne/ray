package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.shapes.*
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.tuples.point
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.awt.geom.QuadCurve2D
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.Double.Companion.POSITIVE_INFINITY

internal class BoundsTest {

    @Test
    fun `Scenario Bounds for a unit sphere`() {
        val s = Sphere()
        val bounds = s.bounds()
        bounds.minimum shouldBe point(-1, -1, -1)
        bounds.maximum shouldBe point(1, 1, 1)
    }

    @Test
    fun `Scenario Bounds for a unit cube`() {
        val c = Cube()
        val bounds = c.bounds()
        bounds.minimum shouldBe point(-1, -1, -1)
        bounds.maximum shouldBe point(1, 1, 1)
    }

    @Test
    fun `Scenario Bounds for a plane`() {
        val p = Plane()
        val bounds = p.bounds()
        bounds.minimum.x shouldBe NEGATIVE_INFINITY
        bounds.minimum.y shouldBe 0.0
        bounds.minimum.z shouldBe NEGATIVE_INFINITY
        bounds.maximum.x shouldBe POSITIVE_INFINITY
        bounds.maximum.y shouldBe 0.0
        bounds.maximum.z shouldBe POSITIVE_INFINITY
    }

    @Test
    fun `Scenario Bounds for a cylinder`() {
        val c = Cylinder()
        val bounds = c.bounds()
        bounds.minimum.x shouldBe -1.0
        bounds.minimum.y shouldBe NEGATIVE_INFINITY
        bounds.minimum.z shouldBe -1.0
        bounds.maximum.x shouldBe 1.0
        bounds.maximum.y shouldBe POSITIVE_INFINITY
        bounds.maximum.z shouldBe 1.0
    }

    @Test
    fun `Scenario Bounds for a finite cylinder`() {
        val c = Cylinder(minimum = -5.0, maximum = 3.0)
        val bounds = c.bounds()
        bounds.minimum shouldBe point(-1, -5, -1)
        bounds.maximum shouldBe point(1, 3, 1)
    }

    @Test
    fun `Scenario Bounds for a cone`() {
        val c = Cone()
        val bounds = c.bounds()
        bounds.minimum.x shouldBe NEGATIVE_INFINITY
        bounds.minimum.y shouldBe NEGATIVE_INFINITY
        bounds.minimum.z shouldBe NEGATIVE_INFINITY
        bounds.maximum.x shouldBe POSITIVE_INFINITY
        bounds.maximum.y shouldBe POSITIVE_INFINITY
        bounds.maximum.z shouldBe POSITIVE_INFINITY
    }

    @Test
    fun `Scenario Bounds for a finite cone`() {
        val c = Cone(minimum = -2.0, maximum = 3.0)
        val bounds = c.bounds()
        bounds.minimum shouldBe point(-2, -2, -2)
        bounds.maximum shouldBe point(3, 3, 3)
    }

    @Test
    fun `Scenario Bounds for a group`() {
        val s1 = Sphere(translation(1, 0, 1))
        val s2 = Sphere(translation(-1, 0, -1))
        val s3 = Sphere(translation(0, 4, 0))
        val g = Group()
        g.addChild(s1)
        g.addChild(s2)
        g.addChild(s3)
        val bounds = g.bounds()
        bounds.minimum shouldBe point(-2, -1, -2)
        bounds.maximum shouldBe point(2, 5, 2)
    }
}