package com.example.movieapp.models

data class Serial(
    val enable: Boolean,
    val last_part: String,
    val parent_title: String,
    val part_text: String,
    val season_id: Int,
    val season_text: String,
    val serial_part: String
)