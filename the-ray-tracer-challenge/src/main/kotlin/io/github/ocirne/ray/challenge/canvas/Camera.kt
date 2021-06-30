package io.github.ocirne.ray.challenge.canvas

import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.raysphere.Ray
import io.github.ocirne.ray.challenge.scene.World
import io.github.ocirne.ray.challenge.tuples.point
import kotlin.math.tan

class Camera(val hSize: Int, val vSize: Int, val fieldOfView: Double, val transform: Matrix = identityMatrix) {

    val halfWidth: Double
    val halfHeight: Double
    val pixelSize: Double

    init {
        val halfView = tan(fieldOfView / 2)
        val aspect = hSize.toDouble() / vSize.toDouble()
        if (aspect >= 1) {
            halfWidth = halfView
            halfHeight = halfView / aspect
        } else {
            halfWidth = halfView * aspect
            halfHeight = halfView
        }
        pixelSize = halfWidth * 2 / hSize
    }

    fun rayForPixel(px: Int, py: Int): Ray {
        val xOffset = (px + 0.5) * pixelSize
        val yOffset = (py + 0.5) * pixelSize
        val worldX = halfWidth - xOffset
        val worldY = halfHeight - yOffset
        val pixel = transform.inverse() * point(worldX, worldY, -1.0)
        val origin = transform.inverse() * point(0, 0, 0)
        val direction = (pixel - origin).normalize()
        return Ray(origin, direction)
    }

    fun render(world: World): Canvas {
        val image = Canvas(hSize, vSize)
        for (y in 0 until vSize) {
            for (x in 0 until hSize) {
                val ray = rayForPixel(x, y)
                val color = world.colorAt(ray)
                image.writePixel(x, y, color)
            }
        }
        return image
    }
}