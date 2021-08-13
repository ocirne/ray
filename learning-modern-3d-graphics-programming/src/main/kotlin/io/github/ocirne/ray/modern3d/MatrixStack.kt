package io.github.ocirne.ray.modern3d

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

fun rotateXMatrix(fAngDeg: Float): Mat3 {

    val fAngRad = degToRad(fAngDeg)
    val fCos = cos(fAngRad)
    val fSin = sin(fAngRad)

    val theMat = Mat3()
    theMat[1].y = fCos
    theMat[2].y = -fSin
    theMat[1].z = fSin
    theMat[2].z = fCos
    return theMat
}

fun rotateYMatrix(fAngDeg: Float): Mat3 {

    val fAngRad = degToRad(fAngDeg)
    val fCos = cos(fAngRad)
    val fSin = sin(fAngRad)

    val theMat = Mat3()
    theMat[0].x = fCos
    theMat[2].x = fSin
    theMat[0].z = -fSin
    theMat[2].z = fCos
    return theMat
}

fun rotateZMatrix(fAngDeg: Float): Mat3 {

    val fAngRad = degToRad(fAngDeg)
    val fCos = cos(fAngRad)
    val fSin = sin(fAngRad)

    val theMat = Mat3()
    theMat[0].x = fCos
    theMat[1].x = -fSin
    theMat[0].y = fSin
    theMat[1].y = fCos
    return theMat
}

fun glm_perspective(fovy: Float, aspect: Float, zNear: Float, zFar: Float): Mat4 {
    val range = tan(degToRad(fovy / 2.0f)) * zNear
    val left = -range * aspect
    val right = range * aspect
    val bottom = -range
    val top = range

    val result = Mat4()
    result[0][0] = (2.0f * zNear) / (right - left)
    result[1][1] = (2.0f * zNear) / (top - bottom)
    result[2][2] = - (zFar + zNear) / (zFar - zNear)
    result[2][3] = - 1.0f
    result[3][2] = - (2.0f * zFar * zNear) / (zFar - zNear)
    return result
}

class MatrixStack {

    private var currMat = Mat4()
    private val matrices = ArrayDeque<Mat4>()

    fun top(): Mat4 {
        return currMat
    }

    fun rotateX(fAngDeg: Float) {
        currMat *= Mat4(rotateXMatrix(fAngDeg))
    }

    fun rotateY(fAngDeg: Float) {
        currMat *= Mat4(rotateYMatrix(fAngDeg))
    }

    fun rotateZ(fAngDeg: Float) {
        currMat *= Mat4(rotateZMatrix(fAngDeg))
    }

    fun scale(scaleVec: Vec3) {
        val scaleMat = Mat4()
        scaleMat[0].x = scaleVec.x
        scaleMat[1].y = scaleVec.y
        scaleMat[2].z = scaleVec.z

        currMat *= scaleMat
    }

    fun translate(offsetVec: Vec3) {
        val translateMat = Mat4()
        translateMat[3] = Vec4(offsetVec, 1.0f)

        currMat *= translateMat
    }

    fun push() {
        matrices.add(currMat)
    }

    fun pop() {
        currMat = matrices.removeLast()
    }

    fun perspective(degFOV: Float, aspectRatio: Float, zNear: Float, zFar: Float) {
        currMat *= glm_perspective(degFOV, aspectRatio, zNear, zFar)
    }

    fun setMatrix(m: Mat4) {
        currMat = m
    }
}
