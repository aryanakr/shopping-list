package com.aryanakbarpour.shoppinglist.service.local

import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.service.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID


class ShoppingListRepositoryLocalImpl (private val shoppingListRoomDao: ShoppingListRoomDao) :
    ShoppingListRepository {

    override fun getAllShoppingListsWithItemsFlow() : Flow<List<ShoppingListWithItems>> = shoppingListRoomDao.getShoppingListWithItems()
    override fun getShoppingListItems(listId: String): Flow<List<ShoppingItem>> = shoppingListRoomDao.getShoppingListItems(listId)

    override suspend fun insertShoppingList(shoppingList: ShoppingList) : String {
        val id = UUID.randomUUID().toString()
        shoppingListRoomDao.insertShoppingList(shoppingList.copy(id = id))

        return id
    }
    override suspend fun updateShoppingList(shoppingList: ShoppingList) = shoppingListRoomDao.updateShoppingList(shoppingList)
    override suspend fun deleteShoppingList(shoppingList: ShoppingListWithItems) = shoppingListRoomDao.deleteShoppingList(shoppingList.shoppingList)
    override suspend fun deleteShoppingListItems(listId: String) = shoppingListRoomDao.deleteShoppingListItems(listId)

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) :String {
        val id = UUID.randomUUID().toString()
        shoppingListRoomDao.insertShoppingItem(shoppingItem.copy(id = id))
        return id
    }
    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) = shoppingListRoomDao.deleteShoppingItem(shoppingItem)
    override suspend fun updateShoppingItem(shoppingItem: ShoppingItem) = shoppingListRoomDao.updateShoppingItem(shoppingItem)

}