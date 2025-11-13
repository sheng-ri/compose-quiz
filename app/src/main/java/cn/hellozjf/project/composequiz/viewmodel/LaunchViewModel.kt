package cn.hellozjf.project.composequiz.viewmodel

import androidx.lifecycle.ViewModel
import cn.hellozjf.project.composequiz.eventbus.NavigationEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
  private val eventBus: NavigationEventBus
) : ViewModel() {

  // 暴露事件流给 compose
  val navigationEvents = eventBus.events
}