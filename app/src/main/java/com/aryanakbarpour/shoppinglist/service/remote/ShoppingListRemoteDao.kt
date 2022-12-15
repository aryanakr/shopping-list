package com.aryanakbarpour.shoppinglist.service.remote

import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import kotlinx.coroutines.flow.Flow

interface ShoppingListRemoteDao {

    fun getShoppingLists(): Flow<List<ShoppingListWithItems>>

    suspend fun getShoppingListWithItems(shoppingListId: String): ShoppingListWithItems?

    fun getShoppingListItemsFlow(shoppingListId: String): Flow<List<ShoppingItem>>

    fun insertShoppingList(shoppingList: ShoppingList) : String

    fun addShoppingItem(shoppingItem: ShoppingItem, listId: String) : String

    fun updateShoppingItem(shoppingItem: ShoppingItem, listId: String)

    fun deleteShoppingItem(shoppingItem: ShoppingItem, listId: String)

    fun deleteShoppingList(shoppingList: ShoppingList)

    fun updateShoppingList(shoppingList: ShoppingList)

    fun deleteShoppingListItems(listId: String)


}