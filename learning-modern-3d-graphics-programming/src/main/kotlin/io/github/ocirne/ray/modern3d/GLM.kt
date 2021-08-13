package io.github.ocirne.ray.modern3d

import kotlin.math.sqrt

class Vec4(var x: Float, var y: Float, var z: Float, var w: Float) {

    constructor(v3: Vec3, w: Float) : this(v3.x, v3.y, v3.z, w)

    constructor() : this(0.0f, 0.0f, 0.0f, 0.0f)

    override fun toString(): String {
        return "$x $y $z $w"
    }

    operator fun set(i: Int, value: Float) {
        when (i) {
            0 -> x = value
            1 -> y = value
            2 -> z = value
            3 -> w = value
        }
    }

    operator fun get(i: Int): Float {
        when (i) {
            0 -> return x
            1 -> return y
            2 -> return z
            3 -> return w
            else -> throw IllegalStateException()
        }
    }
}

class Vec3(var x: Float, var y: Float, var z: Float) {

    constructor(s: Float) : this(s, s, s)

    constructor() : this(0.0f, 0.0f, 0.0f)

    fun length(): Float {
        return sqrt(x*x + y*y + z*z)
    }

    fun normalize(): Vec3 {
        val length = length()
        return Vec3(x/length, y/length, z/length)
    }

    operator fun plus(b: Vec3): Vec3 {
        return Vec3(x + b.x, y + b.y, z + b.z)
    }

    operator fun times(f: Float): Vec3 {
        return Vec3(x * f, y * f, z * f)
    }

    operator fun minus(b: Vec3): Vec3 {
        return Vec3(x - b.x, y - b.y, z - b.z)
    }

    operator fun unaryMinus(): Vec3 {
        return Vec3(-x, -y, -z)
    }

    fun cross(b: Vec3): Vec3 {
        return Vec3(
            y * b.z - z * b.y,
            z * b.x - x * b.z,
            x * b.y - y * b.x
        )
    }
}

class Mat3 {
    val m = Array(3) { Vec3() }

    init {
        m[0].x = 1.0f
        m[1].y = 1.0f
        m[2].z = 1.0f
    }

    operator fun get(i: Int): Vec3 {
        return m[i]
    }
}

class Mat4(mat3: Mat3) {
    val m = Array(4) { Vec4() }

    init {
        m[0].x = mat3[0].x
        m[0].y = mat3[0].y
        m[0].z = mat3[0].z
        m[1].x = mat3[1].x
        m[1].y = mat3[1].y
        m[1].z = mat3[1].z
        m[2].x = mat3[2].x
        m[2].y = mat3[2].y
        m[2].z = mat3[2].z
        m[3].w = 1.0f
    }

    constructor() : this(Mat3())

    operator fun set(i: Int, vec: Vec4) {
        m[i] = vec
    }

    operator fun get(i: Int): Vec4 {
        return m[i]
    }

    fun toFloatArray(): FloatArray {
        val fa = FloatArray(16) { 1.0f }
        m.forEachIndexed { x, vec4 ->
            fa[4 * x + 0] = vec4.x
            fa[4 * x + 1] = vec4.y
            fa[4 * x + 2] = vec4.z
            fa[4 * x + 3] = vec4.w
        }
        return fa
    }

    override fun toString(): String {
        return m.map { v -> v.toString() }.joinToString()
    }

    operator fun times(b: Mat4): Mat4 {
        val r = Mat4()
        for (row in 0 until 4) {
            for (col in 0 until 4) {
                // Different from Matrix multiplication in the ray tracer challenge (!?)
                r.m[row][col] = b.m[row][0] * m[0][col] +
                                b.m[row][1] * m[1][col] +
                                b.m[row][2] * m[2][col] +
                                b.m[row][3] * m[3][col]
            }
        }
        return r
    }

    fun transpose(): Mat4 {
        val r = Mat4()
        for (row in 0 until 4) {
            for (col in 0 until 4) {
                r.m[row][col] = m[col][row]
            }
        }
        return r
    }
}

fun mix(x: Float, y: Float, a: Float): Float {
    return x * (1.0f - a) + y * a
}

fun length(v: Vec3): Float {
    return v.length()
}

fun normalize(v: Vec3): Vec3 {
    return v.normalize()
}

fun cross(a: Vec3, b: Vec3): Vec3 {
    return a.cross(b)
}