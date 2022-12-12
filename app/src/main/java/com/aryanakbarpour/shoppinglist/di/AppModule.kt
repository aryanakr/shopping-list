package com.aryanakbarpour.shoppinglist.di

import android.content.Context
import com.aryanakbarpour.shoppinglist.service.ShoppingListRepository
import com.aryanakbarpour.shoppinglist.service.local.AppDatabase
import com.aryanakbarpour.shoppinglist.service.local.ShoppingListRoomDao
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
    fun provideShoppingListRepository(shoppingListDao: ShoppingListRoomDao) : ShoppingListRepository {
        return ShoppingListRepository(shoppingListDao)
    }

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
}