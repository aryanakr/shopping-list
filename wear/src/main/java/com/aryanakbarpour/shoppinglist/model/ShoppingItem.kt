package com.aryanakbarpour.shoppinglist.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


data class ShoppingItem(
    val id: String = "",
    val name: String = "",
    val quantity: String = "",
    val unit: String = "",
    val collectionStatus: CollectionStatus = CollectionStatus.NOT_COLLECTED,
    val listId: String? = null,
    val position: Int = 0
)

enum class CollectionStatus {
    NOT_COLLECTED,
    COLLECTED,
    MISSING
}
