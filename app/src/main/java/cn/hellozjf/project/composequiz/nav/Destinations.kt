package cn.hellozjf.project.composequiz.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destinations(
  val label: String,
  val contentDescription: String,
  val icon: ImageVector
) {
  HOME("Home", "Home Screen", Icons.Default.Home),
  CONTACTS("Contacts", "Contects screen", Icons.Default.Face),
  FAVORITES("Favorites", "Favorites screen", Icons.Default.Favorite),
}