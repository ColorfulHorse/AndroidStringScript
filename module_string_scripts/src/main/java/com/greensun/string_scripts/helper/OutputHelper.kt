package com.greensun.string_scripts.helper


import com.greensun.string_scripts.bean.AndroidStringBean
import com.greensun.string_scripts.logger.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

object OutputHelper {
    private val TAG = "OutHelper"

    val headFlag = """
        <resources>
    """.trimIndent()

    val endFlag = """
        </resources>
    """.trimIndent()

    val temps = arrayListOf(
        "\\n",
        "\\n2",
        "\\n3",
        "\\n\\n",
        "\\n\\n0",
        "\\n\\n1",
        "\\n\\n2",
        "\\n\\n3",
        "%d",
        "%1\$d",
        "%2\$d",
        "%3\$d",
        "%s",
        "%S",
        "%1\$s",
        "%2\$s",
        "%3\$s",
        "%d\$s"
    )


    fun writeFile(
        file: File,
        map: LinkedHashMap<String, AndroidStringBean>) {
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if (!file.exists()) {
            file.createNewFile()
        }
        Log.e("writeFile", file.toString())
        val writer = OutputStreamWriter(FileOutputStream(file), "UTF-8")
        writer.appendLine(headFlag)
        map.forEach {
            if (it.value.word.isEmpty()) {
                Log.d(TAG, "没有翻译：lang: ${it.key} name: ${it.value.name}")
            } else {
                writer.appendLine("    <string name=\"${it.value.name}\">${it.value.word}</string>")
            }
        }
        writer.appendLine(endFlag)
        writer.flush()
        writer.close()
    }


    private fun hasFormatted(s: String): Boolean {
        temps.forEach {
            if (s.contains(it)) return true
        }
        return false
    }

}