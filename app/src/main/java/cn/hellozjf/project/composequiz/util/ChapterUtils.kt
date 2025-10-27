package cn.hellozjf.project.composequiz.util

import cn.hellozjf.project.composequiz.dto.ChapterDTO
import org.openqa.selenium.WebDriver
import java.io.File

/**
 * 章节工具
 */
class ChapterUtils {

  companion object {

    /**
     * 从 PDF 文件中获取章节信息
     * 其中章节信息里面的 simpleTitle 和 fullUrl 需要通过 selenium 打开网页获取
     */
    fun getChapterDTOListFromPdfFile(
      file: File,
      driver: WebDriver,
      timeoutSeconds: Long = 10L
    ): List<ChapterDTO> {
      val simpleChapterDTOList = PdfUtils.getChapterDTOList(file)
      val chapterInfoList = SeleniumUtils.fillChapterDTOList(
        chapterDTOList = simpleChapterDTOList,
        driver = driver,
        timeoutSeconds = timeoutSeconds
      )
      return chapterInfoList
    }

    /**
     * 获取用于 Excel 或 CSV 上面的标题
     */
    fun getHeader(): List<String> {
      val header = listOf(
        ChapterConstant.INDEX,
        ChapterConstant.FULL_TITLE,
        ChapterConstant.SIMPLE_TITLE,
        ChapterConstant.SIMPLE_URL,
        ChapterConstant.FULL_URL
      )
      return header
    }
  }
}