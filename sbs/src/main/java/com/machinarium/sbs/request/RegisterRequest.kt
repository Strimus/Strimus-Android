package com.machinarium.sbs.request


data class RegisterRequest(
    val key: String, val secret: String, val streamer: User
) {
    val clientId = "client_1"

    class User(
        val uniqueId: String,
        val name: String,
        val imageUrl: String,
        val email: String,
        val profileUrl: String,
    )
}