package cn.hellozjf.project.composequiz.util

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class SeleniumUtils {
  companion object {
    data class FullChapterInfo(
      val index: Int,
      val title: String,
      val simpleTitle: String,
      val url: String,
      val actualUrl: String
    )

    fun getAllChapterInfoList(
      chapterInfoList: List<PdfUtils.ChapterInfo>,
      driver: WebDriver,
      timeoutSeconds: Long
    ): List<FullChapterInfo> {

      val result = mutableListOf<FullChapterInfo>()

      for (chapterInfo in chapterInfoList) {
        // println(chapterInfo)
        if (chapterInfo.index == 1) {
          // 第一章是所有测试的汇总地址，跳过
          continue
        }
        chapterInfo.url?.let {
          // 上面的内容还不够，我需要简化的标题和实际的习题网址
          // 所以需要用 selenium 打开网页，获取页面元素信息
          driver.get(it)
          WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds)).until(
            ExpectedConditions.presenceOfElementLocated(
              By.cssSelector("h1.has-text-align-center.alignwide.wp-block-post-title")
            )
          )
          // 获取本章简化标题
          val elements = driver.findElements(
            By.cssSelector("h1.has-text-align-center.alignwide.wp-block-post-title")
          )
          val simpleTitle = elements[0].text
          val actualUrl = driver.currentUrl ?: ""
          println("index: ${chapterInfo.index}, title: $simpleTitle, url: ${driver.currentUrl}")
          result.add(
            FullChapterInfo(
              index = chapterInfo.index,
              title = chapterInfo.title,
              simpleTitle = simpleTitle,
              url = it,
              actualUrl = actualUrl
            )
          )
        }
      }
      return result
    }
  }
}