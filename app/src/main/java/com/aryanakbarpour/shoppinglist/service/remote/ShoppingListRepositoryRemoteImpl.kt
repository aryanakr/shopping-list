package com.aryanakbarpour.shoppinglist.service.remote

import android.util.Log
import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.service.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ShoppingListRepositoryRemoteImpl : ShoppingListRepository {
    override fun getAllShoppingListsWithItemsFlow(): Flow<List<ShoppingListWithItems>> {
        Log.d("kir", "remote")
        return flow { emit(listOf()) }
    }

    override suspend fun insertShoppingList(shoppingList: ShoppingListWithItems): Long {
        Log.d("kir", "remote")
        return -1
    }

    override suspend fun updateShoppingList(shoppingList: ShoppingListWithItems) {
        Log.d("kir", "remote")
    }

    override suspend fun deleteShoppingList(shoppingList: ShoppingListWithItems) {
        Log.d("kir", "remote")
    }

    override suspend fun deleteShoppingListItems(shoppingList: ShoppingListWithItems) {
        Log.d("kir", "remote")
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem): Long {
        Log.d("kir", "remote")
        return -1
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        Log.d("kir", "remote")
    }

    override suspend fun updateShoppingItem(shoppingItem: ShoppingItem) {
        Log.d("kir", "remote")
    }
}