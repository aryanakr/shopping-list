package com.aryanakbarpour.shoppinglist.service.remote

import android.util.Log
import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.service.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ShoppingListRepositoryRemoteImpl(private val dao: ShoppingListRemoteDao) : ShoppingListRepository {

    override fun getAllShoppingListsWithItemsFlow(): Flow<List<ShoppingListWithItems>> = dao.getShoppingLists()

    override fun getShoppingListItems(listId: String): Flow<List<ShoppingItem>> = dao.getShoppingListItemsFlow(listId)

    override suspend fun insertShoppingList(shoppingList: ShoppingList): String {
        return dao.insertShoppingList(shoppingList)
    }

    override suspend fun updateShoppingList(shoppingList: ShoppingList) {
        dao.updateShoppingList(shoppingList)
    }

    override suspend fun deleteShoppingList(shoppingList: ShoppingListWithItems) {
        dao.deleteShoppingList(shoppingList.shoppingList)
    }

    override suspend fun deleteShoppingListItems(listId: String) {
        dao.deleteShoppingListItems(listId)
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem): String {
        if (shoppingItem.listId == null) {
            Log.e("ShoppingListRepository", "listId is null")
            return ""
        }
        return dao.addShoppingItem(shoppingItem, shoppingItem.listId)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        if (shoppingItem.listId == null) {
            Log.e("ShoppingListRepository", "listId is null")
            return
        }
        dao.deleteShoppingItem(shoppingItem, shoppingItem.listId)
    }

    override suspend fun updateShoppingItem(shoppingItem: ShoppingItem) {
        if (shoppingItem.listId == null) {
            Log.e("ShoppingListRepository", "listId is null")
            return
        }
        dao.updateShoppingItem(shoppingItem, shoppingItem.listId)
    }
}