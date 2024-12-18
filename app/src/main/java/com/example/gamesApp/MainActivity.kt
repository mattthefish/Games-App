package com.example.gamesApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.gamesApp.engine.games.TicTacToeViewModel
import com.example.gamesApp.ui.NavGraphs
import com.example.gamesApp.ui.theme.AppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myModule = module {
            viewModel { TicTacToeViewModel() }
        }

        startKoin{
            androidLogger()
            androidContext(this@MainActivity)
            modules(myModule)
        }

        setContent {
            AppTheme{
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                )
            }
        }
    }
}

