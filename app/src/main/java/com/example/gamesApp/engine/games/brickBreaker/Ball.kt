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

class Ball(
    val velocity: Velocity = Velocity(0f, -5f),
    val offset: Offset = Offset(-50f,-50f)
) {
    val color: Color = Color.White
    val diameter: Float = 100f

    fun angledVelocity(
        launchPadOffset: Offset,
        launchPadWidth: Float
    ): Ball {
        var resultBall = Ball(offset = offset, velocity = velocity)
        val ballCenter = offset.x + diameter/2
        val launchPadCenter = launchPadOffset.x + launchPadWidth/2

        if (ballCenter.isBetween(launchPadOffset.x + launchPadWidth, launchPadOffset.x)) {
            val relativeImpactX = (ballCenter - launchPadCenter) / (launchPadWidth / 2)

            val clampedImpactX = relativeImpactX.coerceIn(-1f, 1f)

            val exitAngle = Math.PI / 4 + (clampedImpactX + 1) * (Math.PI / 4)

            // Compute new velocity components
            val speed = sqrt(velocity.x.pow(2) + velocity.y.pow(2)) * -1f
            val vx = speed * cos(exitAngle).toFloat()
            val vy = speed * sin(exitAngle).toFloat()

            resultBall = changeBall(
                velocity = Velocity(vx, vy),
                ball = resultBall
            )
        }
        return resultBall
    }

    private fun changeBall(velocity: Velocity? = null, offset: Offset? = null, ball: Ball): Ball {
        return Ball(
            velocity = velocity ?: ball.velocity,
            offset = offset?: ball.offset
        )
    }

    fun checkWallCollision(
        screenWidth: Float,
        screenHeight: Float,
    ): Ball {
        var resultBall = Ball()
        if (offset.x + diameter >= screenWidth
            || offset.x <= 0
        ) {
            resultBall = changeBall(
               velocity = Velocity(
                   -this.velocity.x,
                   this.velocity.y
               ),
               ball = resultBall
            )

            resultBall = if (offset.x <= 0 ) {
                changeBall(offset = Offset(0f, offset.y), ball = resultBall)
            } else {
                changeBall(offset = Offset(screenWidth - diameter, offset.y), ball = resultBall)
            }
        } else if (offset.y + diameter >= screenHeight
            || offset.y <= 0
        ) {
            resultBall = changeBall(
                velocity = Velocity(
                    this.velocity.x,
                    -this.velocity.y
                ),
                ball = resultBall
            )

            resultBall = if (offset.y <= 0 ) {
                changeBall(offset =  Offset(offset.x, 0f), ball = resultBall)
            } else {
                changeBall(offset = Offset(offset.x, screenHeight - diameter), ball = resultBall)
            }
        }
        return resultBall
    }
    // returns null if no collision detected
    fun checkRectangleCollision(
        rectBounds: Rect,
        isBrick: Boolean
    ): Ball? {
        val ballRadius = this.diameter / 2
        val ballX = offset.x + ballRadius
        val ballY = offset.y + ballRadius

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
            Ball(
                velocity = Velocity(
                    this.velocity.x,
                    -this.velocity.y
                ),
                offset =  Offset(offset.x, rectTop - this.diameter)
            )
        } else if (ballIntersectsRectHorizontally &&
            ballTop >= rectBounds.center.y &&
            ballTop <= rectBottom &&
            velocity.y < 0 &&
            isBrick
        ) {
            // Ball hits the bottom of the rectangle
            Ball(
                velocity = Velocity(
                    this.velocity.x,
                    -this.velocity.y
                ),
                offset = Offset(offset.x, rectBottom)
            )
        } else if (ballIntersectsRectVertically &&
            rectBounds.center.x > ballRight &&
            ballRight > rectLeft &&
            velocity.x > 0
        ) {
            // Ball hits the left side of the rectangle
            Ball(
                velocity = Velocity(
                    -this.velocity.x,
                    this.velocity.y
                ),
                offset = Offset(rectLeft - this.diameter - 1, offset.y)
            )
        } else if (ballIntersectsRectVertically &&
            rectBounds.center.x < ballLeft &&
            ballLeft < rectRight &&
            velocity.x < 0
        ) {
            // Ball hits the right side of the rectangle
            Ball(
                velocity = Velocity(
                    -this.velocity.x,
                    this.velocity.y
                ),
                offset = Offset(rectRight + 1f, offset.y)
            )
        } else null
    }
}