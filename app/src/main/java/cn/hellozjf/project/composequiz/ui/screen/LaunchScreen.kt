package cn.hellozjf.project.composequiz.ui.screen

import android.util.Log
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
import cn.hellozjf.project.composequiz.eventbus.NavigationEvent
import cn.hellozjf.project.composequiz.viewmodel.LaunchViewModel

private val TAG = "LaunchScreen"

@Composable
fun LaunchScreen(
  onOpenMainScreen: () -> Unit,
  viewModel: LaunchViewModel = hiltViewModel()
) {

  LaunchedEffect(Unit) {
    viewModel.navigationEvents.collect { event ->
      when (event) {
        is NavigationEvent.DatabaseOpened -> {
          Log.d(TAG, "receive NavigationEvent.DatabaseOpened")
          onOpenMainScreen()
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
      modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Text("App 启动中……")
    }
  }
}