package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.parser.ObjFileParser
import io.github.ocirne.ray.challenge.shapes.Group
import io.github.ocirne.ray.challenge.triangles.Triangle
import io.github.ocirne.ray.challenge.tuples.*
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ObjFileParserTest {

    @Test
    fun `Scenario Ignoring unrecognized lines`() {
        val gibberish = """
            There was a young lady named Bright
            who traveled much faster than light.
            She set out one day
            in a relative way,
            and came back the previous night.
            """
        val parser = ObjFileParser(gibberish)
        parser.ignored shouldBe 5
    }

    @Test
    fun `Scenario Vertex records`() {
        val file = """
            v -1 1 0
            v -1.0000 0.5000 0.0000
            v 1 0 0
            v 1 1 0
            """
        val parser = ObjFileParser(file)
        parser.vertices[0] shouldBe point(-1, 1, 0)
        parser.vertices[1] shouldBe point(-1.0, 0.5, 0.0)
        parser.vertices[2] shouldBe point(1, 0, 0)
        parser.vertices[3] shouldBe point(1, 1, 0)
    }

    @Test
    fun `Scenario Parsing triangle faces`() {
        val file = """
            v -1 1 0
            v -1 0 0
            v 1 0 0
            v 1 1 0
        
            f 1 2 3
            f 1 3 4
            """
        val parser = ObjFileParser(file)
        val g = parser["default"] as Group
        val t1 = g[0] as Triangle
        val t2 = g[1] as Triangle
        t1.p1 shouldBe parser.vertices[0]
        t1.p2 shouldBe parser.vertices[1]
        t1.p3 shouldBe parser.vertices[2]
        t2.p1 shouldBe parser.vertices[0]
        t2.p2 shouldBe parser.vertices[2]
        t2.p3 shouldBe parser.vertices[3]
    }

    @Test
    fun `Scenario Triangulating polygons`() {
        val file = """
            v -1 1 0
            v -1 0 0
            v 1 0 0
            v 1 1 0
            v 0 2 0
        
            f 1 2 3 4 5
            """
        val parser = ObjFileParser(file)
        val g = parser["default"] as Group
        val t1 = g[0] as Triangle
        val t2 = g[1] as Triangle
        val t3 = g[2] as Triangle
        t1.p1 shouldBe parser.vertices[0]
        t1.p2 shouldBe parser.vertices[1]
        t1.p3 shouldBe parser.vertices[2]
        t2.p1 shouldBe parser.vertices[0]
        t2.p2 shouldBe parser.vertices[2]
        t2.p3 shouldBe parser.vertices[3]
        t3.p1 shouldBe parser.vertices[0]
        t3.p2 shouldBe parser.vertices[3]
        t3.p3 shouldBe parser.vertices[4]
    }

    @Test
    fun `Scenario Triangles in groups`() {
        val content = this::class.java.classLoader.getResource("triangles.obj")!!.readText()
        val parser = ObjFileParser(content)
        val g1 = parser["FirstGroup"] as Group
        val g2 = parser["SecondGroup"] as Group
        val t1 = g1.first() as Triangle
        val t2 = g2.first() as Triangle
        t1.p1 shouldBe parser.vertices[0]
        t1.p2 shouldBe parser.vertices[1]
        t1.p3 shouldBe parser.vertices[2]
        t2.p1 shouldBe parser.vertices[0]
        t2.p2 shouldBe parser.vertices[2]
        t2.p3 shouldBe parser.vertices[3]
    }

    @Test
    fun `Scenario Converting an OBJ file to a group`() {
        val content = this::class.java.classLoader.getResource("triangles.obj")!!.readText()
        val parser = ObjFileParser(content)
        val g = parser.toGroup()
        println(g)
        g shouldContain parser["FirstGroup"]
        g shouldContain parser["SecondGroup"]
    }

/*
@Test
fun `Scenario Vertex normal records`() {
  val file = a file containing:
    """
    vn 0 0 1
    vn 0.707 0 -0.707
    vn 1 2 3
    """
  val parser = ObjFileParser(file)
  parser.normals[1] shouldBe vector(0, 0, 1)
  parser.normals[2] shouldBe vector(0.707, 0, -0.707)
     parser.normals[3] shouldBe vector(1, 2, 3)

}

@Test
fun `Scenario Faces with normals`() {
  val file = a file containing:
    """
    v 0 1 0
    v -1 0 0
    v 1 0 0

    vn -1 0 0
    vn 1 0 0
    vn 0 1 0

    f 1//3 2//1 3//2
    f 1/0/3 2/102/1 3/14/2
    """
  val parser = ObjFileParser(file)
    val g = parser.default_group
    val t1 = first child of g
    val t2 = second child of g
  t1.p1 shouldBe parser.vertices[1]
     t1.p2 shouldBe parser.vertices[2]
     t1.p3 shouldBe parser.vertices[3]
     t1.n1 shouldBe parser.normals[3]
     t1.n2 shouldBe parser.normals[1]
     t1.n3 shouldBe parser.normals[2]
     t2 shouldBe t1
}
  */
}