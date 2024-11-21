package com.example.gamesApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.gamesApp.ui.NavGraphs
import com.example.gamesApp.ui.theme.AppTheme
import com.ramcosta.composedestinations.DestinationsNavHost


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme{
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                )
            }
        }
    }
}

