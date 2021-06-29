package io.github.ocirne.ray.challenge.transformations

import io.github.ocirne.ray.challenge.matrices.Matrix
import io.github.ocirne.ray.challenge.matrices.identityMatrix
import kotlin.math.cos
import kotlin.math.sin

fun translation(x: Double, y: Double, z: Double): Matrix {
    return Matrix(4,
        1.0, 0.0, 0.0, x,
        0.0, 1.0, 0.0, y,
        0.0, 0.0, 1.0, z,
        0.0, 0.0, 0.0, 1.0
    )
}

fun translation(x: Int, y: Int, z: Int): Matrix {
    return translation(x.toDouble(), y.toDouble(), z.toDouble())
}

fun scaling(x: Double, y: Double, z: Double): Matrix {
    return Matrix(4,
        x, 0.0, 0.0, 0.0,
        0.0, y, 0.0, 0.0,
        0.0, 0.0, z, 0.0,
        0.0, 0.0, 0.0, 1.0
    )
}

fun scaling(x: Int, y: Int, z: Int): Matrix {
    return scaling(x.toDouble(), y.toDouble(), z.toDouble())
}

fun rotationX(r: Double): Matrix {
    val cr = cos(r)
    val sr = sin(r)
    return Matrix(4,
        1.0, 0.0, 0.0, 0.0,
        0.0, cr, -sr, 0.0,
        0.0, sr, cr, 0.0,
        0.0, 0.0, 0.0, 1.0
    )
}

fun rotationY(r: Double): Matrix {
    val cr = cos(r)
    val sr = sin(r)
    return Matrix(4,
        cr, 0.0, sr, 0.0,
        0.0, 1.0, 0.0, 0.0,
        -sr, 0.0, cr, 0.0,
        0.0, 0.0, 0.0, 1.0
    )
}

fun rotationZ(r: Double): Matrix {
    val cr = cos(r)
    val sr = sin(r)
    return Matrix(4,
        cr, -sr, 0.0, 0.0,
        sr, cr, 0.0, 0.0,
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    )
}

fun shearing(xy: Double, xz: Double, yx: Double, yz: Double, zx: Double, zy: Double): Matrix {
    return Matrix(4,
        1.0, xy, xz, 0.0,
        yx, 1.0, yz, 0.0,
        zx, zy, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    )
}

fun shearing(xy: Int, xz: Int, yx: Int, yz: Int, zx: Int, zy: Int): Matrix {
    return shearing(xy.toDouble(), xz.toDouble(), yx.toDouble(), yz.toDouble(), zx.toDouble(), zy.toDouble())
}

fun identity(): Matrix {
    return identityMatrix
}
