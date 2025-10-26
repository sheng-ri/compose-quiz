package cn.hellozjf.project.composequiz

import cn.hellozjf.project.composequiz.util.ChapterConstant
import cn.hellozjf.project.composequiz.util.ChapterQuizConstant
import cn.hellozjf.project.composequiz.util.PdfUtils
import cn.hellozjf.project.composequiz.util.SeleniumUtils
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.time.Duration

/**
 * 演示如何在 Android 模块的本地单元测试 (JVM) 中使用 Selenium 打开 Google 首页。
 */
class SeleniumTest {

  // 声明 WebDriver 变量
  private lateinit var driver: WebDriver

  private val ZIP_RESOURCE_PATH =
    System.getProperty("user.dir")!! + "\\..\\other\\driver\\chromedriver.zip"
  private val TARGET_DIR = System.getProperty("java.io.tmpdir")!! + "\\.chrome_driver"
  private val DRIVER_FILE_NAME = "chromedriver.exe"

  /**
   * 在每个测试方法运行前执行 (对应 JUnit 4 的 @Before)。
   */
  @Before
  fun setUp() {

    // 解压 ChromeDriver
    val chromeDriverPath = ChromeDriverExtractor.extractChromeDriverFromFilepath(
      zipFilePath = ZIP_RESOURCE_PATH,
      targetDir = TARGET_DIR,
      driverFileName = DRIVER_FILE_NAME
    )

    // 1. 设置 WebDriver 系统属性
    // 如果您使用 Selenium 4.6+ 并信任 Selenium Manager 自动管理驱动，可以省略这行。
    // 如果需要手动指定路径：
    System.setProperty("webdriver.chrome.driver", chromeDriverPath)

    println("正在初始化 ChromeDriver...")

    // 2. 配置 Chrome 选项
    val options = ChromeOptions()
    options.addArguments("--proxy-server=socks5://127.0.0.1:1080")
    // options.addArguments("--headless") // 可选：如果不需要界面显示，请取消注释

    // 3. 创建 ChromeDriver 实例
    driver = ChromeDriver(options)

    // 4. 最大化窗口（可选）
    driver.manage().window().maximize()
    println("ChromeDriver 初始化成功。")
  }

  @Test
  fun readPdf() {
    println("正在获取PDF章节信息")
    val allChapterInfoList = PdfUtils.getAllChapterInfoList()
    for (chapterInfo in allChapterInfoList) {
      println(chapterInfo)
    }
  }

  @Test
  fun readPdf2() {
    val chapterInfoList = PdfUtils.getAllChapterInfoList()
    val fullChapterInfoList = SeleniumUtils.getAllChapterInfoList(
      chapterInfoList = chapterInfoList,
      driver = driver,
      timeoutSeconds = 10L
    )
    for (fullChapterInfo in fullChapterInfoList) {
      println(fullChapterInfo)
    }
  }

  @Test
  fun readPdfAndWriteExcel() {

    val timeoutSeconds = 10L

    // 把 chapterInfoList 写入到 excel 中
    val title = listOf("章节号", "章节标题", "简化标题", "习题网址", "实际网址")

    val chapterInfoList = PdfUtils.getAllChapterInfoList()
    val fullChapterInfoList =
      SeleniumUtils.getAllChapterInfoList(chapterInfoList, driver, timeoutSeconds)
    val dataList = fullChapterInfoList.map {
      listOf(
        it.index.toString(),
        it.title,
        it.simpleTitle,
        it.url,
        it.actualUrl
      )
    }
    val excelTest = ExcelTest()
    excelTest.writeToExcel(title, dataList)
  }

  /**
   * 读取每章的URL，从URL中提取该章节所有题目，并写入CSV中
   */
  @Test
  fun readChapterAndWriteQuizToCsv() {

    val chapterQuizList: MutableList<List<Quiz>> = mutableListOf()

    // 首先把 ChapterQuizConstant.PATH 文件变成一个章节列表，在这个文件中出现的章节，后面就不用打开URL搜索题库了
    val chatperSet = mutableSetOf<Int>()
    FileReader("src/main/assets/${ChapterQuizConstant.PATH_EN}").use { reader ->
      val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

      val quizList = mutableListOf<Quiz>()
      for (record in csvParser) {
        val chapterIndex = record.get(ChapterQuizConstant.CHAPTER_INDEX).toInt()
        val question = record.get(ChapterQuizConstant.QUESTION)
        val correctOption = record.get(ChapterQuizConstant.CORRECT_OPTION)
        val wrongOption1 = record.get(ChapterQuizConstant.WRONG_OPTION1)
        val wrongOption2 = record.get(ChapterQuizConstant.WRONG_OPTION2)
        val wrongOption3 = record.get(ChapterQuizConstant.WRONG_OPTION3)
        val explanation = record.get(ChapterQuizConstant.EXPLANATION)

        chatperSet.add(chapterIndex)
        quizList.add(
          Quiz(
            chapterIndex = chapterIndex,
            question = question,
            correctOption = correctOption,
            wrongOptions = listOf(wrongOption1, wrongOption2, wrongOption3),
            explanation = explanation
          )
        )
      }
      if (quizList.isNotEmpty()) {
        chapterQuizList.add(quizList)
      }
    }

    // 读取章节 CSV，然后依次打开每章 URL，读取该章下面的题目
    try {
      FileReader("src/main/assets/${ChapterConstant.PATH_EN}").use { reader ->
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        for (record in csvParser) {
          val index = record.get(ChapterConstant.INDEX).toInt()
          if (chatperSet.contains(index)) {
            continue
          }
          val fullUrl = record.get(ChapterConstant.FULL_URL)
          driver.get(fullUrl)
          val quizList = getQuizList(driver, 1, 10, index)
          chapterQuizList.add(quizList)
        }

      }
    } catch (e: IOException) {
      e.printStackTrace()
    }

    // 将所有章节下面的所有题目写入到 CSV 中
    FileWriter("src/main/assets/${ChapterQuizConstant.PATH_EN}").use { writer ->
      CSVPrinter(writer, CSVFormat.DEFAULT).use { printer ->
        val title = listOf(
          ChapterQuizConstant.CHAPTER_INDEX,
          ChapterQuizConstant.QUESTION,
          ChapterQuizConstant.CORRECT_OPTION,
          ChapterQuizConstant.WRONG_OPTION1,
          ChapterQuizConstant.WRONG_OPTION2,
          ChapterQuizConstant.WRONG_OPTION3,
          ChapterQuizConstant.EXPLANATION,
        )
        // 写入表头
        printer.printRecord(title)

        // 写入数据
        for (quizList in chapterQuizList) {
          for (quiz in quizList) {
            val data = listOf(
              quiz.chapterIndex,
              quiz.question,
              quiz.correctOption,
              quiz.wrongOptions[0],
              quiz.wrongOptions[1],
              quiz.wrongOptions[2],
              quiz.explanation
            )
            printer.printRecord(data)
          }
        }
      }
      println("CSV 文件写入完成！")
    }
  }

  /**
   * 读取 PDF，并且将章节目录写入到 CSV 中
   */
  @Test
  fun readPdfAndWriteCsv() {

    val timeoutSeconds = 10L

    // 把 chapterInfoList 写入到 excel 中
    val title = listOf(
      ChapterConstant.INDEX,
      ChapterConstant.FULL_TITLE,
      ChapterConstant.SIMPLE_TITLE,
      ChapterConstant.SIMPLE_URL,
      ChapterConstant.FULL_URL
    )
    val dataList = mutableListOf<List<String>>()

    val chapterInfoList = PdfUtils.getAllChapterInfoList()
    for (chapterInfo in chapterInfoList) {
      // println(chapterInfo)
      if (chapterInfo.index == 1) {
        // 第一章是所有测试的汇总地址，跳过
        continue
      }
      chapterInfo.url?.let {
        // 只记录有习题网址的章节
        // 好像书升级之后，原来1.7版本书中的一些章节在1.8版本没有了，但是在习题网站中依旧有1.7版本书中的章节测试
        // 这些仅在1.7版本书中的章节我就忽略了
//        val data = listOf(chapterInfo.number.toString(), chapterInfo.title, it)
//        dataList.add(data)

        // 上面的内容还不够，我需要简化的标题和实际的习题网址
        driver.get(it)
        WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds)).until(
          ExpectedConditions.presenceOfElementLocated(By.cssSelector("h1.has-text-align-center.alignwide.wp-block-post-title"))
        )
        // 获取本章简化标题
        val elements =
          driver.findElements(By.cssSelector("h1.has-text-align-center.alignwide.wp-block-post-title"))
        val title = elements[0].text
        println("index: ${chapterInfo.index}, title: $title, url: ${driver.currentUrl}")
        val data = listOf(
          chapterInfo.index.toString(),
          chapterInfo.title,
          title,
          it,
          driver.currentUrl ?: ""
        )
        dataList.add(data)
      }
    }
//     val excelTest = ExcelTest()
//     excelTest.writeToExcel(title, dataList)

    FileWriter("src/main/assets/${ChapterConstant.PATH_EN}").use { writer ->
      CSVPrinter(writer, CSVFormat.DEFAULT).use { printer ->
        // 写入表头
        printer.printRecord(title)

        // 写入数据
        for (data in dataList) {
          printer.printRecord(data)
        }
      }
      println("CSV 文件写入完成！")
    }
  }

  private fun getQuizList(
    driver: WebDriver,
    shortTimeout: Long,
    longTimeout: Long,
    chapterIndex: Int = 0
  ): List<Quiz> {
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
      val quizList = mutableListOf<Quiz>()
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

        quizList.add(
          Quiz(
            chapterIndex,
            questionText,
            simpleOptions.toList(),
            correctOption,
            explanation
          )
        )
      }
      println("获取第${chapterIndex}章数据成功")
      return quizList.toList()

    } catch (e: Exception) {
      println("获取第${chapterIndex}章数据失败!!!!!!!")
      return listOf()
    }

  }

  /**
   * 测试打开 Google 首页并验证标题 (对应 JUnit 4 的 @Test)。
   */
  @Test
  fun testOpenComposeQuizPage() {

    val timeoutSeconds = 10L

    val url = "https://www.answertopia.com/quizzes/compose-project-quiz/"
    println("正在打开 URL: $url")

    // 1. 打开 Google 首页
    driver.get(url)

    // 2. 获取页面标题
    val pageTitle = driver.title
    println("页面标题是: $pageTitle")

    // 点击 Start Quiz 按钮
    clickButtonWithMultipleStrategies(
      driver, listOf(
        "CSS" to ".qmn_btn.mlw_qmn_quiz_link.mlw_next.mlw_custom_start"
      ), timeoutSeconds
    )

    while (true) {
      // 如果能找到 Next 按钮，就一直点 Next 按钮
      if (!clickButtonWithMultipleStrategies(
          driver, listOf(
            "CSS" to ".qmn_btn.mlw_qmn_quiz_link.mlw_next.mlw_custom_next"
          ), timeoutSeconds
        )
      ) {
        break
      }
    }

    // 点击 Submit 按钮
    clickButtonWithMultipleStrategies(
      driver, listOf(
        "CSS" to ".qsm-btn.qsm-submit-btn.qmn_btn"
      ), timeoutSeconds
    )

    // 等到答案出现
    WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds)).until(
      ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.qsm-results-page"))
    )

    // 记录问题
    val quizList = mutableListOf<Quiz>()
    val questions = driver.findElements(By.cssSelector("div.qmn_question_answer"))
    for (question in questions) {
      val questionText = question.findElement(By.cssSelector("span.qsm-result-question-title")).text
      val simpleOptions = mutableListOf<String>()
      question.findElements(By.cssSelector("span.qsm-text-simple-option")).forEach {
        simpleOptions.add(it.text)
      }
      val correctOption = question.findElement(By.cssSelector("span.qsm-text-correct-option")).text
      val explanation = question.text.split("\n").last().replace("Explanation: ", "")

//      println("questionText = $questionText")
//      println("correctOption = $correctOption")
//      println("simpleOptions = $simpleOptions")
//      println("explanation = $explanation")

      quizList.add(
        Quiz(
          0,
          questionText,
          simpleOptions.toList(),
          correctOption,
          explanation
        )
      )
    }

    for (question in quizList) {
      // TODO 明天把这些写入到数据库中
      println("questionText = ${question.question}")
      println("correctOption = ${question.correctOption}")
      println("simpleOptions = ${question.wrongOptions}")
      println("explanation = ${question.explanation}")
      println()
    }
    Thread.sleep(60 * 60 * 1000)
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
   * 在每个测试方法运行后执行，用于清理资源 (对应 JUnit 4 的 @After)。
   */
  @After
  fun tearDown() {
    println("正在关闭浏览器...")
    // 退出 WebDriver
    driver.quit()
    println("浏览器已关闭。")
  }
}

data class Quiz(
  val chapterIndex: Int,
  val question: String,
  val wrongOptions: List<String>,
  val correctOption: String,
  val explanation: String
)