package com.aryanakbarpour.shoppinglist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.aryanakbarpour.shoppinglist.model.AppMode
import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.service.ShoppingListRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

interface ShoppingListViewModelInterface {
    val shoppingListsFlow: Flow<List<ShoppingListWithItems>>

    fun addShoppingList(shoppingList: ShoppingListWithItems)
    fun updateShoppingList(shoppingList: ShoppingListWithItems)
    fun deleteShoppingList(shoppingList: ShoppingListWithItems)
    fun toggleShoppingListActiveState(shoppingList: ShoppingListWithItems)

    fun updateShoppingItem(shoppingItem: ShoppingItem)
    fun archiveList(shoppingList: ShoppingListWithItems)

    fun getShoppingList(id: String): Flow<ShoppingList>
    fun getShoppingListItems(listId: String): Flow<List<ShoppingItem>>

}

@HiltViewModel
class ShoppingListViewModel @Inject internal constructor(
    private val shoppingRepo: ShoppingListRepository) : ViewModel(), ShoppingListViewModelInterface {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    override val shoppingListsFlow: Flow<List<ShoppingListWithItems>> = shoppingRepo.getAllShoppingListsWithItemsFlow()

    override fun addShoppingList(shoppingList: ShoppingListWithItems) {
        ioScope.launch {
            val shoppingListId = shoppingRepo.insertShoppingList(shoppingList.shoppingList)

            shoppingList.items.forEach {
                val item = it.copy(listId = shoppingListId)
                shoppingRepo.insertShoppingItem(item)
            }
        }

    }
    override fun updateShoppingList(shoppingList: ShoppingListWithItems) {
        ioScope.launch {
            // update list
            shoppingRepo.updateShoppingList(shoppingList.shoppingList)

            // update items
            shoppingList.items.forEach {
                if (it.id.isBlank()) {
                    val item = it.copy(listId = shoppingList.shoppingList.id)
                    shoppingRepo.insertShoppingItem(item)
                } else {
                    shoppingRepo.updateShoppingItem(it)
                }
            }
        }
    }
    override fun deleteShoppingList(shoppingList: ShoppingListWithItems) {
        ioScope.launch {
            shoppingRepo.deleteShoppingList(shoppingList)
        }
    }


    override fun toggleShoppingListActiveState(shoppingList: ShoppingListWithItems) {
        ioScope.launch {
            val newList = shoppingList.shoppingList.copy(isActive = !shoppingList.shoppingList.isActive)
            ioScope.launch {
                shoppingRepo.updateShoppingList(newList)
            }

        }
    }

    override fun updateShoppingItem(shoppingItem: ShoppingItem) {
        ioScope.launch {
            shoppingRepo.updateShoppingItem(shoppingItem)
        }
    }

    override fun archiveList(shoppingList: ShoppingListWithItems) {
        ioScope.launch {
            val newList = shoppingList.shoppingList.copy(isClosed = true)
            shoppingRepo.updateShoppingList(newList)
        }
    }

    override fun getShoppingList(id: String): Flow<ShoppingList> {
        TODO("Not yet implemented")
    }

    override fun getShoppingListItems(listId: String): Flow<List<ShoppingItem>> {
        return shoppingRepo.getShoppingListItems(listId)
    }

}

fun getTestShoppingListViewModel() : ShoppingListViewModelInterface {
    return object : ShoppingListViewModelInterface {
        override val shoppingListsFlow: Flow<List<ShoppingListWithItems>>
            get() = flowOf(
                listOf(
                    ShoppingListWithItems(ShoppingList(UUID.randomUUID().toString(), "test"), listOf(ShoppingItem(name = "test item 1", quantity = 3.0, unit = "kg"), ShoppingItem(name = "test item 2", quantity = 2.0, unit = "kg"))),
                    ShoppingListWithItems(ShoppingList(UUID.randomUUID().toString(), "test2"), listOf(ShoppingItem(name = "test item 1", quantity = 3.0, unit = "kg"), ShoppingItem(name = "test item 2", quantity = 2.0, unit = "kg"))),
                    ShoppingListWithItems(ShoppingList(UUID.randomUUID().toString(), "test3"), listOf(ShoppingItem(name = "test item 1", quantity = 3.0, unit = "kg"), ShoppingItem(name = "test item 2", quantity = 2.0, unit = "kg")))
                )
            )

        override fun addShoppingList(shoppingList: ShoppingListWithItems) {return }

        override fun updateShoppingList(shoppingList: ShoppingListWithItems) {}

        override fun deleteShoppingList(shoppingList: ShoppingListWithItems) {}


        override fun toggleShoppingListActiveState(shoppingList: ShoppingListWithItems) {}
        override fun updateShoppingItem(shoppingItem: ShoppingItem) {
            TODO("Not yet implemented")
        }

        override fun archiveList(shoppingList: ShoppingListWithItems) {
            TODO("Not yet implemented")
        }

        override fun getShoppingList(id: String): Flow<ShoppingList> {
            TODO("Not yet implemented")
        }

        override fun getShoppingListItems(listId: String): Flow<List<ShoppingItem>> {
            TODO("Not yet implemented")
        }

    }
}