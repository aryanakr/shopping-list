package com.aryanakbarpour.shoppinglist.service

import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.service.local.ShoppingListRepositoryLocalImpl
import com.aryanakbarpour.shoppinglist.service.local.ShoppingListRoomDao
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun getAllShoppingListsWithItemsFlow() : Flow<List<ShoppingListWithItems>>
    fun getShoppingListItems(listId: String) : Flow<List<ShoppingItem>>
    suspend fun insertShoppingList(shoppingList: ShoppingList): String
    suspend fun updateShoppingList(shoppingList: ShoppingList)
    suspend fun deleteShoppingList(shoppingList: ShoppingListWithItems)
    suspend fun deleteShoppingListItems(listId: String)

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem) : String
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    suspend fun updateShoppingItem(shoppingItem: ShoppingItem)
}