package cn.hellozjf.project.composequiz.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class TextFileUtils private constructor() {

  companion object {
    private const val TEXT_ASSET_DIR = "texts"
    private const val TEXT_LOCAL_DIR = "texts"

    /**
     * 拷贝单个txt文件
     */
    fun copyTextFile(context: Context, assetPath: String, destPath: String): Boolean {
      return try {
        context.assets.open(assetPath).use { inputStream ->
          FileOutputStream(destPath).use { outputStream ->
            val file = File(destPath)
            file.parentFile?.mkdirs()

            inputStream.copyTo(outputStream)
            true
          }
        }
      } catch (e: IOException) {
        e.printStackTrace()
        false
      }
    }

    /**
     * 拷贝assets/texts目录下的所有txt文件
     */
    fun copyAllTextFiles(context: Context) {
      try {
        val files = context.assets.list(TEXT_ASSET_DIR)
        files?.forEach { fileName ->
          if (fileName.endsWith(".txt", true)) {
            val assetPath = "$TEXT_ASSET_DIR/$fileName"
            val destPath = getTextFileLocalPath(context, fileName)
            copyTextFile(context, assetPath, destPath)
          }
        }
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }

    /**
     * 直接读取assets中的txt文件内容（不拷贝到本地）
     */
    fun readTextFromAssets(context: Context, assetPath: String): String? {
      return try {
        context.assets.open(assetPath).bufferedReader().use { it.readText() }
      } catch (e: IOException) {
        e.printStackTrace()
        null
      }
    }

    /**
     * 读取已拷贝到本地的txt文件内容
     */
    fun readTextFromLocal(context: Context, fileName: String): String? {
      val file = getTextFile(context, fileName)
      return if (file.exists()) {
        try {
          file.bufferedReader().use { it.readText() }
        } catch (e: IOException) {
          e.printStackTrace()
          null
        }
      } else {
        null
      }
    }

    /**
     * 按行读取txt文件，返回List
     */
    fun readTextLinesFromAssets(context: Context, assetPath: String): List<String> {
      return try {
        context.assets.open(assetPath).bufferedReader().useLines { it.toList() }
      } catch (e: IOException) {
        e.printStackTrace()
        emptyList()
      }
    }

    /**
     * 按行读取本地txt文件
     */
    fun readTextLinesFromLocal(context: Context, fileName: String): List<String> {
      val file = getTextFile(context, fileName)
      return if (file.exists()) {
        try {
          file.bufferedReader().useLines { it.toList() }
        } catch (e: IOException) {
          e.printStackTrace()
          emptyList()
        }
      } else {
        emptyList()
      }
    }

    /**
     * 获取txt文件的本地路径
     */
    fun getTextFileLocalPath(context: Context, fileName: String): String {
      return File(context.filesDir, "$TEXT_LOCAL_DIR/$fileName").absolutePath
    }

    /**
     * 获取txt文件的File对象
     */
    fun getTextFile(context: Context, fileName: String): File {
      return File(context.filesDir, "$TEXT_LOCAL_DIR/$fileName")
    }

    /**
     * 检查txt文件是否已存在
     */
    fun isTextFileExists(context: Context, fileName: String): Boolean {
      return getTextFile(context, fileName).exists()
    }

    /**
     * 获取所有已拷贝的txt文件列表
     */
    fun getLocalTextFiles(context: Context): List<String> {
      val textDir = File(context.filesDir, TEXT_LOCAL_DIR)
      return if (textDir.exists() && textDir.isDirectory) {
        textDir.listFiles { _, name -> name.endsWith(".txt", true) }
          ?.map { it.name }
          ?: emptyList()
      } else {
        emptyList()
      }
    }

    /**
     * 获取assets中所有txt文件列表
     */
    fun getAssetTextFiles(context: Context): List<String> {
      return try {
        context.assets.list(TEXT_ASSET_DIR)
          ?.filter { it.endsWith(".txt", true) }
          ?: emptyList()
      } catch (e: IOException) {
        e.printStackTrace()
        emptyList()
      }
    }

    /**
     * 删除本地txt文件
     */
    fun deleteLocalTextFile(context: Context, fileName: String): Boolean {
      return getTextFile(context, fileName).delete()
    }

    /**
     * 清空本地txt文件目录
     */
    fun clearLocalTextFiles(context: Context): Boolean {
      val textDir = File(context.filesDir, TEXT_LOCAL_DIR)
      return if (textDir.exists() && textDir.isDirectory) {
        textDir.listFiles()?.all { it.delete() } ?: true
      } else {
        true
      }
    }
  }
}