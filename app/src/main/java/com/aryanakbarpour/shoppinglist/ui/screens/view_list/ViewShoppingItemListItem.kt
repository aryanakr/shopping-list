package com.aryanakbarpour.shoppinglist.ui.screens.view_list

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aryanakbarpour.shoppinglist.core.model.CollectionStatus
import com.aryanakbarpour.shoppinglist.core.model.ShoppingItem
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryDark
import com.aryanakbarpour.shoppinglist.util.CircleCheckBox
import com.aryanakbarpour.shoppinglist.util.getBottomLineShape
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ViewShoppingItemListItem(
    item: ShoppingItem,
    setItemStatus: (ShoppingItem, CollectionStatus) -> Unit,
    checkSize: Dp
) {
    val lineThickness = with(LocalDensity.current) { 2.dp.toPx() }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            when (it) {
                DismissValue.DismissedToEnd -> {
                    setItemStatus(item, CollectionStatus.MISSING)
                    false
                }
                DismissValue.DismissedToStart -> {
                    setItemStatus(item, CollectionStatus.COLLECTED)
                    false
                }
                else -> false
            }
        }
    )


    SwipeToDismiss(
        state = dismissState ,
        directions = setOf(
            DismissDirection.StartToEnd,
            DismissDirection.EndToStart,
        ),
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.25f)
        },
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToEnd -> Color.Red
                    DismissValue.DismissedToStart -> Color.Green
                }
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> FaIcons.Times
                DismissDirection.EndToStart -> FaIcons.Check
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                FaIcon(
                    icon,
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent =  {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .background(Color.White)
                    .border(2.dp, PrimaryDark, shape = getBottomLineShape(lineThickness)),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // is missing checkbox
                Box(modifier = Modifier.width(checkSize), contentAlignment = Alignment.Center) {
                    CircleCheckBox(
                        checked = item.collectionStatus == CollectionStatus.MISSING,
                        onCheckedChange = {value ->
                            setItemStatus(item, if (value) CollectionStatus.MISSING else CollectionStatus.NOT_COLLECTED)
                        },
                        color = Color.Red,
                        size = (checkSize.value - 8).toInt())
                }

                Text(text = item.name, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Text(text = "${item.quantity} ${item.unit}", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)

                // is collected checkbox
                Box(modifier = Modifier.width(checkSize), contentAlignment = Alignment.Center) {
                    CircleCheckBox(
                        checked = item.collectionStatus == CollectionStatus.COLLECTED,
                        onCheckedChange = {value ->
                            setItemStatus(item, if (value) CollectionStatus.COLLECTED else CollectionStatus.NOT_COLLECTED)
                        },
                        color = Color.Green,
                        size = (checkSize.value - 8).toInt())
                }
            }
        }

    )
}

@Preview
@Composable
fun ViewShoppingItemListItemPreview() {
    ViewShoppingItemListItem(
        item = ShoppingItem(
            name = "Test",
            quantity = "1",
            unit = "kg",
            collectionStatus = CollectionStatus.NOT_COLLECTED
        ),
        setItemStatus = { _, _ -> },
        checkSize = 40.dp
    )
}