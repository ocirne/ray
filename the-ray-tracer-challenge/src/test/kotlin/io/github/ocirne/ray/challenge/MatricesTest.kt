package io.github.ocirne.ray.challenge

import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import io.github.ocirne.ray.challenge.tuples.Tuple
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

internal class MatricesTest {

    @Test
    fun `Scenario Constructing and inspecting a 4x4 matrix`() {
        val m = Matrix(4,
            1.0, 2.0, 3.0, 4.0,
            5.5, 6.5, 7.5, 8.5,
            9.0, 10.0, 11.0, 12.0,
            13.5, 14.5, 15.5, 16.5
        )
        m[0, 0] shouldBe 1
        m[0, 3] shouldBe 4
        m[1, 0] shouldBe 5.5
        m[1, 2] shouldBe 7.5
        m[2, 2] shouldBe 11
        m[3, 0] shouldBe 13.5
        m[3, 2] shouldBe 15.5
    }

    @Test
    fun `Scenario A 2x2 matrix ought to be representable`() {
        val m = Matrix(2,
            -3, 5,
            1, -2
        )
        m[0, 0] shouldBe -3
        m[0, 1] shouldBe 5
        m[1, 0] shouldBe 1
        m[1, 1] shouldBe -2
    }

    @Test
    fun `Scenario A 3x3 matrix ought to be representable`() {
        val m = Matrix(3,
            -3, 5, 0,
            1, -2, -7,
            0, 1, 1
        )
        m[0, 0] shouldBe -3
        m[1, 1] shouldBe -2
        m[2, 2] shouldBe 1
    }

    @Test
    fun `Scenario Matrix equality with identical matrices`() {
        val a = Matrix(4,
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 8, 7, 6,
            5, 4, 3, 2
        )
        val b = Matrix(4,
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 8, 7, 6,
            5, 4, 3, 2
        )
        a shouldBe b
    }

    @Test
    fun `Scenario Matrix equality with different matrices`() {
        val a = Matrix(4,
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 8, 7, 6,
            5, 4, 3, 2
        )
        val b = Matrix(4,
            2, 3, 4, 5,
            6, 7, 8, 9,
            8, 7, 6, 5,
            4, 3, 2, 1
        )
        a shouldNotBe b
    }

    @Test
    fun `Scenario Multiplying two matrices`() {
        val a = Matrix(4,
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 8, 7, 6,
            5, 4, 3, 2
        )
        val b = Matrix(4,
            -2, 1, 2, 3,
            3, 2, 1, -1,
            4, 3, 6, 5,
            1, 2, 7, 8
        )
        a * b shouldBe Matrix(4,
            20, 22, 50, 48,
            44, 54, 114, 108,
            40, 58, 110, 102,
            16, 26, 46, 42
        )
    }

    @Test
    fun `Scenario A matrix multiplied by a tuple`() {
        val a = Matrix(4,
            1, 2, 3, 4,
            2, 4, 4, 2,
            8, 6, 4, 1,
            0, 0, 0, 1
        )
        val b = Tuple(1, 2, 3, 1)
        a * b shouldBe Tuple(18, 24, 33, 1)
    }

    @Test
    fun `Scenario Multiplying a matrix by the identity matrix`() {
        val a = Matrix(4,
            0, 1, 2, 4,
            1, 2, 4, 8,
            2, 4, 8, 16,
            4, 8, 16, 32
        )
        a * identityMatrix shouldBe a
    }

    @Test
    fun `Scenario Multiplying the identity matrix by a tuple`() {
        val a = Tuple(1, 2, 3, 4)
        identityMatrix * a shouldBe a
    }

    @Test
    fun `Scenario Transposing a matrix`() {
        val a = Matrix(4,
            0, 9, 3, 0,
            9, 8, 0, 8,
            1, 8, 5, 3,
            0, 0, 5, 8
        )
        a.transpose() shouldBe
                Matrix(4,
                    0, 9, 1, 0,
                    9, 8, 8, 0,
                    3, 0, 5, 5,
                    0, 8, 3, 8
                )
    }

    @Test
    fun `Scenario Transposing the identity matrix`() {
        val a = identityMatrix.transpose()
        a shouldBe identityMatrix
    }

    @Test
    fun `Scenario Calculating the determinant of a 2x2 matrix`() {
        val a = Matrix(2,
            1, 5,
            -3, 2
        )
        a.determinant() shouldBe 17.0
    }


    @Test
    fun `Scenario A submatrix of a 3x3 matrix is a 2x2 matrix`() {
        val a = Matrix(3,
            1, 5, 0,
            -3, 2, 7,
            0, 6, -3
        )
        a.submatrix(0, 2) shouldBe
                Matrix(2,
                    -3, 2,
                    0, 6
                )
    }

    @Test
    fun `Scenario A submatrix of a 4x4 matrix is a 3x3 matrix`() {
        val a = Matrix(4,
            -6, 1, 1, 6,
            -8, 5, 8, 6,
            -1, 0, 8, 2,
            -7, 1, -1, 1
        )
        a.submatrix(2, 1) shouldBe Matrix(3
            -6, 1, 6,
            -8, 8, 6,
            -7, -1, 1
        )
    }

    @Test
    fun `Scenario Calculating a minor of a 3x3 matrix`() {
        val a = Matrix(3,
            3, 5, 0,
            2, -1, -7,
            6, -1, 5
        )
        val b = a.submatrix(1, 0)
        b.determinant() shouldBe 25
        a.minor(1, 0) shouldBe 25
    }

    @Test
    fun `Scenario Calculating a cofactor of a 3x3 matrix`() {
        val a = Matrix(3,
            3, 5, 0,
            2, -1, -7,
            6, -1, 5
        )
        a.minor(0, 0) shouldBe -12
        a.cofactor(0, 0) shouldBe -12
        a.minor(1, 0) shouldBe 25
        a.cofactor(1, 0) shouldBe -25
    }

    @Test
    fun `Scenario Calculating the determinant of a 3x3 matrix`() {
        val a = Matrix(3,
            1, 2, 6,
            -5, 8, -4,
            2, 6, 4
        )
        a.cofactor(0, 0) shouldBe 56
        a.cofactor(0, 1) shouldBe 12
        a.cofactor(0, 2) shouldBe -46
        a.determinant() shouldBe -196
    }

    @Test
    fun `Scenario Calculating the determinant of a 4x4 matrix`() {
        val a = Matrix(4,
            -2, -8, 3, 5,
            -3, 1, 7, 3,
            1, 2, -9, 6,
            -6, 7, 7, -9
        )
        a.cofactor(0, 0) shouldBe 690
        a.cofactor(0, 1) shouldBe 447
        a.cofactor(0, 2) shouldBe 210
        a.cofactor(0, 3) shouldBe 51
        a.determinant() shouldBe -4071
    }

    @Test
    fun `Scenario Testing an invertible matrix for invertibility`() {
        val a = Matrix(4,
            6, 4, 4, 4,
            5, 5, 7, 6,
            4, -9, 3, -7,
            9, 1, 7, -6
        )
        a.determinant() shouldBe -2120
        a.isInvertible() shouldBe true
    }

    @Test
    fun `Scenario Testing a noninvertible matrix for invertibility`() {
        val a = Matrix(4,
            -4, 2, -2, -3,
            9, 6, 2, 6,
            0, -5, 1, -5,
            0, 0, 0, 0
        )
        a.determinant() shouldBe 0
        a.isInvertible() shouldBe false
    }

    @Test
    fun `Scenario Calculating the inverse of a matrix`() {
        val a = Matrix(4,
            -5, 2, 6, -8,
            1, -5, 1, 8,
            7, 7, -6, -7,
            1, -3, 7, 4
        )
        val b = a.inverse()
        a.determinant() shouldBe 532
        a.cofactor(2, 3) shouldBe -160
        b[3, 2] shouldBe -160.0 / 532.0
        a.cofactor(3, 2) shouldBe 105
        b[2, 3] shouldBe 105.0 / 532.0
        b shouldBe
                Matrix(4,
                    0.21805, 0.45113, 0.24060, -0.04511,
                    -0.80827, -1.45677, -0.44361, 0.52068,
                    -0.07895, -0.22368, -0.05263, 0.19737,
                    -0.52256, -0.81391, -0.30075, 0.30639
                )
    }

    @Test
    fun `Scenario Calculating the inverse of another matrix`() {
        val a = Matrix(4,
            8, -5, 9, 2,
            7, 5, 6, 1,
            -6, 0, 9, 6,
            -3, 0, -9, -4
        )
        a.inverse() shouldBe Matrix(4,
            -0.15385, -0.15385, -0.28205, -0.53846,
            -0.07692, 0.12308, 0.02564, 0.03077,
            0.35897, 0.35897, 0.43590, 0.92308,
            -0.69231, -0.69231, -0.76923, -1.92308
        )
    }

    @Test
    fun `Scenario Calculating the inverse of a third matrix`() {
        val a = Matrix(4,
            9, 3, 0, 9,
            -5, -2, -6, -3,
            -4, 9, 6, 4,
            -7, 6, 6, 2
        )
        a.inverse() shouldBe Matrix(4,
            -0.04074, -0.07778, 0.14444, -0.22222,
            -0.07778, 0.03333, 0.36667, -0.33333,
            -0.02901, -0.14630, -0.10926, 0.12963,
            0.17778, 0.06667, -0.26667, 0.33333
        )
    }

    @Test
    fun `Scenario Multiplying a product by its inverse`() {
        val a = Matrix(4,
            3, -9, 7, 3,
            3, -8, 2, -9,
            -4, 4, 4, 1,
            -6, 5, -1, 1
        )
        val b = Matrix(4,
            8, 2, 2, 2,
            3, -1, 7, 0,
            7, 0, 5, 4,
            6, -2, 0, 5
        )
        val c = a * b
        c * b.inverse() shouldBe a
    }
}
