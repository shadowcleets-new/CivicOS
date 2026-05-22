package com.nivar.app.data.model

// Enums for Language Support
// Enums for Language Support
enum class Language(val displayName: String) {
    ENG("English"),
    ENG_SIMPLE("English (Simple)"),
    HIN("Hindi"),
    HIN_SIMPLE("Hindi (Simple)"),
    GUJ("Gujarati"),
    GUJ_SIMPLE("Gujarati (Simple)"),
    TAM("Tamil"),
    TAM_SIMPLE("Tamil (Simple)"),
    ODI("Odia"),
    ODI_SIMPLE("Odia (Simple)")
}

data class ConstitutionArticle(
    val id: String,         // e.g., "Art14"
    val part: String,       // e.g., "Part III: Fundamental Rights"
    val title: String,      // e.g., "Equality before law"
    val content: Map<Language, String>
)

object ConstitutionRepository {
    private var _articles: List<ConstitutionArticle> = emptyList()
    val articles: List<ConstitutionArticle>
        get() = _articles

    fun loadArticles(context: android.content.Context) {
        if (_articles.isNotEmpty()) return

        try {
            val jsonString = context.assets.open("constitution.json").bufferedReader(Charsets.UTF_8).use { it.readText() }
            val jsonArray = org.json.JSONArray(jsonString)
            val list = mutableListOf<ConstitutionArticle>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getString("id")
                val part = obj.getString("part")
                val title = obj.getString("title")
                
                // Map the flat JSON layout to the typesafe Map
                val contentMap = mutableMapOf<Language, String>()
                if (obj.has("content_eng")) contentMap[Language.ENG] = obj.getString("content_eng")
                if (obj.has("content_eng_simple")) contentMap[Language.ENG_SIMPLE] = obj.getString("content_eng_simple")
                
                if (obj.has("content_hin")) contentMap[Language.HIN] = obj.getString("content_hin")
                if (obj.has("content_hin_simple")) contentMap[Language.HIN_SIMPLE] = obj.getString("content_hin_simple")

                if (obj.has("content_guj")) contentMap[Language.GUJ] = obj.getString("content_guj")
                if (obj.has("content_guj_simple")) contentMap[Language.GUJ_SIMPLE] = obj.getString("content_guj_simple")

                if (obj.has("content_tam")) contentMap[Language.TAM] = obj.getString("content_tam")
                if (obj.has("content_tam_simple")) contentMap[Language.TAM_SIMPLE] = obj.getString("content_tam_simple")

                if (obj.has("content_odi")) contentMap[Language.ODI] = obj.getString("content_odi")
                if (obj.has("content_odi_simple")) contentMap[Language.ODI_SIMPLE] = obj.getString("content_odi_simple")
                
                list.add(ConstitutionArticle(id, part, title, contentMap))
            }
            _articles = list
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback empty or error log
        }
    }
}
