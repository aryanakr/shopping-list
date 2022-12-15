package com.aryanakbarpour.shoppinglist.service.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListRoomDao {

    // Shopping list methods
    @Query("SELECT * FROM shopping_list")
    suspend fun getShoppingLists(): List<ShoppingList>

    @Query("SELECT * FROM shopping_item WHERE listId = :shoppingListId")
    fun getShoppingListItems(shoppingListId: String): Flow<List<ShoppingItem>>

    @Insert
    suspend fun insertShoppingList(shoppingList: ShoppingList)

    @Update
    suspend fun updateShoppingList(shoppingList: ShoppingList)

    @Delete
    suspend fun deleteShoppingList(shoppingList: ShoppingList)

    @Query("DELETE FROM shopping_item WHERE listId = :listId")
    suspend fun deleteShoppingListItems(listId: String)

    @Transaction
    @Query("SELECT * FROM shopping_list")
    fun getShoppingListWithItems(): Flow<List<ShoppingListWithItems>>

    // Shopping item methods
    @Insert
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    @Update
    suspend fun updateShoppingItem(shoppingItem: ShoppingItem)

}