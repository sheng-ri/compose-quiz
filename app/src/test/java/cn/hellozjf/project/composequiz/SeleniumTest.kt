package cn.hellozjf.project.composequiz

import cn.hellozjf.project.composequiz.util.ChapterUtils
import cn.hellozjf.project.composequiz.util.ChromeDriverUtils
import cn.hellozjf.project.composequiz.util.ExcelUtils
import cn.hellozjf.project.composequiz.util.QuizUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File

/**
 * 演示如何在 Android 模块的本地单元测试 (JVM) 中使用 Selenium 打开 Google 首页。
 */
class SeleniumTest {

  // 声明 WebDriver 变量
  private lateinit var driver: WebDriver

  /**
   * 在每个测试方法运行前执行 (对应 JUnit 4 的 @Before)。
   */
  @Before
  fun setUp() {

    // 解压 ChromeDriver
    val chromeDriverPath = ChromeDriverUtils.extractChromeDriverFromFilepath()

    // 1. 设置 WebDriver 系统属性
    // 如果您使用 Selenium 4.6+ 并信任 Selenium Manager 自动管理驱动，可以省略这行。
    // 如果需要手动指定路径：
    System.setProperty(ChromeDriverUtils.PROPERTY_CHROME_DRIVER, chromeDriverPath)

    println("正在初始化 ChromeDriver...")

    // 2. 配置 Chrome 选项
    val options = ChromeOptions()
    options.addArguments(ChromeDriverUtils.PROXY_SERVER)
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
    val simpleChapterDTOList = ChapterUtils.getSimpleChapterDTOListFromPdfFile()
    for (chapterDTO in simpleChapterDTOList) {
      println(chapterDTO)
    }
  }

  /**
   * 从 PDF 文件中读取章节信息，使用 selenium 完善章节信息，并打印章节信息
   */
  @Test
  fun readPdfAndPrintFull() {
    val chapterDTOList = ChapterUtils.getChapterDTOListFromPdfFile(
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
      driver = driver
    )
    ExcelUtils.writeToExcel(
      file = File(ChapterUtils.defaultExcelFilePath),
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
  fun readPdfAndWriteChapterToCsv() {
    val chapterDTOList = ChapterUtils.getChapterDTOListFromPdfFile(
      driver = driver
    )
    ChapterUtils.writeChapterDTOListToCsv(
      file = File(ChapterUtils.defaultEnCsvFilePath),
      chapterDTOList = chapterDTOList
    )
  }

  /**
   * 读取每章的URL，从URL中提取该章节所有题目，并写入CSV中
   * 如果题目CSV文件已存在，会自动跳过已读过的章节
   */
  @Test
  fun readChapterAndWriteQuizToCsv() {

    // 首先把 ChapterQuizConstant.PATH_EN 文件变成一个章节列表
    // 在这个文件中出现的章节，后面就不用打开URL搜索题库了
    // 这么写是因为我读取题库的时候，有时候会被服务器拒绝，导致异常
    // 加了这段代码之后，就能跳过已经读过的题目了
    val readedQuizDTOList = QuizUtils.getQuizDTOListFromCsv()
    val skipChapterIndexSet = readedQuizDTOList
      .map { it.chapterIndex }
      .toSet()

    // 读取章节 CSV，然后依次打开每章 URL，读取该章下面的题目
    val chapterQuizDTOListMap = QuizUtils.getChapterQuizDTOListFromNetwork(
      driver = driver,
      skipChapterIndexSet = skipChapterIndexSet
    )

    // 所有题目包含已经读过的题目，以及刚才读取的题目
    val dataList = readedQuizDTOList + chapterQuizDTOListMap.values.flatten()
    val sortedDataList = dataList.sortedWith(
      compareBy({ it.chapterIndex }, { it.quizIndex })
    )
    // 将所有章节下面的所有题目写入到 CSV 中
    QuizUtils.writeQuizDTOListToCsv(
      file = File(QuizUtils.defaultEnCsvFilePath),
      quizDTOList = sortedDataList
    )
    // 将所有章节下面的所有题目写入到 EXT CSV 中
    QuizUtils.writeQuizDTOListToExtCsv(
      file = File(QuizUtils.defaultExtCsvFilePath),
      quizDTOList = sortedDataList
    )
  }

  @Test
  fun test() {
    val property = System.getenv("JAVA_HOME")
    println(property)
  }
}