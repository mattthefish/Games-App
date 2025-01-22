package com.example.gamesApp.engine.games.brickBreaker

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Velocity
import java.util.Vector

class Ball {
    val color: Color = Color.White
    val diameter: Float = 100f

    var velocity: Velocity = Velocity(6f,5f)

    var direction: Vector<Float> = Vector()

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
        launchPadOffset: Offset,
        launchPad: LaunchPad
    ) {
        if (ballOffset.x + this.diameter >= launchPadOffset.x
            && ballOffset.x <= launchPadOffset.x + launchPad.width
            && ballOffset.y + this.diameter >= launchPadOffset.y
            && ballOffset.y <= launchPadOffset.y + launchPad.height
        ) {
            if (ballOffset.y + this.diameter == launchPadOffset.y) {
                this.velocity = Velocity(
                    this.velocity.x,
                    -this.velocity.y
                )
            }
            if (ballOffset.x + this.diameter == launchPadOffset.x
                || ballOffset.x == launchPadOffset.x + launchPad.width
            ) {
                this.velocity = Velocity(
                    -this.velocity.x,
                    this.velocity.y
                )
            }
        }
    }
}