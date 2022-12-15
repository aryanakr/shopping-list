package com.aryanakbarpour.shoppinglist.service

import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.service.local.ShoppingListRepositoryLocalImpl
import com.aryanakbarpour.shoppinglist.service.local.ShoppingListRoomDao
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun getAllShoppingListsWithItemsFlow() : Flow<List<ShoppingListWithItems>>
    suspend fun insertShoppingList(shoppingList: ShoppingListWithItems): Long
    suspend fun updateShoppingList(shoppingList: ShoppingListWithItems)
    suspend fun deleteShoppingList(shoppingList: ShoppingListWithItems)
    suspend fun deleteShoppingListItems(shoppingList: ShoppingListWithItems)

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem) : Long
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    suspend fun updateShoppingItem(shoppingItem: ShoppingItem)
}