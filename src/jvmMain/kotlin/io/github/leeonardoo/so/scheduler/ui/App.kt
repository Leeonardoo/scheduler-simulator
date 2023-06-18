package io.github.leeonardoo.so.scheduler.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.leeonardoo.so.scheduler.Algorithm
import io.github.leeonardoo.so.scheduler.ui.main.NavigationRailScaffold
import io.github.leeonardoo.so.scheduler.ui.navigation.ChildStack
import io.github.leeonardoo.so.scheduler.ui.navigation.ProvideComponentContext
import io.github.leeonardoo.so.scheduler.ui.simulation.SimulationScreen

@Composable
fun App() {
    val navigation = remember { StackNavigation<Algorithm>() }

    var selectedAlgorithm by remember {
        mutableStateOf(Algorithm.FIFO)
    }

    var isDialogVisible by remember {
        mutableStateOf(false)
    }

    MaterialTheme(colorScheme = darkColorScheme()) {
        if (isDialogVisible) {
            ProcessDialog(algorithm = selectedAlgorithm, onDismiss = {isDialogVisible = false})
        }

        NavigationRailScaffold(
            onClickFab = {
                isDialogVisible = true
            },
            onClickItem = {
                selectedAlgorithm = it
                navigation.navigate { _ ->
                    listOf(it)
                }
            },
            selectedAlgorithm = selectedAlgorithm,
            content = {
                Surface(modifier = Modifier.weight(1f), tonalElevation = 0.5.dp) {
                    ChildStack(
                        source = navigation,
                        initialStack = { listOf(Algorithm.FIFO) },
                        handleBackButton = true,
                        animation = stackAnimation(scale() + fade()),
                    ) { algorithm ->
                        SimulationScreen(algorithm)
                    }
                }
            }
        )
    }
}

fun main() {
    val lifecycle = LifecycleRegistry()
    val rootComponentContext = DefaultComponentContext(lifecycle = lifecycle)

    application {
        val windowState = rememberWindowState(size = DpSize(1280.dp, 720.dp))

        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Simulador de Schedulers"
        ) {
            CompositionLocalProvider(LocalScrollbarStyle provides defaultScrollbarStyle()) {
                ProvideComponentContext(rootComponentContext) {
                    App()
                }
            }
        }
    }
}
