package com.example.gamesApp.engine.games.brickBreaker

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Velocity

class Ball {
    val color: Color = Color.White
    val diameter: Float = 100f

    var velocity: Velocity = Velocity(10f, 30f)

    //TODO: bottom currently rebounds balls - not future behaviour
    fun checkWallCollision(
        screenWidth: Float,
        screenHeight: Float,
        ballOffset: Offset
    ): Offset {
        if (ballOffset.x + diameter >= screenWidth
            || ballOffset.x <= 0
        ) {
            this.velocity = Velocity(
                -this.velocity.x,
                this.velocity.y
            )
            return if (ballOffset.x <= 0 ) {
                Offset(0f, ballOffset.y)
            } else {
                Offset(screenWidth - diameter, ballOffset.y)
            }
        } else if (ballOffset.y + diameter >= screenHeight
            || ballOffset.y <= 0
        ) {
            this.velocity = Velocity(
                this.velocity.x,
                -this.velocity.y
            )
            return if (ballOffset.y <= 0 ) {
                Offset(ballOffset.x, 0f)
            } else {
                Offset(ballOffset.x, screenHeight - diameter)
            }
        }
        else {
            return ballOffset
        }
    }

    fun checkRectangleCollision(
        ballOffset: Offset,
        ballDiameter: Float,
        rectBounds: Rect,
        isBrick: Boolean
    ): Offset {
        val ballRadius = ballDiameter / 2
        val ballX = ballOffset.x + ballRadius
        val ballY = ballOffset.y + ballRadius

        // Ball-Rectangle Collision Detection
        val ballLeft = ballX - ballRadius
        val ballRight = ballX + ballRadius
        val ballTop = ballY - ballRadius
        val ballBottom = ballY + ballRadius

        val rectLeft = rectBounds.left
        val rectRight = rectBounds.right
        val rectTop = rectBounds.top
        val rectBottom = rectBounds.bottom

        val ballIntersectsRectHorizontally = ballRight > rectLeft && ballLeft < rectRight
        val ballIntersectsRectVertically = ballBottom > rectTop && ballTop < rectBottom

        return if (ballIntersectsRectHorizontally &&
            ballBottom <= rectBounds.center.y &&
            ballBottom >= rectTop &&
            velocity.y > 0
        ) {
            // Ball hits the top of the rectangle
            this.velocity = Velocity(
                this.velocity.x,
                -this.velocity.y
            )
            Offset(ballOffset.x, rectTop - ballDiameter)
        } else if (ballIntersectsRectHorizontally &&
            ballTop >= rectBounds.center.y &&
            ballTop <= rectBottom &&
            velocity.y < 0 &&
            isBrick
        ) {
            // Ball hits the bottom of the rectangle
            this.velocity = Velocity(
                this.velocity.x,
                -this.velocity.y
            )
            Offset(ballOffset.x, rectBottom)
        } else if (ballIntersectsRectVertically &&
            rectBounds.center.x > ballRight &&
            ballRight > rectLeft &&
            velocity.x > 0
        ) {
            // Ball hits the left side of the rectangle
            this.velocity = Velocity(
                -this.velocity.x,
                this.velocity.y
            )
            Offset(rectLeft - ballDiameter - 1, ballOffset.y)
        } else if (ballIntersectsRectVertically &&
            rectBounds.center.x < ballLeft &&
            ballLeft < rectRight &&
            velocity.x < 0
        ) {
            // Ball hits the right side of the rectangle
            this.velocity = Velocity(
                -this.velocity.x,
                this.velocity.y
            )
            Offset(rectRight + 1f, ballOffset.y)
        } else Offset(ballOffset.x, ballOffset.y)
    }
}