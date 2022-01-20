package com.greensun.string_scripts.helper

import com.greensun.string_scripts.logger.Log
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import java.io.*

object WordHelper {

    private val TAG = "WordHelper"


    // 第一列存放string的name，作为第二表头
    const val colHead = "name"
    private const val DEFAULT_LANG = "values"
    private const val BASE_LANG = "values-zh-rCN"
    private const val ROOT_TAG = "resources"
    private const val TAG_NAME = "string"

    // 是否将基准语言的值相同（即使name不同）的string也视为同一个string
    // 比如以中文为基准，那么<string name="name1">相同值</string> 等同于 <string name="name2">相同值</string>
    // 这样的话可以以某一语言为基准[导出时去重/导入时恢复]恢复内容重复的string
    // 导入导出时应该用同一个基准
    private const val isBaseOnWord = true

    /**
     *  处理可能出现的相同内容但是不同key的string
     *  [source] <name，<语言目录，word>>
     *  @return <name，<语言目录，word>>
     */
    fun processSameWords(source: LinkedHashMap<String, LinkedHashMap<String, String>>): LinkedHashMap<String, LinkedHashMap<String, String>> {
        // 由于可能存在不同key但是相同内容的string，导出时将内容相同的string聚合到一起
        val haveCNKey = source.entries.first().value.containsKey(BASE_LANG)
        val baseLang = if (haveCNKey) BASE_LANG else DEFAULT_LANG
        // 是否根据中文或者默认语言的内容为基准去重，否则将相同的内容行排序到一起
        return if (isBaseOnWord) {
            // 去重
            source.entries.distinctBy {
                val baseWord = it.value[baseLang]
                return@distinctBy if (!baseWord.isNullOrBlank())
                    baseWord
                else
                    it
            }.fold(linkedMapOf()) { acc, entry ->
                acc[entry.key] = entry.value
                acc
            }
        } else {
            // 相同的排到一起
            source.entries.sortedBy {
                val baseWord = it.value[baseLang]
                if (!baseWord.isNullOrEmpty())
                    return@sortedBy baseWord
                else
                    return@sortedBy null
            }.fold(linkedMapOf()) { acc, entry ->
                acc[entry.key] = entry.value
                acc
            }
        }
    }

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
        Log.e("resData", "resData: ${resData.size}")
        return resData
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
        if (isBaseOnWord) {
            /**
             * 1. excel中某个string name匹配到了项目中的某个string name
             * 2. 找到项目中和该string基准语言的内容相同的其他string
             * 3. 将这些string视为相同的string，复制一份添加到newData中
             */
            val baseLang = if (resData.containsKey(BASE_LANG)) BASE_LANG else DEFAULT_LANG
            val baseLangMap = newData[baseLang]
            if (baseLangMap != null) {
                // 寻找基准值相同的string
                val sameWords = baseLangMap.map { (name, newWord) ->
                    val oldBaseWord = resData[baseLang]?.get(name)
                    return@map name to resData[baseLang]?.filter {
                        if (!oldBaseWord.isNullOrBlank()) {
                            return@filter it.value == oldBaseWord
                        }
                        false
                    }?.keys
                }
                sameWords.forEach { pair ->
                    if (pair.second?.size ?: 0 > 1) {
                        Log.e(TAG, "newName:${pair.first} mapping old names:${pair.second}")
                    }
                    val newName = pair.first
                    pair.second?.forEach { oldName ->
                        newData.forEach { (lang, map) ->
                            map[oldName] = map[newName] ?: ""
                        }
                    }
                }
            }
        }
        // 遍历更新项目string
        newData.forEach { (lang, map) ->
            // 排除第一列
            if (lang == colHead)
                return@forEach
            // 当前项目中一条包含多语种的string map
            val nameWordMap = resData.computeIfAbsent(lang) { linkedMapOf() }
            map.forEach { (name, newWord) ->
                // 项目中存在该语言的字符，遍历每个的值，依次覆盖为excel中的新值
                if (name.isNotEmpty() && newWord.isNotBlank()) {
                    val oldWord = nameWordMap[name]
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
                    nameWordMap[name] = newWord
                }
            }
        }
    }

    /**
     * 解析当前的多语言内容 <语言目录（如values-zh-rCN），<name，word>>
     */
    fun collectRes(res: File): LinkedHashMap<String, LinkedHashMap<String, String>> {
        val hashMap = LinkedHashMap<String, LinkedHashMap<String, String>>()
        hashMap[WordHelper.colHead] = LinkedHashMap()
        val saxReader = SAXReader()
        res.listFiles().forEach { langDir ->
            val stringFile = File(langDir, "strings.xml")
            if (!stringFile.exists())
                return@forEach
            val data = LinkedHashMap<String, String>()
            // 收集所有string name
            val names = hashMap.computeIfAbsent(WordHelper.colHead) { LinkedHashMap() }
            val doc = saxReader.read(stringFile)
            val root = doc.rootElement
            if (root.name == ROOT_TAG) {
                val iterator = root.elementIterator()
                while (iterator.hasNext()) {
                    val element = iterator.next()
                    if (element.name == TAG_NAME) {
                        val name = element.attribute("name").text
                        val word = element.text
                        names[name] = name
                        data[name] = word
                    }
                }
            }
            hashMap[langDir.name] = data
        }
        return hashMap
    }

    /**
     * 将excel中string导入到项目
     */
    fun importWords(newLangNameMap: LinkedHashMap<String, LinkedHashMap<String, String>>, parentDir: File) {
        newLangNameMap.forEach { (langDir, hashMap) ->
            if (langDir.startsWith("values")) {
                val stringFile = File(parentDir, "$langDir/strings.xml")
                if (stringFile.exists()) {
                    // 修改原本dom
                    val saxReader = SAXReader()
                    val doc = saxReader.read(stringFile)
                    val root = doc.rootElement
                    val nodeMap = linkedMapOf<String, Element>()
                    if (root.name == ROOT_TAG) {
                        val iterator = root.elementIterator()
                        while (iterator.hasNext()) {
                            val element = iterator.next()
                            if (element.name == TAG_NAME) {
                                val name = element.attribute("name").text
                                nodeMap[name] = element
                            }
                        }
                    }
                    hashMap.forEach { (name, word) ->
                        val node = nodeMap[name]
                        if (node == null) {
                            root.addElement(TAG_NAME)
                                .addAttribute("name", name)
                                .addText(word)
                        } else {
                            if (node.text != word) {
                                node.text = word
                            }
                        }
                    }
                    outputStringFile(doc, stringFile)
                } else {
                    // 创建新dom
                    val langFile = File(parentDir, langDir)
                    langFile.mkdirs()
                    stringFile.createNewFile()
                    val doc = DocumentHelper.createDocument()
                    val root = doc.addElement(ROOT_TAG)
                    hashMap.forEach { (name, word) ->
                        val element = root.addElement(TAG_NAME)
                        element.addAttribute("name", name)
                            .addText(word)
                    }
                    outputStringFile(doc, stringFile)
                }
            }
        }
    }

    /**
     * 输出string文件
     */
    private fun outputStringFile(doc: Document, file: File) {
        // 遍历所有节点，移除掉原本的换行符节点，否则输出时会因为newlines多出换行符
        val root = doc.rootElement
        if (root.name == ROOT_TAG) {
            val iterator = root.nodeIterator()
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (element.nodeType == org.dom4j.Node.TEXT_NODE) {
                    if (element.text.isBlank()) {
                        iterator.remove()
                    }
                }
            }
        }
        // 输出
        val format = OutputFormat()
        format.encoding = "utf-8"
        format.setIndentSize(4)
        format.isNewLineAfterDeclaration = false
        format.isNewlines = true
        format.lineSeparator = System.getProperty("line.separator")
        file.outputStream().use { os ->
            val writer = XMLWriter(os, format)
            // 是否将字符转义
            writer.isEscapeText = false
            writer.write(doc)
            writer.flush()
            writer.close()
        }
    }
}