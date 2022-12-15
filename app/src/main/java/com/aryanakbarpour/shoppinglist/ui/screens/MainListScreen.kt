package com.aryanakbarpour.shoppinglist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aryanakbarpour.shoppinglist.model.CollectionStatus
import com.aryanakbarpour.shoppinglist.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.model.ShoppingList
import com.aryanakbarpour.shoppinglist.model.ShoppingListWithItems
import com.aryanakbarpour.shoppinglist.service.remote.AuthViewModel
import com.aryanakbarpour.shoppinglist.ui.theme.Primary
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryLight
import com.aryanakbarpour.shoppinglist.viewmodel.ShoppingListViewModelInterface
import com.aryanakbarpour.shoppinglist.viewmodel.getTestShoppingListViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun MainListScreen(
    navController: NavController,
    shoppingListViewModel: ShoppingListViewModelInterface
) {

    val shoppingListsState = shoppingListViewModel.shoppingListsFlow.collectAsState(initial = listOf())

    Scaffold(
        topBar = { TopAppBar(
            title = { Text(text ="Shopping Lists")},
            actions = {
                IconButton(onClick = { shoppingListViewModel.test() }) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Add new list")
                }
                IconButton(onClick = { Firebase.auth.signOut() }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add new list")
                }
            },
            elevation = 0.dp
        )},
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.CreateListScreen.route)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Create new list")
            }
        }
        ) {
        Box(modifier = Modifier
            .padding(it)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Primary,
                        PrimaryLight,
                        PrimaryLight
                    ),
                )
            )
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(vertical = 8.dp, horizontal = 16.dp)) {
            Column {
                // Active lists
                SectionHeader(title = "Active Lists")
                LazyColumn(modifier = Modifier.weight(1f)) {
                    val activateItems = shoppingListsState.value.filter { it.shoppingList.isActive}
                    items(activateItems.size) { index ->
                        val shoppingList = activateItems[index]
                        ShoppingListItem(
                            shoppingList,
                            shoppingListViewModel::toggleShoppingListActiveState,
                            onItemClickListener = {navController.navigate(Screen.ViewListScreen.withArgs(shoppingList.shoppingList.id!!))})
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Inactive lists
                SectionHeader(title = "All Lists")
                LazyColumn(modifier = Modifier.weight(3f)) {
                    val allItems = shoppingListsState.value.filter { !it.shoppingList.isActive }
                    items(allItems.size) { index ->
                        val shoppingList = allItems[index]
                        ShoppingListItem(
                            shoppingList,
                            shoppingListViewModel::toggleShoppingListActiveState,
                            onItemClickListener = {navController.navigate(Screen.ViewListScreen.withArgs(shoppingList.shoppingList.id!!))})
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            }

        }


    }
}

@Composable
fun ShoppingListItem(shoppingList: ShoppingListWithItems, activateList: (ShoppingListWithItems) -> Unit, onItemClickListener: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .clickable {
            onItemClickListener.invoke()
        }
        .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = shoppingList.shoppingList.name)
            Row(verticalAlignment = Alignment.CenterVertically) {

                if (shoppingList.items.isNotEmpty())
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "${shoppingList.items.filter { it.collectionStatus == CollectionStatus.COLLECTED }.size.toString()}/${shoppingList.items.size}")

                Button(onClick = {activateList(shoppingList)},
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (shoppingList.shoppingList.isActive) PrimaryDark else Primary,
                        contentColor =  if (shoppingList.shoppingList.isActive) Color.White else Color.Black)) {
                    Text(text = if (shoppingList.shoppingList.isActive) "Remove" else "Activate")
                }
            }
        }

    }

}

@Composable
fun SectionHeader(title: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 32.dp), contentAlignment = Alignment.Center) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier
                .height(2.dp)
                .weight(1f)
                .background(PrimaryDark))
            Text(modifier = Modifier.padding(16.dp),text = title, color = PrimaryDark)
            Box(modifier = Modifier
                .height(2.dp)
                .weight(1f)
                .background(PrimaryDark))
        }

    }

}

@Preview
@Composable
fun MainScreenPreview() {
    MainListScreen(
        navController = NavController(LocalContext.current),
        shoppingListViewModel = getTestShoppingListViewModel()
    )
}