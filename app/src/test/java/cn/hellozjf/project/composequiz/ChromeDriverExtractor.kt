package cn.hellozjf.project.composequiz

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class ChromeDriverExtractor {
  companion object {
    /**
     * 从 classpath 解压 ChromeDriver 到目标目录
     */
    fun extractChromeDriverFromFilepath(
      zipFilePath: String,
      targetDir: String,
      driverFileName: String = "chromedriver"
    ): String {
      val targetPath = Paths.get(targetDir, driverFileName).toString()

      // 如果驱动已存在，直接返回
      if (File(targetPath).exists()) {
        println("ChromeDriver 已存在: $targetPath")
        return targetPath
      }

      // 确保目标目录存在
      Files.createDirectories(Paths.get(targetDir))

      // 从 classpath 获取资源
      val inputStream = FileInputStream(zipFilePath)

      // 解压 ZIP 文件
      extractZip(inputStream, targetDir, driverFileName)

      // 给执行权限 (Linux/Mac)
      setExecutablePermission(targetPath)

      println("成功解压 ChromeDriver 到: $targetPath")
      return targetPath
    }

    private fun extractZip(inputStream: InputStream, targetDir: String, driverFileName: String) {
      ZipInputStream(BufferedInputStream(inputStream)).use { zipIn ->
        var entry: ZipEntry?
        while (zipIn.nextEntry.also { entry = it } != null) {
          val entryName = entry!!.name

          // 只解压 chromedriver 文件
          if (entryName.contains(driverFileName) && !entry!!.isDirectory) {
            val outputPath = Paths.get(targetDir, File(entryName).name)
            extractFile(zipIn, outputPath)
            break
          }
        }
      }
    }

    private fun extractFile(zipIn: ZipInputStream, outputPath: Path) {
      BufferedOutputStream(FileOutputStream(outputPath.toFile())).use { bos ->
        val bytesIn = ByteArray(4096)
        var read: Int
        while (zipIn.read(bytesIn).also { read = it } != -1) {
          bos.write(bytesIn, 0, read)
        }
      }
    }

    private fun setExecutablePermission(filePath: String) {
      if (!System.getProperty("os.name").lowercase().contains("windows")) {
        File(filePath).setExecutable(true)
      }
    }
  }
}