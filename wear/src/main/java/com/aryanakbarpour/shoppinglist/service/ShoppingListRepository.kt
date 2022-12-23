package com.aryanakbarpour.shoppinglist.service

import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun getAllShoppingListsFlow() : Flow<List<ShoppingList>>
    fun getShoppingListItems(listId: String) : Flow<List<ShoppingItem>>
    suspend fun updateShoppingList(shoppingList: ShoppingList)

    suspend fun updateShoppingItem(shoppingItem: ShoppingItem)
}