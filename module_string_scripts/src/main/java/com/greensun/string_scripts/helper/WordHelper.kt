package com.greensun.string_scripts.helper

import com.greensun.string_scripts.bean.AndroidStringBean
import com.greensun.string_scripts.bean.CsvBean
import com.greensun.string_scripts.logger.Log
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.*
import java.util.regex.Pattern

object WordHelper {

    private val TAG = "LanguageWordHelper"

    private const val cnKey = "values-zh-rCN"

    // 第一列存放string的name，作为第二表头
    private const val colHead = "name"
    private const val NEW_LINE_SEPARATOR = "\n"

    /**
     *  转换string map数据结构，以name为行标识方便写入excel
     *  [source] <语言目录（如values-zh-rCN），<name，bean>>
     *  @return <name，<语言目录，值>>
     */
    fun transformResData(source: LinkedHashMap<String, LinkedHashMap<String, AndroidStringBean>>): LinkedHashMap<String, LinkedHashMap<String, String>> {
        // <name，<语言目录，值>>
        val resData = LinkedHashMap<String, LinkedHashMap<String, String>>()
        source.forEach { (langDir, value) ->
            value.forEach { (name, bean) ->
                val wordRes = resData.computeIfAbsent(name) { LinkedHashMap() }
                wordRes[langDir] = bean.word
            }
        }
        return resData
    }

    /**
     *  还原string map数据结构，以language为行标识方便写入多个string.xml
     *  [source] <name，<语言目录，值>>
     *  @return <语言目录（如values-zh-rCN），<name，bean>>
     */
    fun revertResData(source: LinkedHashMap<String, LinkedHashMap<String, String>>): LinkedHashMap<String, LinkedHashMap<String, AndroidStringBean>> {
        // <语言目录（如values-zh-rCN），<name，bean>>
        val resData = LinkedHashMap<String, LinkedHashMap<String, AndroidStringBean>>()
        source.forEach { (name, value) ->
            value.forEach { (langDir, word) ->
                val langRes = resData.computeIfAbsent(langDir) { LinkedHashMap() }
                langRes[name] = AndroidStringBean(name, word, false)
            }
        }
        return resData
    }

    /**
     * 解析当前的多语言内容 <语言目录（如values-zh-rCN），<name，bean>>
     */
    fun collectRes(res: File): LinkedHashMap<String, LinkedHashMap<String, AndroidStringBean>> {
        val hashMap = LinkedHashMap<String, LinkedHashMap<String, AndroidStringBean>>()
        val regexStr =
            "^\\s*<\\s*string\\s+name\\s*=\\s*\"(.*?)\"\\s*formatted=\"false\"\\s*>(.*?)</\\s*string\\s*>\\s*\$"

        val regexStr2 =
            "^\\s*<\\s*string\\s+name\\s*=\\s*\"(.*?)\"\\s*>(.*?)</\\s*string\\s*>\\s*\$"
        // 添加string name作为第一列，用作索引
        hashMap[colHead] = LinkedHashMap()
        res.listFiles().forEach {
            val data = LinkedHashMap<String, AndroidStringBean>()
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
                        names[name] =
                            AndroidStringBean(name, name, false)
                        data[name] =
                            AndroidStringBean(name, value, false)
                    } else {
                        // 使用第二个规则获取
                        val p2 = Pattern.compile(regexStr2)
                        val groups2 = p2.matcher(line)
                        if (groups2.find() && groups2.groupCount() == 2) {
                            val name = groups2.group(1)
                            val value = groups2.group(2)
                            names[name] =
                                AndroidStringBean(name, name, true)
                            data[name] =
                                AndroidStringBean(name, value, true)
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
     * @param oldCsv 当前的csv文件中的资源 <name，<语言目录，值>>
     * @param res 模块中的资源文件解析出来的资源 <name，<语言目录，值>>
     * @param resDir 模块中的多语言目录
     * @param csvHead 本地的csv表头
     */
    fun combineAndOutCsv(
        oldCsv: LinkedHashMap<String, LinkedHashMap<String, String>>,
        res: LinkedHashMap<String, LinkedHashMap<String, String>>,
        resDir: ArrayList<String>,
        csvHead: ArrayList<String>, childFilePath: String
    ) {
        Log.d(TAG, "csv表头：${csvHead} \n size:${csvHead.size}")
        Log.d(TAG, "资源表头：${resDir} \n size:${resDir.size}")
        // 先确定表头
        val newCsvHead = ArrayList<String>()
        newCsvHead.add(colHead)
        newCsvHead.add(cnKey)
        newCsvHead.add("values")
        csvHead.forEach {
            if (!newCsvHead.contains(it)) {
                newCsvHead.add(it)
            }
        }
        resDir.forEach {
            if (!newCsvHead.contains(it)) {
                newCsvHead.add(it)
            }
        }
        // 对应的翻译中文
        val newCsvCn = ArrayList<String>()
        newCsvHead.forEach {
            newCsvCn.add(LanguageKeyMap.map[it] ?: "")
        }

        Log.d(TAG, "新表头：${newCsvHead} \n size:${newCsvHead.size}")
        val fileWriter = OutputStreamWriter(FileOutputStream(File("./", childFilePath)), "UTF-8")
        val formator: CSVFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR)
        val printer = CSVPrinter(fileWriter, formator)
        printer.printRecord(newCsvHead)
        printer.printRecord(newCsvCn)
        Log.e(TAG, "oldCsv size ${oldCsv.size} res size ${res.size}")
        val lines = linkedMapOf<String, List<String>>()
        // 编辑每行数据
        // 先对本地csv的数据先进行填充
        oldCsv.forEach {
            val name = it.key
            val line = ArrayList<String>()
            newCsvHead.forEach { h ->
                // 根据头进行填充
                val csvWord = it.value[h]
                if (csvWord.isNullOrEmpty()) {
                    // 本地为空，从资源中查找
                    line.add("")
                } else {
                    line.add(csvWord)
                }
            }
            lines[name] = line
        }

        // csv 的数据补充完，再对res的数据进行补充
        res.forEach {
            val name = it.key
            val line = ArrayList<String>()
            // csv 的就不进行重新添加了
            newCsvHead.forEach { h ->
                val resWord = it.value[h]
                if (resWord.isNullOrEmpty()) {
                    line.add("")
                } else {
                    line.add(resWord)
                }
            }
            lines[name] = line
        }
        lines.values.forEach {
            printer.printRecord(it)
        }
        printer.close(true)
        fileWriter.close()
    }

    /**
     * 从csv文件读出
     */
    fun readCsv(childFilePath: String): CsvBean {
        // <name，<语言目录，值>>
        val nameDataMap = LinkedHashMap<String, LinkedHashMap<String, String>>()
        val isr = InputStreamReader(FileInputStream(File("./", childFilePath)), "UTF-8")
        val `in` = BufferedReader(isr)
        val csv = CSVFormat.DEFAULT.parse(`in`)
        val head = ArrayList<String>()
        // names在第几列
        var keyIndex = -1
        csv.forEach {
            if (it.recordNumber == 1L) {
                // 第一行是多语言文件夹
                head.addAll(it.toList())
                keyIndex = head.indexOf(colHead)
            } else if (it.recordNumber == 2L) {
                // 第二行是多语言对应的中文名称

            } else {
                if (keyIndex < 0) return CsvBean(ArrayList(), LinkedHashMap())
                // 当前行的string name
                val name = it.get(keyIndex)
                if (name.isNotEmpty()) {
                    val langWordMap = LinkedHashMap<String, String>()
                    // 遍历该行每个单元格
                    it.toList().forEachIndexed { index, s ->
                        langWordMap[head[index]] = s
                    }
                    nameDataMap[name] = langWordMap
                }
            }
        }
        csv.close()
        `in`.close()
        return CsvBean(head, nameDataMap)
    }

    /**
     * 将excel中string合并到项目原本string中，不存在则追加，存在则覆盖
     * [newData] excel中读出的string <name，<语言目录，值>>
     * [resData] 项目中读出的string <name，<语言目录，值>>
     */
    fun mergeStringData(
        newData: CsvBean,
        resData: LinkedHashMap<String, LinkedHashMap<String, String>>
    ) {
        newData.data.forEach { (k, v) ->
            // 当前项目中一条包含多语种的string map
            val resWord = resData[k]
            if (resWord != null) {
                // 项目中存在该name的字符，遍历每种语言的值，依次覆盖为excel中的新值
                v.forEach { (key, newWord) ->
                    if (key.isNotEmpty() && newWord.isNotBlank()) {
                        resWord[key] = newWord
                    }
                }
            } else {
                // 项目中不存在该name的字符，将excel表中的插入
                resData[k] = v
            }
        }
    }

    /**
     * [data] <语言目录（如values-zh-rCN），<name，bean>>
     */
    fun importWords(data: LinkedHashMap<String, LinkedHashMap<String, AndroidStringBean>>, parentFile: File) {
        data.forEach { (langDir, hashMap) ->
            if (langDir.startsWith("values")) {
                val stringFile = File(parentFile, "$langDir/strings.xml")
                OutputHelper.writeFile(stringFile, hashMap)
            }
        }
    }
}