package io.github.leeonardoo.so.scheduler.ui.simulation

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.github.leeonardoo.so.scheduler.Algorithm
import io.github.leeonardoo.so.scheduler.Scheduler
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SimulationViewModel(
    val algorithm: Algorithm
) : InstanceKeeper.Instance {

    private val _addedProcesses = MutableStateFlow(emptyList<SimulatedProcess>())
    val addedProcesses = _addedProcesses.asStateFlow()

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
    }

    fun addProcess(simulatedProcess: SimulatedProcess) {
        _addedProcesses.value = _addedProcesses.value.toMutableList().also {
            it.add(simulatedProcess)
        }.sortedBy { it.arrivalTime }
    }

    fun removeProcess(simulatedProcess: SimulatedProcess) {
        _addedProcesses.value = _addedProcesses.value.toMutableList().also {
            it.remove(simulatedProcess)
        }.sortedBy { it.arrivalTime }
    }

    fun editProcess(simulatedProcess: SimulatedProcess) {
        _addedProcesses.value = _addedProcesses.value.toMutableList().also {
            it.removeIf { oldProcess -> oldProcess.id == simulatedProcess.id }
            it.add(simulatedProcess)
        }.sortedBy { it.arrivalTime }
    }

    override fun onDestroy() {

    }

}