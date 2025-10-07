package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ContactsScreen(
  modifier: Modifier = Modifier,
  icon: ImageVector,
  contentDescription: String
) {
  Box(
    modifier = modifier.fillMaxSize()
  ) {
    Icon(
      imageVector = icon,
      contentDescription = contentDescription,
      tint = Color.Magenta,
      modifier = Modifier
        .align(Alignment.Center)
        .fillMaxSize()
    )
  }
}