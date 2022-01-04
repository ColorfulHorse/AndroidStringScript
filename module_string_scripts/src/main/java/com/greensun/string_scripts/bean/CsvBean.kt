package com.greensun.string_scripts.bean

data class CsvBean(
    var head: ArrayList<String>, // 头,语言目录
    var data: LinkedHashMap<String, LinkedHashMap<String, String>> // <name，<语言目录，值>>
)