package com.pinguo.camera360.gl.programs

import android.content.Context
import android.opengl.GLES20.*
import com.pinguo.camera360.R

class TextureShaderProgram :ShaderProgram{
    private var uMatrixLocation = 0
    private var uTextureUnitLocation = 0
    private var aPositionLocation = 0
    private var aTextureCoordinatesLocation:Int = 0
    constructor(context: Context):super(context, R.raw.texture_vertex_shader,R.raw.texture_fragment_shader){
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT)
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES)

    }
    fun setUniforms(matrix:FloatArray,textureId:Int){
        glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D,textureId)
        glUniform1i(uTextureUnitLocation,0)
    }

    fun getPositionAttributeLocation():Int{
        return aPositionLocation
    }

    fun getTextureCoordinatesAttributeLocation():Int{
        return aTextureCoordinatesLocation
    }
}