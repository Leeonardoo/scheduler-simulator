package io.github.leeonardoo.so.scheduler

import androidx.compose.ui.graphics.Color
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess

/**
 * Em cada algoritimo, o resultado é uma lista com cada processo que estava em execução em cada ponto do tempo
 */
class Scheduler(
    private val processes: List<SimulatedProcess>,
    private val algorithm: Algorithm
) {

    fun runSimulation(): List<SimulatedProcess> {
        return when (algorithm) {
            Algorithm.FIFO -> firstInFirstOut()

            Algorithm.SJF -> shortestJobFirst()

            Algorithm.RoundRobin -> TODO()

            Algorithm.Priority -> TODO()
        }
    }

    private val byArrivalTime: List<SimulatedProcess>
        get() = processes.sortedBy { it.arrivalTime }

    private fun firstInFirstOut(): List<SimulatedProcess> {
        val sorted = byArrivalTime
        var currentTime = 0
        val scheduledProcesses = mutableListOf<SimulatedProcess>()

        sorted.forEach { process ->
            // Adiciona todos que já chegaram nesse momento
            while (currentTime < process.arrivalTime) {
                scheduledProcesses.add(process)
            }

            // Simula a execução
            repeat(process.burstTime) {
                scheduledProcesses.add(
                    process.copy()
                )

                currentTime += 1
            }
        }

        return scheduledProcesses
    }

    private fun shortestJobFirst(): List<SimulatedProcess> {
        val queue = byArrivalTime.toMutableList()
        var currentTime = 0
        val scheduledProcesses = mutableListOf<SimulatedProcess>()

        // Continua enquanto ainda há processos para serem escalonados
        while (queue.isNotEmpty()) {
            var currentShortestIndex: Int? = null

            // Percorre todos os processos disponíveis
            queue.forEachIndexed { index, process ->
                // Verifica se já chegou
                if (process.arrivalTime <= currentTime) {
                    // Se é o primeiro processo ou é mais curto que o mais curto atual, atualiza currentShortestIndex
                    if (currentShortestIndex == null || process.burstTime < queue[currentShortestIndex!!].burstTime) {
                        currentShortestIndex = index
                    }
                }
            }

            // Se pode ser escalonado, deve continuar sem parar até finalizar
            if (currentShortestIndex != null) {
                val process = queue[currentShortestIndex!!]
                repeat(process.burstTime) {
                    scheduledProcesses.add(
                        SimulatedProcess(
                            color = process.color,
                            arrivalTime = process.arrivalTime,
                            burstTime = process.burstTime,
                            priority = process.priority
                        )
                    )
                    currentTime++
                }

                // Ao finalizar, remove da fila de processos
                queue.removeAt(currentShortestIndex!!)
            } else {
                // Se nenhum processo pode executar, adiciona um período vazio de cor transparente
                scheduledProcesses.add(
                    SimulatedProcess(
                        color = Color.Transparent,
                        arrivalTime = currentTime,
                        burstTime = 1,
                        priority = 0
                    )
                )
                currentTime++
            }
        }

        return scheduledProcesses
    }

    companion object {
        val testFIFOProcesses: List<SimulatedProcess>
            get() = listOf(
                SimulatedProcess(
                    arrivalTime = 0,
                    burstTime = 9
                ),
                SimulatedProcess(
                    arrivalTime = 5,
                    burstTime = 11
                ),
                SimulatedProcess(
                    arrivalTime = 5,
                    burstTime = 7
                ),
                SimulatedProcess(
                    arrivalTime = 11,
                    burstTime = 14
                )
            )

        val testSJFProcesses: List<SimulatedProcess>
            get() = listOf(
                SimulatedProcess(
                    arrivalTime = 0,
                    burstTime = 9
                ),
                SimulatedProcess(
                    arrivalTime = 5,
                    burstTime = 11
                ),
                SimulatedProcess(
                    arrivalTime = 5,
                    burstTime = 7
                ),
                SimulatedProcess(
                    arrivalTime = 29,
                    burstTime = 14
                )
            )
    }
}