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