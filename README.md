# AndroidStringScript
## Android String导入导出脚本，用于国际化翻译
- ExportStrings2ExcelScript.kt 收集项目所有模块string.xml中的字符，导出为excel，模块名对应表名
- ImportFromExcelScript.kt 从excel导入字符到string.xml，存在则覆盖旧值，不存在则添加到末尾
