package com.greensun.string_scripts

import com.greensun.string_scripts.helper.ExcelHelper
import com.greensun.string_scripts.helper.WordHelper
import com.greensun.string_scripts.logger.Log
import java.io.File

// 是否只导入指定语言
const val useInclude = false

// 指定语言
val importInclude = listOf<String>(
    "values-ja-rJP",
    "values-ko-rKR",
    "values-ar"
)

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

        val f1 = File(file.absolutePath, "src/main/res/values/strings.xml")
        if (f1.exists()) return@listFiles true
        return@listFiles false
    }
    // 打印相关可以使用的模块
    Log.i("ImportFromExcelScript", moduleDirList?.map { it.name }.toString())
    val filePath = "./module_string_scripts/strings.xlsx"
    val sheetsData = ExcelHelper.getSheetsData(filePath)
    moduleDirList.forEach {
        // 模块名对应excel表数据  <name，<语言目录，值>>
        val newData = sheetsData[it.name]
        if (newData != null) {
            val newLangNameMap = WordHelper.revertResData(newData)
            val parentFile = File(it, "src/main/res")
            // 项目中读出的string map，<语言目录（如values-zh-rCN），<name，word>>
            // todo 收集的同时进行合并
            var resLangNameMap = WordHelper.collectRes(parentFile)
            WordHelper.mergeLangNameString(newLangNameMap, resLangNameMap)
            if (useInclude) {
                resLangNameMap = resLangNameMap.filterKeys { lang ->
                    importInclude.contains(lang)
                } as LinkedHashMap<String, LinkedHashMap<String, String>>
            }
            WordHelper.importWords(resLangNameMap, parentFile)
        }
    }
}