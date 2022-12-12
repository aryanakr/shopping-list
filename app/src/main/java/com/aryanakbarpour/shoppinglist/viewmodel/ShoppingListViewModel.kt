package com.aryanakbarpour.shoppinglist.viewmodel

import androidx.lifecycle.ViewModel
import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.service.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ShoppingListViewModelInterface {
    val shoppingListsFlow: Flow<List<ShoppingListWithItems>>

    fun addShoppingList(shoppingList: ShoppingListWithItems)
    fun updateShoppingList(shoppingList: ShoppingListWithItems)
    fun deleteShoppingList(shoppingList: ShoppingListWithItems)
    fun deleteShoppingListItems(shoppingList: ShoppingListWithItems)
    fun toggleShoppingListActiveState(shoppingList: ShoppingListWithItems)

    fun updateShoppingItem(shoppingItem: ShoppingItem)
    fun archiveList(shoppingList: ShoppingListWithItems)
}

@HiltViewModel
class ShoppingListViewModel @Inject internal constructor(private val repository: ShoppingListRepository) : ViewModel(), ShoppingListViewModelInterface {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    override val shoppingListsFlow: Flow<List<ShoppingListWithItems>> = repository.getAllShoppingListsWithItemsFlow()

    override fun addShoppingList(shoppingList: ShoppingListWithItems) {
        ioScope.launch {
            val shoppingListId = repository.insertShoppingList(shoppingList)

            shoppingList.items.forEach {
                val item = it.copy(listId = shoppingListId)
                repository.insertShoppingItem(item)
            }
        }

    }
    override fun updateShoppingList(shoppingList: ShoppingListWithItems) {
        ioScope.launch {
            // update list
            repository.updateShoppingList(shoppingList)

            // update items
            shoppingList.items.forEach {
                if (it.id == null) {
                    val item = it.copy(listId = shoppingList.shoppingList.id!!)
                    repository.insertShoppingItem(item)
                } else {
                    repository.updateShoppingItem(it)
                }
            }
        }
    }
    override fun deleteShoppingList(shoppingList: ShoppingListWithItems) {
        ioScope.launch {
            repository.deleteShoppingList(shoppingList)
        }
    }
    override fun deleteShoppingListItems(shoppingList: ShoppingListWithItems) {
        ioScope.launch {
            repository.deleteShoppingListItems(shoppingList)
        }
    }

    override fun toggleShoppingListActiveState(shoppingList: ShoppingListWithItems) {
        ioScope.launch {
            val newList = shoppingList.shoppingList.copy(isActive = !shoppingList.shoppingList.isActive)
            ioScope.launch {
                repository.updateShoppingList(shoppingList.copy(shoppingList = newList))
            }

        }
    }

    override fun updateShoppingItem(shoppingItem: ShoppingItem) {
        ioScope.launch {
            repository.updateShoppingItem(shoppingItem)
        }
    }

    override fun archiveList(shoppingList: ShoppingListWithItems) {
        ioScope.launch {
            val newList = shoppingList.shoppingList.copy(isClosed = true)
            repository.updateShoppingList(shoppingList.copy(shoppingList = newList))
        }
    }

}