package com.aryanakbarpour.shoppinglist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ShoppingListWearApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}