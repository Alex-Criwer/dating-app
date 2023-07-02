package com.example.sonder_domain.models

import java.sql.Timestamp

data class Message(
    val fromUID: String,
    val toUID: String,
    val timestamp: Long,
    val text: String
)
