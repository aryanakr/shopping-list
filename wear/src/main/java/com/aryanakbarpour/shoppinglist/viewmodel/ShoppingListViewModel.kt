package com.aryanakbarpour.shoppinglist.viewmodel

import androidx.lifecycle.ViewModel
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.core.model.ShoppingList
import com.aryanakbarpour.shoppinglist.core.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.core.service.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ShoppingListViewModelInterface {

    val shoppingListsFlow: Flow<List<ShoppingListWithItems>>

    fun updateShoppingList(shoppingList: ShoppingList)

    fun updateShoppingItem(shoppingItem: ShoppingItem)
    fun archiveList(shoppingList: ShoppingList)

    fun getShoppingListItems(listId: String): Flow<List<ShoppingItem>>

}

@HiltViewModel
class ShoppingListViewModel @Inject internal constructor(
    private val shoppingRepo: ShoppingListRepository
) : ViewModel(), ShoppingListViewModelInterface {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    override val shoppingListsFlow: Flow<List<ShoppingListWithItems>> = shoppingRepo.getAllShoppingListsWithItemsFlow()

    override fun updateShoppingList(shoppingList: ShoppingList) {
        ioScope.launch {
            // update list
            shoppingRepo.updateShoppingList(shoppingList)
        }
    }

    override fun updateShoppingItem(shoppingItem: ShoppingItem) {
        ioScope.launch {
            shoppingRepo.updateShoppingItem(shoppingItem)
        }
    }

    override fun archiveList(shoppingList: ShoppingList) {
        ioScope.launch {
            val newList = shoppingList.copy(isArchived = true)
            shoppingRepo.updateShoppingList(newList)
        }
    }


    override fun getShoppingListItems(listId: String): Flow<List<ShoppingItem>> {
        return shoppingRepo.getShoppingListItems(listId)
    }

}
