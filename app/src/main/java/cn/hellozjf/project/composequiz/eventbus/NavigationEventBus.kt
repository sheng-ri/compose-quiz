package cn.hellozjf.project.composequiz.eventbus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationEventBus @Inject constructor() {
  private val _events = MutableSharedFlow<NavigationEvent>()
  val events = _events.asSharedFlow()

  suspend fun sendEvent(event: NavigationEvent) {
    _events.emit(event)
  }
}