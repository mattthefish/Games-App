package com.example.gamesApp.engine.games.brickBreaker

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Velocity
import com.example.gamesApp.ui.utils.isBetween
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class Ball {
    val color: Color = Color.White
    val diameter: Float = 100f

    var velocity: Velocity = Velocity(0f, -5f)


    fun angledVelocity(
        ballOffset: Offset,
        launchPadOffset: Offset,
        launchPadWidth: Float
    ) {
        val ballCenter = ballOffset.x + diameter/2
        val launchPadCenter = launchPadOffset.x + launchPadWidth/2

        if (ballCenter.isBetween(launchPadOffset.x + launchPadWidth, launchPadOffset.x)) {
            val relativeImpactX = (ballCenter - launchPadCenter) / (launchPadWidth / 2)

            val clampedImpactX = relativeImpactX.coerceIn(-1f, 1f)

            val exitAngle = Math.PI / 4 + (clampedImpactX + 1) * (Math.PI / 4)

            // Compute new velocity components
            val speed = sqrt(velocity.x.pow(2) + velocity.y.pow(2)) * -1f
            val vx = speed * cos(exitAngle).toFloat()
            val vy = speed * sin(exitAngle).toFloat()

            velocity = Velocity(vx, vy)
        }
    }

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
    // returns null if no collision detected
    fun checkRectangleCollision(
        ballOffset: Offset,
        rectBounds: Rect,
        isBrick: Boolean
    ): Offset? {
        val ballRadius = this.diameter / 2
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
            Offset(ballOffset.x, rectTop - this.diameter)
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
            Offset(rectLeft - this.diameter - 1, ballOffset.y)
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
        } else null
    }
}