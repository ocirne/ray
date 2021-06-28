package io.github.ocirne.ray.challenge.matrices

import io.github.ocirne.ray.challenge.math.equalsDelta
import io.github.ocirne.ray.challenge.tuples.Tuple

/** No tests für m x n matrices with m != n. */
class Matrix(private val size: Int, vararg values: Double) {

    constructor(size: Int, vararg values: Int) :
            this(size, *(values.map { i -> i.toDouble() }.toDoubleArray()))

    private val elements = Array(size) { DoubleArray(size) }

    init {
        if (values.size != 1) {
            require(size * size == values.size)
            for (r in 0 until size) {
                for (c in 0 until size) {
                    elements[r][c] = values[r * size + c]
                }
            }
        }
    }

    override fun toString(): String {
        return "$size x $size"
    }

    operator fun get(row: Int, col: Int): Double {
        return elements[row][col]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as Matrix
        if (size != other.size) return false
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (!elements[r][c].equalsDelta(other.elements[r][c])) {
                    return false
                }
            }
        }
        return true
    }

    operator fun times(b: Matrix): Matrix {
        val m = Matrix(size, 0)
        for (row in 0 until size) {
            for (col in 0 until size) {
                m.elements[row][col] = elements[row][0] * b.elements[0][col] +
                                       elements[row][1] * b.elements[1][col] +
                                       elements[row][2] * b.elements[2][col] +
                                       elements[row][3] * b.elements[3][col]
            }
        }
        return m
    }

    operator fun times(b: Tuple): Tuple {
        val r = DoubleArray(4)
        for (row in 0 until size) {
            r[row] = elements[row][0] * b.x +
                     elements[row][1] * b.y +
                     elements[row][2] * b.z +
                     elements[row][3] * b.w
        }
        return Tuple(r[0], r[1], r[2], r[3])
    }

    fun transpose(): Matrix {
        val m = Matrix(size, 0)
        for (row in 0 until size) {
            for (col in 0 until size) {
                m.elements[row][col] = elements[col][row]
            }
        }
        return m
    }

    fun submatrix(dr: Int, dc: Int): Matrix {
        TODO("Not yet implemented")
    }

    fun determinant(): Double {
        TODO("Not yet implemented")
    }

    fun isInvertible(): Boolean {
        TODO("Not yet implemented")
    }

    fun inverse(): Matrix {
        TODO("Not yet implemented")
    }

    fun minor(i: Int, j: Int): Double {
        TODO("Not yet implemented")
    }

    fun cofactor(i: Int, j: Int): Double {
        TODO("Not yet implemented")
    }
}

val identityMatrix = Matrix(4,
    1, 0, 0, 0,
    0, 1, 0, 0,
    0, 0, 1, 0,
    0, 0, 0, 1)