package com.example.gamesApp.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Collects from a StateFlow, automatically cancelling the collection when the lifecycle of the
 * lifecycle owner is not at least resumed, and automatically restarting the collection when it
 * resumes again. NB: This cancels the collection, not suspends it, when not at least resumed.
 */
@Suppress("StateFlowValueCalledInComposition")
@Composable
fun <T> StateFlow<T>.collectAsStateRepeatedly(
    context: CoroutineContext = EmptyCoroutineContext
): State<T> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val flowLifecycleAware = remember(this, lifecycleOwner) {
        flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }
    return flowLifecycleAware
        .collectAsState(value, context)
}

/**
 * Collects from a Flow, automatically cancelling the collection when the lifecycle of the
 * lifecycle owner is not at least resumed, and automatically restarting the collection when it
 * resumes again. NB: This cancels the collection, not suspends it, when not at least resumed.
 */
@Composable
fun <T : R, R> Flow<T>.collectAsStateRepeatedly(
    initial: R,
    context: CoroutineContext = EmptyCoroutineContext
): State<R> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val flowLifecycleAware = remember(this, lifecycleOwner) {
        flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }
    return flowLifecycleAware.collectAsState(initial, context)
}
