package io.github.leeonardoo.so.scheduler

import androidx.compose.ui.graphics.Color
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess
import kotlin.math.min

/**
 * Em cada algoritimo, o resultado é uma lista com cada processo que estava em execução em cada ponto do tempo
 */
class Scheduler(
    private val processes: List<SimulatedProcess>,
    private val algorithm: Algorithm,
    private val quantum: Int = 1
) {

    private val byArrivalTime: List<SimulatedProcess>
        get() = processes.sortedBy { it.arrivalTime }

    fun simulate(): List<SimulatedProcess> {
        return when (algorithm) {
            Algorithm.FIFO -> firstInFirstOut()

            Algorithm.SJF -> shortestJobFirst()

            Algorithm.RoundRobin -> roundRobin()

            Algorithm.NonPreemptiveStaticPriority -> nonPreemptiveStaticPriority()

            Algorithm.PreemptiveStaticPriority -> preemptiveStaticPriority()
        }
    }

    private fun firstInFirstOut(): List<SimulatedProcess> {
        val processQueue = byArrivalTime
        var currentTime = 0
        val scheduledProcesses = mutableListOf<SimulatedProcess>()

        processQueue.forEach { process ->
            // Se não há nenhum processo para executar, adiciona um período de espera de cor transparente
            while (currentTime < process.arrivalTime) {
                scheduledProcesses.add(
                    SimulatedProcess(
                        color = Color.Transparent,
                        arrivalTime = currentTime,
                        burstTime = 1
                    )
                )

                currentTime += 1
            }

            // Executa o processo até finalizar
            repeat(process.burstTime) {
                scheduledProcesses.add(process.copy())

                currentTime += 1
            }
        }

        return scheduledProcesses
    }

    private fun shortestJobFirst(): List<SimulatedProcess> {
        val processQueue = byArrivalTime.toMutableList()
        var currentTime = 0
        val scheduledProcesses = mutableListOf<SimulatedProcess>()

        // Continua enquanto ainda há processos para escalonar
        while (processQueue.isNotEmpty()) {
            var shortestIndex = -1

            // Percorre todos os processos disponíveis
            processQueue.forEachIndexed { index, process ->
                // Verifica se já chegou
                if (process.arrivalTime <= currentTime) {
                    // Se é o primeiro processo ou é mais curto que o mais curto atual, atualiza currentShortestIndex
                    if (shortestIndex == -1 || process.burstTime < processQueue[shortestIndex].burstTime) {
                        shortestIndex = index
                    }
                }
            }

            // Se pode ser escalonado, deve continuar sem parar até finalizar
            if (shortestIndex != -1) {
                val process = processQueue[shortestIndex]
                repeat(process.burstTime) {
                    scheduledProcesses.add(process.copy())

                    currentTime++
                }

                // Ao finalizar, remove da fila de processos
                processQueue.removeAt(shortestIndex)
            } else {
                // Se nenhum processo pode executar, adiciona um período de espera de cor transparente
                scheduledProcesses.add(
                    SimulatedProcess(
                        color = Color.Transparent,
                        arrivalTime = currentTime,
                        burstTime = 1
                    )
                )

                currentTime++
            }
        }

        return scheduledProcesses
    }

    private fun roundRobin(): List<SimulatedProcess> {
        val processQueue = byArrivalTime.toMutableList()
        var currentTime = 0
        val scheduledProcesses = mutableListOf<SimulatedProcess>()

        // Continua enquanto ainda há processos para escalonar
        while (processQueue.isNotEmpty()) {
            var hasRun = false
            var index = 0

            // Verifica se o processo pode rodar (não é o último)
            while (index < processQueue.size) {
                val process = processQueue[index]

                // Se o processo ainda não chegou, sai do loop. E devido à ordenação, nenhum outro processo chegou ainda
                if (process.arrivalTime > currentTime) {
                    break
                } else {
                    // Roda esse processo pela duração do quantum ou do tempo restante, o que for menor
                    hasRun = true
                    val duration = min(quantum, process.burstTime)

                    repeat(duration) {
                        scheduledProcesses.add(process.copy())
                        currentTime++
                        process.remainingTime--
                    }

                    // Se finalizou a execução, remove o processo da fila
                    if (process.remainingTime <= 0) {
                        processQueue.removeAt(index)
                        index-- // Volta um index para compensar a remoção e não ficar fora dos limites
                    }
                }
                index++
            }

            // Se nenhum processo pode executar, adiciona um período vazio de cor transparente
            if (!hasRun) {
                scheduledProcesses.add(
                    SimulatedProcess(
                        color = Color.Transparent,
                        arrivalTime = currentTime,
                        burstTime = 1
                    )
                )

                currentTime++
            }
        }

        return scheduledProcesses
    }

    private fun nonPreemptiveStaticPriority(): List<SimulatedProcess> {
        val processQueue = byArrivalTime.toMutableList()
        var currentTime = 0
        val scheduledProcesses = mutableListOf<SimulatedProcess>()

        // Continua enquanto ainda há processos para escalonar
        while (processQueue.isNotEmpty()) {
            // Encontra o processo com a maior prioridade que já chegou
            val currentHighestPriorityProcess = processQueue
                .filter { it.arrivalTime <= currentTime }
                .maxByOrNull { it.priority }

            if (currentHighestPriorityProcess != null) {
                // Executa o processo até finalizar
                repeat(currentHighestPriorityProcess.burstTime) {
                    scheduledProcesses.add(currentHighestPriorityProcess.copy())

                    currentTime++
                }

                // Ao finalizar, remove da fila de processos
                processQueue.remove(currentHighestPriorityProcess)
            } else {
                // Se nenhum processo pode executar, adiciona um período de espera de cor transparente
                scheduledProcesses.add(
                    SimulatedProcess(
                        color = Color.Transparent,
                        arrivalTime = currentTime,
                        burstTime = 1
                    )
                )

                currentTime++
            }
        }

        return scheduledProcesses
    }

    private fun preemptiveStaticPriority(): List<SimulatedProcess> {
        val processQueue = byArrivalTime.toMutableList()
        var currentTime = 0
        val scheduledProcesses = mutableListOf<SimulatedProcess>()

        // Continua enquanto ainda há processos para escalonar
        while (processQueue.isNotEmpty()) {
            // Encontra o processo com a maior prioridade que já chegou
            val currentHighestPriorityProcess = processQueue
                .filter { it.arrivalTime <= currentTime }
                .sortedBy { it.arrivalTime }
                .maxByOrNull { it.priority }

            if (currentHighestPriorityProcess != null) {
                // Roda esse processo pela duração do quantum ou do tempo restante, o que for menor
                val duration = min(quantum, currentHighestPriorityProcess.remainingTime)

                repeat(duration) {
                    scheduledProcesses.add(currentHighestPriorityProcess.copy())

                    currentTime++
                    currentHighestPriorityProcess.remainingTime--
                }

                processQueue[processQueue.indexOf(currentHighestPriorityProcess)] =
                    currentHighestPriorityProcess.copy(arrivalTime = currentHighestPriorityProcess.arrivalTime + duration)

                // Remove o processo atual da fila se o tempo de execução restante for 0
                if (currentHighestPriorityProcess.remainingTime <= 0) {
                    processQueue.remove(currentHighestPriorityProcess)
                }
            } else {
                // Se nenhum processo pode executar, adiciona um período vazio de cor transparente
                scheduledProcesses.add(
                    SimulatedProcess(
                        color = Color.Transparent,
                        arrivalTime = currentTime,
                        burstTime = 1
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

        val testRRProcesses: List<SimulatedProcess>
            get() = listOf(
                SimulatedProcess(
                    arrivalTime = 0,
                    burstTime = 9
                ),
                SimulatedProcess(
                    arrivalTime = 5,
                    burstTime = 7
                ),
                SimulatedProcess(
                    arrivalTime = 6,
                    burstTime = 11
                ),
                SimulatedProcess(
                    arrivalTime = 29,
                    burstTime = 14
                )
            )

        val testnonPreemptiveStaticPriorityProcesses: List<SimulatedProcess>
            get() = listOf(
                SimulatedProcess(
                    arrivalTime = 0,
                    burstTime = 10,
                    priority = 0
                ),
                SimulatedProcess(
                    arrivalTime = 2,
                    burstTime = 5,
                    priority = 1
                ),
                SimulatedProcess(
                    arrivalTime = 9,
                    burstTime = 10,
                    priority = 2
                )
            )

        val testPreemptiveStaticPriorityProcesses: List<SimulatedProcess>
            get() = listOf(
                SimulatedProcess(
                    arrivalTime = 0,
                    burstTime = 10,
                    priority = 0
                ),
                SimulatedProcess(
                    arrivalTime = 1,
                    burstTime = 6,
                    priority = 1
                ),
                SimulatedProcess(
                    arrivalTime = 2,
                    burstTime = 7,
                    priority = 1
                ),
                SimulatedProcess(
                    arrivalTime = 9,
                    burstTime = 10,
                    priority = 2
                )
            )
    }
}
