package io.github.leeonardoo.so.scheduler

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.Sort
import androidx.compose.ui.graphics.vector.ImageVector

enum class Algorithm(val label: String, val abbreviatedName: String, val icon: ImageVector) {

    FIFO(
        label = "FIFO (First-in-First-out",
        abbreviatedName = "FIFO",
        icon = Icons.Default.FormatListNumbered
    ), // Não Preemptivo

    SJF(
        label = "SJF (Shortest-Job-First",
        abbreviatedName = "SJF",
        icon = Icons.Default.Sort
    ), // Não Preemptivo

    RoundRobin(
        label = "RR (Round Robin)",
        abbreviatedName = "Round Robin",
        icon = Icons.Default.Loop
    ), // Preemptivo

    NonPreemptiveStaticPriority(
        label = "Prioridade (Não preemptivo, Estática)",
        abbreviatedName = "Prioridade",
        icon = Icons.Default.LowPriority
    ), // Não Preemptivo

    PreemptiveStaticPriority(
        label = "Prioridade (Preemptivo, Estática)",
        abbreviatedName = "Prioridade\n(Preemptivo)",
        icon = Icons.Default.LowPriority
    ) // Preemptivo
}