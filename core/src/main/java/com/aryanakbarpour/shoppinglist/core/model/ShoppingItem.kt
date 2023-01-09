package com.aryanakbarpour.shoppinglist.core.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.aryanakbarpour.shoppinglist.core.model.ShoppingList

@Entity(
    tableName = "shopping_item",
    foreignKeys = [ForeignKey(
        entity = ShoppingList::class,
        parentColumns = ["id"],
        childColumns = ["listId"],
        onDelete = ForeignKey.CASCADE)],
    indices = [androidx.room.Index(value = ["listId"])]
)
data class ShoppingItem(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val quantity: String = "",
    val unit: String = "",
    val collectionStatus: CollectionStatus = CollectionStatus.NOT_COLLECTED,
    val listId: String? = null,
)

enum class CollectionStatus {
    NOT_COLLECTED,
    COLLECTED,
    MISSING
}
