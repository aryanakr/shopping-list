package com.aryanakbarpour.shoppinglist.service

import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import kotlinx.coroutines.flow.Flow

interface ShoppingListDao {

    fun getShoppingLists(): Flow<List<ShoppingList>>

    fun getShoppingListItemsFlow(shoppingListId: String): Flow<List<ShoppingItem>>

    fun updateShoppingItem(shoppingItem: ShoppingItem, listId: String)

    fun updateShoppingList(shoppingList: ShoppingList)

}