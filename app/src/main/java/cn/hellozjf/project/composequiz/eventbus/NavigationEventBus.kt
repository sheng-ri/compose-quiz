package cn.hellozjf.project.composequiz.eventbus

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationEventBus @Inject constructor() {
  private val _events = MutableSharedFlow<NavigationEvent>(
    replay = 1, // 新订阅者收到最近1个事件
    extraBufferCapacity = 10,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
  )
  val events = _events.asSharedFlow()

  suspend fun sendEvent(event: NavigationEvent) {
    _events.emit(event)
  }
}