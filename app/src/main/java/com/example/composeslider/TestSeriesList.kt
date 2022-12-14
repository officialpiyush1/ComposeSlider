@file:OptIn(ExperimentalMaterialApi::class)

package com.example.composeslider

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun TestSeriesList() {
    MainUI()
}

@Composable
private fun MainUI() {
    val scrollScroll = rememberLazyListState()

    val swipingState = rememberSwipeableState(initialValue = SwipeableStates.EXPANDED)

    val connection = remember {
        object : NestedScrollConnection {

            override fun onPreScroll( // Desides if use the sroll for parent (Swipe) or pass it to the childern
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                return swipingState.performDrag(delta).toOffset()
            }


            override fun onPostScroll( // If there is any leftover sroll from childern, let's try to use it on parent swipe
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                return swipingState.performDrag(delta).toOffset()
            }

            private fun Float.toOffset() = Offset(0f, this)
        }
    }

    val topBarHeightPX = with(LocalDensity.current) { 60.dp.toPx() }
    Box(
        modifier = Modifier.fillMaxSize()
            .nestedScroll(connection)
            .swipeable(
                state = swipingState,
                orientation = Orientation.Vertical,
                anchors = mapOf(
                    -topBarHeightPX to SwipeableStates.COLLAPSED,
                    0f to SwipeableStates.EXPANDED
                )
            ).graphicsLayer {
                translationY = swipingState.offset.value
            }
    ) {
        TopAppBar(modifier = Modifier.zIndex(4f))

        LazyColumn(
            modifier = Modifier.zIndex(3f),
            state = scrollScroll,
            contentPadding = PaddingValues(top = 160.dp)
        ) {
            items(100) {
                Box(
                    modifier = Modifier.padding(vertical = 12.dp).height(60.dp).fillMaxWidth()
                        .background(color = Color.Cyan)
                )
            }
        }
    }
}

@Composable
fun TopAppBar(modifier: Modifier) {

    Column(
        modifier = modifier
            .fillMaxWidth()

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(color = Color.DarkGray)
        ) {
            Box(
                modifier = Modifier.height(60.dp).fillMaxWidth(),

                ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart),
                    tint = Color.White
                )
                Text("Title", modifier = Modifier.align(Alignment.Center), color = Color.White)
            }

        }

        Box(
            modifier = Modifier.padding(top = 20.dp, start = 12.dp, end = 12.dp)
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(50))
                .background(color = Color.DarkGray)
        ) {

        }
    }
}
