package io.github.ocirne.ray.modern3d

import org.lwjgl.opengl.GL30C.*
import kotlin.math.min

class RotatingTriangle: Framework {

    private val theProgram: Int
    private var elapsedTimeUniform: Int = 0
    private val positionBufferObject: Int
    private val vao: Int

    init {
        theProgram = initializeProgram()
        positionBufferObject = initializeVertexBuffer()

        vao = glGenVertexArrays()
        glBindVertexArray(vao)
    }

    override fun display() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

        glUseProgram(theProgram)
        glUniform1f(elapsedTimeUniform, Support.glutGet())

        glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0)
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 48)

        glDrawArrays(GL_TRIANGLES, 0, 3)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glUseProgram(0)
    }

    private fun initializeVertexBuffer(): Int {
        val vertexPositions = floatArrayOf(
            0.0f,    0.5f, 0.0f, 1.0f,
            0.5f, -0.366f, 0.0f, 1.0f,
            -0.5f, -0.366f, 0.0f, 1.0f,
            1.0f,    0.0f, 0.0f, 1.0f,
            0.0f,    1.0f, 0.0f, 1.0f,
            0.0f,    0.0f, 1.0f, 1.0f,
        )
        val positionBufferObject = glGenBuffers()

        glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject)
        glBufferData(GL_ARRAY_BUFFER, vertexPositions, GL_STREAM_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        return positionBufferObject
    }

    fun initializeProgram(): Int {
        val shaderList = listOf(
            Support.loadShader(GL_VERTEX_SHADER, "OffsettingShader.vert"),
            Support.loadShader(GL_FRAGMENT_SHADER, "calcColor.frag")
        )
        val theProgram = Support.createProgram(shaderList)
        elapsedTimeUniform = glGetUniformLocation(theProgram, "time")

        val loopDurationUnf = glGetUniformLocation(theProgram, "loopDuration")
        val fragLoopDurUnf = glGetUniformLocation(theProgram, "fragLoopDuration")
        glUseProgram(theProgram)
        glUniform1f(loopDurationUnf, 5.0f)
        glUniform1f(fragLoopDurUnf, 10.0f)
        glUseProgram(0)

        for (shader in shaderList) {
            glDeleteShader(shader)
        }
        return theProgram
    }

    override fun reshape(w: Int, h: Int) {
        val size = min(w, h)
        glViewport(
            (w - size) / 2,
            (h - size) / 2,
            size,
            size
        )
    }
}
