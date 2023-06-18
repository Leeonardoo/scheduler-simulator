package io.github.leeonardoo.so.scheduler.ui.simulation

import io.github.leeonardoo.so.scheduler.Algorithm
import io.github.leeonardoo.so.scheduler.Scheduler
import io.github.leeonardoo.so.scheduler.model.ProcessCompletionInfo
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SimulationViewModel(
    val algorithm: Algorithm
) {

    private val _addedProcesses = MutableStateFlow(emptyList<SimulatedProcess>())
    val addedProcesses = _addedProcesses.asStateFlow()

    private val _scheduledProcesses = MutableStateFlow(emptyList<SimulatedProcess>())
    val scheduledProcesses = _scheduledProcesses.asStateFlow()

    private val _avgTurnArround = MutableStateFlow(0.0)
    val avgTurnArround = _avgTurnArround.asStateFlow()

    private val _avgWaiting = MutableStateFlow(0.0)
    val avgWaiting = _avgWaiting.asStateFlow()

    init {
        println("Initialized")

        when (algorithm) {
            Algorithm.FIFO -> {
                _addedProcesses.value = Scheduler.testFIFOProcesses
            }

            Algorithm.SJF -> {
                _addedProcesses.value = Scheduler.testSJFProcesses
            }

            Algorithm.RoundRobin -> {
                _addedProcesses.value = Scheduler.testRRProcesses
            }

            Algorithm.NonPreemptiveStaticPriority -> {
                _addedProcesses.value = Scheduler.testnonPreemptiveStaticPriorityProcesses
            }

            Algorithm.PreemptiveStaticPriority -> {
                _addedProcesses.value = Scheduler.testPreemptiveStaticPriorityProcesses
            }
        }

        simulate()
    }

    // Redefinir todas as propriedades que podiam ter sido alteradas pelo Scheduler
    private fun prepareItems() {
        _addedProcesses.value = _addedProcesses.value.map {
            it.copy(completionInfo = null, remainingTime = it.burstTime)
        }
    }

    fun addProcess(simulatedProcess: SimulatedProcess) {
        _addedProcesses.value = _addedProcesses.value.toMutableList().also {
            it.add(simulatedProcess)
        }.sortedBy { it.arrivalTime }

        simulate()
    }

    fun removeProcess(simulatedProcess: SimulatedProcess) {
        _addedProcesses.value = _addedProcesses.value.toMutableList().also {
            it.removeAll { oldProcess -> oldProcess.id == simulatedProcess.id }
        }.sortedBy { it.arrivalTime }

        simulate()
    }

    fun updateProcess(simulatedProcess: SimulatedProcess) {
        _addedProcesses.value = _addedProcesses.value.toMutableList().also {
            it.removeAll { oldProcess -> oldProcess.id == simulatedProcess.id }
            it.add(simulatedProcess)
        }.sortedBy { it.arrivalTime }

        simulate()
    }

    private fun simulate() {
        CoroutineScope(Dispatchers.Default).launch {
            prepareItems()

            if (_addedProcesses.value.isNotEmpty()) {
                val scheduler = Scheduler(processes = _addedProcesses.value, algorithm = algorithm)
                _scheduledProcesses.value = scheduler.simulate()
            } else {
                _scheduledProcesses.value = emptyList()
            }

            calculateStatistics()
        }
    }

    private fun calculateStatistics() {
        // Calcular as informações para cada processo
        _addedProcesses.value.forEach { process ->
            val arrivalTime = process.arrivalTime
            var completedAt = 0
            var waitingTime = 0

            // Encontrar o tempo de conclusão do processo
            _scheduledProcesses.value.forEachIndexed { scheduledIndex, scheduledProcess ->
                if (scheduledProcess.id == process.id) {
                    completedAt = scheduledIndex
                }
            }

            // Calcular o tempo de resposta (turnaround)
            val turnAroundTime = completedAt - arrivalTime

            // Calcular o tempo de espera do processo
            for (foundIndex in arrivalTime until completedAt) {
                if (_scheduledProcesses.value[foundIndex].id != process.id) {
                    waitingTime += 1
                }
            }

            // Atualizar as informações do processo
            val completedProcess = process.copy(
                completionInfo = ProcessCompletionInfo(
                    completedAt = completedAt,
                    turnAroundTime = turnAroundTime + 1,
                    waitingTime = waitingTime
                )
            )

            _addedProcesses.value = _addedProcesses.value.toMutableList().also {
                it.removeIf { oldProcess -> oldProcess.id == completedProcess.id }
                it.add(completedProcess)
            }.sortedBy { it.arrivalTime }
        }

        // Overall
        // Calcular o tempo médio de espera e o tempo médio de resposta
        var avgWaitingTime = 0.0
        var avgTurnaroundTime = 0.0
        _addedProcesses.value.forEach { process ->
            avgWaitingTime += process.completionInfo?.waitingTime ?: 0
            avgTurnaroundTime += process.completionInfo?.turnAroundTime ?: 0
        }

        // Calcular a média se houver processos
        if (_addedProcesses.value.isNotEmpty()) {
            avgWaitingTime /= _addedProcesses.value.size
            avgTurnaroundTime /= _addedProcesses.value.size
        }

        // Atualizar as estatísticas gerais
        _avgTurnArround.value = avgWaitingTime
        _avgWaiting.value = avgTurnaroundTime
    }

}