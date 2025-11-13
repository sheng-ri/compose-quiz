package cn.hellozjf.project.composequiz.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import cn.hellozjf.project.composequiz.eventbus.NavigationEvent
import cn.hellozjf.project.composequiz.nav.MainScreenKey
import cn.hellozjf.project.composequiz.viewmodel.LaunchViewModel

@Composable
fun LaunchScreen(
  onNavigation: (NavKey) -> Unit,
  viewModel: LaunchViewModel = hiltViewModel()
) {

  LaunchedEffect(Unit) {
    viewModel.navigationEvents.collect { event ->
      when (event) {
        is NavigationEvent.DatabaseCreated -> {
          onNavigation(MainScreenKey)
        }

        else -> {

        }
      }
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
  ) { innerPadding ->
    Box(
      modifier = Modifier.padding(innerPadding),
      contentAlignment = Alignment.Center
    ) {
      Text("App 启动中……")
    }
  }
}