package io.github.ocirne.ray.modern3d

import io.github.ocirne.ray.modern3d.Support.glutGet
import org.lwjgl.opengl.GL30C.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Tutorial6: Framework {

    private var theProgram: Int = 0
    private var modelToCameraMatrixUnif: Int = 0
    private var cameraToClipMatrixUnif: Int = 0

    private var cameraToClipMatrix = Mat4()

    private var vertexBufferObject: Int = 0
    private var indexBufferObject: Int = 0
    private var vao: Int = 0

    val numberOfVertices = 8

    val fFrustumScale = calcFrustumScale(45.0f)

    val vertexData = floatArrayOf(
        +1.0f, +1.0f, +1.0f,
        -1.0f, -1.0f, +1.0f,
        -1.0f, +1.0f, -1.0f,
        +1.0f, -1.0f, -1.0f,

        -1.0f, -1.0f, -1.0f,
        +1.0f, +1.0f, -1.0f,
        +1.0f, -1.0f, +1.0f,
        -1.0f, +1.0f, +1.0f,

        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f,

        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        0.5f, 0.5f, 0.0f, 1.0f,
    )

    val indexData = shortArrayOf(
        0, 1, 2,
        1, 0, 3,
        2, 3, 0,
        3, 2, 1,

        5, 4, 6,
        4, 5, 7,
        7, 6, 4,
        6, 7, 5,
    )

    val g_instanceList = listOf(
        StationaryOffset,
        OvalOffset,
        BottomCircleOffset,
    )

    fun initializeProgram(): Int {
        val shaderList = listOf(
            Support.loadShader(GL_VERTEX_SHADER, "PosColorLocalTransform.vert"),
            Support.loadShader(GL_FRAGMENT_SHADER, "ColorPassthrough.frag")
        )
        val theProgram = Support.createProgram(shaderList)

        modelToCameraMatrixUnif = glGetUniformLocation(theProgram, "modelToCameraMatrix")
        cameraToClipMatrixUnif = glGetUniformLocation(theProgram, "cameraToClipMatrix")

        val fzNear = 1.0f
        val fzFar = 45.0f

        cameraToClipMatrix[0].x = fFrustumScale
        cameraToClipMatrix[1].y = fFrustumScale
        cameraToClipMatrix[2].z = (fzFar + fzNear) / (fzNear - fzFar)
        cameraToClipMatrix[2].w = -1.0f
        cameraToClipMatrix[3].z = (2 * fzFar * fzNear) / (fzNear - fzFar)

        glUseProgram(theProgram)
        glUniformMatrix4fv(cameraToClipMatrixUnif, false, cameraToClipMatrix.toFloatArray())
        glUseProgram(0)

        for (shader in shaderList) {
            glDeleteShader(shader)
        }
        return theProgram
    }

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
        glDepthFunc(GL_LEQUAL)
        glDepthRange(0.0, 1.0)
    }

    override fun display() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        glClearDepth(1.0)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

        glUseProgram(theProgram)
        glBindVertexArray(vao)

        val fElapsedTime = glutGet()
        for (currInst in g_instanceList) {
            val transformMatrix = currInst.constructMatrix(fElapsedTime)

            glUniformMatrix4fv(modelToCameraMatrixUnif, false, transformMatrix.toFloatArray())
            glDrawElements(GL_TRIANGLES, indexData.size, GL_UNSIGNED_SHORT, 0)
        }
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

    override fun reshape(w: Int, h: Int) {
        cameraToClipMatrix[0].x = fFrustumScale * (h / w.toFloat())
        cameraToClipMatrix[1].y = fFrustumScale
        glUseProgram(theProgram)
        glUniformMatrix4fv(cameraToClipMatrixUnif, false, cameraToClipMatrix.toFloatArray())
        glUseProgram(0)
        glViewport(0,0, w, h)
    }

    override fun keyboard(key: Int, x: Int, y: Int) { }

    fun calcFrustumScale(fFovDeg: Float): Float {
        val degToRad = 3.14159f * 2.0f / 360.0f
        val fFovRad = fFovDeg * degToRad
        return 1.0f / tan(fFovRad / 2.0f)
    }
}


object StationaryOffset: Instance() {
    override fun calcOffset(fElapsedTime: Float): Vec3 {
        return Vec3(0.0f, 0.0f, -20.0f)
    }
}

object OvalOffset: Instance() {

    override fun calcOffset(fElapsedTime: Float): Vec3 {
        val fLoopDuration = 3
        val fScale = 3.14159f * 2.0f / fLoopDuration

        val fCurrTimeThroughLoop = fElapsedTime % fLoopDuration

        return Vec3(
            cos(fCurrTimeThroughLoop * fScale) * 4.0f,
            sin(fCurrTimeThroughLoop * fScale) * 6.0f,
            -20.0f
        )
    }
}

object BottomCircleOffset: Instance() {

    override fun calcOffset(fElapsedTime: Float): Vec3 {
        val fLoopDuration = 12
        val fScale = 3.14159f * 2.0f / fLoopDuration

        val fCurrTimeThroughLoop = fElapsedTime % fLoopDuration

        return Vec3(
            cos(fCurrTimeThroughLoop * fScale) * 5.0f,
            -3.5f,
            sin(fCurrTimeThroughLoop * fScale) * 5.0f - 20.0f
        )
    }
}

abstract class Instance {

    fun constructMatrix(fElapsedTime: Float): Mat4 {
        val theMat = Mat4()

        theMat[3] = Vec4(calcOffset(fElapsedTime), 1.0f)

        return theMat
    }

    abstract fun calcOffset(fElapsedTime: Float): Vec3
}

class Vec4(var x: Float, var y: Float, var z: Float, var w: Float) {

    constructor(v3: Vec3, w: Float): this(v3.x, v3.y, v3.z, w)

    constructor(): this (0.0f, 0.0f, 0.0f, 0.0f)

    override fun toString(): String {
        return "$x $y $z $w"
    }
}

class Vec3(val x: Float, val y: Float, val z: Float)

class Mat4 {
    val m = Array(4) { Vec4() }

    init {
        m[0].x = 1.0f
        m[1].y = 1.0f
        m[2].z = 1.0f
        m[3].w = 1.0f
    }

    operator fun set(i: Int, vec: Vec4) {
        m[i] = vec
    }

    operator fun get(i: Int): Vec4 {
        return m[i]
    }

    fun toFloatArray(): FloatArray {
        val fa = FloatArray(16) { 1.0f }
        m.forEachIndexed { x, vec4 ->
            fa[4*x+0] = vec4.x
            fa[4*x+1] = vec4.y
            fa[4*x+2] = vec4.z
            fa[4*x+3] = vec4.w
        }
        return fa
    }

    override fun toString(): String {
        return m.map { v -> v.toString() }.joinToString()
    }
}