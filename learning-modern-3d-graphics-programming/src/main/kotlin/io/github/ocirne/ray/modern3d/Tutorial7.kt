package io.github.ocirne.ray.modern3d

import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL30C.*
import org.lwjgl.opengl.GL32C.GL_DEPTH_CLAMP
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

data class ProgramData(
    val theProgram: Int,
    val modelToWorldMatrixUnif: Int,
    val worldToCameraMatrixUnif: Int,
    val cameraToClipMatrixUnif: Int,
    val baseColorUnif: Int)

fun loadProgram(strVertexShader: String, strFragmentShader: String): ProgramData {
    val shaderList = listOf(
        Support.loadShader(GL_VERTEX_SHADER, strVertexShader),
        Support.loadShader(GL_FRAGMENT_SHADER, strFragmentShader)
    )
    val theProgram = Support.createProgram(shaderList)

    return ProgramData(
        theProgram = theProgram,
        modelToWorldMatrixUnif = glGetUniformLocation(theProgram, "modelToWorldMatrix"),
        worldToCameraMatrixUnif = glGetUniformLocation(theProgram, "worldToCameraMatrix"),
        cameraToClipMatrixUnif = glGetUniformLocation(theProgram, "cameraToClipMatrix"),
        baseColorUnif = glGetUniformLocation(theProgram, "baseColor"),
    )
}

data class TreeData(
    val fXPos: Float,
    val fZPos: Float,
    val fTrunkHeight: Float,
    val fConeHeight: Float)


class Tutorial7 : Framework {

    val g_fzNear = 1.0f
    val g_fzFar = 1000.0f

    var uniformColor: ProgramData? = null
    var objectColor: ProgramData? = null
    var uniformColorTint: ProgramData? = null

    var g_pConeMesh: Mesh? = null
    var g_pCylinderMesh: Mesh? = null
    var g_pCubeTintMesh: Mesh? = null
    var g_pCubeColorMesh: Mesh? = null
    var g_pPlaneMesh: Mesh? = null

    val g_fYAngle = 0.0f
    val g_fXAngle = 0.0f

    val g_fParthenonWidth = 14.0f
    val g_fParthenonLength = 20.0f
    val g_fParthenonColumnHeight = 5.0f
    val g_fParthenonBaseHeight = 1.0f
    val g_fParthenonTopHeight = 2.0f
    val g_fColumnBaseHeight = 0.25f

    var g_bDrawLookatPoint = false
    val g_camTarget = Vec3(0.0f, 0.4f, 0.0f)
    val g_sphereCamRelPos = Vec3(67.5f, -46.0f, 150.0f)

    val g_forest: Array<TreeData> = arrayOf(
        TreeData(-45.0f, -40.0f, 2.0f, 3.0f),
        TreeData(-42.0f, -35.0f, 2.0f, 3.0f),
        TreeData(-39.0f, -29.0f, 2.0f, 4.0f),
        TreeData(-44.0f, -26.0f, 3.0f, 3.0f),
        TreeData(-40.0f, -22.0f, 2.0f, 4.0f),
        TreeData(-36.0f, -15.0f, 3.0f, 3.0f),
        TreeData(-41.0f, -11.0f, 2.0f, 3.0f),
        TreeData(-37.0f, -6.0f, 3.0f, 3.0f),
        TreeData(-45.0f, 0.0f, 2.0f, 3.0f),
        TreeData(-39.0f, 4.0f, 3.0f, 4.0f),
        TreeData(-36.0f, 8.0f, 2.0f, 3.0f),
        TreeData(-44.0f, 13.0f, 3.0f, 3.0f),
        TreeData(-42.0f, 17.0f, 2.0f, 3.0f),
        TreeData(-38.0f, 23.0f, 3.0f, 4.0f),
        TreeData(-41.0f, 27.0f, 2.0f, 3.0f),
        TreeData(-39.0f, 32.0f, 3.0f, 3.0f),
        TreeData(-44.0f, 37.0f, 3.0f, 4.0f),
        TreeData(-36.0f, 42.0f, 2.0f, 3.0f),

        TreeData(-32.0f, -45.0f, 2.0f, 3.0f),
        TreeData(-30.0f, -42.0f, 2.0f, 4.0f),
        TreeData(-34.0f, -38.0f, 3.0f, 5.0f),
        TreeData(-33.0f, -35.0f, 3.0f, 4.0f),
        TreeData(-29.0f, -28.0f, 2.0f, 3.0f),
        TreeData(-26.0f, -25.0f, 3.0f, 5.0f),
        TreeData(-35.0f, -21.0f, 3.0f, 4.0f),
        TreeData(-31.0f, -17.0f, 3.0f, 3.0f),
        TreeData(-28.0f, -12.0f, 2.0f, 4.0f),
        TreeData(-29.0f, -7.0f, 3.0f, 3.0f),
        TreeData(-26.0f, -1.0f, 2.0f, 4.0f),
        TreeData(-32.0f, 6.0f, 2.0f, 3.0f),
        TreeData(-30.0f, 10.0f, 3.0f, 5.0f),
        TreeData(-33.0f, 14.0f, 2.0f, 4.0f),
        TreeData(-35.0f, 19.0f, 3.0f, 4.0f),
        TreeData(-28.0f, 22.0f, 2.0f, 3.0f),
        TreeData(-33.0f, 26.0f, 3.0f, 3.0f),
        TreeData(-29.0f, 31.0f, 3.0f, 4.0f),
        TreeData(-32.0f, 38.0f, 2.0f, 3.0f),
        TreeData(-27.0f, 41.0f, 3.0f, 4.0f),
        TreeData(-31.0f, 45.0f, 2.0f, 4.0f),
        TreeData(-28.0f, 48.0f, 3.0f, 5.0f),

        TreeData(-25.0f, -48.0f, 2.0f, 3.0f),
        TreeData(-20.0f, -42.0f, 3.0f, 4.0f),
        TreeData(-22.0f, -39.0f, 2.0f, 3.0f),
        TreeData(-19.0f, -34.0f, 2.0f, 3.0f),
        TreeData(-23.0f, -30.0f, 3.0f, 4.0f),
        TreeData(-24.0f, -24.0f, 2.0f, 3.0f),
        TreeData(-16.0f, -21.0f, 2.0f, 3.0f),
        TreeData(-17.0f, -17.0f, 3.0f, 3.0f),
        TreeData(-25.0f, -13.0f, 2.0f, 4.0f),
        TreeData(-23.0f, -8.0f, 2.0f, 3.0f),
        TreeData(-17.0f, -2.0f, 3.0f, 3.0f),
        TreeData(-16.0f, 1.0f, 2.0f, 3.0f),
        TreeData(-19.0f, 4.0f, 3.0f, 3.0f),
        TreeData(-22.0f, 8.0f, 2.0f, 4.0f),
        TreeData(-21.0f, 14.0f, 2.0f, 3.0f),
        TreeData(-16.0f, 19.0f, 2.0f, 3.0f),
        TreeData(-23.0f, 24.0f, 3.0f, 3.0f),
        TreeData(-18.0f, 28.0f, 2.0f, 4.0f),
        TreeData(-24.0f, 31.0f, 2.0f, 3.0f),
        TreeData(-20.0f, 36.0f, 2.0f, 3.0f),
        TreeData(-22.0f, 41.0f, 3.0f, 3.0f),
        TreeData(-21.0f, 45.0f, 2.0f, 3.0f),

        TreeData(-12.0f, -40.0f, 2.0f, 4.0f),
        TreeData(-11.0f, -35.0f, 3.0f, 3.0f),
        TreeData(-10.0f, -29.0f, 1.0f, 3.0f),
        TreeData(-9.0f, -26.0f, 2.0f, 2.0f),
        TreeData(-6.0f, -22.0f, 2.0f, 3.0f),
        TreeData(-15.0f, -15.0f, 1.0f, 3.0f),
        TreeData(-8.0f, -11.0f, 2.0f, 3.0f),
        TreeData(-14.0f, -6.0f, 2.0f, 4.0f),
        TreeData(-12.0f, 0.0f, 2.0f, 3.0f),
        TreeData(-7.0f, 4.0f, 2.0f, 2.0f),
        TreeData(-13.0f, 8.0f, 2.0f, 2.0f),
        TreeData(-9.0f, 13.0f, 1.0f, 3.0f),
        TreeData(-13.0f, 17.0f, 3.0f, 4.0f),
        TreeData(-6.0f, 23.0f, 2.0f, 3.0f),
        TreeData(-12.0f, 27.0f, 1.0f, 2.0f),
        TreeData(-8.0f, 32.0f, 2.0f, 3.0f),
        TreeData(-10.0f, 37.0f, 3.0f, 3.0f),
        TreeData(-11.0f, 42.0f, 2.0f, 2.0f),

        TreeData(15.0f, 5.0f, 2.0f, 3.0f),
        TreeData(15.0f, 10.0f, 2.0f, 3.0f),
        TreeData(15.0f, 15.0f, 2.0f, 3.0f),
        TreeData(15.0f, 20.0f, 2.0f, 3.0f),
        TreeData(15.0f, 25.0f, 2.0f, 3.0f),
        TreeData(15.0f, 30.0f, 2.0f, 3.0f),
        TreeData(15.0f, 35.0f, 2.0f, 3.0f),
        TreeData(15.0f, 40.0f, 2.0f, 3.0f),
        TreeData(15.0f, 45.0f, 2.0f, 3.0f),

        TreeData(25.0f, 5.0f, 2.0f, 3.0f),
        TreeData(25.0f, 10.0f, 2.0f, 3.0f),
        TreeData(25.0f, 15.0f, 2.0f, 3.0f),
        TreeData(25.0f, 20.0f, 2.0f, 3.0f),
        TreeData(25.0f, 25.0f, 2.0f, 3.0f),
        TreeData(25.0f, 30.0f, 2.0f, 3.0f),
        TreeData(25.0f, 35.0f, 2.0f, 3.0f),
        TreeData(25.0f, 40.0f, 2.0f, 3.0f),
        TreeData(25.0f, 45.0f, 2.0f, 3.0f))

    fun initializeProgram() {
        uniformColor = loadProgram("PosOnlyWorldTransform.vert", "ColorUniform.frag")
        objectColor = loadProgram("PosColorWorldTransform.vert", "ColorPassthrough.frag")
        uniformColorTint = loadProgram("PosColorWorldTransform.vert", "ColorMultUniform.frag")
    }

    override fun initialization() {
        initializeProgram()

        g_pConeMesh = Mesh("UnitConeTint.xml")
        g_pCylinderMesh = Mesh("UnitCylinderTint.xml")
        g_pCubeTintMesh = Mesh("UnitCubeTint.xml")
        g_pCubeColorMesh = Mesh("UnitCubeColor.xml")
        g_pPlaneMesh = Mesh("UnitPlane.xml")

        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glFrontFace(GL_CW)

        glEnable(GL_DEPTH_TEST)
        glDepthMask(true)
        glDepthFunc(GL_LEQUAL)
        glDepthRange(0.0, 1.0)
        glEnable(GL_DEPTH_CLAMP)
    }

    override fun display() {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        glClearDepth(1.0)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // clear the framebuffer

        if(g_pConeMesh != null && g_pCylinderMesh != null && g_pCubeTintMesh != null && g_pCubeColorMesh != null && g_pPlaneMesh != null) {
            val camPos = resolveCamPosition()

            val camMatrix = MatrixStack()
            camMatrix.setMatrix(calcLookAtMatrix(camPos, g_camTarget, Vec3(0.0f, 1.0f, 0.0f)))

            glUseProgram(uniformColor!!.theProgram)
            glUniformMatrix4fv(uniformColor!!.worldToCameraMatrixUnif, false, camMatrix.top().toFloatArray())
            glUseProgram(objectColor!!.theProgram)
            glUniformMatrix4fv(objectColor!!.worldToCameraMatrixUnif, false, camMatrix.top().toFloatArray())
            glUseProgram(uniformColorTint!!.theProgram)
            glUniformMatrix4fv(uniformColorTint!!.worldToCameraMatrixUnif, false, camMatrix.top().toFloatArray())
            glUseProgram(0)

            val modelMatrix = MatrixStack()

            // Render the ground plane.
            modelMatrix.push()

            modelMatrix.scale(Vec3(100.0f, 1.0f, 100.0f))

            glUseProgram(uniformColor!!.theProgram)
            glUniformMatrix4fv(uniformColor!!.modelToWorldMatrixUnif, false, modelMatrix.top().toFloatArray())
            glUniform4f(uniformColor!!.baseColorUnif, 0.302f, 0.416f, 0.0589f, 1.0f)
            g_pPlaneMesh!!.render()
            glUseProgram(0)
            modelMatrix.pop()

            //Draw the trees
            drawForest(modelMatrix)

            //Draw the building.
            modelMatrix.push()
            modelMatrix.translate(Vec3(20.0f, 0.0f, -10.0f))

            drawParthenon(modelMatrix)
            modelMatrix.pop()

            if(g_bDrawLookatPoint) {
                glDisable(GL_DEPTH_TEST)
                val idenity = Mat4()

                modelMatrix.push()

                val cameraAimVec = g_camTarget - camPos
                modelMatrix.translate(Vec3(0.0f, 0.0f, -length(cameraAimVec)))
                modelMatrix.scale(Vec3(1.0f, 1.0f, 1.0f))

                glUseProgram(objectColor!!.theProgram)
                glUniformMatrix4fv(objectColor!!.modelToWorldMatrixUnif, false, modelMatrix.top().toFloatArray())
                glUniformMatrix4fv(objectColor!!.worldToCameraMatrixUnif, false, idenity.toFloatArray())
                g_pCubeColorMesh!!.render()
                glUseProgram(0)
                glEnable(GL_DEPTH_TEST)
                modelMatrix.pop()
            }
        }
    }

    override fun reshape(w: Int, h: Int) {

        val persMatrix = MatrixStack()
        persMatrix.perspective(45.0f, w / h.toFloat(), g_fzNear, g_fzFar)

        glUseProgram(uniformColor!!.theProgram)
        glUniformMatrix4fv(uniformColor!!.cameraToClipMatrixUnif, false, persMatrix.top().toFloatArray())
        glUseProgram(objectColor!!.theProgram)
        glUniformMatrix4fv(objectColor!!.cameraToClipMatrixUnif, false, persMatrix.top().toFloatArray())
        glUseProgram(uniformColorTint!!.theProgram)
        glUniformMatrix4fv(uniformColorTint!!.cameraToClipMatrixUnif, false, persMatrix.top().toFloatArray())
        glUseProgram(0)

        glViewport(0, 0, w, h)
    }

    override fun keyboard(key: Int, x: Int, y: Int) {
        when (key) {
/*            GLFW.GLFW_KEY_w -> g_camTarget.z -= 4.0f
            GLFW.GLFW_KEY_s -> g_camTarget.z += 4.0f
            GLFW.GLFW_KEY_d -> g_camTarget.x += 4.0f
            GLFW.GLFW_KEY_a -> g_camTarget.x -= 4.0f
            GLFW.GLFW_KEY_e -> g_camTarget.y -= 4.0f
            GLFW.GLFW_KEY_q -> g_camTarget.y += 4.0f

 */
            GLFW.GLFW_KEY_W -> g_camTarget.z -= 0.4f
            GLFW.GLFW_KEY_S -> g_camTarget.z += 0.4f
            GLFW.GLFW_KEY_D -> g_camTarget.x += 0.4f
            GLFW.GLFW_KEY_A -> g_camTarget.x -= 0.4f
            GLFW.GLFW_KEY_E -> g_camTarget.y -= 0.4f
            GLFW.GLFW_KEY_Q -> g_camTarget.y += 0.4f
/*            GLFW.GLFW_KEY_i -> g_sphereCamRelPos.y -= 11.25f
            GLFW.GLFW_KEY_k -> g_sphereCamRelPos.y += 11.25f
            GLFW.GLFW_KEY_j -> g_sphereCamRelPos.x -= 11.25f
            GLFW.GLFW_KEY_l -> g_sphereCamRelPos.x += 11.25f
            GLFW.GLFW_KEY_o -> g_sphereCamRelPos.z -= 5.0f
            GLFW.GLFW_KEY_u -> g_sphereCamRelPos.z += 5.0f */
            GLFW.GLFW_KEY_I -> g_sphereCamRelPos.y -= 1.125f
            GLFW.GLFW_KEY_K -> g_sphereCamRelPos.y += 1.125f
            GLFW.GLFW_KEY_J -> g_sphereCamRelPos.x -= 1.125f
            GLFW.GLFW_KEY_L -> g_sphereCamRelPos.x += 1.125f
            GLFW.GLFW_KEY_O -> g_sphereCamRelPos.z -= 0.5f
            GLFW.GLFW_KEY_U -> g_sphereCamRelPos.z += 0.5f
            GLFW.GLFW_KEY_SPACE -> {
                g_bDrawLookatPoint = !g_bDrawLookatPoint
                println("Target: ${g_camTarget.x}, ${g_camTarget.y}, ${g_camTarget.z}")
                println("Position: ${g_sphereCamRelPos.x}, ${g_sphereCamRelPos.y}, ${g_sphereCamRelPos.z}")
            }
        }
        g_sphereCamRelPos.y = clamp(g_sphereCamRelPos.y, -78.75f, -1.0f)
        g_camTarget.y = max(g_camTarget.y, 0.0f)
        g_sphereCamRelPos.z = max(g_sphereCamRelPos.z, 5.0f)
    }

    fun calcLookAtMatrix(cameraPt: Vec3, lookPt: Vec3, upPt: Vec3): Mat4 {
        val lookDir = normalize(lookPt - cameraPt)
        val upDir = normalize(upPt)

        val rightDir = normalize(cross(lookDir, upDir))
        val perpUpDir = cross(rightDir, lookDir)

        val rotMat = Mat4()
        rotMat[0] = Vec4(rightDir, 0.0f)
        rotMat[1] = Vec4(perpUpDir, 0.0f)
        rotMat[2] = Vec4(-lookDir, 0.0f)

        val transMat = Mat4()
        transMat[3] = Vec4(-cameraPt, 1.0f)

        return rotMat.transpose() * transMat
    }

    //Trees are 3x3 in X/Z, and fTrunkHeight+fConeHeight in the Y.
    fun drawTree(modelMatrix: MatrixStack, fTrunkHeight: Float = 2.0f, fConeHeight: Float = 3.0f) {
        //Draw trunk.
        modelMatrix.push()

        modelMatrix.scale(Vec3(1.0f, fTrunkHeight, 1.0f))
        modelMatrix.translate(Vec3(0.0f, 0.5f, 0.0f))

        glUseProgram(uniformColorTint!!.theProgram)
        glUniformMatrix4fv(uniformColorTint!!.modelToWorldMatrixUnif, false, modelMatrix.top().toFloatArray())
        glUniform4f(uniformColorTint!!.baseColorUnif, 0.694f, 0.4f, 0.106f, 1.0f)
        g_pCylinderMesh!!.render()
        glUseProgram(0)
        modelMatrix.pop()

        //Draw the treetop
        modelMatrix.push()

        modelMatrix.translate(Vec3(0.0f, fTrunkHeight, 0.0f))
        modelMatrix.scale(Vec3(3.0f, fConeHeight, 3.0f))

        glUseProgram(uniformColorTint!!.theProgram)
        glUniformMatrix4fv(uniformColorTint!!.modelToWorldMatrixUnif, false, modelMatrix.top().toFloatArray())
        glUniform4f(uniformColorTint!!.baseColorUnif, 0.0f, 1.0f, 0.0f, 1.0f)
        g_pConeMesh!!.render()
        glUseProgram(0)
        modelMatrix.pop()
    }

    //Columns are 1x1 in the X/Z, and fHieght units in the Y.
    fun drawColumn(modelMatrix: MatrixStack, fHeight: Float = 5.0f) {
        //Draw the bottom of the column.
        modelMatrix.push()

        modelMatrix.scale(Vec3(1.0f, g_fColumnBaseHeight, 1.0f))
        modelMatrix.translate(Vec3(0.0f, 0.5f, 0.0f))

        glUseProgram(uniformColorTint!!.theProgram)
        glUniformMatrix4fv(uniformColorTint!!.modelToWorldMatrixUnif, false, modelMatrix.top().toFloatArray())
        glUniform4f(uniformColorTint!!.baseColorUnif, 1.0f, 1.0f, 1.0f, 1.0f)
        g_pCubeTintMesh!!.render()
        glUseProgram(0)
        modelMatrix.pop()

        //Draw the top of the column.
        modelMatrix.push()

        modelMatrix.translate(Vec3(0.0f, fHeight - g_fColumnBaseHeight, 0.0f))
        modelMatrix.scale(Vec3(1.0f, g_fColumnBaseHeight, 1.0f))
        modelMatrix.translate(Vec3(0.0f, 0.5f, 0.0f))

        glUseProgram(uniformColorTint!!.theProgram)
        glUniformMatrix4fv(uniformColorTint!!.modelToWorldMatrixUnif, false, modelMatrix.top().toFloatArray())
        glUniform4f(uniformColorTint!!.baseColorUnif, 0.9f, 0.9f, 0.9f, 0.9f)
        g_pCubeTintMesh!!.render()
        glUseProgram(0)
        modelMatrix.pop()

        //Draw the main column.
        modelMatrix.push()

        modelMatrix.translate(Vec3(0.0f, g_fColumnBaseHeight, 0.0f))
        modelMatrix.scale(Vec3(0.8f, fHeight - (g_fColumnBaseHeight * 2.0f), 0.8f))
        modelMatrix.translate(Vec3(0.0f, 0.5f, 0.0f))

        glUseProgram(uniformColorTint!!.theProgram)
        glUniformMatrix4fv(uniformColorTint!!.modelToWorldMatrixUnif, false, modelMatrix.top().toFloatArray())
        glUniform4f(uniformColorTint!!.baseColorUnif, 0.9f, 0.9f, 0.9f, 0.9f)
        g_pCylinderMesh!!.render()
        glUseProgram(0)
        modelMatrix.pop()
    }

    fun drawParthenon(modelMatrix: MatrixStack) {
        //Draw base.
        modelMatrix.push()

        modelMatrix.scale(Vec3(g_fParthenonWidth, g_fParthenonBaseHeight, g_fParthenonLength))
        modelMatrix.translate(Vec3(0.0f, 0.5f, 0.0f))

        glUseProgram(uniformColorTint!!.theProgram)
        glUniformMatrix4fv(uniformColorTint!!.modelToWorldMatrixUnif, false, modelMatrix.top().toFloatArray())
        glUniform4f(uniformColorTint!!.baseColorUnif, 0.9f, 0.9f, 0.9f, 0.9f)
        g_pCubeTintMesh!!.render()
        glUseProgram(0)
        modelMatrix.pop()

        //Draw top.
        modelMatrix.push()

        modelMatrix.translate(Vec3(0.0f, g_fParthenonColumnHeight + g_fParthenonBaseHeight, 0.0f))
        modelMatrix.scale(Vec3(g_fParthenonWidth, g_fParthenonTopHeight, g_fParthenonLength))
        modelMatrix.translate(Vec3(0.0f, 0.5f, 0.0f))

        glUseProgram(uniformColorTint!!.theProgram)
        glUniformMatrix4fv(uniformColorTint!!.modelToWorldMatrixUnif, false, modelMatrix.top().toFloatArray())
        glUniform4f(uniformColorTint!!.baseColorUnif, 0.9f, 0.9f, 0.9f, 0.9f)
        g_pCubeTintMesh!!.render()
        glUseProgram(0)
        modelMatrix.pop()

        //Draw columns.
        val fFrontZVal = (g_fParthenonLength / 2.0f) - 1.0f
        val fRightXVal = (g_fParthenonWidth / 2.0f) - 1.0f

        val countColumnsWidth = (g_fParthenonWidth / 2.0f).toInt()
        for (iColumnNum in 0 until countColumnsWidth) {
            modelMatrix.push()
            modelMatrix.translate(Vec3((2.0f * iColumnNum) - (g_fParthenonWidth / 2.0f) + 1.0f,
                g_fParthenonBaseHeight, fFrontZVal))

            drawColumn(modelMatrix, g_fParthenonColumnHeight)
            modelMatrix.pop()

            modelMatrix.push()
            modelMatrix.translate(Vec3((2.0f * iColumnNum) - (g_fParthenonWidth / 2.0f) + 1.0f,
                g_fParthenonBaseHeight, -fFrontZVal))

            drawColumn(modelMatrix, g_fParthenonColumnHeight)
            modelMatrix.pop()
        }

        //Don't draw the first or last columns, since they've been drawn already.
        val countColumnsLength = ((g_fParthenonLength - 2.0f) / 2.0f).toInt()
        for (iColumnNum in 1 until countColumnsLength) {
            modelMatrix.push()
            modelMatrix.translate(Vec3(fRightXVal,
                g_fParthenonBaseHeight, (2.0f * iColumnNum) - (g_fParthenonLength / 2.0f) + 1.0f))

            drawColumn(modelMatrix, g_fParthenonColumnHeight)
            modelMatrix.pop()

            modelMatrix.push()
            modelMatrix.translate(Vec3(-fRightXVal,
                g_fParthenonBaseHeight, (2.0f * iColumnNum) - (g_fParthenonLength / 2.0f) + 1.0f))

            drawColumn(modelMatrix, g_fParthenonColumnHeight)
            modelMatrix.pop()
        }

        //Draw interior.
        modelMatrix.push()

        modelMatrix.translate(Vec3(0.0f, 1.0f, 0.0f))
        modelMatrix.scale(Vec3(g_fParthenonWidth - 6.0f, g_fParthenonColumnHeight,
            g_fParthenonLength - 6.0f))
        modelMatrix.translate(Vec3(0.0f, 0.5f, 0.0f))

        glUseProgram(objectColor!!.theProgram)
        glUniformMatrix4fv(objectColor!!.modelToWorldMatrixUnif, false, modelMatrix.top().toFloatArray())
        g_pCubeColorMesh!!.render()
        glUseProgram(0)
        modelMatrix.pop()

        //Draw headpiece.
        modelMatrix.push()

        modelMatrix.translate(Vec3(
            0.0f,
            g_fParthenonColumnHeight + g_fParthenonBaseHeight + (g_fParthenonTopHeight / 2.0f),
            g_fParthenonLength / 2.0f))
        modelMatrix.rotateX(-135.0f)
        modelMatrix.rotateY(45.0f)

        glUseProgram(objectColor!!.theProgram)
        glUniformMatrix4fv(objectColor!!.modelToWorldMatrixUnif, false, modelMatrix.top().toFloatArray())
        g_pCubeColorMesh!!.render()
        glUseProgram(0)
        modelMatrix.pop()
    }


    fun drawForest(modelMatrix: MatrixStack) {
        for (currTree in g_forest) {
            modelMatrix.push()
            modelMatrix.translate(Vec3(currTree.fXPos, 0.0f, currTree.fZPos))
            drawTree(modelMatrix, currTree.fTrunkHeight, currTree.fConeHeight)
            modelMatrix.pop()
        }
    }

    fun resolveCamPosition(): Vec3 {
        val tempMat = MatrixStack()

        val phi = degToRad(g_sphereCamRelPos.x)
        val theta = degToRad(g_sphereCamRelPos.y + 90.0f)

        val fSinTheta = sin(theta)
        val fCosTheta = cos(theta)
        val fCosPhi = cos(phi)
        val fSinPhi = sin(phi)

        val dirToCamera = Vec3(fSinTheta * fCosPhi, fCosTheta, fSinTheta * fSinPhi)
        return (dirToCamera * g_sphereCamRelPos.z) + g_camTarget
    }
}