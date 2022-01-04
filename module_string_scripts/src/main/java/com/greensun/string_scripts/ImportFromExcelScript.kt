package com.greensun.string_scripts

import com.greensun.string_scripts.helper.ExcelHelper
import com.greensun.string_scripts.helper.WordHelper
import com.greensun.string_scripts.logger.Log
import java.io.File

/**
 * 从excel导入string
 */
fun main() {
    val f = File("./")
    val moduleDirList = f.listFiles { file ->
        // 过滤隐藏文件
        if (file.name.startsWith(".")) return@listFiles false
        // 过滤文件
        if (!file.isDirectory) return@listFiles false
        // 中文资源文件，做为key，必须有
        val f1 = File(file.absolutePath, "src/main/res/values/strings.xml")
        if (f1.exists()) return@listFiles true
        return@listFiles false
    }
    // 打印相关可以使用的模块
    Log.i("ImportFromExcelScript", moduleDirList?.map { it.name }.toString())
    val filePath = "./module_language_scripts/strings.xlsx"
    val sheetsData = ExcelHelper.getSheetsData(filePath)

    moduleDirList.forEach {
        // 模块名对应excel表数据  <name，<语言目录，值>>
        val newData = sheetsData[it.name]
        if (newData != null) {
            val parentFile = File(it, "src/main/res")
            // <语言目录（如values-zh-rCN），<name，bean>>
            var resMap = WordHelper.collectRes(parentFile)
            // 将资源转换格式 <name，<语言目录，值>>
            val resData = WordHelper.transformResData(resMap)
            WordHelper.mergeStringData(newData, resData)
            // 移除第一行
            resData.remove("")
            resMap = WordHelper.revertResData(resData)
            WordHelper.importWords(resMap, parentFile)
        }
    }
}