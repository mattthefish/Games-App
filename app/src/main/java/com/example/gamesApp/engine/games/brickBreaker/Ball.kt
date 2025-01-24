package com.example.gamesApp.engine.games.brickBreaker

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Velocity

class Ball {
    val color: Color = Color.White
    val diameter: Float = 100f

    var velocity: Velocity = Velocity(6f,5f)

    //TODO: bottom currently rebounds balls - not future behaviour
    fun checkWallCollision(
        screenWidth: Float,
        screenHeight: Float,
        ballOffset: Offset
    ) {
        if (ballOffset.x + diameter >= screenWidth
            || ballOffset.x <= 0
            ) {
            this.velocity = Velocity(
                -this.velocity.x,
                this.velocity.y
            )
        }
        if (ballOffset.y + diameter >= screenHeight
            || ballOffset.y <= 0
            ) {
            this.velocity = Velocity(
                this.velocity.x,
                -this.velocity.y
            )
        }
    }

    fun checkLaunchPadCollision(
        ballOffset: Offset,
        ballDiameter: Float,
        launchPadBounds: MutableState<Rect>,
    ): Offset {
        val ballRadius = ballDiameter / 2
        val ballX = ballOffset.x + ballRadius
        val ballY = ballOffset.y + ballRadius

        // Ball-Rectangle Collision Detection
        val ballLeft = ballX - ballRadius
        val ballRight = ballX + ballRadius
        val ballTop = ballY - ballRadius
        val ballBottom = ballY + ballRadius

        val launchLeft = launchPadBounds.value.left
        val launchRight = launchPadBounds.value.right
        val launchTop = launchPadBounds.value.top
        val launchBottom = launchPadBounds.value.bottom

        val ballIntersectsRectHorizontally = ballRight > launchLeft && ballLeft < launchRight
        val ballIntersectsRectVertically = ballBottom > launchTop && ballTop < launchBottom

        return if (ballIntersectsRectHorizontally &&
            ballBottom <= launchPadBounds.value.center.y &&
            ballBottom >= launchTop &&
            velocity.y > 0
        ) {
            // Ball hits the top of the rectangle
            Log.d("","BALL HIT TOP")
            this.velocity = Velocity(
                this.velocity.x,
                -this.velocity.y
            )
            Offset(ballOffset.x,launchTop - ballDiameter)
        } else if (ballIntersectsRectVertically &&
            launchPadBounds.value.center.x > ballRight &&
            ballRight > launchLeft  &&
            velocity.x > 0
        ) {
            // Ball hits the left side of the rectangle
            Log.d("","BALL HIT LEFT SIDE")
            this.velocity = Velocity(
                -this.velocity.x,
                this.velocity.y
            )
            Offset(launchLeft - ballDiameter - 1, ballOffset.y)
        } else if (ballIntersectsRectVertically &&
            launchPadBounds.value.center.x < ballLeft &&
            ballLeft < launchRight &&
            velocity.x < 0
        ) {
            // Ball hits the right side of the rectangle
            Log.d("","BALL HIT RIGHT SIDE")
            this.velocity = Velocity(
                -this.velocity.x,
                this.velocity.y
            )
            Offset(launchRight + 1f, ballOffset.y)
        } else Offset(ballOffset.x, ballOffset.y)
    }
}