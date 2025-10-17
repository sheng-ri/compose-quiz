package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import cn.hellozjf.project.composequiz.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
  title: String,
  toggleLanguage: suspend () -> Unit,
  language: String,
) {

  val coroutineScope = rememberCoroutineScope()

  TopAppBar(
    title = {
      Text(
        text = title,
        fontWeight = FontWeight.Bold
      )
    },
    navigationIcon = {
      IconButton(onClick = { /* 处理导航菜单点击 */ }) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = "导航菜单"
        )
      }
    },
    actions = {
      Row(
        modifier = Modifier.clickable {
          coroutineScope.launch {
            toggleLanguage()
          }
        }
      ) {
        Icon(
          painter = painterResource(R.drawable.outline_language_24),
          contentDescription = "语言"
        )
        Text(
          text = language
        )
      }
    }
  )
}