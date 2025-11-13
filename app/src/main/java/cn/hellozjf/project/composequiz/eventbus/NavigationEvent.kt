package cn.hellozjf.project.composequiz.eventbus

sealed class NavigationEvent {
  object DatabaseOpened : NavigationEvent()
  object DatabaseMigrationNeeded : NavigationEvent()
  data class DatabaseError(val message: String) : NavigationEvent()
}