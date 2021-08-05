package io.github.ocirne.ray.challenge.parser

import io.github.ocirne.ray.challenge.lights.Material
import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.shapes.Group
import io.github.ocirne.ray.challenge.shapes.Shape
import io.github.ocirne.ray.challenge.triangles.SmoothTriangle
import io.github.ocirne.ray.challenge.triangles.Triangle
import io.github.ocirne.ray.challenge.tuples.*

// TODO Refactor
class ObjFileParser(objectsFile: String, materialsFile: String? = null) {

    var ignoredInMaterials = 0

    var ignoredInObjects = 0

    var vertices = mutableListOf<Point>()

    var normals = mutableListOf<Vector>()

    var currentGroup: Group? = null

    var currentMaterial: Material? = null

    val materials = mutableMapOf<String, Material>()

    val groups = mutableMapOf<String, Shape>()

    init {
        materialsFile?.let { mf ->
            for (line in mf.lines()) {
                if (line.isBlank()) {
                    continue
                }
                val token = line.trim().split("\\s+".toRegex())
                when (token[0]) {
                    "newmtl" -> startNewMaterial(token)
                    "Kd" -> currentMaterial!!.color = lineToColor(token)
                    "D" -> currentMaterial!!.transparency = token[1].toDouble()
                    else -> { ignoredInMaterials++ }
                }
            }
        }
        for (line in objectsFile.lines()) {
            if (line.isBlank()) {
                continue
            }
            val token = line.trim().split("\\s+".toRegex())
            when (token[0]){
                "v" -> vertices.add(lineToVertex(token))
                "vn" -> normals.add(lineToNormal(token))
                "f" -> lineToFace(token).forEach { child -> getOrCreateCurrentGroup().addChild(child) }
                "g" -> lineToGroup(token)
                "usemtl" -> currentGroup!!.material = materials[token[1]]!!
                else -> ignoredInObjects++
            }
        }
    }

    private fun startNewMaterial(token: List<String>) {
        val name = token[1]
        currentMaterial = Material()
        materials[name] = currentMaterial!!
    }

    private fun lineToColor(token: List<String>): Color {
        return color(token[1].toDouble(), token[2].toDouble(), token[3].toDouble())
    }

    private fun lineToVertex(token: List<String>): Point {
        return point(token[1].toDouble(), token[2].toDouble(), token[3].toDouble())
    }

    private fun lineToNormal(token: List<String>): Vector {
        return vector(token[1].toDouble(), token[2].toDouble(), token[3].toDouble())
    }

    private fun lineToFace(token: List<String>): List<Shape> {
        return if (token[1].contains('/')) {
            val faceVertices = token.drop(1).map {
                vertices[it.split('/')[0].toInt() - 1]
            }
            val faceNormals = token.drop(1).map {
                normals[it.split('/')[2].toInt() - 1]
            }
            fanSmoothTriangulation(faceVertices, faceNormals)
        } else {
            val faceVertices = token.drop(1).map {
                vertices[it.split('/')[0].toInt() - 1]
            }
            fanTriangulation(faceVertices)
        }
    }

    private fun fanTriangulation(faceVertices: List<Point>): List<Triangle> {
        return (2 until faceVertices.size)
            .map { Triangle(faceVertices[0], faceVertices[it - 1], faceVertices[it]) }
    }

    private fun fanSmoothTriangulation(faceVertices: List<Point>, faceNormals: List<Vector>): List<SmoothTriangle> {
        return (2 until faceVertices.size)
            .map { SmoothTriangle(
                faceVertices[0], faceVertices[it - 1], faceVertices[it],
                faceNormals[0], faceNormals[it - 1], faceNormals[it]) }
    }

    private fun lineToGroup(token: List<String>) {
        val name = token[1]
        currentGroup = Group()
        groups[name] = currentGroup!!
    }

    private fun getOrCreateCurrentGroup(): Group {
        if (currentGroup == null) {
            currentGroup = Group()
            groups["default"] = currentGroup!!
        }
        return currentGroup!!
    }

    operator fun get(s: String): Shape {
        return groups[s]!!
    }

    // TODO withTransform in Group / Shape?
    fun toGroup(transform: Matrix = identityMatrix): Group {
        val group = Group(transform)
        for (g in groups.values) {
            group.addChild(g)
        }
        return group
    }
}