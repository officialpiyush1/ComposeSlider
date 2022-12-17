@file:OptIn(ExperimentalMaterialApi::class)

package com.example.composeslider

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.layoutId
import com.example.composeslider.ui.theme.colorBackground

@Composable
fun NewlyLaunchedScreen() {
    MainUI()
}

@SuppressLint("Range")
@Composable
private fun MainUI() {
    val constraintEndSet = ConstraintSet {
        val titleRef = createRefFor("titleRef")
        val backIconRef = createRefFor("backIconRef")
        val newTextRef = createRefFor("newTextRef")
        val newTextBgRef = createRefFor("newTextBgRef")
        val contentRef = createRefFor("contentRef")

        constrain(backIconRef) {
            start.linkTo(parent.start, 24.dp)
            top.linkTo(parent.top, 24.dp)
        }

        constrain(titleRef) {
            start.linkTo(backIconRef.end, 12.dp)
            top.linkTo(parent.top, 24.dp)
            scaleX = 1f
            scaleY = 1f

        }

        constrain(newTextRef) {
            alpha = 0.0f
            translationY = (-10).dp
            end.linkTo(parent.end, 24.dp)
            top.linkTo(parent.top, 24.dp)
        }

        constrain(newTextBgRef) {
            alpha = 0.0f
            scaleX = 0.7f
            scaleY = 0.7f
            translationY = (-10).dp
            end.linkTo(parent.end, (-24).dp)
            top.linkTo(parent.top, (-24).dp)
        }

        constrain(contentRef) {
            top.linkTo(titleRef.bottom, 24.dp)
        }
    }


    val constraintStartSet = ConstraintSet {
        val titleRef = createRefFor("titleRef")
        val backIconRef = createRefFor("backIconRef")
        val newTextRef = createRefFor("newTextRef")
        val newTextBgRef = createRefFor("newTextBgRef")
        val contentRef = createRefFor("contentRef")

        constrain(backIconRef) {
            start.linkTo(parent.start, 24.dp)
            top.linkTo(parent.top, 24.dp)
        }

        constrain(titleRef) {
            start.linkTo(parent.start, 24.times(1.5f).dp)
            top.linkTo(backIconRef.bottom, 50.dp)
            scaleX = 1.2f
            scaleY = 1.2f
        }

        constrain(newTextRef) {
            alpha = 1.0f
            translationY = 0.dp
            end.linkTo(parent.end, 24.dp)
            top.linkTo(parent.top, 24.dp)
        }

        constrain(newTextBgRef) {
            end.linkTo(parent.end, (-24).dp)
            top.linkTo(parent.top, (-24).dp)
            alpha = 0.5f
            scaleX = 1.0f
            scaleY = 1.0f
        }

        constrain(contentRef) {
            top.linkTo(titleRef.bottom, 24.dp)
        }
    }


    SKMotionLayout(
        start = constraintStartSet, end = constraintEndSet
    ) { progress, swipeAbleState,connnection ->

        Icon(
            Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier.layoutId("backIconRef")
        )
        Text(
            text = "Newly Launched", fontSize = 18.sp,
            modifier = Modifier.layoutId("titleRef")
        )

        Box(
            modifier = Modifier
                .layoutId("newTextBgRef")
                .size(160.dp)
                .background(color = Color.Green.copy(alpha = 0.5f), shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .layoutId("newTextRef")
                .size(70.dp)
                .clip(RoundedCornerShape(5))
                .background(color = Color.Green),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "NEW",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier.layoutId("contentRef").fillMaxSize()
                .border(width = 2.dp, color = Color.DarkGray)
        ) {
            LazyColumn {
                items(30) {
                    Box(
                        modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth().height(50.dp)
                            .background(color = Color.Red,)
                    ) { }
                }
            }
        }
    }
}