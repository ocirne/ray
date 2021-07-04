package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.Cube
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CubeTest {

    data class ExampleRayIntersectsACube(
        val desc: String,
        val origin: Point,
        val direction: Vector,
        val t1: Int,
        val t2: Int
    )

    fun examplesRayIntersectsACube() = listOf(
        ExampleRayIntersectsACube("+x", point(5.0, 0.5, 0.0), vector(-1, 0, 0), 4, 6),
        ExampleRayIntersectsACube("-x", point(-5.0, 0.5, 0.0), vector(1, 0, 0), 4, 6),
        ExampleRayIntersectsACube("+y", point(0.5, 5.0, 0.0), vector(0, -1, 0), 4, 6),
        ExampleRayIntersectsACube("-y", point(0.5, -5.0, 0.0), vector(0, 1, 0), 4, 6),
        ExampleRayIntersectsACube("+z", point(0.5, 0.0, 5.0), vector(0, 0, -1), 4, 6),
        ExampleRayIntersectsACube("-z", point(0.5, 0.0, -5.0), vector(0, 0, 1), 4, 6),
        ExampleRayIntersectsACube("inside", point(0.0, 0.5, 0.0), vector(0, 0, 1), -1, 1)
    )

    @ParameterizedTest
    @MethodSource("examplesRayIntersectsACube")
    fun `Scenario Outline A ray intersects a Cube`(ex: ExampleRayIntersectsACube) {
        val c = Cube()
        val r = Ray(ex.origin, ex.direction)
        val xs = c.localIntersect(r)
        xs.size shouldBe 2
        xs[0].t shouldBe ex.t1
        xs[1].t shouldBe ex.t2
    }

    data class ExampleRayMissesACube(val origin: Point, val direction: Vector)

    fun examplesRayMissesACube() = listOf(
        ExampleRayMissesACube(point(-2, 0, 0), vector(0.2673, 0.5345, 0.8018)),
        ExampleRayMissesACube(point(0, -2, 0), vector(0.8018, 0.2673, 0.5345)),
        ExampleRayMissesACube(point(0, 0, -2), vector(0.5345, 0.8018, 0.2673)),
        ExampleRayMissesACube(point(2, 0, 2), vector(0, 0, -1)),
        ExampleRayMissesACube(point(0, 2, 2), vector(0, -1, 0)),
        ExampleRayMissesACube(point(2, 2, 0), vector(-1, 0, 0))
    )

    @ParameterizedTest
    @MethodSource("examplesRayMissesACube")
    fun `Scenario Outline A ray misses a Cube`(ex: ExampleRayMissesACube) {
        val c = Cube()
        val r = Ray(ex.origin, ex.direction)
        val xs = c.localIntersect(r)
        xs shouldBe beEmpty<Intersection>()
    }

    data class ExampleTheNormalOnTheSurfaceOfACube(val point: Point, val normal: Vector)

    fun examplesTheNormalOnTheSurfaceOfACube() = listOf(
        ExampleTheNormalOnTheSurfaceOfACube(point(1.0, 0.5, -0.8), vector(1, 0, 0)),
        ExampleTheNormalOnTheSurfaceOfACube(point(-1.0, -0.2, 0.9), vector(-1, 0, 0)),
        ExampleTheNormalOnTheSurfaceOfACube(point(-0.4, 1.0, -0.1), vector(0, 1, 0)),
        ExampleTheNormalOnTheSurfaceOfACube(point(0.3, -1.0, -0.7), vector(0, -1, 0)),
        ExampleTheNormalOnTheSurfaceOfACube(point(-0.6, 0.3, 1.0), vector(0, 0, 1)),
        ExampleTheNormalOnTheSurfaceOfACube(point(0.4, 0.4, -1.0), vector(0, 0, -1)),
        ExampleTheNormalOnTheSurfaceOfACube(point(1, 1, 1), vector(1, 0, 0)),
        ExampleTheNormalOnTheSurfaceOfACube(point(-1, -1, -1), vector(-1, 0, 0))
    )

    @ParameterizedTest
    @MethodSource("examplesTheNormalOnTheSurfaceOfACube")
    fun `Scenario Outline The normal on the surface of a Cube`(ex: ExampleTheNormalOnTheSurfaceOfACube) {
        val c = Cube()
        c.localNormalAt(ex.point) shouldBe ex.normal
    }
}