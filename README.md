# AndroidStringScript
## Android String导入导出脚本，用于国际化翻译
### 使用方法
1. 导入module_string_scripts模块到项目中
2. 更改Config类中的配置
3. 运行ExportStrings2ExcelScript.kt 收集项目所有模块string.xml中的字符，导出为excel，模块名对应表名
4. 把excel第一列保护起来，设为锁定，然后提供出去进行翻译
5. 运行ImportFromExcelScript.kt 把翻译完的excel内容导入合并到项目中，**如果isBaseOnWord为true，导入时需要至少需要一个参照列（BASE_LANG或者DEFAULT_LANG）**

### 效果图
![image](https://user-images.githubusercontent.com/20135323/160737865-57cc68a8-d822-49e5-b6cb-0004c962ffa0.png)

**博客 https://juejin.cn/post/7056708227060203534**

## update history
### 2022/6/30  
fix 包含空列时统计列数不正确问题  
feature 导出时根据某个语言的文案进行排序，把空行排到最后
