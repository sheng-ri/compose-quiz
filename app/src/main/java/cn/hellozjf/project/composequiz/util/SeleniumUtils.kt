package cn.hellozjf.project.composequiz.util

import cn.hellozjf.project.composequiz.dto.ChapterDTO
import cn.hellozjf.project.composequiz.dto.QuizDTO
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

class SeleniumUtils {
  companion object {

    val ZIP_RESOURCE_PATH =
      (System.getProperty("user.dir") ?: "") + "\\..\\other\\driver\\chromedriver.zip"
    val TARGET_DIR = (System.getProperty("java.io.tmpdir") ?: "") + "\\.chrome_driver"
    val DRIVER_FILE_NAME = "chromedriver.exe"

    /**
     * 填充 chapterDTOList 中的简化标题和实际习题网址
     */
    fun fillChapterDTOList(
      chapterDTOList: List<ChapterDTO>,
      driver: WebDriver,
      timeoutSeconds: Long
    ): List<ChapterDTO> {

      val result = mutableListOf<ChapterDTO>()

      for (chapterDTO in chapterDTOList) {
        // println(chapterInfo)
        if (chapterDTO.index == 1) {
          // 第一章是所有测试的汇总地址，跳过
          continue
        }

        // 上面的内容还不够，我需要简化的标题和实际的习题网址
        // 所以需要用 selenium 打开网页，获取页面元素信息
        driver.get(chapterDTO.simpleUrl)
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
        val fullUrl = driver.currentUrl ?: ""
        println("index: ${chapterDTO.index}, title: $simpleTitle, url: ${driver.currentUrl}")
        result.add(
          chapterDTO.copy(
            simpleTitle = simpleTitle,
            fullUrl = fullUrl
          )
        )

      }
      return result
    }

    /**
     * 根据提供的 chapterIndex，获取该章下面所有的问题列表
     */
    fun getQuizList(
      driver: WebDriver,
      shortTimeout: Long,
      longTimeout: Long,
      chapterIndex: Int = 0
    ): List<QuizDTO> {

      println("正在获取第${chapterIndex}章问答题目")

      // 点击 Start Quiz 按钮
      clickButtonWithMultipleStrategies(
        driver, listOf(
          "CSS" to ".qmn_btn.mlw_qmn_quiz_link.mlw_next.mlw_custom_start"
        ), shortTimeout
      )

      while (true) {
        // 如果能找到 Next 按钮，就一直点 Next 按钮
        if (!clickButtonWithMultipleStrategies(
            driver, listOf(
              "CSS" to ".qmn_btn.mlw_qmn_quiz_link.mlw_next.mlw_custom_next"
            ), shortTimeout
          )
        ) {
          break
        }
      }

      // 点击 Submit 按钮
      clickButtonWithMultipleStrategies(
        driver, listOf(
          "CSS" to ".qsm-btn.qsm-submit-btn.qmn_btn"
        ), shortTimeout
      )

      // 等到答案出现
      try {

        WebDriverWait(driver, Duration.ofSeconds(longTimeout)).until(
          ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.qsm-results-page"))
        )

        // 记录问题
        val quizList = mutableListOf<QuizDTO>()
        val questions = driver.findElements(By.cssSelector("div.qmn_question_answer"))
        for (question in questions) {
          val questionText =
            question.findElement(By.cssSelector("span.qsm-result-question-title")).text
          val simpleOptions = mutableListOf<String>()
          question.findElements(By.cssSelector("span.qsm-text-simple-option")).forEach {
            simpleOptions.add(it.text)
          }
          val correctOption =
            question.findElement(By.cssSelector("span.qsm-text-correct-option")).text
          val explanation = question.text.split("\n").last().replace("Explanation: ", "")

          val options = mutableListOf<String>()
          options.addAll(simpleOptions)
          options.add(correctOption)
          options.sort()
          val correctOptionIndex = options.binarySearch(correctOption)

          quizList.add(
            QuizDTO(
              chapterIndex = chapterIndex,
              quizIndex = -1,
              question = questionText,
              options = options,
              correctOptionIndex = correctOptionIndex,
              explanation = explanation
            )
          )
        }
        println("获取第${chapterIndex}章数据成功")

        // 将 quizList 按照 question 排序
        quizList.sortBy { it.question }
        for (quizIndex in 0 until quizList.size) {
          // 把刚才没填的 quizIndex 填好
          quizList[quizIndex] = quizList[quizIndex].copy(quizIndex = quizIndex)
        }

        return quizList.toList()

      } catch (e: Exception) {
        println("获取第${chapterIndex}章数据失败!!!!!!!")
        return listOf()
      }

    }

    /**
     * 使用多种策略尝试点击按钮
     */
    fun clickButtonWithMultipleStrategies(
      driver: WebDriver,
      strategies: List<Pair<String, String>>,
      timeoutSeconds: Long = 10
    ): Boolean {
      for ((method, selector) in strategies) {
        val by = when (method.uppercase()) {
          "ID" -> By.id(selector)
          "XPATH" -> By.xpath(selector)
          "CSS" -> By.cssSelector(selector)
          "CLASS" -> By.className(selector)
          "NAME" -> By.name(selector)
          else -> continue
        }

        if (clickDynamicButton(driver, by, timeoutSeconds)) {
          return true
        }
      }
      // println("所有定位策略都失败了")
      return false
    }

    /**
     * 点击动态加载的按钮
     *
     * @param driver WebDriver实例
     * @param by 定位方式
     * @param timeoutSeconds 最大等待时间(秒)，默认为10秒
     * @return 成功点击返回true，否则返回false
     */
    fun clickDynamicButton(driver: WebDriver, by: By, timeoutSeconds: Long = 10): Boolean {
      return try {
        val wait = WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
        val button = wait.until(ExpectedConditions.elementToBeClickable(by))
        button.click()
        // println("成功点击按钮: $by")
        true
      } catch (e: Exception) {
        // println("点击按钮失败: ${e.message}, by = $by")
        false
      }
    }
  }
}