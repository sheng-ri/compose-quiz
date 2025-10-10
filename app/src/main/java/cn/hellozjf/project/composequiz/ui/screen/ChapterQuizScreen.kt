package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel

/**
 * 章节题目
 */
@Composable
fun ChapterQuizScreen(
  chapterIndex: Int,
  chapterViewModel: ChapterViewModel,
  chapterQuizViewModel: ChapterQuizViewModel,
  onNavigation: (NavKey) -> Unit
) {

  Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    Box(
      modifier = Modifier.padding(innerPadding)
    ) {
      Text(
        text = "第 $chapterIndex 章"
      )
    }
  }

//  // TODO 这里要从 chapterQuizViewModel 获取所有的章节，显示在列表中
//  // TODO 点击章节的时候，使用 chapterQuizViewModel 查询该章节下面所有的题目，进行问答测试
//
//  val searchResults by chapterViewModel.searchResults.observeAsState(listOf())
//  val listState = rememberLazyListState()
//
//  LaunchedEffect(key1 = Unit) {
//    chapterViewModel.findAllOrderByIndex()
//  }
//
//  LazyColumn(
//    modifier = modifier.fillMaxSize(),
//    state = listState
//  ) {
//    searchResults.forEach { chapter ->
//      item {
//        ChapterListItem(
//          index = chapter.index,
//          simpleTitle = chapter.simpleTitle,
//          onItemClick = { index ->
//            // TODO 点击进入章节测试题
//          }
//        )
//      }
//    }
//  }
}

//@Composable
//fun ChapterListItem(
//  index: Int,
//  simpleTitle: String,
//  onItemClick: (Int) -> Unit,
//  modifier: Modifier = Modifier
//) {
//  Card(
//    colors = CardDefaults.cardColors(
//      containerColor = MaterialTheme.colorScheme.onPrimary
//    ),
//    modifier = modifier
//      .padding(3.dp)
//      .fillMaxWidth()
//      .clickable {
//        onItemClick(index)
//      },
//    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
//  ) {
//    Row(
//      verticalAlignment = Alignment.CenterVertically
//    ) {
//      Text(
//        text = "第 $index 章",
//        modifier = Modifier.width(75.dp)
//      )
//      Spacer(modifier = Modifier.width(8.dp))
//      Text(
//        text = simpleTitle,
//        style = MaterialTheme.typography.headlineSmall,
//        modifier = Modifier.padding(8.dp)
//      )
//    }
//  }
//}