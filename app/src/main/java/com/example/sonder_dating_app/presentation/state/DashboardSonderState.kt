package com.example.sonder_dating_app.presentation.state

import com.example.sonder_domain.models.User

internal data class DashboardSonderState(
    val user: User = User(),
    var token: String? = null
)
