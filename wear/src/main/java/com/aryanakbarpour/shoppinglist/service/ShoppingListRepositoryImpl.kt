package com.aryanakbarpour.shoppinglist.service

import android.util.Log
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.core.model.ShoppingList
import com.aryanakbarpour.shoppinglist.core.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.core.service.ShoppingListRepository
import kotlinx.coroutines.flow.Flow

class ShoppingListRepositoryImpl(private val dao: ShoppingListDao) : ShoppingListRepository {

    override fun getAllShoppingListsWithItemsFlow(): Flow<List<ShoppingListWithItems>> = dao.getShoppingLists()

    override fun getShoppingListItems(listId: String): Flow<List<ShoppingItem>> = dao.getShoppingListItemsFlow(listId)



    override suspend fun updateShoppingList(shoppingList: ShoppingList) {
        dao.updateShoppingList(shoppingList)
    }


    override suspend fun updateShoppingItem(shoppingItem: ShoppingItem) {
        val listId = shoppingItem.listId

        if (listId == null) {
            Log.e("ShoppingListRepository", "listId is null")
            return
        }
        dao.updateShoppingItem(shoppingItem, listId)
    }

    // Unsupported operations

    override suspend fun insertShoppingList(shoppingList: ShoppingList): String {
        return ""
    }

    override suspend fun deleteShoppingList(shoppingList: ShoppingListWithItems) {
        return
    }

    override suspend fun deleteShoppingListItems(listId: String) {
        return
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem): String {
        return ""
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        return
    }
}