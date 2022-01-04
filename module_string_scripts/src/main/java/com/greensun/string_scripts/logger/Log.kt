package com.greensun.string_scripts.logger

import java.text.SimpleDateFormat
import java.util.*

object Log {
    private val colorful = ColorfulString()
    private val date = Date()

    private fun Date.getNowTimeDetail(): String {
        val sdf = SimpleDateFormat("HH:mm:ss SSS")
        return sdf.format(this).plus("(${Thread.currentThread().id})")
    }

    fun d(tag:String="", content:String){
        println("${date.getNowTimeDetail()} D/$tag: $content")
    }

    fun i(tag:String="", content: String){
        println(colorful.renderUltramarine("${date.getNowTimeDetail()} I/$tag: $content"))
    }

    fun w(tag:String="", content:String){
        println(colorful.renderYellow("${date.getNowTimeDetail()} W/$tag: $content"))
    }

    fun e(tag:String="", content:String){
        println(colorful.renderRed("${date.getNowTimeDetail()} E/$tag: $content"))
    }
}