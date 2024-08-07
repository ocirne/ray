package io.github.ocirne.ray.modern3d

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL30C.*
import org.lwjgl.opengl.GL32C.GL_DEPTH_CLAMP
import org.lwjgl.opengl.GL32C.glDrawElementsBaseVertex

class Tutorial5: Framework {

    private var theProgram: Int = 0
    private var offsetUniform: Int = 0
    private var perspectiveMatrixUnif: Int = 0
    private val perspectiveMatrix = FloatArray(16) { 0f }
    private val fFrustumScale = 1.0f

    private var vertexBufferObject: Int = 0
    private var indexBufferObject: Int = 0
    private var vao: Int = 0

    val numberOfVertices = 36

    val RIGHT_EXTENT = 0.8f
    val LEFT_EXTENT = -RIGHT_EXTENT
    val TOP_EXTENT = 0.20f
    val MIDDLE_EXTENT = 0.0f
    val BOTTOM_EXTENT = -TOP_EXTENT
    val FRONT_EXTENT = -1.25f
    val REAR_EXTENT = -1.75f

    val vertexData = VertexData(
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

//	0, 2, 1,
//	3, 2, 0,

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
    ).colors(
        //Object 1 colors
        GREEN_COLOR,
        GREEN_COLOR,
        GREEN_COLOR,
        GREEN_COLOR,

        BLUE_COLOR,
        BLUE_COLOR,
        BLUE_COLOR,
        BLUE_COLOR,

        RED_COLOR,
        RED_COLOR,
        RED_COLOR,

        GREY_COLOR,
        GREY_COLOR,
        GREY_COLOR,

        BROWN_COLOR,
        BROWN_COLOR,
        BROWN_COLOR,
        BROWN_COLOR,

        //Object 2 colors
        RED_COLOR,
        RED_COLOR,
        RED_COLOR,
        RED_COLOR,

        BROWN_COLOR,
        BROWN_COLOR,
        BROWN_COLOR,
        BROWN_COLOR,

        BLUE_COLOR,
        BLUE_COLOR,
        BLUE_COLOR,

        GREEN_COLOR,
        GREEN_COLOR,
        GREEN_COLOR,

        GREY_COLOR,
        GREY_COLOR,
        GREY_COLOR,
        GREY_COLOR,
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

    override fun initialization() {
        theProgram = initializeProgram()
        vertexBufferObject = initializeVertexBuffer()
        indexBufferObject = initializeIndexBuffer()
        vao = initializeVertexArrayObject()

        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glFrontFace(GL_CW)

        glEnable(GL_DEPTH_TEST)
        glDepthMask(true)
        glDepthFunc(GL_LESS)
        glDepthRange(0.0, 1.0)
    }

    override fun display() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        glClearDepth(1.0)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

        glUseProgram(theProgram)
        glBindVertexArray(vao)
        glUniform3f(offsetUniform, 0.0f, 0.0f, 0.5f)
        glDrawElements(GL_TRIANGLES, indexData.size, GL_UNSIGNED_SHORT, 0)

        glUniform3f(offsetUniform, 0.0f, 0.0f, 0.4f)
        glDrawElementsBaseVertex(GL_TRIANGLES, indexData.size, GL_UNSIGNED_SHORT, 0, numberOfVertices / 2)

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

    fun initializeVertexArrayObject(): Int {
        val vao = glGenVertexArrays()
        glBindVertexArray(vao)

        val colorDataOffset = 12L * numberOfVertices
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, colorDataOffset)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObject)

        glBindVertexArray(0)
        return vao
    }

    fun initializeProgram(): Int {
        val shaderList = listOf(
            Support.loadShader(GL_VERTEX_SHADER, "Standard.vert"),
            Support.loadShader(GL_FRAGMENT_SHADER, "Standard.frag")
        )
        val theProgram = Support.createProgram(shaderList)
        offsetUniform = glGetUniformLocation(theProgram, "offset")
        perspectiveMatrixUnif = glGetUniformLocation(theProgram, "perspectiveMatrix")

        val fzNear = 1.0f
        val fzFar = 3.0f

        perspectiveMatrix[0] = fFrustumScale
        perspectiveMatrix[5] = fFrustumScale
        perspectiveMatrix[10] = (fzFar + fzNear) / (fzNear - fzFar)
        perspectiveMatrix[14] = (2 * fzFar * fzNear) / (fzNear - fzFar)
        perspectiveMatrix[11] = -1.0f

        glUseProgram(theProgram)
        glUniformMatrix4fv(perspectiveMatrixUnif, false, perspectiveMatrix)

        glUseProgram(0)
        for (shader in shaderList) {
            glDeleteShader(shader)
        }
        return theProgram
    }

    var depthClampingActive = false

    override fun keyboard(key: Int, x: Int, y: Int) {
        when (key) {
            GLFW_KEY_SPACE -> {
                if (depthClampingActive) {
                    glDisable(GL_DEPTH_CLAMP)
                } else {
                    glEnable(GL_DEPTH_CLAMP)
                }
                depthClampingActive = !depthClampingActive
            }
        }
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
