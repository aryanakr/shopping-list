package com.aryanakbarpour.shoppinglist.di

import android.content.Context
import com.aryanakbarpour.shoppinglist.core.service.ShoppingListRepository
import com.aryanakbarpour.shoppinglist.service.local.AppDatabase
import com.aryanakbarpour.shoppinglist.service.local.ShoppingListRepositoryLocalImpl
import com.aryanakbarpour.shoppinglist.service.local.ShoppingListRoomDao
import com.aryanakbarpour.shoppinglist.service.remote.ShoppingListFirebaseDaoImpl
import com.aryanakbarpour.shoppinglist.service.remote.ShoppingListRemoteDao
import com.aryanakbarpour.shoppinglist.service.remote.ShoppingListRepositoryRemoteImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideShoppingListDao(database: AppDatabase) : ShoppingListRoomDao {
        return database.shoppingListDao()
    }

    @Singleton
    @Provides
    fun provideShoppingListRemoteDao(auth: FirebaseAuth, db: FirebaseFirestore) : ShoppingListRemoteDao {
        return ShoppingListFirebaseDaoImpl(db, auth)
    }

    @Provides
    fun provideShoppingListRepository(localDao: ShoppingListRoomDao, remoteDao: ShoppingListRemoteDao ) : ShoppingListRepository {
        if (Firebase.auth.currentUser == null) {
            return ShoppingListRepositoryLocalImpl(localDao)
        }
        return ShoppingListRepositoryRemoteImpl(remoteDao)
    }

}