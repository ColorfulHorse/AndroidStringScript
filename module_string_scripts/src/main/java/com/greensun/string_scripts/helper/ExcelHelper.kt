package com.greensun.string_scripts.helper

import com.greensun.string_scripts.logger.Log
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * 导入导出excel
 */
object ExcelHelper {

    private val TAG = ExcelHelper.javaClass.simpleName
    private const val ROOT_TAG = "resources"
    private const val TAG_NAME = "string"

    /**
     * 获取excel某个表的数据  <表名，<name，<语言目录，值>>>
     */
    fun getSheetsData(filePath: String): LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> {
        val inputStream = FileInputStream(filePath)
        val excelWBook = XSSFWorkbook(inputStream)
        val map = linkedMapOf<String, LinkedHashMap<String, LinkedHashMap<String, String>>>()
        excelWBook.forEach {
            val dataMap = LinkedHashMap<String, LinkedHashMap<String, String>>()
            val head = ArrayList<String>()
            //获取工作簿
            val excelWSheet = excelWBook.getSheet(it.sheetName)
            excelWSheet.run {
                // 总行数
                val rowCount = lastRowNum - firstRowNum + 1
                // 总列数
                val colCount = getRow(0).physicalNumberOfCells
                // 获取所有语言目录
                for (col in 0 until colCount) {
                    head.add(getCellData(excelWBook, sheetName, 0, col))
                }

                for (row in 1 until rowCount) {
                    // 第一列为string name
                    val name = getCellData(excelWBook, sheetName, row, 0)
                    Log.d(TAG, "第${row}行，name = $name")
                    val v = LinkedHashMap<String, String>()
                    for (col in 0 until colCount) {
                        v[head[col]] = getCellData(excelWBook, sheetName, row, col)
                        Log.d(TAG, "key = ${head[col]} ,value = ${v[head[col]]}")
                    }
                    dataMap[name] = v
                }

                excelWBook.close()
                inputStream.close()
            }
            map[it.sheetName] = dataMap
        }
        excelWBook.close()
        return map
    }

    /**
     * 读取单元格数据
     *
     * @param sheetName 工作簿名称
     * @param rowNum    行号
     * @param colNum    列号
     */
    private fun getCellData(
        excelWBook: XSSFWorkbook,
        sheetName: String,
        rowNum: Int,
        colNum: Int
    ): String {
        val excelWSheet = excelWBook.getSheet(sheetName)
        try {
            val cell = excelWSheet.getRow(rowNum).getCell(colNum)
            cell.run {
                val cellData: String = when (cellType) {
                    CellType.STRING -> {
                        stringCellValue
                    }
                    CellType.BLANK -> {
                        ""
                    }
                    else -> {
                        throw RuntimeException("no support data type")
                    }
                }
                Log.d(TAG, "工作表[" + sheetName + "]的第" + rowNum + "行第" + colNum + "列的值为：" + cellData)
                return cellData
            }
        } catch (e: Exception) {
            Log.d(TAG, "工作表[" + sheetName + "]的第" + rowNum + "行第" + colNum + "列的值时异常：" + e)
        }
        return ""
    }

    /**
     * 写入单元格数据
     *
     * @param sheetName 工作簿名称
     * @param rowNum    行号
     * @param colNum    列号
     */
    fun setCellData(
        filePath: String,
        sheetName: String,
        rowNum: Int,
        colNum: Int,
        resultValue: String
    ) {
        val inputStream = FileInputStream(filePath)
        val excelWBook = XSSFWorkbook(inputStream)
        val excelWSheet = excelWBook.getSheet(sheetName)
        try {
            // 获取 excel文件中的行对象
            val row = getRow(excelWSheet, rowNum)
            // 如果单元格为空，则返回 Null
            val cell = getCell(row, colNum)

            cell.setCellValue(resultValue)
        } catch (e: Exception) {
            Log.d(TAG, "set excel data error-->$e")
        }

        try {
            val fileOut = FileOutputStream(filePath)
            excelWBook.write(fileOut)
            fileOut.flush()
            fileOut.close()

            excelWBook.close()
            inputStream.close()
        } catch (e: IOException) {
            Log.d(TAG, e.toString())
        }
    }

    /**
     * 导出多语言文案到 Excel
     */
    fun resource2File(
        filePath: String,
        sheetName: String,
        head: ArrayList<String>,
        res: LinkedHashMap<String, LinkedHashMap<String, String>>,
        excelWBook: XSSFWorkbook, index: Int, size: Int
    ) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        } else {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        val excelWSheet = excelWBook.createSheet(sheetName)
        try {
            // 表头
            Log.d(TAG, "表头：${head}")
            val headRow = getRow(excelWSheet, 0)
            head.forEachIndexed { index, s ->
                val cell = getCell(headRow, index)
                cell.setCellValue(s)
            }

            val newCsvCn = ArrayList<String>()
            head.forEach {
                newCsvCn.add(LanguageKeyMap.map[it] ?: "")
            }
            val cnRow = getRow(excelWSheet, 1)
            newCsvCn.forEachIndexed { index, s ->
                val cell = getCell(cnRow, index)
                cell.setCellValue(s)
            }

            // 填充多语言的翻译
            var index = 2
            res.forEach { (key, value) ->
                val row = getRow(excelWSheet, index)
                head.forEachIndexed { index, h ->
                    val resWord = value[h]
                    val subCell = getCell(row, index)
                    subCell.setCellValue(resWord)
                }

                index++
            }
        } catch (e: Exception) {
            Log.e(TAG, "set excel data error-->$e")
        }
        if (index >= size) {
            try {
                val fileOut = FileOutputStream(filePath)
                excelWBook.write(fileOut)
                fileOut.flush()
                fileOut.close()

                excelWBook.close()
            } catch (e: IOException) {
                Log.e(TAG, e.toString())
            }
        }
    }

    private fun getRow(excelWSheet: XSSFSheet, rowNum: Int): XSSFRow {
        return excelWSheet.getRow(rowNum) ?: excelWSheet.createRow(rowNum)
    }

    private fun getCell(row: XSSFRow, colNum: Int): XSSFCell {
        return row.getCell(colNum, MissingCellPolicy.RETURN_BLANK_AS_NULL) ?: row.createCell(colNum)
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
                        Log.e(TAG, "name: $name, word: $word")
                        names[name] = name
                        data[name] = word
                    }
                }
            }
            hashMap[langDir.name] = data
        }
        return hashMap
    }

    fun importWords(newLangNameMap: LinkedHashMap<String, LinkedHashMap<String, String>>, parentDir: File) {

        newLangNameMap.forEach { (langDir, hashMap) ->
            if (langDir.startsWith("values")) {
                val stringFile = File(parentDir, "$langDir/strings.xml")
                if (stringFile.exists()) {
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
                    createStringFile(doc, stringFile)
                } else {
                    parentDir.mkdirs()
                    stringFile.createNewFile()
                    val doc = DocumentHelper.createDocument()
                    val root = doc.addElement(ROOT_TAG)
                    hashMap.forEach { (name, word) ->
                        val element = root.addElement(TAG_NAME)
                        element.addAttribute("name", name)
                            .addText(word)
                    }
                    createStringFile(doc, stringFile)
                }
            }
        }
    }

    private fun createStringFile(doc: Document, file: File) {
        var format = OutputFormat.createPrettyPrint()
        format.setIndentSize(4)
        format.isNewlines = true
        format.lineSeparator = System.getProperty("line.separator")
        file.outputStream().use { os ->
            val writer = XMLWriter(os, format)
            writer.write(doc)
            writer.close()
        }
    }
}