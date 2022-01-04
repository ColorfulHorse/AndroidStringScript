package com.greensun.string_scripts.helper

/**
 * 目录与中文映射数据
- 中文（中国）：values-zh-rCN
- 中文（台湾）：values-zh-rTW
- 中文（香港）：values-zh-rHK
- 英语（美国）：values-en-rUS
- 英语（英国）：values-en-rGB

- 英文（澳大利亚）：values-en-rAU
- 英文（加拿大）  ：values-en-rCA
- 英文（爱尔兰）  ：values-en-rIE
- 英文（印度）    ：values-en-rIN
- 英文（新西兰）  ：values-en-rNZ
- 英文（新加坡）  ：values-en-rSG
- 英文（南非）    ：values-en-rZA

- 阿拉伯文（埃及）        ：values-ar-rEG
- 阿拉伯文（以色列）       ：values-ar-rIL
- 保加利亚文           : values-bg-rBG
- 加泰罗尼亚文           ：values-ca-rES
- 捷克文              ：values-cs-rCZ
- 丹麦文                ：values-da-rDK
- 德文（奥地利）         ：values-de-rAT
- 德文（瑞士）           ：values-de-rCH
- 德文（德国）           ：values-de-rDE
- 德文（列支敦士登）      ：values-de-rLI
- 希腊文                 ：values-el-rGR
- 西班牙文（西班牙）      ：values-es-rES
- 西班牙文（美国）        ：values-es-rUS
- 芬兰文（芬兰）          ：values-fi-rFI
- 法文（比利时）          ：values-fr-rBE
- 法文（加拿大）          ：values-fr-rCA
- 法文（瑞士）            ：values-fr-rCH
- 法文（法国）            ：values-fr-rFR
- 希伯来文               ：values-iw-rIL

- 印地文       ：values-hi-rIN
- 克罗里亚文     ：values-hr-rHR
- 匈牙利文      ：values-hu-rHU
- 印度尼西亚文    ：values-in-rID
- 意大利文（瑞士）  ：values-it-rCH
- 意大利文（意大利） ：values-it-rIT

- 日文：       values-ja-rJP
- 韩文：       values-ko-rKR
- 立陶宛文：     values-lt-rLT
- 拉脱维亚文：    values-lv-rLV
- 挪威博克马尔文：  values-nb-rNO
- 荷兰文(比利时)： values-nl-BE

- 荷兰文（荷兰）：  values-nl-rNL
- 波兰文：      values-pl-rPL
- 葡萄牙文（巴西）： values-pt-rBR
- 葡萄牙文（葡萄牙）：values-pt-rPT
- 罗马尼亚文：    values-ro-rRO

- 俄文：           values-ru-rRU
- 斯洛伐克文：        values-sk-rSK
- 斯洛文尼亚文：       values-sl-rSI
- 塞尔维亚文：        values-sr-rRS
- 瑞典文：          values-sv-rSE
- 泰文：           values-th-rTH

- 塔加洛语：values-tl-rPH
- 土耳其文：values--r-rTR
- 乌克兰文：values-uk-rUA
- 越南文： values-vi-rVN
- 拉美西文： values-es-rLA
 */
object LanguageKeyMap {

    val map = hashMapOf<String, String>(
            Pair("values-zh-rCN", "中文（中国）"),
            Pair("values-zh-rTW", "中文（台湾）"),
            Pair("values-zh-rHK", "中文（香港）"),
            Pair("values-en-rUS", "英语（美国）"),
            Pair("values-en-rGB", "英语（英国）"),

            Pair("values-en-rAU", "英文（澳大利亚）"),
            Pair("values", "英文"),
            Pair("values-en-rCA", "英文（加拿大）"),
            Pair("values-en-rIE", "英文（爱尔兰）"),
            Pair("values-en-rIN", "英文（印度）"),
            Pair("values-en-rNZ", "英文（新西兰）"),
            Pair("values-en-rSG", "英文（新加坡）"),
            Pair("values-en-rZA", "英文（南非）"),

            Pair("values-ar-rEG", "阿拉伯文（埃及）"),
            Pair("values-ar-rIL", "阿拉伯文（以色列）"),
            Pair("values-ar", "阿拉伯文"),
            Pair("values-bg-rBG", "保加利亚文"),
            Pair("values-ca-rES", "加泰罗尼亚文"),
            Pair("values-cs-rCZ", "捷克文"),
            Pair("values-cs", "捷克文"),
            Pair("values-da-rDK", "丹麦文"),
            Pair("values-de-rAT", "德文（奥地利）"),
            Pair("values-de-rCH", "德文（瑞士）"),
            Pair("values-de-rDE", "德文（德国）"),
            Pair("values-de-rLI", "德文（列支敦士登）"),
            Pair("values-el-rGR", "希腊文"),
            Pair("values-es-rES", "西班牙文（西班牙）"),
            Pair("values-es-rUS", "西班牙文（美国）"),
            Pair("values-es", "西班牙文"),
            Pair("values-fi-rFI", "芬兰文（芬兰）"),

            Pair("values-fr-rBE", "法文（比利时）"),
            Pair("values-fr", "法文"),
            Pair("values-fr-rCA", "法文（加拿大）"),
            Pair("values-fr-rCH", "法文（瑞士）"),
            Pair("values-fr-rFR", "法文（法国）"),

            Pair("values-hi-rIN", "印地文"),
            Pair("values-hr-rHR", "克罗里亚文"),
            Pair("values-hu-rHU", "匈牙利文"),
            Pair("values-in-rID", "印度尼西亚文"),
            Pair("values-it-rCH", "意大利文（瑞士）"),
            Pair("values-it-rIT", "意大利文（意大利）"),

            Pair("values-ja-rJP", "日文"),
            Pair("values-ko-rKR", "韩文"),
            Pair("values-lt-rLT", "立陶宛文"),
            Pair("values-lv-rLV", "拉脱维亚文"),
            Pair("values-nb-rNO", "挪威博克马尔文"),
            Pair("values-nl-BE", "荷兰文(比利时)"),

            Pair("values-nl-rNL", "荷兰文（荷兰）"),
            Pair("values-pl-rPL", "波兰文"),
            Pair("values-pt-rBR", "葡萄牙文（巴西）"),
            Pair("values-pt-rPT", "葡萄牙文（葡萄牙）"),
            Pair("values-ro-rRO", "罗马尼亚文"),

            Pair("values-ru-rRU", "俄文"),
            Pair("values-sk-rSK", "斯洛伐克文"),
            Pair("values-sl-rSI", "斯洛文尼亚文"),
            Pair("values-sr-rRS", "塞尔维亚文"),
            Pair("values-sv-rSE", "瑞典文"),
            Pair("values-th-rTH", "泰文"),
            Pair("values-tl-rPH", "塔加洛语"),
            Pair("values-tr-rTR", "土耳其文"),
            Pair("values-uk-rUA", "乌克兰文"),
            Pair("values-vi-rVN", "越南文"),

            Pair("values-es-rLA", "拉美西文"),
            Pair("values-iw-rIL", "希伯来文")
    )

}