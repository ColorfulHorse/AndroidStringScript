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

    val map = hashMapOf(
        "values-zh-rCN" to "中文（中国）",
        "values-zh-rTW" to "中文（台湾）",
        "values-zh-rHK" to "中文（香港）",
        "values-en-rUS" to "英语（美国）",
        "values-en-rGB" to "英语（英国）",

        "values-en-rAU" to "英文（澳大利亚）",
        "values" to "英文",
        "values-en-rCA" to "英文（加拿大）",
        "values-en-rIE" to "英文（爱尔兰）",
        "values-en-rIN" to "英文（印度）",
        "values-en-rNZ" to "英文（新西兰）",
        "values-en-rSG" to "英文（新加坡）",
        "values-en-rZA" to "英文（南非）",

        "values-ar-rEG" to "阿拉伯文（埃及）",
        "values-ar-rIL" to "阿拉伯文（以色列）",
        "values-ar" to "阿拉伯文",
        "values-bg-rBG" to "保加利亚文",
        "values-ca-rES" to "加泰罗尼亚文",
        "values-cs-rCZ" to "捷克文",
        "values-cs" to "捷克文",
        "values-da-rDK" to "丹麦文",
        "values-de-rAT" to "德文（奥地利）",
        "values-de-rCH" to "德文（瑞士）",
        "values-de-rDE" to "德文（德国）",
        "values-de-rLI" to "德文（列支敦士登）",
        "values-el-rGR" to "希腊文",
        "values-es-rES" to "西班牙文（西班牙）",
        "values-es-rUS" to "西班牙文（美国）",
        "values-es" to "西班牙文",
        "values-fi-rFI" to "芬兰文（芬兰）",

        "values-fr-rBE" to "法文（比利时）",
        "values-fr" to "法文",
        "values-fr-rCA" to "法文（加拿大）",
        "values-fr-rCH" to "法文（瑞士）",
        "values-fr-rFR" to "法文（法国）",

        "values-hi-rIN" to "印地文",
        "values-hr-rHR" to "克罗里亚文",
        "values-hu-rHU" to "匈牙利文",
        "values-in-rID" to "印度尼西亚文",
        "values-it-rCH" to "意大利文（瑞士）",
        "values-it-rIT" to "意大利文（意大利）",

        "values-ja-rJP" to "日文",
        "values-ko-rKR" to "韩文",
        "values-lt-rLT" to "立陶宛文",
        "values-lv-rLV" to "拉脱维亚文",
        "values-nb-rNO" to "挪威博克马尔文",
        "values-nl-BE" to "荷兰文(比利时)",

        "values-nl-rNL" to "荷兰文（荷兰）",
        "values-pl-rPL" to "波兰文",
        "values-pt-rBR" to "葡萄牙文（巴西）",
        "values-pt-rPT" to "葡萄牙文（葡萄牙）",
        "values-ro-rRO" to "罗马尼亚文",

        "values-ru-rRU" to "俄文",
        "values-sk-rSK" to "斯洛伐克文",
        "values-sl-rSI" to "斯洛文尼亚文",
        "values-sr-rRS" to "塞尔维亚文",
        "values-sv-rSE" to "瑞典文",
        "values-th-rTH" to "泰文",
        "values-tl-rPH" to "塔加洛语",
        "values-tr-rTR" to "土耳其文",
        "values-uk-rUA" to "乌克兰文",
        "values-vi-rVN" to "越南文",

        "values-es-rLA" to "拉美西文",
        "values-iw-rIL" to "希伯来文"
    )

}