package com.greensun.string_scripts

import com.greensun.string_scripts.helper.ExcelHelper
import com.greensun.string_scripts.helper.WordHelper
import com.greensun.string_scripts.logger.Log
import java.io.File

/**
 * Author : greensunliao
 * Date   : 2022/1/20
 * Email  : liao962381394@sina.cn
 * Blog   : https://juejin.cn/user/3263006244363095
 * Desc   : 从excel导入string到项目中
 */
fun main() {
    val root = File(Config.IMPORT_PROJECT_PATH)
    val moduleDirList = root.listFiles { file ->
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
    val sheetsData = ExcelHelper.getSheetsData(Config.INPUT_PATH)
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
            if (Config.useImportInclude) {
                resLangNameMap = resLangNameMap.filterKeys { lang ->
                    Config.importInclude.contains(lang)
                } as LinkedHashMap<String, LinkedHashMap<String, String>>
            }
            WordHelper.importWords(resLangNameMap, parentFile)
        }
    }
}