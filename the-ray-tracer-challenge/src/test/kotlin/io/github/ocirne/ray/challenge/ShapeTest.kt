package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.shapes.Shape
import io.github.ocirne.ray.challenge.transformations.translation
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TestShape(transform: Matrix= identityMatrix, material: Material=Material()) : Shape(transform, material) {

}

internal class ShapeTest {

      @Test
      fun `Scenario The default transformation`() {
      val s = TestShape()
      s.transform shouldBe identityMatrix
    }
  
          @Test
          fun `Scenario Assigning a transformation`() {
  val s = TestShape(translation(2, 3, 4))
  s.transform shouldBe translation(2, 3, 4)
}

@Test
fun `Scenario The default material`() {
  val s = TestShape()
  s.material shouldBe Material()

}

@Test
fun `Scenario Assigning a material`() {
    val m = Material(ambient = 1.0)
  val s = TestShape(material = m)
  s.material shouldBe m
}

    /*
@Test
fun `Scenario Intersecting a scaled shape with a ray`() {
  val r = Ray(point(0, 0, -5), vector(0, 0, 1))
    val s = TestShape()
  val set_transform(s, scaling(2, 2, 2))
    val xs = intersect(s, r)
  s.saved_ray.origin shouldBe point(0, 0, -2.5)
    val s.saved_ray.direction shouldBe vector(0, 0, 0.5)

}

@Test
fun `Scenario Intersecting a translated shape with a ray`() {
  val r = Ray(point(0, 0, -5), vector(0, 0, 1))
    val s = TestShape()
  val set_transform(s, translation(5, 0, 0))
    val xs = intersect(s, r)
  s.saved_ray.origin shouldBe point(-5, 0, -5)
    val s.saved_ray.direction shouldBe vector(0, 0, 1)

}

@Test
fun `Scenario Computing the normal on a translated shape`() {
  val s = TestShape()
  val set_transform(s, translation(0, 1, 0))
    val n = normal_at(s, point(0, 1.70711, -0.70711))
  n shouldBe vector(0, 0.70711, -0.70711)

}

@Test
fun `Scenario Computing the normal on a transformed shape`() {
  val s = TestShape()
    val m = scaling(1, 0.5, 1) * rotation_z(PI/5)
  val set_transform(s, m)
    val n = normal_at(s, point(0, √2/2, -√2/2))
  n shouldBe vector(0, 0.97014, -0.24254)

}

@Test
fun `Scenario A shape has a parent attribute`() {
  val s = TestShape()
  s.parent is nothing

}

@Test
fun `Scenario Converting a point from world to object space`() {
  val g1 = group()
    val set_transform(g1, rotation_y(PI/2))
    val g2 = group()
    val set_transform(g2, scaling(2, 2, 2))
    val add_child(g1, g2)
    val s = Sphere()
    val set_transform(s, translation(5, 0, 0))
    val add_child(g2, s)
  val p = world_to_object(s, point(-2, 0, -10))
  p shouldBe point(0, 0, -1)

}

@Test
fun `Scenario Converting a normal from object to world space`() {
  val g1 = group()
    val set_transform(g1, rotation_y(PI/2))
    val g2 = group()
    val set_transform(g2, scaling(1, 2, 3))
    val add_child(g1, g2)
    val s = Sphere()
    val set_transform(s, translation(5, 0, 0))
    val add_child(g2, s)
  val n = normal_to_world(s, vector(√3/3, √3/3, √3/3))
  n shouldBe vector(0.2857, 0.4286, -0.8571)

}

@Test
fun `Scenario Finding the normal on a child object`() {
  val g1 = group()
    val set_transform(g1, rotation_y(PI/2))
    val g2 = group()
    val set_transform(g2, scaling(1, 2, 3))
    val add_child(g1, g2)
    val s = Sphere()
    val set_transform(s, translation(5, 0, 0))
    val add_child(g2, s)
  val n = normal_at(s, point(1.7321, 1.1547, -5.5774))
  n shouldBe vector(0.2857, 0.4286, -0.8571)
} */
}