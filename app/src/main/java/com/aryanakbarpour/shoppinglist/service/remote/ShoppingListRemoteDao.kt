package com.aryanakbarpour.shoppinglist.service.remote

import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.core.model.ShoppingList
import com.aryanakbarpour.shoppinglist.core.model.ShoppingListWithItems
import kotlinx.coroutines.flow.Flow

interface ShoppingListRemoteDao {

    /**
     * retrieves all shopping lists from the remote database
     * @return a flow of list of shopping lists
     */
    fun getShoppingLists(): Flow<List<ShoppingListWithItems>>

    /**
     * retrieves all shopping lists with shopping items from the remote database
     * @param listId the id of the shopping list
     * @return a flow of list of shopping items
     */
    suspend fun getShoppingListWithItems(shoppingListId: String): ShoppingListWithItems?

    /**
     * retrieves all shopping items from the remote database
     * @param shoppingListId the id of the shopping list
     * @return a flow of list of shopping items
     */
    fun getShoppingListItemsFlow(shoppingListId: String): Flow<List<ShoppingItem>>

    /**
     * inserts a shopping list into the remote database
     * @param shoppingList the shopping list to be inserted
     * @return the id of the inserted shopping list
     */
    fun insertShoppingList(shoppingList: ShoppingList) : String

    /**
     * inserts a shopping item into the remote database
     * @param shoppingItem the shopping item to be inserted
     * @param listId the id of the shopping list
     */
    fun addShoppingItem(shoppingItem: ShoppingItem, listId: String) : String

    /**
     * updates a shopping list in the remote database
     * @param shoppingList the shopping list to be updated
     * @param listId the id of the shopping list
     */
    fun updateShoppingItem(shoppingItem: ShoppingItem, listId: String)

    /**
     * updates a shopping item in the remote database
     * @param shoppingItem the shopping item to be updated
     * @param listId the id of the shopping list
     */
    fun deleteShoppingItem(shoppingItem: ShoppingItem, listId: String)

    /**
     * deletes a shopping list from the remote database
     * @param shoppingList the shopping list to be deleted
     */
    fun deleteShoppingList(shoppingList: ShoppingList)

    /**
     * updates shopping list in the remote database
     * @param shoppingList the shopping list to be updated
     */
    fun updateShoppingList(shoppingList: ShoppingList)

    /**
     * deletes all shopping items for list from the remote database
     * @param listId the id of the shopping list
     */
    fun deleteShoppingListItems(listId: String)

}