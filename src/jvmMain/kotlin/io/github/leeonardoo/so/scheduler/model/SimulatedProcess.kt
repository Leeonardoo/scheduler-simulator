package io.github.leeonardoo.so.scheduler.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import java.util.UUID
import kotlin.random.Random

@Immutable
data class SimulatedProcess (
    val id: UUID = UUID.randomUUID(),
    val color: Color = Color.random(),
    val arrivalTime: Int, // Tempo de chegada
    val burstTime: Int, // Duração
    var remainingTime: Int = burstTime, // Tempo restante
    val priority: Int = 0, // Prioridade
    val completionInfo: ProcessCompletionInfo? = null
)

fun Color.Companion.random() = Color(
    red = Random.nextInt(256),
    green = Random.nextInt(256),
    blue = Random.nextInt(256)
)