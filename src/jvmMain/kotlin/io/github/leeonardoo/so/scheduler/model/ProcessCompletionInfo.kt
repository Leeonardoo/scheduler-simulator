package io.github.leeonardoo.so.scheduler.model

import androidx.compose.runtime.Immutable

@Immutable
data class ProcessCompletionInfo(
    val completedAt: Int, // Em que ponto da linha do tempo foi finalizado
    val turnAroundTime: Int, // Quanto tempo levou para ser finalizado (desde que chegou)
    val waitingTime: Int, // Quanto tempo ficou em espera
)