package com.pinguo.camera360.helper

import android.opengl.GLES20.*
import android.util.Log

object ShaderHelper {
    private val TAG = "ShaderHelper"
    fun compilVertexShader(shaderCode:String):Int{
        return compilShader(GL_VERTEX_SHADER,shaderCode)
    }
    fun compilFragmentShader(shaderCode:String):Int{
        return compilShader(GL_FRAGMENT_SHADER,shaderCode)
    }
    fun compilShader(type:Int,shaderCode:String):Int{
        val shaderObjectId = glCreateShader(type)//创建Shader对象
        if (shaderObjectId == 0){
            Log.w(TAG,"Could not create new shader")
            return 0
        }
        glShaderSource(shaderObjectId,shaderCode)//将代码上传到Shader对象
        glCompileShader(shaderObjectId)//编译Shader
        val compileStatus = IntArray(1)
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS,compileStatus,0)//获取编译结果状态
        Log.v(TAG,"Results of compiling source:\n$shaderCode\n${glGetShaderInfoLog(shaderObjectId)}")
        if (compileStatus[0] == 0){
            glDeleteShader(shaderObjectId)//出错了删除Shader
            Log.w(TAG,"Compilation of shader failed.")
            return 0
        }
        return shaderObjectId
    }

    //链接顶点Shader和片段Shader
    fun linkProgram(vertexShaderId:Int,fragmentShaderId:Int):Int{
        val programObjectId = glCreateProgram()//创建程序
        if (programObjectId == 0){
            Log.w(TAG,"Could not create new program")
            return 0
        }
        glAttachShader(programObjectId,vertexShaderId)//给程序设置Shader
        glAttachShader(programObjectId,fragmentShaderId)
        glLinkProgram(programObjectId)//链接程序
        val linkStatus = IntArray(1)
        glGetProgramiv(programObjectId, GL_LINK_STATUS,linkStatus,0)
        Log.v(TAG,"Results of link program:${glGetProgramInfoLog(programObjectId)}")
        if (linkStatus[0] == 0){
            glDeleteProgram(programObjectId)
            Log.w(TAG,"Link of shader failed.")
            return 0
        }
        return programObjectId
    }
    //验证程序
    fun validatePrograme(programObjectId:Int):Boolean{
        glValidateProgram(programObjectId)
        val validateStatus = IntArray(1)
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS,validateStatus,0)

        Log.v(TAG,"Results of validate program:${validateStatus[0]} \nLog:${glGetProgramInfoLog(programObjectId)}")
        if (validateStatus[0] == 0){
            glDeleteProgram(programObjectId)
            Log.w(TAG,"Validate of shader failed.")
            return false
        }
        return validateStatus[0] != 0
    }
}