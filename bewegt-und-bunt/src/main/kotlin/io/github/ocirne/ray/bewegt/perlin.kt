package io.github.ocirne.ray.bewegt

import kotlin.math.abs
import kotlin.math.floor
import kotlin.random.Random

class perlin() {

    val point_count = 256

    val ranvec: Array<vec3> = Array(point_count) { vec3.random(-1.0, 1.0).unitVector() }
    val perm_x : Array<Int>
    val perm_y : Array<Int>
    val perm_z : Array<Int>

    init {
        perm_x = perlin_generate_perm()
        perm_y = perlin_generate_perm()
        perm_z = perlin_generate_perm()
    }

    fun noise(p: point3) : Double {
        val u = p.x() - floor(p.x())
        val v = p.y() - floor(p.y())
        val w = p.z() - floor(p.z())

        val i = floor(p.x()).toInt()
        val j = floor(p.y()).toInt()
        val k = floor(p.z()).toInt()
        val c = Array(2) { Array(2) { Array<vec3?>(2) { null } } }

        for (di in 0..1) {
            for (dj in 0..1) {
                for (dk in 0..1) {
                    c[di][dj][dk] = ranvec[
                                    perm_x[(i + di) and 255] xor
                                    perm_y[(j + dj) and 255] xor
                                    perm_z[(k + dk) and 255]]
                }
            }
        }
        return perlin_interp(c, u, v, w)
    }

    fun perlin_generate_perm(): Array<Int> {
        val p = Array(point_count) { i -> i }
        permute(p, point_count)
        return p
    }

    fun permute(p: Array<Int>, n: Int) {
        for (i in n-1 downTo 1) {
            val target = Random.nextInt(i)
            val tmp = p[i]
            p[i] = p[target]
            p[target] = tmp
        }
    }

    fun turb(p: point3, depth: Int=7): Double {
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

fun trilinear_interp(c: Array<Array<Array<Double>>>, u: Double, v: Double, w :Double): Double {
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

fun perlin_interp(c: Array<Array<Array<vec3?>>>, u: Double, v: Double, w: Double): Double {
    val uu = u*u*(3-2*u)
    val vv = v*v*(3-2*v)
    val ww = w*w*(3-2*w)
    var accum = 0.0

    for (i in 0..1) {
        for (j in 0..1) {
            for (k in 0..1) {
                val weight_v = vec3(u - i, v-j, w-k)
                accum += (i * uu + (1 - i) * (1 - uu)) *
                         (j * vv + (1 - j) * (1 - vv)) *
                         (k * ww + (1 - k) * (1 - ww)) * c[i][j][k]!!.dot(weight_v)
            }
        }
    }
    return accum
}
