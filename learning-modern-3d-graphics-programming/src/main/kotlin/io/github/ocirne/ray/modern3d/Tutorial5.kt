package io.github.ocirne.ray.modern3d

import org.lwjgl.opengl.GL30C.*

class Tutorial5: Framework {

    private val theProgram: Int
    private var offsetUniform: Int = 0
    private var perspectiveMatrixUnif: Int = 0
    private val perspectiveMatrix = FloatArray(16) { 0f }
    private val fFrustumScale = 1.0f

    private val vertexBufferObject: Int
    private val indexBufferObject: Int
    private val vaoObject1: Int
    private val vaoObject2: Int

    val numberOfVertices = 36

    val RIGHT_EXTENT = 0.8f
    val LEFT_EXTENT = -RIGHT_EXTENT
    val TOP_EXTENT = 0.20f
    val MIDDLE_EXTENT = 0.0f
    val BOTTOM_EXTENT = -TOP_EXTENT
    val FRONT_EXTENT = -1.25f
    val REAR_EXTENT = -1.75f

    val vertexData = floatArrayOf(
        // Object 1 positions
        LEFT_EXTENT,	TOP_EXTENT,		REAR_EXTENT,
        LEFT_EXTENT,	MIDDLE_EXTENT,	FRONT_EXTENT,
        RIGHT_EXTENT,	MIDDLE_EXTENT,	FRONT_EXTENT,
        RIGHT_EXTENT,	TOP_EXTENT,		REAR_EXTENT,

        LEFT_EXTENT,	BOTTOM_EXTENT,	REAR_EXTENT,
        LEFT_EXTENT,	MIDDLE_EXTENT,	FRONT_EXTENT,
        RIGHT_EXTENT,	MIDDLE_EXTENT,	FRONT_EXTENT,
        RIGHT_EXTENT,	BOTTOM_EXTENT,	REAR_EXTENT,

        LEFT_EXTENT,	TOP_EXTENT,		REAR_EXTENT,
        LEFT_EXTENT,	MIDDLE_EXTENT,	FRONT_EXTENT,
        LEFT_EXTENT,	BOTTOM_EXTENT,	REAR_EXTENT,

        RIGHT_EXTENT,	TOP_EXTENT,		REAR_EXTENT,
        RIGHT_EXTENT,	MIDDLE_EXTENT,	FRONT_EXTENT,
        RIGHT_EXTENT,	BOTTOM_EXTENT,	REAR_EXTENT,

        LEFT_EXTENT,	BOTTOM_EXTENT,	REAR_EXTENT,
        LEFT_EXTENT,	TOP_EXTENT,		REAR_EXTENT,
        RIGHT_EXTENT,	TOP_EXTENT,		REAR_EXTENT,
        RIGHT_EXTENT,	BOTTOM_EXTENT,	REAR_EXTENT,

        // Object 2 positions
        TOP_EXTENT,		RIGHT_EXTENT,	REAR_EXTENT,
        MIDDLE_EXTENT,	RIGHT_EXTENT,	FRONT_EXTENT,
        MIDDLE_EXTENT,	LEFT_EXTENT,	FRONT_EXTENT,
        TOP_EXTENT,		LEFT_EXTENT,	REAR_EXTENT,

        BOTTOM_EXTENT,	RIGHT_EXTENT,	REAR_EXTENT,
        MIDDLE_EXTENT,	RIGHT_EXTENT,	FRONT_EXTENT,
        MIDDLE_EXTENT,	LEFT_EXTENT,	FRONT_EXTENT,
        BOTTOM_EXTENT,	LEFT_EXTENT,	REAR_EXTENT,

        TOP_EXTENT,		RIGHT_EXTENT,	REAR_EXTENT,
        MIDDLE_EXTENT,	RIGHT_EXTENT,	FRONT_EXTENT,
        BOTTOM_EXTENT,	RIGHT_EXTENT,	REAR_EXTENT,

        TOP_EXTENT,		LEFT_EXTENT,	REAR_EXTENT,
        MIDDLE_EXTENT,	LEFT_EXTENT,	FRONT_EXTENT,
        BOTTOM_EXTENT,	LEFT_EXTENT,	REAR_EXTENT,

        BOTTOM_EXTENT,	RIGHT_EXTENT,	REAR_EXTENT,
        TOP_EXTENT,		RIGHT_EXTENT,	REAR_EXTENT,
        TOP_EXTENT,		LEFT_EXTENT,	REAR_EXTENT,
        BOTTOM_EXTENT,	LEFT_EXTENT,	REAR_EXTENT,

        // Object 1 colors
        0.75f, 0.75f, 1.0f, 1.0f,
        0.75f, 0.75f, 1.0f, 1.0f,
        0.75f, 0.75f, 1.0f, 1.0f,
        0.75f, 0.75f, 1.0f, 1.0f,

        0.0f, 0.5f, 0.0f, 1.0f,
        0.0f, 0.5f, 0.0f, 1.0f,
        0.0f, 0.5f, 0.0f, 1.0f,
        0.0f, 0.5f, 0.0f, 1.0f,

        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,

        0.8f, 0.8f, 0.8f, 1.0f,
        0.8f, 0.8f, 0.8f, 1.0f,
        0.8f, 0.8f, 0.8f, 1.0f,

        0.5f, 0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f,

        // Object 2 colors
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,

        0.5f, 0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f,

        0.0f, 0.5f, 0.0f, 1.0f,
        0.0f, 0.5f, 0.0f, 1.0f,
        0.0f, 0.5f, 0.0f, 1.0f,

        0.75f, 0.75f, 1.0f, 1.0f,
        0.75f, 0.75f, 1.0f, 1.0f,
        0.75f, 0.75f, 1.0f, 1.0f,

        0.8f, 0.8f, 0.8f, 1.0f,
        0.8f, 0.8f, 0.8f, 1.0f,
        0.8f, 0.8f, 0.8f, 1.0f,
        0.8f, 0.8f, 0.8f, 1.0f,
    )

    val indexData = shortArrayOf(
        0, 2, 1,
        3, 2, 0,

        4, 5, 6,
        6, 7, 4,

        8, 9, 10,
        11, 13, 12,

        14, 16, 15,
        17, 16, 14,
    )

    init {
        theProgram = initializeProgram()
        vertexBufferObject = initializeVertexBuffer()
        indexBufferObject = initializeIndexBuffer()
        vaoObject1 = initializeVertexArrayObject1()
        vaoObject2 = initializeVertexArrayObject2()

        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glFrontFace(GL_CW)
    }

    override fun display() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

        glUseProgram(theProgram)
        glBindVertexArray(vaoObject1)
        glUniform3f(offsetUniform, 0.0f, 0.0f, 0.0f)
        glDrawElements(GL_TRIANGLES, indexData.size, GL_UNSIGNED_SHORT, 0)

        glBindVertexArray(vaoObject2)
        glUniform3f(offsetUniform, 0.0f, 0.0f, -1.0f)
        glDrawElements(GL_TRIANGLES, indexData.size, GL_UNSIGNED_SHORT, 0)

        glBindVertexArray(0)
        glUseProgram(0)
    }

    private fun initializeVertexBuffer(): Int {
        val vertexBufferObj = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObj)
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        return vertexBufferObj
    }

    private fun initializeIndexBuffer(): Int {
        val indexBufferObj = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, indexBufferObj)
        glBufferData(GL_ARRAY_BUFFER, indexData, GL_STATIC_DRAW)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        return indexBufferObj
    }

    fun initializeVertexArrayObject1(): Int {
        val vaoObj = glGenVertexArrays()
        glBindVertexArray(vaoObj)

        val colorDataOffset = 12 * numberOfVertices

        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, colorDataOffset.toLong())
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject)

        glBindVertexArray(0)
        return vaoObj
    }

    fun initializeVertexArrayObject2(): Int {
        val vaoObj = glGenVertexArrays()
        glBindVertexArray(vaoObj)

        val posDataOffset = 6 * numberOfVertices
        val colorDataOffset = 20 * numberOfVertices

        //Use the same buffer object previously bound to GL_ARRAY_BUFFER. (!)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, posDataOffset.toLong())
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, colorDataOffset.toLong())
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject)

        glBindVertexArray(0)
        return vaoObj
    }

    fun initializeProgram(): Int {
        val shaderList = listOf(
            Support.loadShader(GL_VERTEX_SHADER, "Standard.vert"),
            Support.loadShader(GL_FRAGMENT_SHADER, "Standard.frag")
        )
        val theProgram = Support.createProgram(shaderList)
        offsetUniform = glGetUniformLocation(theProgram, "offset")
        perspectiveMatrixUnif = glGetUniformLocation(theProgram, "perspectiveMatrix");

        val fzNear = 1.0f
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
        perspectiveMatrix[0] = fFrustumScale / (h / w.toFloat())
        perspectiveMatrix[5] = fFrustumScale
        glUseProgram(theProgram)
        glUniformMatrix4fv(perspectiveMatrixUnif, false, perspectiveMatrix)
        glUseProgram(0)
        glViewport(0,0, w, h)
    }
}
