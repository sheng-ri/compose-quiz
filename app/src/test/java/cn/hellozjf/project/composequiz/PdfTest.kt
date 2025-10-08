package cn.hellozjf.project.composequiz

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitHeightDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode
import org.apache.pdfbox.text.PDFTextStripper
import org.junit.Test
import java.io.File

class PdfTest {

  @Test
  fun test() {
    val pdfFile =
      "D:\\hellozjf\\code\\gitee\\ComposeQuiz\\other\\book\\JetpackCompose1.8Essentials\\JetpackCompose1.8Essentials.pdf"
    val bookmarks = extractBookmarksWithContent(pdfFile)
    bookmarks.forEach { bookmark ->
      println("标题: ${bookmark.title}")
      println("层级: ${bookmark.level}")
      println("页码: ${bookmark.pageNumber}")
      println("内容: ${bookmark.content}")
      println("---")
    }
  }

  // 定义数据类，用来存储书签信息
  data class BookmarkContent(
    val title: String,      // 书签标题
    val level: Int,         // 书签层级
    val pageNumber: Int,    // 页码
    val content: String     // 页面内容
  )

  // 主函数：读取PDF书签和内容
  fun extractBookmarksWithContent(filePath: String): List<BookmarkContent> {
    val document = PDDocument.load(File(filePath))
    val result = mutableListOf<BookmarkContent>()

    try {
      // 获取PDF的大纲（书签）
      val outline = document.documentCatalog.documentOutline
      if (outline != null) {
        // 递归处理所有书签
        processBookmarks(outline, document, result, 0)
      }
    } finally {
      document.close()
    }

    return result
  }

  // 递归处理书签
  private fun processBookmarks(
    outline: PDOutlineNode,
    document: PDDocument,
    result: MutableList<BookmarkContent>,
    level: Int
  ) {
    var current: PDOutlineItem? = outline.firstChild

    // 遍历当前层级的所有书签
    while (current != null) {
      val title = current.title

      // 获取书签对应的页码
      val pageNumber = getPageNumberFromBookmark(current, document)

      if (pageNumber != -1) {
        // 提取该页面的文本内容
        val content = extractPageContent(document, pageNumber)

        // 保存结果
        result.add(
          BookmarkContent(
            title = title,
            level = level,
            pageNumber = pageNumber,
            content = content
          )
        )
      }

      // 递归处理子书签
      if (current.firstChild != null) {
        processBookmarks(current, document, result, level + 1)
      }

      current = current.nextSibling
    }
  }

  // 从书签获取页码
  private fun getPageNumberFromBookmark(bookmark: PDOutlineItem, document: PDDocument): Int {
    return try {
      // 方法1：通过action获取（大部分PDF使用这种方式）
      val action = bookmark.action
      if (action is PDActionGoTo) {
        val destination = action.destination
        return getPageNumberFromDestination(destination, document)
      }

      // 方法2：直接获取destination（少数PDF使用这种方式）
      val destination = bookmark.destination
      if (destination != null) {
        return getPageNumberFromDestination(destination, document)
      }

      -1 // 无法获取页码
    } catch (e: Exception) {
      -1
    }
  }

  // 从目标对象获取页码
  private fun getPageNumberFromDestination(destination: PDDestination, document: PDDocument): Int {
    return when (destination) {

      // 命名目标
      is PDNamedDestination -> {
        val resolvedDest = document.documentCatalog.findNamedDestinationPage(destination)
        // 递归调用，处理解析后的目标
        getPageNumberFromDestination(resolvedDest, document)
//        if (resolvedDest != null) {
//          document.pages.indexOf(resolvedDest as org.apache.pdfbox.pdmodel.PDPage) + 1
//        } else {
//          -1
//        }
      }
      is PDPageXYZDestination -> {
        // 然后处理具体的子类
        if (destination.page != null) {
          document.pages.indexOf(destination.page) + 1
        } else {
          -1
        }
      }
      is PDPageFitDestination -> {
        if (destination.page != null) {
          document.pages.indexOf(destination.page) + 1
        } else {
          -1
        }
      }
      is PDPageFitHeightDestination -> {
        if (destination.page != null) {
          document.pages.indexOf(destination.page) + 1
        } else {
          -1
        }
      }
      is PDPageFitWidthDestination -> {
        if (destination.page != null) {
          document.pages.indexOf(destination.page) + 1
        } else {
          -1
        }
      }
      is PDPageDestination -> {
        // 最后处理通用的父类
        destination.pageNumber + 1
      }
      // 其他类型的目标
      else -> -1
    }
  }

  // 提取指定页面的文本内容
  private fun extractPageContent(document: PDDocument, pageNumber: Int): String {
    return try {
      val stripper = PDFTextStripper().apply {
        startPage = pageNumber
        endPage = pageNumber
      }
      stripper.getText(document).trim()
    } catch (e: Exception) {
      "无法提取页面内容: ${e.message}"
    }
  }
}