package cn.hellozjf.project.composequiz.dto

data class ChapterDTO(
  val id: Int = 0,
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
}