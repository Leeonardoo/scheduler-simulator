package io.github.leeonardoo.so.scheduler.ui.navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

sealed class Screen : Parcelable {
    @Parcelize
    object Home : Screen()

    @Parcelize
    object Child : Screen()
}
