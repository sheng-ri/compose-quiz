package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults.rememberTooltipPositionProvider
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
  val tooltipState = rememberTooltipState()
  val positionProvider = rememberTooltipPositionProvider(TooltipAnchorPosition.Above)

  TopAppBar(
    title = {
      TooltipBox(
        positionProvider = positionProvider,
        tooltip = {
          PlainTooltip {
            Text(text = title)
          }
        },
        state = tooltipState
      ) {
        Text(
          text = title,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.clickable {
            coroutineScope.launch {
              tooltipState.show()
            }
          }
        )
      }
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
        },
        verticalAlignment = Alignment.CenterVertically
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