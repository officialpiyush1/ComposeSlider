package com.example.composeslider

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity

@ExperimentalMaterialApi
@Composable

fun CollapsableToolbar() {
	val swipingState = rememberSwipeableState(initialValue = SwipeableStates.EXPANDED)

	BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

		val heightInPx = with(LocalDensity.current) { maxHeight.toPx() } // Get height of screen
		val connection = remember {

			return@remember object : NestedScrollConnection {

					override fun onPreScroll(
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

				override fun onPostScroll(
					consumed: Offset,
					available: Offset,
					source: NestedScrollSource
				): Offset {
					val delta = available.y
					return swipingState.performDrag(delta).toOffset()
				}

				override suspend fun onPostFling(
					consumed: Velocity,
					available: Velocity
				): Velocity {
					swipingState.performFling(velocity = available.y)
					return onPostFling(consumed, available)
				}

				private fun Float.toOffset() = Offset(0f, this)
			}
		}
		Box(
			modifier = Modifier
				.fillMaxSize()
				.swipeable(
					state = swipingState,
					thresholds = { _, _ -> FractionalThreshold(0.5f) },
					orientation = Orientation.Vertical,
					anchors = mapOf(
						// Maps anchor points (in px) to states
						0f to SwipeableStates.COLLAPSED,
						heightInPx to SwipeableStates.EXPANDED,
					)
				)
				.nestedScroll(connection)
		) {
//		var animateToEnd by remember { mutableStateOf(false) }
//		val progress by animateFloatAsState(
//			targetValue = if (animateToEnd) 1f else 0f,
//			animationSpec = tween(1000)
//		)
			Column() {
//				Text(text = "From: ${swipingState.progress.from}", modifier = Modifier.padding(16.dp))
//				Text(text = "To: ${swipingState.progress.to}", modifier = Modifier.padding(16.dp))
//				Text(text = swipingState.progress.fraction.toString(), modifier = Modifier.padding(16.dp))
				MotionLayoutHeader(progress =  if (swipingState.progress.to == SwipeableStates.COLLAPSED) swipingState.progress.fraction else 1f - swipingState.progress.fraction) {
					ScrollableContent()
				}
			}
//		Button(onClick = { animateToEnd = animateToEnd.not() },
//			Modifier
//				.align(Alignment.BottomCenter)
//				.padding(16.dp)) {
//			Text(text = if (!animateToEnd) "Collapse" else "Expand")
//		}
		}
	}
}

// Helper class defining swiping State
enum class SwipeableStates {
	EXPANDED,
	COLLAPSED
}