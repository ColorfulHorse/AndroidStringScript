package com.greensun.string_scripts

/**
 * Author : greensunliao
 * Date   : 2022/1/20
 * Email  : liao962381394@sina.cn
 * Blog   : https://juejin.cn/user/3263006244363095
 * Desc   : 配置类
 */
object Config {

    // 导入导出时的excel路径
    const val OUTPUT_PATH = "./module_string_scripts/output.xlsx"
    const val INPUT_PATH = "./module_string_scripts/input.xlsx"
    // 需要导入导出的项目目录
    const val EXPORT_PROJECT_PATH = "./"
    const val IMPORT_PROJECT_PATH = "./"

    // 是否只导入/导出指定语言
    const val useImportInclude = false
    const val useExportInclude = false

    // 指定语言
    val importInclude = listOf<String>(
        "values-ja-rJP",
        "values-ko-rKR",
        "values-ar"
    )

    // 导出指定语言
    val exportInclude = listOf<String>(
        "values",
        "values-zh-rCN",
        "values-ro-rRO"
    )

    // 导出时排除某些name
    val exportExcludeNames = listOf<String>(
    )
    // 是否将基准语言的值相同（即使name不同）的string也视为同一个string
    // 比如以中文为基准，那么<string name="name1">相同值</string> 等同于 <string name="name2">相同值</string>
    // 这样的话可以以某一语言为基准[导出时去重/导入时恢复]恢复内容重复的string
    // 导入导出时应该用同一个基准
    const val isBaseOnWord = true
    // 去重/恢复 基准语言，找不到用默认
    const val BASE_LANG = "values-zh-rCN"
    const val DEFAULT_LANG = "values"
    // 导出时基于哪个语言排序
    const val SORT_LANG = BASE_LANG
}