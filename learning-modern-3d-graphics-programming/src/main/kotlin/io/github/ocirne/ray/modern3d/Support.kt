package io.github.ocirne.ray.modern3d

import org.lwjgl.opengl.GL30C.*

object Support {

    fun loadShader(eShaderType: Int, shaderFilename: String): Int {
        val strShader = Support::class.java.classLoader.getResource(shaderFilename)!!.readText()
        val shader = glCreateShader(eShaderType)
        glShaderSource(shader, strShader)
        glCompileShader(shader)
        val status = glGetShaderi(shader, GL_COMPILE_STATUS)
        if (status == GL_FALSE) {
            val strInfoLog = glGetShaderInfoLog(shader)
            val strShaderType = when (eShaderType) {
                GL_VERTEX_SHADER -> "vertex"
                GL_FRAGMENT_SHADER -> "fragment"
                else -> throw IllegalArgumentException()
            }
            println("Compile failure in $strShaderType shader:\n$strInfoLog\n")
        }
        return shader
    }

    fun createProgram(shaderList: List<Int>): Int {
        val program = glCreateProgram()
        for (shader in shaderList) {
            glAttachShader(program, shader)
        }
        glLinkProgram(program)
        val status = glGetProgrami(program, GL_LINK_STATUS)
        if (status == GL_FALSE) {
            val strInfoLog = glGetProgramInfoLog(program)
            println("Linker failure: $strInfoLog\n")
        }
        for (shader in shaderList) {
            glDetachShader(program, shader)
        }
        return program
    }

    val start = System.currentTimeMillis()

    fun glutGet(): Float {
        val fElapsedTime = (System.currentTimeMillis() - start) / 1000.0f
        return fElapsedTime
    }
}

data class Color(val r: Float, val g: Float, val b: Float, val a: Float)

val RED_COLOR = Color(1.0f, 0.0f, 0.0f, 1.0f)
val GREEN_COLOR = Color(0.0f, 1.0f, 0.0f, 1.0f)
val BLUE_COLOR = Color(0.0f, 0.0f, 1.0f, 1.0f)
val YELLOW_COLOR = Color(1.0f, 1.0f, 0.0f, 1.0f)
val CYAN_COLOR = Color(0.0f, 1.0f, 1.0f, 1.0f)
val MAGENTA_COLOR = Color(1.0f, 0.0f, 1.0f, 1.0f)
val GREY_COLOR = Color(0.8f, 0.8f, 0.8f, 1.0f)
val BROWN_COLOR = Color(0.5f, 0.5f, 0.0f, 1.0f)


class VertexData(vararg elements: Float) {

    val vertices = elements

    fun colors(vararg colors: Color): FloatArray {
        var result = vertices
        for (color in colors) {
            result += floatArrayOf(color.r, color.g, color.b, color.a)
        }
        return result
    }
}

fun degToRad(fAngDeg: Float): Float {
    val fDegToRad = 3.14159f * 2.0f / 360.0f
    return fAngDeg * fDegToRad
}

fun clamp(fValue: Float, fMinValue: Float, fMaxValue: Float): Float {
    if (fValue < fMinValue)
        return fMinValue
    if (fValue > fMaxValue)
        return fMaxValue
    return fValue
}

class Mesh(filename: String) {

    fun render() {
        TODO("Not yet implemented")
    }
}