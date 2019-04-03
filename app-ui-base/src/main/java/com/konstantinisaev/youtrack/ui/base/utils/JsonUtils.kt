package com.konstantinisaev.youtrack.ui.base.utils

import com.google.gson.JsonElement
import timber.log.Timber

object ObjectMapper {

	fun mapCustomFieldFromJson(jsonElement: JsonElement?): ParsedValueOfEnumField {
		return try {
			jsonElement?.takeIf { it.isJsonObject }?.asJsonObject?.let {
				val issueCustomField = ParsedValueOfEnumField(
					it["name"]?.asString.orEmpty(),
					getColor(it)
				)
				issueCustomField
			} ?: ParsedValueOfEnumField("", Pair("", ""))
		} catch (ex: Exception) {
			Timber.e(ex)
			ParsedValueOfEnumField("", Pair("", ""))
		}
	}

	fun mapPeriodicFieldFromJson(jsonElement: JsonElement?): ParsedValueOfTimeField {
		var minutes = 0
		var presentation = ""
		try {
			jsonElement?.takeIf { it.isJsonObject }?.asJsonObject?.let {
				minutes = it["minutes"]?.asString?.toIntOrNull() ?: 0
				presentation = it["presentation"]?.asString.orEmpty()
			}
		} catch (ex: Exception) {
			Timber.e(ex)
		}
		return ParsedValueOfTimeField(minutes = minutes, presentation = presentation)
	}

	fun mapUserFieldFromJson(jsonElement: JsonElement?): ParsedValueOfUserField {
		return try {
			val name = jsonElement?.takeIf { it.isJsonObject }?.asJsonObject?.get("name")?.asString.orEmpty()
			val avatarUrl = jsonElement?.takeIf { it.isJsonObject }?.asJsonObject?.get("avatarUrl")?.asString.orEmpty()
			ParsedValueOfUserField(name, avatarUrl)
		} catch (ex: Exception) {
			Timber.e(ex)
			ParsedValueOfUserField("", "")
		}
	}

	private fun getColor(jsonElement: JsonElement?): Pair<String, String> {
		val jsonColor = jsonElement?.asJsonObject?.get("color")
		var pairColor = Pair("", "")
		jsonColor?.asJsonObject?.let {
			var background = it.get("background").asString
			var foreground = it.get("foreground").asString
			if (background.length == 4) {
				background = "#${background.substring(1, 4)}${background.substring(1, 4)}"
			}
			if (foreground.length == 4) {
				foreground = "#${foreground.substring(1, 4)}${foreground.substring(1, 4)}"
			}
			pairColor = Pair(background, foreground)
		}
		return pairColor
	}

}
data class ParsedValueOfEnumField(val name: String, val colors: Pair<String,String>)
data class ParsedValueOfUserField(val name: String, val avatarUrl: String)
data class ParsedValueOfTimeField(val minutes: Int, val presentation: String)