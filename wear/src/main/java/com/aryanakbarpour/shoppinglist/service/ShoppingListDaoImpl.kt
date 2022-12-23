package com.aryanakbarpour.shoppinglist.service

import android.util.Log
import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import com.aryanakbarpour.shoppinglist.service.Constants.SHOPPING_ITEM
import com.aryanakbarpour.shoppinglist.service.Constants.SHOPPING_LIST
import com.aryanakbarpour.shoppinglist.service.Constants.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingListDaoImpl @Inject constructor(private val db: FirebaseFirestore, private val auth: FirebaseAuth) : ShoppingListDao {

    fun getUserDocument() : DocumentReference? {
        val uid = auth.currentUser?.uid ?: return null

        Log.d(TAG, "got user id: $uid")
        return db.collection(USERS).document(uid)
    }

    override fun getShoppingLists(): Flow<List<ShoppingList>> = callbackFlow {

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
                trySend(shoppingLists)
            } else {
                Log.d(TAG, "Current data: null")
                close()
            }
        }

        awaitClose{
            listenerRegister.remove()
        }
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


    override fun updateShoppingItem(shoppingItem: ShoppingItem, listId: String) {
        val collection = getUserDocument()?.collection(SHOPPING_LIST) ?: return
        val docRef = collection.document(listId).collection(SHOPPING_ITEM).document(shoppingItem.id)

        docRef.set(shoppingItem)
    }


    override fun updateShoppingList(shoppingList: ShoppingList) {
        val collection = getUserDocument()?.collection(SHOPPING_LIST) ?: return
        val docRef = collection.document(shoppingList.id)

        docRef.set(shoppingList, SetOptions.merge())
    }


    companion object {
        private const val TAG = "ShoppingListFirebaseDaoImpl"
    }
}