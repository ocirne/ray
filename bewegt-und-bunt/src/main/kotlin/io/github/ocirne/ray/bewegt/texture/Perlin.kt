package io.github.ocirne.ray.bewegt.texture

import io.github.ocirne.ray.bewegt.math.Point3
import io.github.ocirne.ray.bewegt.math.Vector3
import kotlin.math.abs
import kotlin.math.floor
import kotlin.random.Random

class Perlin {

    private val pointCount = 256

    private val randomVector: Array<Vector3> = Array(pointCount) { Vector3.random(-1.0, 1.0).unitVector() }
    private val permutationX: Array<Int> = perlinGeneratePermutation()
    private val permutationY: Array<Int> = perlinGeneratePermutation()
    private val permutationZ: Array<Int> = perlinGeneratePermutation()

    fun noise(p: Point3) : Double {
        val u = p.x - floor(p.x)
        val v = p.y - floor(p.y)
        val w = p.z - floor(p.z)

        val i = floor(p.x).toInt()
        val j = floor(p.y).toInt()
        val k = floor(p.z).toInt()
        val c = Array(2) { Array(2) { Array<Vector3?>(2) { null } } }

        for (di in 0..1) {
            for (dj in 0..1) {
                for (dk in 0..1) {
                    c[di][dj][dk] = randomVector[
                            permutationX[(i + di) and 255] xor
                            permutationY[(j + dj) and 255] xor
                            permutationZ[(k + dk) and 255]
                    ]
                }
            }
        }
        return perlinInterpolation(c, u, v, w)
    }

    private fun perlinGeneratePermutation(): Array<Int> {
        val p = Array(pointCount) { i -> i }
        permute(p)
        return p
    }

    private fun permute(p: Array<Int>) {
        for (i in pointCount - 1 downTo 1) {
            val target = Random.nextInt(i)
            p[i] = p[target].also { p[target] = p[i] }
        }
    }

    fun turbulence(p: Point3, depth: Int = 7): Double {
        var accum = 0.0
        var temp_p = p
        var weight = 1.0

        for (i in 0 until depth) {
            accum += weight * noise(temp_p)
            weight *= 0.5
            temp_p *= 2.0
        }

        return abs(accum)
    }
}

fun trilinearInterpolation(c: Array<Array<Array<Double>>>, u: Double, v: Double, w: Double): Double {
    var accum = 0.0
    for (i in 0..1) {
        for (j in 0..1) {
            for (k in 0..1) {
                accum += (i * u + (1 - i) * (1 - u)) *
                         (j * v + (1 - j) * (1 - v)) *
                         (k * w + (1 - k) * (1 - w)) * c[i][j][k]
            }
        }
    }
    return accum
}

fun perlinInterpolation(c: Array<Array<Array<Vector3?>>>, u: Double, v: Double, w: Double): Double {
    val uu = u * u * (3 - 2 * u)
    val vv = v * v * (3 - 2 * v)
    val ww = w * w * (3 - 2 * w)
    var accum = 0.0

    for (i in 0..1) {
        for (j in 0..1) {
            for (k in 0..1) {
                val weightVector = Vector3(u - i, v - j, w - k)
                accum += (i * uu + (1 - i) * (1 - uu)) *
                         (j * vv + (1 - j) * (1 - vv)) *
                         (k * ww + (1 - k) * (1 - ww)) * c[i][j][k]!!.dot(weightVector)
            }
        }
    }
    return accum
}
