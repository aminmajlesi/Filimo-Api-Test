package com.example.movieapp.models

data class Attributes(
    val HD: Boolean,
    val age_range: String,
    val audio: Audio,
    val avg_rate_label: String,
    val badge: Badge,
    val categories: List<Category>,
    val commingsoon_txt: String,
    val countries: List<Country>,
    val cover: String,
    val descr: String,
    val director: String,
    val dubbed: Dubbed,
    val duration: Duration,
    val freemium: Boolean,
    val imdb_rate: String,
    val language_info: LanguageInfo,
    val last_watch: List<Any>,
    val link_key: String,
    val link_type: String,
    val movie_id: String,
    val movie_title: String,
    val movie_title_en: String,
    val output_type: String,
    val pic: Pic,
    val position: Int,
    val pro_year: String,
    val publish_date: String,
    val rate_avrage: String,
    val rate_enable: Boolean,
    val rel_data: RelData,
    val serial: Serial,
    val sid: Int,
    val subtitle: Subtitle,
    val tag_id: Any,
    val theme: String,
    val uid: String,
    val uuid: String,
    val watch_list_action: String,
    val watermark: Boolean
)