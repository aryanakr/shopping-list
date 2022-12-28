package com.aryanakbarpour.shoppinglist.service.remote

import android.util.Log
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.core.model.ShoppingList
import com.aryanakbarpour.shoppinglist.core.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.core.Constants.SHOPPING_ITEM
import com.aryanakbarpour.shoppinglist.core.Constants.SHOPPING_LIST
import com.aryanakbarpour.shoppinglist.core.Constants.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingListFirebaseDaoImpl @Inject constructor(private val db: FirebaseFirestore, private val auth: FirebaseAuth) : ShoppingListRemoteDao {

    fun getUserDocument() : DocumentReference? {
        val uid = auth.currentUser?.uid ?: return null

        Log.d(TAG, "got user id: $uid")
        return db.collection(USERS).document(uid)
    }

    override fun getShoppingLists(): Flow<List<ShoppingListWithItems>> = callbackFlow {

        val userDoc = getUserDocument()

        if (userDoc == null) {
            Log.d(TAG, "userDoc is null")
            close()
            return@callbackFlow
        }

        val listenerRegister = userDoc.collection(SHOPPING_LIST).addSnapshotListener {
            snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                close()
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val shoppingLists = snapshot.toObjects(ShoppingList::class.java)
                val shoppingListsWithItems = shoppingLists.map {
                    ShoppingListWithItems(it, listOf())
                }
                trySend(shoppingListsWithItems)
            } else {
                Log.d(TAG, "Current data: null")
                close()
            }
        }

        awaitClose{
            listenerRegister.remove()
        }
    }

    override suspend fun getShoppingListWithItems(shoppingListId: String): ShoppingListWithItems? {
        val userDoc = getUserDocument() ?: return null

        val shoppingListDoc = userDoc.collection(SHOPPING_LIST).document(shoppingListId)
        val shoppingList = shoppingListDoc.get().await().toObject(ShoppingList::class.java) ?: return null

        val shoppingItems = shoppingListDoc.collection(SHOPPING_ITEM).get().await().toObjects(ShoppingItem::class.java)

        return ShoppingListWithItems(shoppingList, shoppingItems)
    }

    override fun getShoppingListItemsFlow(shoppingListId: String): Flow<List<ShoppingItem>> = callbackFlow {
        val userDoc = getUserDocument()

        if (userDoc == null) {
            Log.d(TAG, "userDoc is null")
            close()
            return@callbackFlow
        }

        val listenerRegister = userDoc.collection(SHOPPING_LIST).document(shoppingListId).collection(SHOPPING_ITEM).addSnapshotListener {
            snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                close()
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                val shoppingItems = snapshot.toObjects(ShoppingItem::class.java)
                trySend(shoppingItems)
            } else {
                Log.d(TAG, "Current data: null")
                close()
            }
        }

        awaitClose{
            listenerRegister.remove()
        }
    }

    override fun insertShoppingList(shoppingList: ShoppingList): String {
        val collection = getUserDocument()?.collection(SHOPPING_LIST) ?: return ""
        val docRef = collection.document()

        val doc = shoppingList.copy(id = docRef.id)
        docRef.set(doc)

        return docRef.id
    }

    override fun addShoppingItem(shoppingItem: ShoppingItem, listId: String) : String {
        val collection = getUserDocument()?.collection(SHOPPING_LIST) ?: return ""
        val docRef = collection.document(listId).collection(SHOPPING_ITEM).document()

        val doc = shoppingItem.copy(id = docRef.id)
        docRef.set(doc)

        return docRef.id
    }

    override fun updateShoppingItem(shoppingItem: ShoppingItem, listId: String) {
        val collection = getUserDocument()?.collection(SHOPPING_LIST) ?: return
        val docRef = collection.document(listId).collection(SHOPPING_ITEM).document(shoppingItem.id)

        docRef.set(shoppingItem)
    }

    override fun deleteShoppingItem(shoppingItem: ShoppingItem, listId: String) {
        val collection = getUserDocument()?.collection(SHOPPING_LIST) ?: return
        val docRef = collection.document(listId).collection(SHOPPING_ITEM).document(shoppingItem.id)

        docRef.delete()
    }

    override fun deleteShoppingList(shoppingList: ShoppingList) {
        val collection = getUserDocument()?.collection(SHOPPING_LIST) ?: return
        val docRef = collection.document(shoppingList.id)

        docRef.delete()
    }

    override fun updateShoppingList(shoppingList: ShoppingList) {
        val collection = getUserDocument()?.collection(SHOPPING_LIST) ?: return
        val docRef = collection.document(shoppingList.id)

        docRef.set(shoppingList, SetOptions.merge())
    }

    override fun deleteShoppingListItems(listId: String) {
        val collection = getUserDocument()?.collection(SHOPPING_LIST) ?: return
        val docRef = collection.document(listId).collection(SHOPPING_ITEM)

        docRef.get().addOnSuccessListener {
            for (doc in it) {
                doc.reference.delete()
            }
        }
    }

    companion object {
        private const val TAG = "ShoppingListFirebaseDaoImpl"
    }
}