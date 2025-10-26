package cn.hellozjf.project.composequiz.util

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
import java.io.File

/**
 * PDF 工具
 */
class PdfUtils {

  companion object {

    /**
     * 书签信息类
     */
    private data class BookmarkContent(
      val index: String,      // 序号，最后带.
      val title: String,      // 书签标题，不带序号
      val pageNumber: Int,    // 页码
      val content: String,     // 页面内容
      val level: Int         // 书签层级
    )

    /**
     * 带有层级的书签信息类
     */
    private data class BookmarkLevelContent(
      val index: String,      // 序号，最后带.
      val title: String,      // 书签标题，不带序号
      val pageNumber: Int,
      val content: String,
      val children: List<BookmarkLevelContent>
    )

    data class ChapterInfo(
      val index: Int,
      val title: String,
      val url: String?
    )

    /**
     * 从 PDF 文件中抽取书签信息
     */
    private fun extractBookmarkContents(file: File): List<BookmarkContent> {
      // 加载 PDF 文件
      val document = PDDocument.load(file)
      val result = mutableListOf<BookmarkContent>()

      try {
        // 获取PDF的大纲（书签）
        val outline = document.documentCatalog.documentOutline
        if (outline != null) {
          // 递归处理所有书签
          result.addAll(
            processBookmarks(
              outline = outline,
              document = document,
              level = 0
            )
          )
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
      level: Int
    ): List<BookmarkContent> {

      var current: PDOutlineItem? = outline.firstChild

      val result = mutableListOf<BookmarkContent>()

      // 遍历当前层级的所有书签
      while (current != null) {

        // 获取标题
        val title = current.title

        // 获取书签对应的页码
        val pageNumber = getPageNumberFromBookmark(current, document)

        if (pageNumber != -1) {
          // 提取该页面的文本内容
          val content = extractPageContent(document, pageNumber)

          // 提取出 1. 或 1.1 或 1.1.1 这样的字符串
          val oldIndex = title.split(" ")[0]
          if (!oldIndex.contains(".")) {
            // 这不是一个有效的书签，跳过它
            current = current.nextSibling
            continue
          }
          val index = if (oldIndex.endsWith(".")) oldIndex else "$oldIndex."

          // 保存结果
          val bookmarkContent = BookmarkContent(
            index = index,
            title = title.substring(oldIndex.length + 1),
            level = level,
            pageNumber = pageNumber,
            content = content
          )
          // println("${bookmarkContent.index} ${bookmarkContent.title}")
          result.add(bookmarkContent)
        }

        // 递归处理子书签
        result.addAll(
          processBookmarks(
            outline = current,
            document = document,
            level = level + 1
          )
        )

        // 处理下一个兄弟书签
        current = current.nextSibling
      }

      return result.toList()
    }

    /**
     * 从书签获取页码
     */
    private fun getPageNumberFromBookmark(
      bookmark: PDOutlineItem,
      document: PDDocument
    ): Int {
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

    /**
     * 从目标对象获取页码
     */
    private fun getPageNumberFromDestination(
      destination: PDDestination,
      document: PDDocument
    ): Int {
      return when (destination) {

        // 命名目标
        is PDNamedDestination -> {
          val resolvedDest = document.documentCatalog.findNamedDestinationPage(destination)
          // 递归调用，处理解析后的目标
          getPageNumberFromDestination(resolvedDest, document)
        }

        is PDPageXYZDestination,
        is PDPageFitDestination,
        is PDPageFitHeightDestination,
        is PDPageFitWidthDestination -> {
          // 然后处理具体的子类
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
    private fun extractPageContent(
      document: PDDocument,
      pageNumber: Int
    ): String {
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

    /**
     * 将 List<BookmarkContent> 转换为 List<BookmarkLevelContent>
     */
    private fun convert(bookmarks: List<BookmarkContent>): List<BookmarkLevelContent> {

      // 首先，把合法的书签信息放到 map 中，key 是书签的序号，value 是书签信息
      val indexBookmarkMap = mutableMapOf<String, BookmarkContent>()
      for (bookmarkContent in bookmarks) {
        indexBookmarkMap.put(bookmarkContent.index, bookmarkContent)
      }

      // 获得结果
      return getBookmarkLevelContentList(
        indexBookmarkMap = indexBookmarkMap,
        parentIndex = ""
      )
    }

    private fun isAnswerTopiaUrl(url: String): Boolean {
      val regex = "^https://www\\.answertopia\\.com/[a-zA-Z0-9]{4}$".toRegex()
      return url.matches(regex)
    }

    /**
     * 将 List<BookmarkLevelContent> 转化为 List<ChapterInfo>
     */
    private fun convert2(
      bookmarkLevelContentList: List<BookmarkLevelContent>
    ): List<ChapterInfo> {
      val chapterInfoList = mutableListOf<ChapterInfo>()
      for (bookmarkLevelContent in bookmarkLevelContentList) {
        // 把 index 的最后一个 . 去掉，转换为数字
        val oldIndex = bookmarkLevelContent.index
        val index: Int = oldIndex.substring(0, oldIndex.length - 1).toInt()
        val title: String = bookmarkLevelContent.title
        var url: String? = null
        for (child in bookmarkLevelContent.children) {
          if (child.title.contains("Take the knowledge test")) {
            // 说明有章节测试
            val lines = child.content.split("\n")
            for (line in lines) {
              val trim = line.trim()
              if (isAnswerTopiaUrl(trim)) {
                url = trim
                break
              }
            }
            break
          }
        }
        chapterInfoList.add(ChapterInfo(index, title, url))
      }
      return chapterInfoList
    }

    private fun getBookmarkLevelContentList(
      indexBookmarkMap: Map<String, BookmarkContent>,
      parentIndex: String
    ): List<BookmarkLevelContent> {
      // 构造结果
      val result = mutableListOf<BookmarkLevelContent>()
      var index = 1
      while (true) {
        val bookmarkContentIndex = "${parentIndex}${index}."
        val bookmarkContent = indexBookmarkMap[bookmarkContentIndex]
        bookmarkContent?.let { content ->
          val bookmarkLevelContent = BookmarkLevelContent(
            index = content.index,
            title = content.title,
            pageNumber = content.pageNumber,
            content = content.content,
            children = getBookmarkLevelContentList(
              indexBookmarkMap = indexBookmarkMap,
              parentIndex = bookmarkContentIndex
            )
          )
          result.add(bookmarkLevelContent)
        } ?: run {
          break
        }
        index++
      }
      return result
    }

    /**
     * "D:\\hellozjf\\code\\gitee\\ComposeQuiz\\other\\book\\JetpackCompose1.8Essentials\\JetpackCompose1.8Essentials.pdf"
     */
    fun getAllChapterInfoList(
      path: String = "D:\\hellozjf\\code\\gitee\\ComposeQuiz\\other\\book\\JetpackCompose1.8Essentials\\JetpackCompose1.8Essentials.pdf"
    ): List<ChapterInfo> {
      val pdfFile = File(path)
//      println(1)
      val bookmarks = extractBookmarkContents(pdfFile)
//      println(2)
      val bookmarkLevelContentList = convert(bookmarks)
//      println(3)
      val chapterInfoList = convert2(bookmarkLevelContentList)
//      println(4)
      return chapterInfoList
    }
  }
}