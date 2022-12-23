package com.aryanakbarpour.shoppinglist.service

import android.util.Log
import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import kotlinx.coroutines.flow.Flow

class ShoppingListRepositoryImpl(private val dao: ShoppingListDao) : ShoppingListRepository {

    override fun getAllShoppingListsFlow(): Flow<List<ShoppingList>> = dao.getShoppingLists()

    override fun getShoppingListItems(listId: String): Flow<List<ShoppingItem>> = dao.getShoppingListItemsFlow(listId)


    override suspend fun updateShoppingList(shoppingList: ShoppingList) {
        dao.updateShoppingList(shoppingList)
    }

    override suspend fun updateShoppingItem(shoppingItem: ShoppingItem) {
        if (shoppingItem.listId == null) {
            Log.e("ShoppingListRepository", "listId is null")
            return
        }
        dao.updateShoppingItem(shoppingItem, shoppingItem.listId)
    }
}