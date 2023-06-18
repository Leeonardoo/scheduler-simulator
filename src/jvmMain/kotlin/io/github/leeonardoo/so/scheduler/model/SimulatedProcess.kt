package io.github.leeonardoo.so.scheduler.model

import androidx.compose.ui.graphics.Color
import java.util.UUID
import kotlin.random.Random

data class SimulatedProcess (
    val id: UUID = UUID.randomUUID(),
    val color: Color = Color.random(),
    val arrivalTime: Int, // Tempo de chegada
    val burstTime: Int, // Duração
    val priority: Int? = null, // Prioridade (se tiver)
    val completionInfo: ProcessCompletionInfo? = null
)

fun Color.Companion.random() = Color(
    red = Random.nextInt(256),
    green = Random.nextInt(256),
    blue = Random.nextInt(256)
)