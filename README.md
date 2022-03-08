# AndroidStringScript
## Android String导入导出脚本，用于国际化翻译
### 使用方法
1. 导入module_string_scripts模块到项目中
2. 更改Config类中的配置
3. 运行ExportStrings2ExcelScript.kt 收集项目所有模块string.xml中的字符，导出为excel，模块名对应表名
4. 把excel第一列保护起来，设为锁定，然后提供出去进行翻译
5. 运行ImportFromExcelScript.kt 把翻译完的excel内容导入合并到项目中

**博客 https://juejin.cn/post/7056708227060203534**
