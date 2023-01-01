package com.aryanakbarpour.shoppinglist.service

import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.core.model.ShoppingList
import com.aryanakbarpour.shoppinglist.core.model.ShoppingListWithItems
import kotlinx.coroutines.flow.Flow

interface ShoppingListDao {

    fun getShoppingLists(): Flow<List<ShoppingListWithItems>>

    fun getShoppingListItemsFlow(shoppingListId: String): Flow<List<ShoppingItem>>

    fun updateShoppingItem(shoppingItem: ShoppingItem, listId: String)

    fun updateShoppingList(shoppingList: ShoppingList)

}