package cn.hellozjf.project.composequiz

import cn.hellozjf.project.composequiz.database.converter.Converters
import cn.hellozjf.project.composequiz.dto.QuizDTO
import cn.hellozjf.project.composequiz.util.ChapterConstant
import cn.hellozjf.project.composequiz.util.ChapterQuizConstant
import cn.hellozjf.project.composequiz.util.ChapterUtils
import cn.hellozjf.project.composequiz.util.CsvUtils
import cn.hellozjf.project.composequiz.util.ExcelUtils
import cn.hellozjf.project.composequiz.util.PdfUtils
import cn.hellozjf.project.composequiz.util.SeleniumUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.time.Duration

/**
 * 演示如何在 Android 模块的本地单元测试 (JVM) 中使用 Selenium 打开 Google 首页。
 */
class SeleniumTest {

  // 声明 WebDriver 变量
  private lateinit var driver: WebDriver

  private val defaultPdfFilePath =
    "D:\\hellozjf\\code\\gitee\\ComposeQuiz\\other\\book\\JetpackCompose1.8Essentials\\JetpackCompose1.8Essentials.pdf"

  /**
   * 在每个测试方法运行前执行 (对应 JUnit 4 的 @Before)。
   */
  @Before
  fun setUp() {

    // 解压 ChromeDriver
    val chromeDriverPath = ChromeDriverExtractor.extractChromeDriverFromFilepath(
      zipFilePath = SeleniumUtils.ZIP_RESOURCE_PATH,
      targetDir = SeleniumUtils.TARGET_DIR,
      driverFileName = SeleniumUtils.DRIVER_FILE_NAME
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

  /**
   * 从 PDF 文件中读取章节信息，并打印章节信息
   */
  @Test
  fun readPdfAndPrintSimple() {
    val chapterDTOList = PdfUtils.getChapterDTOList(
      file = File(defaultPdfFilePath)
    )
    for (chapterDTO in chapterDTOList) {
      println(chapterDTO)
    }
  }

  /**
   * 从 PDF 文件中读取章节信息，使用 selenium 完善章节信息，并打印章节信息
   */
  @Test
  fun readPdfAndPrintFull() {
    val chapterDTOList = ChapterUtils.getChapterDTOListFromPdfFile(
      file = File(defaultPdfFilePath),
      driver = driver
    )
    for (chapterDTO in chapterDTOList) {
      println(chapterDTO)
    }
  }

  /**
   * 从 PDF 文件中读取章节信息，使用 selenium 完善章节信息，并写入到 excel 文件中
   */
  @Test
  fun readPdfAndWriteExcel() {
    val chapterDTOList = ChapterUtils.getChapterDTOListFromPdfFile(
      file = File(defaultPdfFilePath),
      driver = driver
    )
    ExcelUtils.writeToExcel(
      file = File("output.xlsx"),
      header = ChapterUtils.getHeader(),
      dataList = chapterDTOList.map {
        it.toDataRow()
      }
    )
  }

  /**
   * 从 PDF 文件中读取章节信息，使用 selenium 完善章节信息，并写入到 csv 文件中
   */
  @Test
  fun readPdfAndWriteCsv() {
    val chapterDTOList = ChapterUtils.getChapterDTOListFromPdfFile(
      file = File(defaultPdfFilePath),
      driver = driver
    )
    CsvUtils.writeToCsv(
      file = File("src/main/assets/${ChapterConstant.PATH_EN}"),
      header = ChapterUtils.getHeader(),
      dataList = chapterDTOList.map {
        it.toDataRow()
      }
    )
  }

  /**
   * 读取每章的URL，从URL中提取该章节所有题目，并写入CSV中
   */
  @Test
  fun readChapterAndWriteQuizToCsv() {

    val chapterQuizList: MutableList<List<QuizDTO>> = mutableListOf()

    // 首先把 ChapterQuizConstant.PATH 文件变成一个章节列表
    // 在这个文件中出现的章节，后面就不用打开URL搜索题库了
    // 这么写是因为我读取题库的时候，有时候会被服务器拒绝，导致异常
    // 加了这段代码之后，就能跳过已经读过的题目了
    val chatperSet = mutableSetOf<Int>()
    // TODO 下面的代码记得加回来，不然没法断点重来了
//    FileReader("src/main/assets/${ChapterQuizConstant.PATH_EN}").use { reader ->
//      val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())
//
//      val quizList = mutableListOf<Quiz>()
//      for (record in csvParser) {
//        val chapterIndex = record.get(ChapterQuizConstant.CHAPTER_INDEX).toInt()
//        val question = record.get(ChapterQuizConstant.QUESTION)
//        val correctOption = record.get(ChapterQuizConstant.CORRECT_OPTION)
//        val wrongOption1 = record.get(ChapterQuizConstant.WRONG_OPTION1)
//        val wrongOption2 = record.get(ChapterQuizConstant.WRONG_OPTION2)
//        val wrongOption3 = record.get(ChapterQuizConstant.WRONG_OPTION3)
//        val explanation = record.get(ChapterQuizConstant.EXPLANATION)
//
//        chatperSet.add(chapterIndex)
//        quizList.add(
//          Quiz(
//            chapterIndex = chapterIndex,
//            question = question,
//            correctOption = correctOption,
//            wrongOptions = listOf(wrongOption1, wrongOption2, wrongOption3),
//            explanation = explanation
//          )
//        )
//      }
//      if (quizList.isNotEmpty()) {
//        chapterQuizList.add(quizList)
//      }
//    }

    // 读取章节 CSV，然后依次打开每章 URL，读取该章下面的题目
    val chapterList = CsvUtils.readChapterDTOListFromCsv(
      file = File("src/main/assets/${ChapterConstant.PATH_EN}")
    )
    for (chapter in chapterList) {
      if (chatperSet.contains(chapter.index)) {
        continue
      }
      driver.get(chapter.fullUrl)
      val quizList = SeleniumUtils.getQuizList(
        driver = driver,
        shortTimeout = 1,
        longTimeout = 10,
        chapterIndex = chapter.index
      )
      chapterQuizList.add(quizList)
    }

    // 将所有章节下面的所有题目写入到 CSV 中
    CsvUtils.writeToCsv(
      file = File("src/main/assets/${ChapterQuizConstant.PATH_EN}"),
      header = listOf(
        ChapterQuizConstant.CHAPTER_INDEX,
        ChapterQuizConstant.QUESTION,
        ChapterQuizConstant.OPTIONS,
        ChapterQuizConstant.CORRECT_OPTION_INDEX,
        ChapterQuizConstant.EXPLANATION,
      ),
      dataList = chapterQuizList
        // 首先把 MutableList<List<QuizDTO>> 平铺成 List<QuizDTO>
        .flatten()
        // 然后把 QuizDTO 转换成 List<String>
        .map {
          listOf(
            it.chapterIndex.toString(),
            it.question,
            Converters().fromList(it.options),
            it.correctOptionIndex.toString(),
            it.explanation
          )
        }
    )
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
}

data class Quiz(
  val chapterIndex: Int,
  val question: String,
  val wrongOptions: List<String>,
  val correctOption: String,
  val explanation: String
)