package com.example.gamesApp.ui.games

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.gamesApp.R

@Composable
fun GameTopNavBar(
    onBackClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .padding(16.dp),
    ) {
        Button(
            modifier = Modifier.size(60.dp,30.dp),
            contentPadding = PaddingValues(6.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = onBackClicked,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondary )
        ) {
            Icon(
                modifier = Modifier
                    .rotate(180f),
                painter = painterResource(id = R.drawable.right_arrow),
                contentDescription = "Go to menu",
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}