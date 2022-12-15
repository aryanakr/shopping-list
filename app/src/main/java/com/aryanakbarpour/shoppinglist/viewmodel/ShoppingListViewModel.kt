package com.aryanakbarpour.shoppinglist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
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
import javax.inject.Inject
import javax.inject.Named

interface ShoppingListViewModelInterface {
    val shoppingListsFlow: Flow<List<ShoppingListWithItems>>

    fun addShoppingList(shoppingList: ShoppingListWithItems)
    fun updateShoppingList(shoppingList: ShoppingListWithItems)
    fun deleteShoppingList(shoppingList: ShoppingListWithItems)
    fun deleteShoppingListItems(shoppingList: ShoppingListWithItems)
    fun toggleShoppingListActiveState(shoppingList: ShoppingListWithItems)

    fun updateShoppingItem(shoppingItem: ShoppingItem)
    fun archiveList(shoppingList: ShoppingListWithItems)

    fun test()
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

    override fun test() {
        val st = if (Firebase.auth.currentUser != null) Firebase.auth.currentUser!!.email else "not logged in"
        Log.d("kir", "test: $st")
    }

}

fun getTestShoppingListViewModel() : ShoppingListViewModelInterface {
    return object : ShoppingListViewModelInterface {
        override val shoppingListsFlow: Flow<List<ShoppingListWithItems>>
            get() = flowOf(
                listOf(
                    ShoppingListWithItems(ShoppingList(1, "test"), listOf(ShoppingItem(name = "test item 1", quantity = 3.0, unit = "kg"), ShoppingItem(name = "test item 2", quantity = 2.0, unit = "kg"))),
                    ShoppingListWithItems(ShoppingList(2, "test2"), listOf(ShoppingItem(name = "test item 1", quantity = 3.0, unit = "kg"), ShoppingItem(name = "test item 2", quantity = 2.0, unit = "kg"))),
                    ShoppingListWithItems(ShoppingList(3, "test3"), listOf(ShoppingItem(name = "test item 1", quantity = 3.0, unit = "kg"), ShoppingItem(name = "test item 2", quantity = 2.0, unit = "kg")))
                )
            )

        override fun addShoppingList(shoppingList: ShoppingListWithItems) {return }

        override fun updateShoppingList(shoppingList: ShoppingListWithItems) {}

        override fun deleteShoppingList(shoppingList: ShoppingListWithItems) {}

        override fun deleteShoppingListItems(shoppingList: ShoppingListWithItems) {}

        override fun toggleShoppingListActiveState(shoppingList: ShoppingListWithItems) {}
        override fun updateShoppingItem(shoppingItem: ShoppingItem) {
            TODO("Not yet implemented")
        }

        override fun archiveList(shoppingList: ShoppingListWithItems) {
            TODO("Not yet implemented")
        }

        override fun test() {
            TODO("Not yet implemented")
        }

    }
}