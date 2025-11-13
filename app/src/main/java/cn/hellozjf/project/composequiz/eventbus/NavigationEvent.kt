package cn.hellozjf.project.composequiz.eventbus

sealed class NavigationEvent {
  object DatabaseCreated : NavigationEvent()
  object DatabaseMigrationNeeded : NavigationEvent()
  data class DatabaseError(val message: String) : NavigationEvent()
}