package com.example.speechi18n

import android.content.Context
import org.json.JSONObject
import java.util.Locale

enum class VoiceType(val key: String) {
    DEFAULT("default"),
    MALE("male"),
    FEMALE("female"),
    CHILD("child")
}

class SpeechTextRepository private constructor(
    private val defaultLocale: String,
    private val aliases: Map<String, String>,
    private val texts: Map<String, Map<String, Map<String, String>>>
) {

    fun get(
        text: String,
        voiceType: VoiceType,
        locale: Locale = Locale.getDefault()
    ): String {
        val normalized = normalizeText(text)
        val canonical = aliases[normalized] ?: normalized
        val entry = texts[canonical] ?: return text

        val voiceMap = entry[voiceType.key] ?: entry[VoiceType.DEFAULT.key] ?: return text
        val localeTag = locale.toLanguageTag()

        return voiceMap[localeTag]
            ?: voiceMap[locale.language]
            ?: voiceMap[defaultLocale]
            ?: text
    }

    private fun normalizeText(text: String): String {
        return text.trim().replace(Regex("\\s+"), " ")
    }

    companion object {
        fun loadFromAssets(
            context: Context,
            assetName: String = "speech_i18n_sample.json"
        ): SpeechTextRepository {
            val json = context.assets.open(assetName).bufferedReader().use { it.readText() }
            return parse(json)
        }

        fun parse(json: String): SpeechTextRepository {
            val root = JSONObject(json)
            val defaultLocale = root.optString("defaultLocale", "zh-CN")
            val aliases = jsonObjectToMap(root.optJSONObject("aliases"))
            val texts = mutableMapOf<String, Map<String, Map<String, String>>>()

            val textsObj = root.getJSONObject("texts")
            for (textKey in textsObj.keys()) {
                val typeObj = textsObj.getJSONObject(textKey)
                val typeMap = mutableMapOf<String, Map<String, String>>()
                for (typeKey in typeObj.keys()) {
                    val localeObj = typeObj.getJSONObject(typeKey)
                    typeMap[typeKey] = jsonObjectToMap(localeObj)
                }
                texts[textKey] = typeMap
            }

            return SpeechTextRepository(defaultLocale, aliases, texts)
        }

        private fun jsonObjectToMap(obj: JSONObject?): Map<String, String> {
            if (obj == null) return emptyMap()
            val map = mutableMapOf<String, String>()
            for (key in obj.keys()) {
                map[key] = obj.getString(key)
            }
            return map
        }
    }
}
