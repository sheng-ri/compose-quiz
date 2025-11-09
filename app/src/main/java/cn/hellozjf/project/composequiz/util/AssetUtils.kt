package cn.hellozjf.project.composequiz.util

import android.content.Context
import java.io.IOException
import java.io.Reader

/**
 * 资源工具，用于读取 apk 包里面的资源
 */
class AssetUtils {
  companion object {
    val CHAPTER_EN_CSV = "csv/chapter.en.csv"
    val CHAPTER_ZH_CSV = "csv/chapter.zh.csv"
    val QUIZ_EXT_CSV = "csv/quiz.ext.csv"
    val QUIZ_EN_CSV = "csv/quiz.en.csv"
    val QUIZ_ZH_CSV = "csv/quiz.zh.csv"

    fun openAndRead(
      context: Context,
      path: String,
      doThing: (Reader) -> Unit
    ) {
      try {
        context.assets.open(path).bufferedReader().use { reader ->
          doThing(reader)
        }
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }
}