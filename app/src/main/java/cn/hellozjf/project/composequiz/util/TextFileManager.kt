package cn.hellozjf.project.composequiz.util

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class TextFileManager {

  interface TextFileCopyListener {
    fun onStart()
    fun onProgress(fileName: String, current: Int, total: Int)
    fun onComplete(success: Boolean, copiedCount: Int)
    fun onError(fileName: String, exception: Exception)
  }

  companion object {
    fun copyTextFilesWithProgress(
      context: Context,
      listener: TextFileCopyListener? = null
    ) {
      Thread {
        try {
          val files = context.assets.list("texts")
          if (files.isNullOrEmpty()) {
            listener?.onComplete(false, 0)
            return@Thread
          }

          val txtFiles = files.filter { it.endsWith(".txt", true) }
          if (txtFiles.isEmpty()) {
            listener?.onComplete(false, 0)
            return@Thread
          }

          listener?.onStart()

          var successCount = 0
          txtFiles.forEachIndexed { index, fileName ->
            listener?.onProgress(fileName, index + 1, txtFiles.size)

            try {
              val assetPath = "texts/$fileName"
              val destPath = TextFileUtils.getTextFileLocalPath(context, fileName)

              val success = TextFileUtils.copyTextFile(context, assetPath, destPath)
              if (success) {
                successCount++
              }
            } catch (e: Exception) {
              listener?.onError(fileName, e)
            }
          }

          listener?.onComplete(successCount == txtFiles.size, successCount)

        } catch (e: IOException) {
          listener?.onComplete(false, 0)
        }
      }.start()
    }

    /**
     * 使用协程版本的拷贝（需要添加协程依赖）
     */
    suspend fun copyTextFilesWithCoroutine(
      context: Context,
      onProgress: (String, Int, Int) -> Unit = { _, _, _ -> }
    ): Boolean {
      return withContext(Dispatchers.IO) {
        try {
          val files = context.assets.list("texts")
          val txtFiles = files?.filter { it.endsWith(".txt", true) } ?: emptyList()

          txtFiles.forEachIndexed { index, fileName ->
            onProgress(fileName, index + 1, txtFiles.size)

            val assetPath = "texts/$fileName"
            val destPath = TextFileUtils.getTextFileLocalPath(context, fileName)
            TextFileUtils.copyTextFile(context, assetPath, destPath)
          }

          txtFiles.isNotEmpty()
        } catch (e: Exception) {
          false
        }
      }
    }
  }
}