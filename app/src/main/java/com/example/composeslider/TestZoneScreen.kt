@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMotionApi::class,
    ExperimentalPagerApi::class
)

package com.example.composeslider

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.*
import com.example.composeslider.ui.theme.colorBackground
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

@Composable
fun TestZoneScreen() {

    MainUI()
}

@Composable
private fun MainUI() {

    val pagerState = rememberPagerState(initialPage = 0)

    val list = listOf(
        "Test Series",
        "Topic Test",
        "Sectional",
        "Mock Drill"
    )


    val constraintStartSet = ConstraintSet {
        val topBarRef = createRefFor("topBar")
        val titleRef = createRefFor("title")
        val tabRowRef = createRefFor("tabRow")
        val content = createRefFor("content")

        constrain(titleRef) {
            start.linkTo(parent.start, 50.dp)
            top.linkTo(parent.top, 50.dp)
            scaleX = 1.2f
            scaleY = 1.2f
        }
        constrain(topBarRef) {
            end.linkTo(parent.end, 24.dp)
            top.linkTo(parent.top, 50.dp)
        }

        constrain(tabRowRef) {
            top.linkTo(titleRef.bottom, 24.dp)
        }

        constrain(content) {
            top.linkTo(topBarRef.bottom, 24.dp)
        }
    }
    val constraintEndSet = ConstraintSet {
        val topBarRef = createRefFor("topBar")
        val tabRowRef = createRefFor("tabRow")
        val content = createRefFor("content")
        val titleRef = createRefFor("title")

        constrain(titleRef) {
            start.linkTo(parent.start, 24.dp)
            top.linkTo(parent.top, 24.dp)
            scaleX = 1f
            scaleY = 1f
        }

        constrain(topBarRef) {
            end.linkTo(parent.end, 24.dp)
            top.linkTo(parent.top, 24.dp)
        }

        constrain(tabRowRef) {
            top.linkTo(titleRef.bottom, 24.dp)
        }

        constrain(content) {
            top.linkTo(topBarRef.bottom, 24.dp)
        }
    }


    SKMotionLayout(
        start = constraintStartSet,
        end = constraintEndSet,
    ) { _, _, connection ->

        Text(
            text = "Testzone",
            modifier = Modifier.layoutId("title"),
            fontSize = 24.sp
        )
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            modifier = Modifier.layoutId("topBar")
        )


        Box(modifier = Modifier.layoutId("content")) {
            HorizontalPager(

                state = pagerState, count = list.size,
            ) { tabIndex ->

                LazyColumn(
                    modifier = Modifier,
                    contentPadding = PaddingValues(
                        top = 80.dp,
                        start = 24.dp,
                        end = 24.dp,
                        bottom = 120.dp
                    )
                ) {
                    if (tabIndex == 0) {
                        item {
                            Box(
                                modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth()
                                    .height(80.dp)
                                    .background(color = Color.Gray)
                            ) {

                            }
                        }
                    }

                    items(10) { item ->
                        Box(
                            modifier = Modifier.padding(vertical = 12.dp).height(80.dp)
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colors.primary)
                        ) {
                            Text(text = item.toString())
                        }
                    }
                }
            }
        }

        QudooScrollableTabLayout(
            modifier = Modifier.layoutId("tabRow"),
            pagerState = pagerState,
            tabTitles = list
        )
    }
}

@Composable
fun SKMotionLayout(
    start: ConstraintSet,
    end: ConstraintSet,
    content: @Composable (progress: Float, swipeAbleState: SwipeableState<SwipeableStates>, connection: NestedScrollConnection) -> Unit
) {

    val swipingState = rememberSwipeableState(initialValue = SwipeableStates.EXPANDED)

    val connection = remember {
        object : NestedScrollConnection {

            override fun onPreScroll( // Desides if use the sroll for parent (Swipe) or pass it to the childern
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                return if (delta < 0) {
                    swipingState.performDrag(delta).toOffset()
                } else {
                    Offset.Zero
                }
            }

            override fun onPostScroll( // If there is any leftover sroll from childern, let's try to use it on parent swipe
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                return swipingState.performDrag(delta).toOffset()
            }

//            override suspend fun onPostFling( // Lets's try to use fling on parent and pass all leftover to childern
//                consumed: Velocity,
//                available: Velocity
//            ): Velocity {
//                swipingState.performFling(velocity = available.y)
//                return super.onPostFling(consumed, available)
//            }

            private fun Float.toOffset() = Offset(0f, this)
        }
    }

    val progress = remember {
        derivedStateOf {
            if (swipingState.progress.to == SwipeableStates.EXPANDED) 1f - swipingState.progress.fraction else swipingState.progress.fraction
        }
    }


    MotionLayout(
        modifier = Modifier
            .nestedScroll(connection)
            .swipeable(
                state = swipingState,
                orientation = Orientation.Vertical,
                anchors = mapOf(
                    0f to SwipeableStates.COLLAPSED,
                    500f to SwipeableStates.EXPANDED
                ),
            ),
        start = start, end = end, progress = progress.value
    ) {
        content(progress.value, swipingState, connection)
    }
}


@Composable
fun QudooScrollableTabLayout(
    modifier: Modifier = Modifier,
    tabTitles: List<String>,
    pagerState: PagerState
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = modifier,
    ) {

        ScrollableTabRow(
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp),
            selectedTabIndex = pagerState.currentPage,

            contentColor = Color.White,
            backgroundColor = Color.LightGray,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.pagerTabIndicatorOffset(
                        pagerState = pagerState,
                        tabPositions = tabPositions
                    ),
                    height = 4.dp,
                    color = Color.Red
                )
            },
            divider = {}
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier.fillMaxHeight().zIndex(4f),
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.body1,
                            color = if (pagerState.currentPage == index)
                                MaterialTheme.colors.onBackground
                            else Color.Blue,
                            textAlign = TextAlign.Center,
                        )
                    })
            }
        }
    }
}