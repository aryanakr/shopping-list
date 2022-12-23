package com.aryanakbarpour.shoppinglist.model

import androidx.room.*

data class ShoppingList (
    val id: String = "",
    val name: String = "",
    val isArchived: Boolean = false,
)