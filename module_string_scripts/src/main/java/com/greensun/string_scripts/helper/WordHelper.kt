package com.greensun.string_scripts.helper

import com.greensun.string_scripts.logger.Log
import java.io.*
import java.util.regex.Pattern

object WordHelper {

    private val TAG = "WordHelper"

    // 第一列存放string的name，作为第二表头
    private const val colHead = "name"
    private const val defaultKey = "values"
    private const val cnKey = "values-zh-rCN"

    /**
     *  转换string map数据结构，以name为行标识方便写入excel
     *  [source] <语言目录（如values-zh-rCN），<name，word>>
     *  @return <name，<语言目录，word>>
     */
    fun transformResData(source: LinkedHashMap<String, LinkedHashMap<String, String>>): LinkedHashMap<String, LinkedHashMap<String, String>> {
        // <name，<语言目录，word>>
        val resData = LinkedHashMap<String, LinkedHashMap<String, String>>()
        source.forEach { (langDir, value) ->
            value.forEach { (name, word) ->
                val wordRes = resData.computeIfAbsent(name) { LinkedHashMap() }
                wordRes[langDir] = word
            }
        }
        // 由于可能存在不同key但是相同内容的string，导出时将内容相同的string聚合到一起
        val key = if (resData.containsKey(cnKey)) cnKey else defaultKey
        val groups = resData.entries.groupBy {
            it.value[key]
        }
        return groups.entries.fold<Map.Entry<String?, List<MutableMap.MutableEntry<String, LinkedHashMap<String, String>>>>, MutableMap<String, LinkedHashMap<String, String>>>(
            mutableMapOf()
        ) { sum, group ->

            group.value.forEach { map ->
                sum[map.key] = map.value
            }
            sum
        } as LinkedHashMap<String, LinkedHashMap<String, String>>
    }

    /**
     *  还原string map数据结构，以language为行标识方便写入多个string.xml
     *  [source] <name，<语言目录，word>>
     *  @return <语言目录（如values-zh-rCN），<name，word>>
     */
    fun revertResData(source: LinkedHashMap<String, LinkedHashMap<String, String>>): LinkedHashMap<String, LinkedHashMap<String, String>> {
        // <语言目录（如values-zh-rCN），<name，word>>
        val resData = LinkedHashMap<String, LinkedHashMap<String, String>>()
        source.forEach { (name, value) ->
            value.forEach { (langDir, word) ->
                val langRes = resData.computeIfAbsent(langDir) { LinkedHashMap() }
                langRes[name] = word
            }
        }
        return resData
    }

    /**
     * 解析当前的多语言内容 <语言目录（如values-zh-rCN），<name，word>>
     */
    fun collectRes(res: File): LinkedHashMap<String, LinkedHashMap<String, String>> {
        val hashMap = LinkedHashMap<String, LinkedHashMap<String, String>>()
        val regexStr =
            "^\\s*<\\s*string\\s+name\\s*=\\s*\"(.*?)\"\\s*formatted=\"false\"\\s*>(.*?)</\\s*string\\s*>\\s*\$"

        val regexStr2 =
            "^\\s*<\\s*string\\s+name\\s*=\\s*\"(.*?)\"\\s*>(.*?)</\\s*string\\s*>\\s*\$"
        // 添加string name作为第一列，用作索引
        hashMap[colHead] = LinkedHashMap()
        res.listFiles().forEach {
            val data = LinkedHashMap<String, String>()
            // 收集所有string name
            val names = hashMap.computeIfAbsent(colHead) { LinkedHashMap() }
            val stringFile = File(it, "strings.xml")
            if (stringFile.exists()) {
                // 将xml内容读取
                stringFile.readLines(Charsets.UTF_8).forEach { line ->
                    val p = Pattern.compile(regexStr)
                    val groups = p.matcher(line)
                    if (groups.find() && groups.groupCount() == 2) {
                        val name = groups.group(1)
                        val value = groups.group(2)
                        names[name] = name
                        data[name] = value
                    } else {
                        // 使用第二个规则获取
                        val p2 = Pattern.compile(regexStr2)
                        val groups2 = p2.matcher(line)
                        if (groups2.find() && groups2.groupCount() == 2) {
                            val name = groups2.group(1)
                            val value = groups2.group(2)
                            names[name] = name
                            data[name] = value
                        }
                    }
                }
                hashMap[it.name] = data
            }
            Log.d(TAG, "${res.parent} : 语言${it.name} 有: ${data.size} 个")
        }
        return hashMap
    }


    /**
     * 将excel中string合并到项目原本string中，不存在则追加，存在则覆盖
     * [newData] excel中读出的string <name，<语言目录，word>>
     * [resData] 项目中读出的string <name，<语言目录，word>>
     */
    fun mergeNameLangString(
        newData: LinkedHashMap<String, LinkedHashMap<String, String>>,
        resData: LinkedHashMap<String, LinkedHashMap<String, String>>
    ) {
        newData.forEach { (name, v) ->
            // 排除第一行，第一行name为空
            if (name.isEmpty())
                return@forEach
            // 当前项目中一条包含多语种的string map
            val langWordMap = resData[name]
            if (langWordMap != null) {
                // 项目中存在该name的字符，遍历每种语言的值，依次覆盖为excel中的新值
                v.forEach { (lang, newWord) ->
                    if (lang.isNotEmpty() && newWord.isNotBlank()) {
                        val oldWord = langWordMap[lang]
                        if (oldWord != null && oldWord.isNotEmpty()) {
                            if (oldWord != newWord) {
                                Log.e(
                                    TAG,
                                    "替换string：[name: $name, lang: $lang, 旧值：$oldWord}, 新值：$newWord]"
                                )
                            }
                        } else {
                            Log.e(TAG, "新增string：[name: $name, lang: $lang, 新值: $newWord]")
                        }
                        langWordMap[lang] = newWord
                    }
                }
            } else {
                // 项目中不存在该name的字符，将excel表中的插入
                v.forEach { (lang, word) ->
                    if (lang.isNotEmpty() && word.isNotBlank()) {
                        Log.e(TAG, "新增string：[name: $name, lang: $lang, 新值: $word]")
                    }
                }
                resData[name] = v
            }
        }
    }

    /**
     * 将excel中string合并到项目原本string中，不存在则追加，存在则覆盖
     * [newData] excel中读出的string <语言目录，<name，word>>
     * [resData] 项目中读出的string <语言目录，<name，word>>
     */
    fun mergeLangNameString(
        newData: LinkedHashMap<String, LinkedHashMap<String, String>>,
        resData: LinkedHashMap<String, LinkedHashMap<String, String>>
    ) {
        newData.forEach { (lang, v) ->
            // 排除第一行，第一行name为空
            if (lang.isEmpty())
                return@forEach
            // 当前项目中一条包含多语种的string map
            val nameWordMap = resData[lang]
            if (nameWordMap != null) {
                // 项目中存在该name的字符，遍历每种语言的值，依次覆盖为excel中的新值
                v.forEach { (name, newWord) ->
                    if (name.isNotEmpty() && newWord.isNotBlank()) {
                        val oldWord = nameWordMap[name]
                        if (oldWord != null && oldWord.isNotEmpty()) {
                            if (oldWord != newWord) {
                                Log.e(
                                    TAG,
                                    "替换string：[name: $name, lang: $name, 旧值：$oldWord}, 新值：$newWord]"
                                )
                            }
                        } else {
                            Log.e(TAG, "新增string：[name: $name, lang: $name, 新值: $newWord]")
                        }
                        nameWordMap[name] = newWord
                    }
                }
            } else {
                // 项目中不存在该语言的字符，将excel表中的插入
                v.forEach { (name, word) ->
                    if (name.isNotBlank() && word.isNotEmpty()) {
                        Log.e(TAG, "新增string：[name: $name, lang: $lang, 新值: $word]")
                        val wordMap = resData.computeIfAbsent(lang) { linkedMapOf() }
                        wordMap[name] = word
                    }
                }
            }
        }
    }

    /**
     * [data] <语言目录（如values-zh-rCN），<name，word>>
     */
    fun importWords(data: LinkedHashMap<String, LinkedHashMap<String, String>>, parentFile: File) {
        data.forEach { (langDir, hashMap) ->
            if (langDir.startsWith("values")) {
                val stringFile = File(parentFile, "$langDir/strings.xml")
                OutputHelper.writeFile(stringFile, hashMap, langDir)
            }
        }
    }
}