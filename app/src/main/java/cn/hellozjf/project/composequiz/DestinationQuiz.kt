package cn.hellozjf.project.composequiz

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Today
import androidx.compose.ui.graphics.vector.ImageVector

enum class DestinationQuiz(
  val label: String,
  val contentDescription: String,
  val icon: ImageVector
) {
  CHAPTER_QUIZ("章节测试", "Chapter Quiz Screen", Icons.Default.Book),
  DAILY_QUIZ("每日测试", "Daily Quiz Screen", Icons.Default.Today),
  FAVORITE_QUIZ("收藏测试", "Favorite Quiz Screen", Icons.Default.Favorite),
}