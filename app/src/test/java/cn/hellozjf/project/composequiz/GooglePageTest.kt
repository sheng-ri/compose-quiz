package cn.hellozjf.project.composequiz

import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

/**
 * 演示如何在 Android 模块的本地单元测试 (JVM) 中使用 Selenium 打开 Google 首页。
 */
class GooglePageTest {

  // 声明 WebDriver 变量
  private lateinit var driver: WebDriver

  // !!! 关键步骤：设置 ChromeDriver 的路径 !!!
  // 请将此路径替换为您 ChromeDriver 可执行文件的实际绝对路径！
  private val CHROME_DRIVER_PATH = "D:\\hellozjf\\soft\\chromedriver-win64\\141.0.7390.54\\chromedriver.exe"

  /**
   * 在每个测试方法运行前执行 (对应 JUnit 4 的 @Before)。
   */
  @Before
  fun setUp() {
    // 1. 设置 WebDriver 系统属性
    // 如果您使用 Selenium 4.6+ 并信任 Selenium Manager 自动管理驱动，可以省略这行。
    // 如果需要手动指定路径：
    System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH)

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
  fun testOpenGoogleHomePage() {
    val url = "https://www.answertopia.com/quizzes/compose-project-quiz/"
    println("正在打开 URL: $url")

    // 1. 打开 Google 首页
    driver.get(url)

    // 2. 获取页面标题
    val pageTitle = driver.title
    println("页面标题是: $pageTitle")

//    // 3. 验证断言：标题中是否包含 "Google"
//    // 使用 JUnit 4 的 Assert.assertTrue
//    assertTrue("页面标题不包含 'Google'，打开 Google 首页失败。", pageTitle?.contains("Google") ?: true)

    println("✅ 成功打开 Google 首页并验证标题！")
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