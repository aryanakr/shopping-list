package com.aryanakbarpour.shoppinglist.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class User (
    val id: String,
    val name: String?,
    val email: String?,
    @ServerTimestamp val createdAt: Date? = null,
)

enum class AppMode{
    ONLINE,
    OFFLINE
}