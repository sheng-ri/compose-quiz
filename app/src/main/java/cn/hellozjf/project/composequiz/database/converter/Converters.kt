package cn.hellozjf.project.composequiz.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

/**
 * TODO 这个也可以改成依赖注入
 */
class Converters {

  @TypeConverter
  fun fromString(value: String): List<String> {
    return Json.decodeFromString(value)
  }

  @TypeConverter
  fun fromList(list: List<String>): String {
    return Json.encodeToString(list)
  }
}