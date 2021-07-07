package io.github.ocirne.ray.challenge.parser

import io.github.ocirne.ray.challenge.shapes.Group
import io.github.ocirne.ray.challenge.shapes.Shape
import io.github.ocirne.ray.challenge.triangles.Triangle
import io.github.ocirne.ray.challenge.tuples.Point
import io.github.ocirne.ray.challenge.tuples.point

// TODO Refactor
class ObjFileParser(content: String) {

    var ignored = 0

    var vertices = mutableListOf<Point>()

    var currentGroup = Group()

    val groups = mutableMapOf<String, Shape>()

    init {
        groups["default"] = currentGroup
        for (line in content.lines().map { line -> line.trim() }) {
            when {
                line.isBlank() -> continue
                line[0] == 'v' -> vertices.add(lineToVertex(line))
                line[0] == 'f' -> lineToFace(line).forEach(currentGroup::addChild)
                line[0] == 'g' -> lineToGroup(line)
                else -> ignored++
            }
        }
    }

    private fun lineToVertex(line: String): Point {
        println(line)
        val token = line.split(' ')
        return point(token[1].toDouble(), token[2].toDouble(), token[3].toDouble())
    }

    private fun lineToFace(line: String): List<Triangle> {
        val faceVertices = line.split(' ').drop(1).map { vertices[it.toInt() - 1] }
        return fanTriangulation(faceVertices)
    }

    private fun lineToGroup(line: String) {
        val name = line.split(' ')[1]
        currentGroup = Group()
        groups[name] = currentGroup
    }

    private fun fanTriangulation(faceVertices: List<Point>): List<Triangle> {
        return (2 until faceVertices.size)
            .map { Triangle(faceVertices[0], faceVertices[it - 1], faceVertices[it]) }
    }

    operator fun get(s: String): Shape {
        return groups[s]!!
    }

    fun toGroup(): Group {
        val group = Group()
        for (g in groups.values) {
            group.addChild(g)
        }
        return group
    }
}