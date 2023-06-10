package com.machinarium.sbs.request


data class AuthRequest(
    val key: String, val secret: String, val uniqueId: String
) {
    val clientId = "client_1"
}