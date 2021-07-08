package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.raysphere.Intersection
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.shapes.CSG
import io.github.ocirne.ray.challenge.shapes.Cube
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.shapes.intersectionAllowed
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.collections.beEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

/** Constructive Solid Geometry */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CsgTest {

    @Test
    fun `Scenario CSG is created with an operation and two shapes`() {
        val s1 = Sphere()
        val s2 = Cube()
        val c = CSG("union", s1, s2)
        c.operation shouldBe "union"
        c.left shouldBe s1
        c.right shouldBe s2
        s1.parent shouldBe c
        s2.parent shouldBe c
    }

    data class ExampleCSGRules(
        val op: String,
        val lhit: Boolean,
        val inl: Boolean,
        val inr: Boolean,
        val result: Boolean
    )

    fun examplesCSGRules() = listOf(
        ExampleCSGRules("union", true, true, true, false),
        ExampleCSGRules("union", true, true, false, true),
        ExampleCSGRules("union", true, false, true, false),
        ExampleCSGRules("union", true, false, false, true),
        ExampleCSGRules("union", false, true, true, false),
        ExampleCSGRules("union", false, true, false, false),
        ExampleCSGRules("union", false, false, true, true),
        ExampleCSGRules("union", false, false, false, true),
        ExampleCSGRules("intersection", true, true, true, true),
        ExampleCSGRules("intersection", true, true, false, false),
        ExampleCSGRules("intersection", true, false, true, true),
        ExampleCSGRules("intersection", true, false, false, false),
        ExampleCSGRules("intersection", false, true, true, true),
        ExampleCSGRules("intersection", false, true, false, true),
        ExampleCSGRules("intersection", false, false, true, false),
        ExampleCSGRules("intersection", false, false, false, false),
        ExampleCSGRules("difference", true, true, true, false),
        ExampleCSGRules("difference", true, true, false, true),
        ExampleCSGRules("difference", true, false, true, false),
        ExampleCSGRules("difference", true, false, false, true),
        ExampleCSGRules("difference", false, true, true, true),
        ExampleCSGRules("difference", false, true, false, true),
        ExampleCSGRules("difference", false, false, true, false),
        ExampleCSGRules("difference", false, false, false, false)
    )

    @ParameterizedTest
    @MethodSource("examplesCSGRules")
    fun `Scenario Outline Evaluating the rule for a CSG operation`(ex: ExampleCSGRules) {
        val result = intersectionAllowed(ex.op, ex.lhit, ex.inl, ex.inr)
        result shouldBe ex.result
    }

    data class ExampleFilteringIntersections(val operation: String, val x0: Int, val x1: Int)

    fun examplesFilteringIntersections() = listOf(
        ExampleFilteringIntersections("union", 0, 3),
        ExampleFilteringIntersections("intersection", 1, 2),
        ExampleFilteringIntersections("difference", 0, 1)
    )

    @ParameterizedTest
    @MethodSource("examplesFilteringIntersections")
    fun `Scenario Outline Filtering a list of intersections`(ex: ExampleFilteringIntersections) {
        val s1 = Sphere()
        val s2 = Cube()
        val c = CSG("<operation>", s1, s2)
        val xs = listOf(Intersection(1, s1), Intersection(2, s2), Intersection(3, s1), Intersection(4, s2))
        val result = c.filterIntersections(xs)
        result.size shouldBe 2
        result[0] shouldBe xs[ex.x0]
        result[1] shouldBe xs[ex.x1]
    }

    @Test
    fun `Scenario A ray misses a CSG object`() {
        val c = CSG("union", Sphere(), Cube())
        val r = Ray(point(0, 2, -5), vector(0, 0, 1))
        val xs = c.localIntersect(r)
        xs should beEmpty()
    }

    @Test
    fun `Scenario A ray hits a CSG object`() {
        val s1 = Sphere()
        val s2 = Sphere(translation(0.0, 0.0, 0.5))
        val c = CSG("union", s1, s2)
        val r = Ray(point(0, 0, -5), vector(0, 0, 1))
        val xs = c.localIntersect(r)
        xs.size shouldBe 2
        xs[0].t shouldBe 4
        xs[0].shape shouldBe s1
        xs[1].t shouldBe 6.5
        xs[1].shape shouldBe s2
    }
}