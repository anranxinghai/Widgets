package com.pinguo.camera360.util

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

object TextResourceReader {
   fun readTextFileFromResource(context:Context,resourceId:Int):String{
       val body = StringBuilder()
       try {
           val inputStream = context.resources.openRawResource(resourceId)
           val inputStreamReader = InputStreamReader(inputStream)
           val bufferReader = BufferedReader(inputStreamReader)
           bufferReader.readLine().takeWhile {
               body.append(it).append("\n")
               it != null
           }
       }catch (e:Exception){
           throw RuntimeException("Could not open resource:$resourceId",e)
       }
       return body.toString()
   }
}