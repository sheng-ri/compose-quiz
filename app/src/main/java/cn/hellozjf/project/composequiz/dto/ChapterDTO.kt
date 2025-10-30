package cn.hellozjf.project.composequiz.dto

import cn.hellozjf.project.composequiz.database.entity.ChapterEn
import cn.hellozjf.project.composequiz.database.entity.ChapterZh

data class ChapterDTO(
  val index: Int = 0,
  val fullTitle: String = "",
  val simpleTitle: String = "",
  val simpleUrl: String = "",
  val fullUrl: String = "",
) {

  /**
   * 生成用于 Excel 或 CSV 上面的数据行
   */
  fun toDataRow(): List<String> {
    return listOf(
      index.toString(),
      fullTitle,
      simpleTitle,
      simpleUrl,
      fullUrl
    )
  }

  fun toChapterEn(): ChapterEn {
    return ChapterEn(
      index = index,
      fullTitle = fullTitle,
      simpleTitle = simpleTitle,
      simpleUrl = simpleUrl,
      fullUrl = fullUrl
    )
  }

  fun toChapterZh(): ChapterZh {
    return ChapterZh(
      index = index,
      fullTitle = fullTitle,
      simpleTitle = simpleTitle,
      simpleUrl = simpleUrl,
      fullUrl = fullUrl
    )
  }
}