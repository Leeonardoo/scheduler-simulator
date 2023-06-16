package io.github.leeonardoo.so.scheduler.ui

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
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
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.leeonardoo.so.scheduler.ui.home.ChildScreen
import io.github.leeonardoo.so.scheduler.ui.home.HomeScreen
import io.github.leeonardoo.so.scheduler.ui.navigation.ChildStack
import io.github.leeonardoo.so.scheduler.ui.navigation.ProvideComponentContext
import io.github.leeonardoo.so.scheduler.ui.navigation.Screen

@Composable
fun App() {
    val navigation = remember { StackNavigation<Screen>() }

    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface(tonalElevation = 0.5.dp) {
            ChildStack(
                source = navigation,
                initialStack = { listOf(Screen.Home) },
                handleBackButton = true,
                animation = stackAnimation(scale() + fade()),
            ) { screen ->
                when (screen) {
                    is Screen.Home -> HomeScreen(onItemClick = { navigation.push(Screen.Child) })
                    is Screen.Child -> ChildScreen(onBack = navigation::pop)
                }
            }
        }
    }
}

fun main() {
    val lifecycle = LifecycleRegistry()
    val rootComponentContext = DefaultComponentContext(lifecycle = lifecycle)

    application {
        val windowState = rememberWindowState()

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
