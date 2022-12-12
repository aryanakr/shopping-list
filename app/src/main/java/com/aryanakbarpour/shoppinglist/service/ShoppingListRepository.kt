package com.aryanakbarpour.shoppinglist.service

import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.service.local.ShoppingListRoomDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class ShoppingListRepository (private val shoppingListRoomDao: ShoppingListRoomDao) {

    fun getAllShoppingListsWithItemsFlow() : Flow<List<ShoppingListWithItems>> = shoppingListRoomDao.getShoppingListWithItems()
    suspend fun insertShoppingList(shoppingList: ShoppingListWithItems) = shoppingListRoomDao.insertShoppingList(shoppingList.shoppingList)
    suspend fun updateShoppingList(shoppingList: ShoppingListWithItems) = shoppingListRoomDao.updateShoppingList(shoppingList.shoppingList)
    suspend fun deleteShoppingList(shoppingList: ShoppingListWithItems) = shoppingListRoomDao.deleteShoppingList(shoppingList.shoppingList)
    suspend fun deleteShoppingListItems(shoppingList: ShoppingListWithItems) = shoppingListRoomDao.deleteShoppingListItems(shoppingList.shoppingList.id!!)

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem) = shoppingListRoomDao.insertShoppingItem(shoppingItem)
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) = shoppingListRoomDao.deleteShoppingItem(shoppingItem)
    suspend fun updateShoppingItem(shoppingItem: ShoppingItem) = shoppingListRoomDao.updateShoppingItem(shoppingItem)

}