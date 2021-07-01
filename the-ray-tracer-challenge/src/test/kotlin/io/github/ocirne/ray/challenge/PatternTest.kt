package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.patterns.StripePattern
import io.github.ocirne.ray.challenge.shapes.Sphere
import io.github.ocirne.ray.challenge.transformations.scaling
import io.github.ocirne.ray.challenge.transformations.translation
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

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
        pattern.stripeAt(point(0, 0, 0)) shouldBe WHITE
        pattern.stripeAt(point(0, 1, 0)) shouldBe WHITE
        pattern.stripeAt(point(0, 2, 0)) shouldBe WHITE
    }

    @Test
    fun `Scenario A stripe pattern is constant in z`() {
        val pattern = StripePattern(WHITE, BLACK)
        pattern.stripeAt(point(0, 0, 0)) shouldBe WHITE
        pattern.stripeAt(point(0, 0, 1)) shouldBe WHITE
        pattern.stripeAt(point(0, 0, 2)) shouldBe WHITE
    }

    @Test
    fun `Scenario A stripe pattern alternates in x`() {
        val pattern = StripePattern(WHITE, BLACK)
        pattern.stripeAt(point(0, 0, 0)) shouldBe WHITE
        pattern.stripeAt(point(0.9, 0.0, 0.0)) shouldBe WHITE
        pattern.stripeAt(point(1, 0, 0)) shouldBe BLACK
        pattern.stripeAt(point(-0.1, 0.0, 0.0)) shouldBe BLACK
        pattern.stripeAt(point(-1, 0, 0)) shouldBe BLACK
        pattern.stripeAt(point(-1.1, 0.0, 0.0)) shouldBe WHITE
    }

    @Test
    fun `Scenario Stripes with an object transformation`() {
      val shape = Sphere(scaling(2, 2, 2))
      val pattern = StripePattern (WHITE, BLACK)
      val c = pattern.stripeAtShape (shape, point(1.5, 0.0, 0.0))
      c shouldBe WHITE
    }

    @Test
    fun `Scenario Stripes with a pattern transformation`() {
      val shape = Sphere()
      val pattern = StripePattern (WHITE, BLACK, transform=scaling(2, 2, 2))
      val c = pattern.stripeAtShape (shape, point(1.5, 0.0, 0.0))
      c shouldBe WHITE
    }

    @Test
    fun `Scenario Stripes with both an object and a pattern transformation`() {
      val shape = Sphere(scaling(2, 2, 2))
      val pattern = StripePattern (WHITE, BLACK, transform=translation(0.5, 0.0, 0.0))
      val c = pattern.stripeAtShape (shape, point(2.5, 0.0, 0.0))
      c shouldBe WHITE
    }

    /*
    @Test
    fun `Scenario The default pattern transformation`() {
      val pattern = test_pattern ()
      pattern . transform shouldBe identityMatrix

    }

    @Test
    fun `Scenario Assigning a transformation`() {
      val pattern = test_pattern ()
      val set_pattern_transform (pattern, translation(1, 2, 3))
      pattern . transform shouldBe translation (1, 2, 3)

    }

    @Test
    fun `Scenario A pattern with an object transformation`() {
      val shape = Sphere ()
      val set_transform (shape, scaling(2, 2, 2))
      val pattern = test_pattern ()
      val c = pattern_at_shape (pattern, shape, point(2, 3, 4))
      c shouldBe color (1, 1.5, 2)

    }

    @Test
    fun `Scenario A pattern with a pattern transformation`() {
      val shape = Sphere ()
      val pattern = test_pattern ()
      val set_pattern_transform (pattern, scaling(2, 2, 2))
      val c = pattern_at_shape (pattern, shape, point(2, 3, 4))
      c shouldBe color (1, 1.5, 2)

    }

    @Test
    fun `Scenario A pattern with both an object and a pattern transformation`() {
      val shape = Sphere ()
      val set_transform (shape, scaling(2, 2, 2))
      val pattern = test_pattern ()
      val set_pattern_transform (pattern, translation(0.5, 1, 1.5))
      val c = pattern_at_shape (pattern, shape, point(2.5, 3, 3.5))
      c shouldBe color (0.75, 0.5, 0.25)

    }

    @Test
    fun `Scenario A gradient linearly interpolates between colors`() {
      val pattern = gradient_pattern (WHITE, BLACK)
      pattern_at (pattern, point(0, 0, 0)) shouldBe WHITE
      val pattern_at (pattern, point(0.25, 0, 0)) shouldBe color(0.75, 0.75, 0.75)
      val pattern_at (pattern, point(0.5, 0, 0)) shouldBe color(0.5, 0.5, 0.5)
      val pattern_at (pattern, point(0.75, 0, 0)) shouldBe color(0.25, 0.25, 0.25)

    }

    @Test
    fun `Scenario A ring should extend in both x and z`() {
      val pattern = ring_pattern (WHITE, BLACK)
      pattern_at (pattern, point(0, 0, 0)) shouldBe WHITE
      val pattern_at (pattern, point(1, 0, 0)) shouldBe BLACK
      val pattern_at (pattern, point(0, 0, 1)) shouldBe BLACK
      # 0.708 shouldBe just slightly more than √2/2
      val pattern_at (pattern, point(0.708, 0, 0.708)) shouldBe BLACK

    }

    @Test
    fun `Scenario Checkers should repeat in x`() {
      val pattern = checkers_pattern (WHITE, BLACK)
      pattern_at (pattern, point(0, 0, 0)) shouldBe WHITE
      val pattern_at (pattern, point(0.99, 0, 0)) shouldBe WHITE
      val pattern_at (pattern, point(1.01, 0, 0)) shouldBe BLACK

    }

    @Test
    fun `Scenario Checkers should repeat in y`() {
      val pattern = checkers_pattern (WHITE, BLACK)
      pattern_at (pattern, point(0, 0, 0)) shouldBe WHITE
      val pattern_at (pattern, point(0, 0.99, 0)) shouldBe WHITE
      val pattern_at (pattern, point(0, 1.01, 0)) shouldBe BLACK

    }

    @Test
    fun `Scenario Checkers should repeat in z`() {
      val pattern = checkers_pattern (WHITE, BLACK)
      pattern_at (pattern, point(0, 0, 0)) shouldBe WHITE
      val pattern_at (pattern, point(0, 0, 0.99)) shouldBe WHITE
      val pattern_at (pattern, point(0, 0, 1.01)) shouldBe BLACK
    }
   */
}