package com.greensun.string_scripts

import com.greensun.string_scripts.helper.ExcelHelper
import com.greensun.string_scripts.helper.WordHelper
import com.greensun.string_scripts.logger.Log
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File

/**
 * 导出项目string到excel
 */
fun main() {
    // 查找当前项目的模块
    val f = File("./")
    val moduleDirList = f.listFiles { file ->
        // 过滤隐藏文件
        if (file.name.startsWith(".")) return@listFiles false
        if (!file.isDirectory) return@listFiles false
        val f1 = File(file.absolutePath, "src/main/res/values/strings.xml")
        if (f1.exists()) return@listFiles true
        return@listFiles false
    }

    Log.i("ExportStrings2ExcelScript", moduleDirList?.map { it.name }.toString())
    val excelWBook = XSSFWorkbook()
    moduleDirList.forEachIndexed { index, it ->

        // 解析当前项目的多语言内容 <语言目录（如values-zh-rCN），<name，word>>
        val hashMap = WordHelper.collectRes(File(it, "src/main/res"))
        if (hashMap.isEmpty())
            return
        val nameLangMap = WordHelper.transformResData(hashMap)
        if (nameLangMap.isEmpty())
            return
        // 将资源转换格式 <name，<语言目录，值>>
        val resData = WordHelper.processSameWords(nameLangMap)

        ExcelHelper.resource2File(
            "./module_string_scripts/strings.xlsx",
            it.name,
            hashMap.keys.toList() as ArrayList<String>,
            resData, excelWBook, index, (moduleDirList.size - 1)
        )
    }

}