package io.github.ocirne.ray.modern3d

import org.lwjgl.opengl.GL30C.*

class RotatingTriangle: Tutorial {

    private val theProgram: Int
    private val elapsedTimeUniform: Int
    private val positionBufferObject: Int

    init {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        theProgram = initializeProgram()
        elapsedTimeUniform = glGetUniformLocation(theProgram, "time")

        val loopDurationUnf = glGetUniformLocation(theProgram, "loopDuration")
        glUseProgram(theProgram)
        glUniform1f(loopDurationUnf, 5.0f)
        glUseProgram(0)

        positionBufferObject = initializeVertexBuffer()
    }

    override fun display() {
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
        glUseProgram(0);
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
}
