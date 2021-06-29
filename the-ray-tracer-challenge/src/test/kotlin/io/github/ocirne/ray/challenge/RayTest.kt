package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.transformations.scaling
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class RayTest {

    @Test
    fun `Scenario Creating and querying a ray`() {
        val origin = point(1, 2, 3)
        val direction = vector(4, 5, 6)
        val r = Ray(origin, direction)
        r.origin shouldBe origin
        r.direction shouldBe direction
    }

    @Test
    fun `Scenario Computing a point from a distance`() {
        val r = Ray(point(2, 3, 4), vector(1, 0, 0))
        r.position(0.0) shouldBe point(2, 3, 4)
        r.position(1.0) shouldBe point(3, 3, 4)
        r.position(-1.0) shouldBe point(1, 3, 4)
        r.position(2.5) shouldBe point(4.5, 3.0, 4.0)
    }

    @Test
    fun `Scenario Translating a ray`() {
        val r = Ray(point(1, 2, 3), vector(0, 1, 0))
        val m = translation(3, 4, 5)
        val r2 = r.transform(m)
        r2.origin shouldBe point(4, 6, 8)
        r2.direction shouldBe vector(0, 1, 0)
    }

    @Test
    fun `Scenario Scaling a ray`() {
        val r = Ray(point(1, 2, 3), vector(0, 1, 0))
        val m = scaling(2, 3, 4)
        val r2 = r.transform(m)
        r2.origin shouldBe point(2, 6, 12)
        r2.direction shouldBe vector(0, 3, 0)
    }
}