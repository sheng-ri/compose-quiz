package cn.hellozjf.project.composequiz.util

/**
 * 和章节题目有关的常量
 */
class QuizConstant {
  companion object {
    val PATH_EN = "csv/chapter_quiz.en.csv"
    val PATH_ZH = "csv/chapter_quiz.zh.csv"
    val CHAPTER_INDEX = "章节序号"      // 实际书中的章节序号
    val QUIZ_INDEX = "题目序号"         // 从0开始的题目序号
    val QUESTION = "问题"
    val OPTIONS = "选项"              // JSON 数组字符串
    val CORRECT_OPTION_INDEX = "正确选项序号"   // 从0开始的选项序号
    val EXPLANATION = "解释"
  }
}