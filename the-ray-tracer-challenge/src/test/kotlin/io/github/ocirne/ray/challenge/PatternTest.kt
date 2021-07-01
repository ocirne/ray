package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.patterns.*
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.transformations.scaling
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test


class TestPattern(transform: Matrix = identityMatrix) : Pattern(transform) {

    override fun patternAt(objectPoint: Point): Color {
        val p = transform.inverse() * objectPoint
        return color(p.x, p.y, p.z)
    }
}


internal class PatternTest {

    @Test
    fun `Scenario Creating a stripe pattern`() {
        val pattern = StripePattern(WHITE, BLACK)
        pattern.a shouldBe WHITE
        pattern.b shouldBe BLACK

    }

    @Test
    fun `Scenario A stripe pattern is constant in y`() {
        val pattern = StripePattern(WHITE, BLACK)
        pattern.patternAt(point(0, 0, 0)) shouldBe WHITE
        pattern.patternAt(point(0, 1, 0)) shouldBe WHITE
        pattern.patternAt(point(0, 2, 0)) shouldBe WHITE
    }

    @Test
    fun `Scenario A stripe pattern is constant in z`() {
        val pattern = StripePattern(WHITE, BLACK)
        pattern.patternAt(point(0, 0, 0)) shouldBe WHITE
        pattern.patternAt(point(0, 0, 1)) shouldBe WHITE
        pattern.patternAt(point(0, 0, 2)) shouldBe WHITE
    }

    @Test
    fun `Scenario A stripe pattern alternates in x`() {
        val pattern = StripePattern(WHITE, BLACK)
        pattern.patternAt(point(0, 0, 0)) shouldBe WHITE
        pattern.patternAt(point(0.9, 0.0, 0.0)) shouldBe WHITE
        pattern.patternAt(point(1, 0, 0)) shouldBe BLACK
        pattern.patternAt(point(-0.1, 0.0, 0.0)) shouldBe BLACK
        pattern.patternAt(point(-1, 0, 0)) shouldBe BLACK
        pattern.patternAt(point(-1.1, 0.0, 0.0)) shouldBe WHITE
    }

    @Test
    fun `Scenario The default pattern transformation`() {
        val pattern = TestPattern()
        pattern.transform shouldBe identityMatrix
    }

    @Test
    fun `Scenario Assigning a transformation`() {
        val pattern = TestPattern(translation(1, 2, 3))
        pattern.transform shouldBe translation(1, 2, 3)
    }

    @Test
    fun `Scenario A pattern with an object transformation`() {
        val shape = Sphere(scaling(2, 2, 2))
        val pattern = TestPattern()
        val c = pattern.patternAtShape(shape, point(2, 3, 4))
        c shouldBe color(1.0, 1.5, 2.0)
    }

    @Test
    fun `Scenario A pattern with a pattern transformation`() {
        val shape = Sphere()
        val pattern = TestPattern(scaling(2, 2, 2))
        val c = pattern.patternAtShape(shape, point(2, 3, 4))
        c shouldBe color(1.0, 1.5, 2.0)
    }

    @Test
    fun `Scenario A pattern with both an object and a pattern transformation`() {
        val shape = Sphere(scaling(2, 2, 2))
        val pattern = TestPattern(translation(0.5, 1.0, 1.5))
        val c = pattern.patternAtShape(shape, point(2.5, 3.0, 3.5))
        c shouldBe color(0.75, 0.5, 0.25)
    }

    @Test
    fun `Scenario A gradient linearly interpolates between colors`() {
        val pattern = GradientPattern(WHITE, BLACK)
        pattern.patternAt(point(0, 0, 0)) shouldBe WHITE
        pattern.patternAt(point(0.25, 0.0, 0.0)) shouldBe color(0.75, 0.75, 0.75)
        pattern.patternAt(point(0.5, 0.0, 0.0)) shouldBe color(0.5, 0.5, 0.5)
        pattern.patternAt(point(0.75, 0.0, 0.0)) shouldBe color(0.25, 0.25, 0.25)
    }

    @Test
    fun `Scenario A ring should extend in both x and z`() {
        val pattern = RingPattern(WHITE, BLACK)
        pattern.patternAt(point(0, 0, 0)) shouldBe WHITE
        pattern.patternAt(point(1, 0, 0)) shouldBe BLACK
        pattern.patternAt(point(0, 0, 1)) shouldBe BLACK
        pattern.patternAt(point(magic2, 0.0, magic2)) shouldBe BLACK
    }

    @Test
    fun `Scenario Checkers should repeat in x`() {
        val pattern = CheckersPattern(WHITE, BLACK)
        pattern.patternAt(point(0, 0, 0)) shouldBe WHITE
        pattern.patternAt(point(0.99, 0.0, 0.0)) shouldBe WHITE
        pattern.patternAt(point(1.01, 0.0, 0.0)) shouldBe BLACK
    }

    @Test
    fun `Scenario Checkers should repeat in y`() {
        val pattern = CheckersPattern(WHITE, BLACK)
        pattern.patternAt(point(0, 0, 0)) shouldBe WHITE
        pattern.patternAt(point(0.0, 0.99, 0.0)) shouldBe WHITE
        pattern.patternAt(point(0.0, 1.01, 0.0)) shouldBe BLACK
    }

    @Test
    fun `Scenario Checkers should repeat in z`() {
        val pattern = CheckersPattern(WHITE, BLACK)
        pattern.patternAt(point(0, 0, 0)) shouldBe WHITE
        pattern.patternAt(point(0.0, 0.0, 0.99)) shouldBe WHITE
        pattern.patternAt(point(0.0, 0.0, 1.01)) shouldBe BLACK
    }
}