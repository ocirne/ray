package io.github.ocirne.ray.modern3d

import org.lwjgl.opengl.GL30C.*

class Tutorial4: Framework {

    private val theProgram: Int
    private var offsetUniform: Int = 0
    private var perspectiveMatrixUnif: Int = 0
    private val perspectiveMatrix = FloatArray(16) { 0f }
    private val fFrustumScale = 1.0f

    private val vertexBufferObject: Int
    private val vao: Int

    val vertexData = floatArrayOf(
        0.25f,  0.25f, -1.25f, 1.0f,
        0.25f, -0.25f, -1.25f, 1.0f,
        -0.25f,  0.25f, -1.25f, 1.0f,

        0.25f, -0.25f, -1.25f, 1.0f,
        -0.25f, -0.25f, -1.25f, 1.0f,
        -0.25f,  0.25f, -1.25f, 1.0f,

        0.25f,  0.25f, -2.75f, 1.0f,
        -0.25f,  0.25f, -2.75f, 1.0f,
        0.25f, -0.25f, -2.75f, 1.0f,

        0.25f, -0.25f, -2.75f, 1.0f,
        -0.25f,  0.25f, -2.75f, 1.0f,
        -0.25f, -0.25f, -2.75f, 1.0f,

        -0.25f,  0.25f, -1.25f, 1.0f,
        -0.25f, -0.25f, -1.25f, 1.0f,
        -0.25f, -0.25f, -2.75f, 1.0f,

        -0.25f,  0.25f, -1.25f, 1.0f,
        -0.25f, -0.25f, -2.75f, 1.0f,
        -0.25f,  0.25f, -2.75f, 1.0f,

        0.25f,  0.25f, -1.25f, 1.0f,
        0.25f, -0.25f, -2.75f, 1.0f,
        0.25f, -0.25f, -1.25f, 1.0f,

        0.25f,  0.25f, -1.25f, 1.0f,
        0.25f,  0.25f, -2.75f, 1.0f,
        0.25f, -0.25f, -2.75f, 1.0f,

        0.25f,  0.25f, -2.75f, 1.0f,
        0.25f,  0.25f, -1.25f, 1.0f,
        -0.25f,  0.25f, -1.25f, 1.0f,

        0.25f,  0.25f, -2.75f, 1.0f,
        -0.25f,  0.25f, -1.25f, 1.0f,
        -0.25f,  0.25f, -2.75f, 1.0f,

        0.25f, -0.25f, -2.75f, 1.0f,
        -0.25f, -0.25f, -1.25f, 1.0f,
        0.25f, -0.25f, -1.25f, 1.0f,

        0.25f, -0.25f, -2.75f, 1.0f,
        -0.25f, -0.25f, -2.75f, 1.0f,
        -0.25f, -0.25f, -1.25f, 1.0f,




        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,

        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,

        0.8f, 0.8f, 0.8f, 1.0f,
        0.8f, 0.8f, 0.8f, 1.0f,
        0.8f, 0.8f, 0.8f, 1.0f,

        0.8f, 0.8f, 0.8f, 1.0f,
        0.8f, 0.8f, 0.8f, 1.0f,
        0.8f, 0.8f, 0.8f, 1.0f,

        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,

        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,

        0.5f, 0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f,

        0.5f, 0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f,

        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,

        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,

        0.0f, 1.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 1.0f, 1.0f,

        0.0f, 1.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 1.0f, 1.0f,
        0.0f, 1.0f, 1.0f, 1.0f)

    init {
        theProgram = initializeProgram()
        vertexBufferObject = initializeVertexBuffer()

        vao = glGenVertexArrays()
        glBindVertexArray(vao)

        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glFrontFace(GL_CW)
    }

    override fun display() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

        glUseProgram(theProgram)
        glUniform2f(offsetUniform, 1.5f, 0.5f)

        val colorData = vertexData.size * 2L

        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0)
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, colorData)

        glDrawArrays(GL_TRIANGLES, 0, 36)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glUseProgram(0)
    }

    private fun initializeVertexBuffer(): Int {
        val vertexBufferObject = glGenBuffers()

        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject)
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)

        return vertexBufferObject
    }

    fun initializeProgram(): Int {
        val shaderList = listOf(
            Support.loadShader(GL_VERTEX_SHADER, "MatrixPerspective.vert"),
            Support.loadShader(GL_FRAGMENT_SHADER, "StandardColors.frag")
        )
        val theProgram = Support.createProgram(shaderList)
        offsetUniform = glGetUniformLocation(theProgram, "offset")
        perspectiveMatrixUnif = glGetUniformLocation(theProgram, "perspectiveMatrix");

        val fzNear = 0.5f
        val fzFar = 3.0f

        perspectiveMatrix[0] = fFrustumScale
        perspectiveMatrix[5] = fFrustumScale
        perspectiveMatrix[10] = (fzFar + fzNear) / (fzNear - fzFar)
        perspectiveMatrix[14] = (2 * fzFar * fzNear) / (fzNear - fzFar)
        perspectiveMatrix[11] = -1.0f

        glUseProgram(theProgram);
        glUniformMatrix4fv(perspectiveMatrixUnif, false, perspectiveMatrix)

        glUseProgram(0);
        for (shader in shaderList) {
            glDeleteShader(shader)
        }
        return theProgram
    }

    override fun reshape(w: Int, h: Int) {
        perspectiveMatrix[0] = fFrustumScale / (w / h.toFloat())
        perspectiveMatrix[5] = fFrustumScale
        glUseProgram(theProgram)
        glUniformMatrix4fv(perspectiveMatrixUnif, false, perspectiveMatrix)
        glUseProgram(0)
        glViewport(0,0, w, h)
    }
}