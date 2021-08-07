package io.github.ocirne.ray.modern3d

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL30C.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Tutorial6FunWithMatrices : Framework {

    private var theProgram: Int = 0
    private var positionAttrib: Int = 0
    private var colorAttrib: Int = 0

    private var modelToCameraMatrixUnif: Int = 0
    private var cameraToClipMatrixUnif: Int = 0

    private var cameraToClipMatrix = Mat4()

    private var vertexBufferObject: Int = 0
    private var indexBufferObject: Int = 0
    private var vao: Int = 0

    private val fFrustumScale = calcFrustumScale(45.0f)

    private var armature: Hierarchy? = null

    private val numberOfVertices = 24

    private val vertexData = VertexData(
        //Front
        +1.0f, +1.0f, +1.0f,
        +1.0f, -1.0f, +1.0f,
        -1.0f, -1.0f, +1.0f,
        -1.0f, +1.0f, +1.0f,

        //Top
        +1.0f, +1.0f, +1.0f,
        -1.0f, +1.0f, +1.0f,
        -1.0f, +1.0f, -1.0f,
        +1.0f, +1.0f, -1.0f,

        //Left
        +1.0f, +1.0f, +1.0f,
        +1.0f, +1.0f, -1.0f,
        +1.0f, -1.0f, -1.0f,
        +1.0f, -1.0f, +1.0f,

        //Back
        +1.0f, +1.0f, -1.0f,
        -1.0f, +1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        +1.0f, -1.0f, -1.0f,

        //Bottom
        +1.0f, -1.0f, +1.0f,
        +1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, -1.0f, +1.0f,

        //Right
        -1.0f, +1.0f, +1.0f,
        -1.0f, -1.0f, +1.0f,
        -1.0f, -1.0f, -1.0f,
        -1.0f, +1.0f, -1.0f
    ).colors(
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
        RED_COLOR,

        YELLOW_COLOR,
        YELLOW_COLOR,
        YELLOW_COLOR,
        YELLOW_COLOR,

        CYAN_COLOR,
        CYAN_COLOR,
        CYAN_COLOR,
        CYAN_COLOR,

        MAGENTA_COLOR,
        MAGENTA_COLOR,
        MAGENTA_COLOR,
        MAGENTA_COLOR,
    )

    private val indexData = shortArrayOf(
        0, 1, 2,
        2, 3, 0,

        4, 5, 6,
        6, 7, 4,

        8, 9, 10,
        10, 11, 8,

        12, 13, 14,
        14, 15, 12,

        16, 17, 18,
        18, 19, 16,

        20, 21, 22,
        22, 23, 20,
    )

    private fun initializeProgram(): Int {
        val shaderList = listOf(
            Support.loadShader(GL_VERTEX_SHADER, "PosColorLocalTransform.vert"),
            Support.loadShader(GL_FRAGMENT_SHADER, "ColorPassthrough.frag")
        )
        val theProgram = Support.createProgram(shaderList)

        positionAttrib = glGetAttribLocation(theProgram, "position")
        colorAttrib = glGetAttribLocation(theProgram, "color")

        modelToCameraMatrixUnif = glGetUniformLocation(theProgram, "modelToCameraMatrix")
        cameraToClipMatrixUnif = glGetUniformLocation(theProgram, "cameraToClipMatrix")

        val fzNear = 1.0f
        val fzFar = 100.0f

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

        armature = Hierarchy(theProgram, vao, modelToCameraMatrixUnif, indexData.size)

        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glFrontFace(GL_CW)

        glEnable(GL_DEPTH_TEST)
        glDepthMask(true)
        glDepthFunc(GL_LEQUAL)
        glDepthRange(0.0, 1.0)
    }

    override fun display() {
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        glClearDepth(1.0)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

        armature!!.draw()
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

    private fun initializeVertexArrayObject(): Int {
        val vao = glGenVertexArrays()
        glBindVertexArray(vao)

        val colorDataOffset = 12L * numberOfVertices
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject)
        glEnableVertexAttribArray(positionAttrib)
        glEnableVertexAttribArray(colorAttrib)
        glVertexAttribPointer(positionAttrib, 3, GL_FLOAT, false, 0, 0)
        glVertexAttribPointer(colorAttrib, 4, GL_FLOAT, false, 0, colorDataOffset)
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
        glViewport(0, 0, w, h)
    }

    override fun keyboard(key: Int, x: Int, y: Int) {
        when (key) {
            GLFW_KEY_A -> armature!!.adjBase(true)
            GLFW_KEY_D -> armature!!.adjBase(false)
            GLFW_KEY_W -> armature!!.adjUpperArm(false)
            GLFW_KEY_S -> armature!!.adjUpperArm(true)
            GLFW_KEY_R -> armature!!.adjLowerArm(false)
            GLFW_KEY_F -> armature!!.adjLowerArm(true)
            GLFW_KEY_T -> armature!!.adjWristPitch(false)
            GLFW_KEY_G -> armature!!.adjWristPitch(true)
            GLFW_KEY_Z -> armature!!.adjWristRoll(true)
            GLFW_KEY_C -> armature!!.adjWristRoll(false)
            GLFW_KEY_Q -> armature!!.adjFingerOpen(true)
            GLFW_KEY_E -> armature!!.adjFingerOpen(false)
            GLFW_KEY_SPACE -> armature!!.writePose()
        }
    }

    private fun calcFrustumScale(fFovDeg: Float): Float {
        val degToRad = 3.14159f * 2.0f / 360.0f
        val fFovRad = fFovDeg * degToRad
        return 1.0f / tan(fFovRad / 2.0f)
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
}

class Hierarchy(val theProgram: Int, val vao: Int, val modelToCameraMatrixUnif: Int, val indexDataSize: Int) {

    private val posBase = Vec3(3.0f, -5.0f, -40.0f)
    private var angBase = -45.0f
    private val posBaseLeft = Vec3(2.0f, 0.0f, 0.0f)
    private val posBaseRight = Vec3(-2.0f, 0.0f, 0.0f)
    private val scaleBaseZ = 3.0f
    private var angUpperArm = -33.75f
    private val sizeUpperArm = 9.0f
    private val posLowerArm = Vec3(0.0f, 0.0f, 8.0f)
    private var angLowerArm = 146.25f
    private val lenLowerArm = 5.0f
    private val widthLowerArm = 1.5f
    private val posWrist = Vec3(0.0f, 0.0f, 5.0f)
    private var angWristRoll = 0.0f
    private var angWristPitch = 67.5f
    private val lenWrist = 2.0f
    private val widthWrist = 2.0f
    private val posLeftFinger = Vec3(1.0f, 0.0f, 1.0f)
    private val posRightFinger = Vec3(-1.0f, 0.0f, 1.0f)
    private var angFingerOpen = 180.0f
    private val lenFinger = 2.0f
    private val widthFinger = 0.5f
    private val angLowerFinger = 45.0f

    private val standardAngleIncrement = 11.25f
    private val smallAngleIncrement = 9.0f

    fun draw() {
        val modelToCameraStack = MatrixStack()

        glUseProgram(theProgram)
        glBindVertexArray(vao)

        modelToCameraStack.translate(posBase)
        modelToCameraStack.rotateY(angBase)

        // Draw left base.
        modelToCameraStack.push()
        modelToCameraStack.translate(posBaseLeft)
        modelToCameraStack.scale(Vec3(1.0f, 1.0f, scaleBaseZ))
        glUniformMatrix4fv(modelToCameraMatrixUnif, false, modelToCameraStack.top().toFloatArray())
        glDrawElements(GL_TRIANGLES, indexDataSize, GL_UNSIGNED_SHORT, 0)
        modelToCameraStack.pop()

        //Draw right base.
        modelToCameraStack.push()
        modelToCameraStack.translate(posBaseRight)
        modelToCameraStack.scale(Vec3(1.0f, 1.0f, scaleBaseZ))
        glUniformMatrix4fv(modelToCameraMatrixUnif, false, modelToCameraStack.top().toFloatArray())
        glDrawElements(GL_TRIANGLES, indexDataSize, GL_UNSIGNED_SHORT, 0)
        modelToCameraStack.pop()

        // Draw main arm.
        drawUpperArm(modelToCameraStack)

        glBindVertexArray(0)
        glUseProgram(0)
    }


    fun adjBase(bIncrement: Boolean) {
        angBase += if (bIncrement) standardAngleIncrement else -standardAngleIncrement
        angBase %= 360
    }

    fun adjUpperArm(bIncrement: Boolean) {
        angUpperArm += if (bIncrement) standardAngleIncrement else -standardAngleIncrement
        angUpperArm = clamp(angUpperArm, -90.0f, 0.0f)
    }

    fun adjLowerArm(bIncrement: Boolean) {
        angLowerArm += if (bIncrement) standardAngleIncrement else -standardAngleIncrement
        angLowerArm = clamp(angLowerArm, 0.0f, 146.25f)
    }

    fun adjWristPitch(bIncrement: Boolean) {
        angWristPitch += if (bIncrement) standardAngleIncrement else -standardAngleIncrement
        angWristPitch = clamp(angWristPitch, 0.0f, 90.0f)
    }

    fun adjWristRoll(bIncrement: Boolean) {
        angWristRoll += if (bIncrement) standardAngleIncrement else -standardAngleIncrement
        angWristRoll %= 360
    }

    fun adjFingerOpen(bIncrement: Boolean) {
        angFingerOpen += if (bIncrement) smallAngleIncrement else -smallAngleIncrement
        angFingerOpen = clamp(angFingerOpen, 9.0f, 180.0f)
    }

    fun writePose() {
        println("angBase:\t$angBase")
        println("angUpperArm:\t$angUpperArm")
        println("angLowerArm:\t$angLowerArm")
        println("angWristPitch:\t$angWristPitch")
        println("angWristRoll:\t$angWristRoll")
        println("angFingerOpen:\t$angFingerOpen")
        println()
    }

    private fun drawFingers(modelToCameraStack: MatrixStack) {
        //Draw left finger
        modelToCameraStack.push()
        modelToCameraStack.translate(posLeftFinger)
        modelToCameraStack.rotateY(angFingerOpen)

        modelToCameraStack.push()
        modelToCameraStack.translate(Vec3(0.0f, 0.0f, lenFinger / 2.0f))
        modelToCameraStack.scale(Vec3(widthFinger / 2.0f, widthFinger / 2.0f, lenFinger / 2.0f))
        glUniformMatrix4fv(modelToCameraMatrixUnif, false, modelToCameraStack.top().toFloatArray())
        glDrawElements(GL_TRIANGLES, indexDataSize, GL_UNSIGNED_SHORT, 0)
        modelToCameraStack.pop()

        //Draw left lower finger
        modelToCameraStack.push()
        modelToCameraStack.translate(Vec3(0.0f, 0.0f, lenFinger))
        modelToCameraStack.rotateY(-angLowerFinger)

        modelToCameraStack.push()
        modelToCameraStack.translate(Vec3(0.0f, 0.0f, lenFinger / 2.0f))
        modelToCameraStack.scale(Vec3(widthFinger / 2.0f, widthFinger / 2.0f, lenFinger / 2.0f))
        glUniformMatrix4fv(modelToCameraMatrixUnif, false, modelToCameraStack.top().toFloatArray())
        glDrawElements(GL_TRIANGLES, indexDataSize, GL_UNSIGNED_SHORT, 0)
        modelToCameraStack.pop()

        modelToCameraStack.pop()

        modelToCameraStack.pop()

        //Draw right finger
        modelToCameraStack.push()
        modelToCameraStack.translate(posRightFinger)
        modelToCameraStack.rotateY(-angFingerOpen)

        modelToCameraStack.push()
        modelToCameraStack.translate(Vec3(0.0f, 0.0f, lenFinger / 2.0f))
        modelToCameraStack.scale(Vec3(widthFinger / 2.0f, widthFinger / 2.0f, lenFinger / 2.0f))
        glUniformMatrix4fv(modelToCameraMatrixUnif, false, modelToCameraStack.top().toFloatArray())
        glDrawElements(GL_TRIANGLES, indexDataSize, GL_UNSIGNED_SHORT, 0)
        modelToCameraStack.pop()

        //Draw right lower finger
        modelToCameraStack.push()
        modelToCameraStack.translate(Vec3(0.0f, 0.0f, lenFinger))
        modelToCameraStack.rotateY(angLowerFinger)

        modelToCameraStack.push()
        modelToCameraStack.translate(Vec3(0.0f, 0.0f, lenFinger / 2.0f))
        modelToCameraStack.scale(Vec3(widthFinger / 2.0f, widthFinger / 2.0f, lenFinger / 2.0f))
        glUniformMatrix4fv(modelToCameraMatrixUnif, false, modelToCameraStack.top().toFloatArray())
        glDrawElements(GL_TRIANGLES, indexDataSize, GL_UNSIGNED_SHORT, 0)
        modelToCameraStack.pop()

        modelToCameraStack.pop()

        modelToCameraStack.pop()
    }

    private fun drawWrist(modelToCameraStack: MatrixStack) {
        modelToCameraStack.push()
        modelToCameraStack.translate(posWrist)
        modelToCameraStack.rotateZ(angWristRoll)
        modelToCameraStack.rotateX(angWristPitch)

        modelToCameraStack.push()
        modelToCameraStack.scale(Vec3(widthWrist / 2.0f, widthWrist / 2.0f, lenWrist / 2.0f))
        glUniformMatrix4fv(modelToCameraMatrixUnif, false, modelToCameraStack.top().toFloatArray())
        glDrawElements(GL_TRIANGLES, indexDataSize, GL_UNSIGNED_SHORT, 0)
        modelToCameraStack.pop()

        drawFingers(modelToCameraStack)

        modelToCameraStack.pop()
    }

    private fun drawLowerArm(modelToCameraStack: MatrixStack) {
        modelToCameraStack.push()
        modelToCameraStack.translate(posLowerArm)
        modelToCameraStack.rotateX(angLowerArm)

        modelToCameraStack.push()
        modelToCameraStack.translate(Vec3(0.0f, 0.0f, lenLowerArm / 2.0f))
        modelToCameraStack.scale(Vec3(widthLowerArm / 2.0f, widthLowerArm / 2.0f, lenLowerArm / 2.0f))
        glUniformMatrix4fv(modelToCameraMatrixUnif, false, modelToCameraStack.top().toFloatArray())
        glDrawElements(GL_TRIANGLES, indexDataSize, GL_UNSIGNED_SHORT, 0)
        modelToCameraStack.pop()

        drawWrist(modelToCameraStack)

        modelToCameraStack.pop()
    }

    private fun drawUpperArm(modelToCameraStack: MatrixStack) {
        modelToCameraStack.push()
        modelToCameraStack.rotateX(angUpperArm)

        modelToCameraStack.push()
        modelToCameraStack.translate(Vec3(0.0f, 0.0f, (sizeUpperArm / 2.0f) - 1.0f))
        modelToCameraStack.scale(Vec3(1.0f, 1.0f, sizeUpperArm / 2.0f))
        glUniformMatrix4fv(modelToCameraMatrixUnif, false, modelToCameraStack.top().toFloatArray())
        glDrawElements(GL_TRIANGLES, indexDataSize, GL_UNSIGNED_SHORT, 0)
        modelToCameraStack.pop()

        drawLowerArm(modelToCameraStack)

        modelToCameraStack.pop()
    }
}
