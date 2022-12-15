package com.aryanakbarpour.shoppinglist.service.local

import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.service.ShoppingListRepository
import kotlinx.coroutines.flow.Flow


class ShoppingListRepositoryLocalImpl (private val shoppingListRoomDao: ShoppingListRoomDao) :
    ShoppingListRepository {

    override fun getAllShoppingListsWithItemsFlow() : Flow<List<ShoppingListWithItems>> = shoppingListRoomDao.getShoppingListWithItems()
    override suspend fun insertShoppingList(shoppingList: ShoppingListWithItems) = shoppingListRoomDao.insertShoppingList(shoppingList.shoppingList)
    override suspend fun updateShoppingList(shoppingList: ShoppingListWithItems) = shoppingListRoomDao.updateShoppingList(shoppingList.shoppingList)
    override suspend fun deleteShoppingList(shoppingList: ShoppingListWithItems) = shoppingListRoomDao.deleteShoppingList(shoppingList.shoppingList)
    override suspend fun deleteShoppingListItems(shoppingList: ShoppingListWithItems) = shoppingListRoomDao.deleteShoppingListItems(shoppingList.shoppingList.id!!)

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) = shoppingListRoomDao.insertShoppingItem(shoppingItem)
    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) = shoppingListRoomDao.deleteShoppingItem(shoppingItem)
    override suspend fun updateShoppingItem(shoppingItem: ShoppingItem) = shoppingListRoomDao.updateShoppingItem(shoppingItem)

}