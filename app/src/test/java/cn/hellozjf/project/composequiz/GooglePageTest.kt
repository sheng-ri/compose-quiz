package cn.hellozjf.project.composequiz

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

/**
 * 演示如何在 Android 模块的本地单元测试 (JVM) 中使用 Selenium 打开 Google 首页。
 */
class GooglePageTest {

  // 声明 WebDriver 变量
  private lateinit var driver: WebDriver

  // !!! 关键步骤：设置 ChromeDriver 的路径 !!!
  // 请将此路径替换为您 ChromeDriver 可执行文件的实际绝对路径！
  private val CHROME_DRIVER_PATH =
    "D:\\hellozjf\\soft\\chromedriver-win64\\141.0.7390.54\\chromedriver.exe"

  private val ZIP_RESOURCE_PATH =
    System.getProperty("user.dir") + "\\..\\other\\driver\\chromedriver.zip"
  private val TARGET_DIR = System.getProperty("java.io.tmpdir") + "\\.chrome_driver"
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
    // options.addArguments("--headless") // 可选：如果不需要界面显示，请取消注释

    // 3. 创建 ChromeDriver 实例
    driver = ChromeDriver(options)

    // 4. 最大化窗口（可选）
    driver.manage().window().maximize()
    println("ChromeDriver 初始化成功。")
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
    val questionList = mutableListOf<Question>()
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

      questionList.add(Question(questionText, simpleOptions.toList(), correctOption, explanation))
    }

    for (question in questionList) {
      // TODO 明天把这些写入到数据库中
      println("questionText = ${question.question}")
      println("correctOption = ${question.correctOption}")
      println("simpleOptions = ${question.simpleOptions}")
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
      println("成功点击按钮: $by")
      true
    } catch (e: Exception) {
      println("点击按钮失败: ${e.message}")
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
    println("所有定位策略都失败了")
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

data class Question(
  val question: String,
  val simpleOptions: List<String>,
  val correctOption: String,
  val explanation: String
)