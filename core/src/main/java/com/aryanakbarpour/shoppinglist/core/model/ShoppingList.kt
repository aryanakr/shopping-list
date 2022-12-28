package com.aryanakbarpour.shoppinglist.core.model

import androidx.room.*

@Entity(tableName = "shopping_list")
data class ShoppingList (
    @PrimaryKey val id: String = "",
    val name: String = "",
    val isArchived: Boolean = false,
)

data class ShoppingListWithItems(
    @Embedded val shoppingList: ShoppingList,
    @Relation(
        parentColumn = "id",
        entityColumn = "listId"
    )
    val items: List<ShoppingItem>
)