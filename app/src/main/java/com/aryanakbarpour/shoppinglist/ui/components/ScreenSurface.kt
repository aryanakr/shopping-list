package com.aryanakbarpour.shoppinglist.ui.components

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aryanakbarpour.shoppinglist.ui.theme.Primary
import com.aryanakbarpour.shoppinglist.ui.theme.PrimaryLight
import kotlin.random.Random


@Composable
fun ScreenSurface(padding: PaddingValues, content: @Composable () -> Unit) {

    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Primary,
                        PrimaryLight,
                        PrimaryLight
                    ),
                )
            )
            .fillMaxSize()
            .padding(padding)
    ) {
        val configuration = LocalConfiguration.current

        val screenWidth = configuration.screenWidthDp.dp
        val screenHeight = configuration.screenHeightDp.dp

        val numHorizontalBoxes = 10

        val boxWidth = screenWidth / numHorizontalBoxes
        val numVerticalBoxes = (screenHeight / boxWidth).toInt()

        Box(modifier = Modifier.fillMaxSize()) {
            val boxVisibilities = remember{ Array(numVerticalBoxes) { Array(numHorizontalBoxes) { Random.nextBoolean() } } }
            Column() {
                for (i in 0 until numVerticalBoxes) {
                    Row() {
                        for (j in 0 until  numHorizontalBoxes) {
                            val animationCycle = remember { Animatable(0f) }
                            Box(modifier = Modifier
                                .width(boxWidth)
                                .height(boxWidth),
                                contentAlignment = Alignment.Center
                            ){
                                if (boxVisibilities[i][j]) {

                                    LaunchedEffect(key1 = animationCycle) {
                                        animationCycle
                                            .animateTo(
                                                targetValue = boxWidth.value,
                                                animationSpec = infiniteRepeatable(
                                                    animation = tween(
                                                        durationMillis = Random.nextInt(500, 2000),
                                                        easing = LinearOutSlowInEasing
                                                    ),
                                                    initialStartOffset = StartOffset(
                                                        Random.nextInt(
                                                            500
                                                        ), StartOffsetType.Delay
                                                    ),
                                                    repeatMode = RepeatMode.Reverse
                                                ),
                                            )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .width(animationCycle.value.dp)
                                            .height(animationCycle.value.dp)
                                            .border(
                                                1.5.dp,
                                                Color(red = 255, blue = 255, green = 255, alpha = 100),
                                                shape = MaterialTheme.shapes.medium
                                            )
                                            .animateContentSize(
                                                animationSpec = tween(
                                                    durationMillis = 300,
                                                    easing = LinearOutSlowInEasing
                                                )
                                            )
                                    )
                                }
                            }
                        }
                    }

            }

        }
            content()
        }
    }

}