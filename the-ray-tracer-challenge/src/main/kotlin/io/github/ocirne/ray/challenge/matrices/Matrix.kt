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
        return "$size x $size " + (0 until size).map { elements[it].joinToString() }
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
        val m = Matrix(size - 1, 0)
        for (row in 0 until size) {
            for (col in 0 until size) {
                if (row == dr) continue
                if (col == dc) continue
                val effRow = if (row > dr) row - 1 else row
                val effCol = if (col > dc) col - 1 else col
                m.elements[effRow][effCol] = elements[row][col]
            }
        }
        return m
    }

    fun determinant(): Double {
        if (size == 2) {
            return elements[0][0] * elements[1][1] - elements[0][1] * elements[1][0]
        }
        return (0 until size).sumOf { elements[0][it] * cofactor(0, it) }
    }

    fun isInvertible(): Boolean {
        return !determinant().equalsDelta(0.0)
    }

    fun inverse(): Matrix {
        if (!isInvertible()) {
            throw IllegalStateException()
        }
        val m = Matrix(size, 0)
        for (row in 0 until size) {
            for (col in 0 until size) {
                val c = cofactor(row, col)
                m.elements[col][row] = c / determinant()
            }
        }
        return m
    }

    fun minor(i: Int, j: Int): Double {
        return submatrix(i,j).determinant()
    }

    fun cofactor(row: Int, col: Int): Double {
        val m = minor(row, col)
        if ((row xor col) and 1 == 0) {
            return m
        }
        return -m
    }
}

val identityMatrix = Matrix(4,
    1, 0, 0, 0,
    0, 1, 0, 0,
    0, 0, 1, 0,
    0, 0, 0, 1)