package com.aryanakbarpour.shoppinglist.service.remote

import android.util.Log
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.core.model.ShoppingList
import com.aryanakbarpour.shoppinglist.core.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.core.service.ShoppingListRepository
import kotlinx.coroutines.flow.Flow

class ShoppingListRepositoryRemoteImpl(private val dao: ShoppingListRemoteDao) :
    ShoppingListRepository {

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
        val listId = shoppingItem.listId
        if (listId == null) {
            Log.e("ShoppingListRepository", "listId is null")
            return ""
        }
        return dao.addShoppingItem(shoppingItem, listId)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        val listId = shoppingItem.listId
        if (listId == null) {
            Log.e("ShoppingListRepository", "listId is null")
            return
        }
        dao.deleteShoppingItem(shoppingItem, listId)
    }

    override suspend fun updateShoppingItem(shoppingItem: ShoppingItem) {
        val listId = shoppingItem.listId
        if (listId == null) {
            Log.e("ShoppingListRepository", "listId is null")
            return
        }
        dao.updateShoppingItem(shoppingItem, listId)
    }
}