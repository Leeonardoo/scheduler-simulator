package io.github.leeonardoo.so.scheduler

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.Sort
import androidx.compose.ui.graphics.vector.ImageVector
import com.arkivanov.essenty.parcelable.Parcelable

enum class Algorithm(val title: String, val label: String, val icon: ImageVector): Parcelable {

    FIFO(
        title = "FIFO (First-in-First-out)",
        label = "FIFO",
        icon = Icons.Default.FormatListNumbered
    ), // Não Preemptivo

    SJF(
        title = "SJF (Shortest-Job-First)",
        label = "SJF",
        icon = Icons.Default.Sort
    ), // Não Preemptivo

    RoundRobin(
        title = "RR (Round Robin)",
        label = "Round Robin",
        icon = Icons.Default.Loop
    ), // Preemptivo

    NonPreemptiveStaticPriority(
        title = "Prioridade (Não preemptivo, Estática)",
        label = "Prioridade",
        icon = Icons.Default.LowPriority
    ), // Não Preemptivo

    PreemptiveStaticPriority(
        title = "Prioridade (Preemptivo, Estática)",
        label = "Prioridade\n(Preemptivo)",
        icon = Icons.Default.LowPriority
    ) // Preemptivo
}