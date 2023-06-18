import io.github.leeonardoo.so.scheduler.Algorithm
import io.github.leeonardoo.so.scheduler.Scheduler

class SchedulerTest {

    fun fifoTest() {
        val scheduler = Scheduler(
            processes = Scheduler.testFIFOProcesses,
            algorithm = Algorithm.FIFO
        )

        val result = scheduler.runSimulation()

        result.forEachIndexed { index, simulatedProcess ->

        }
    }
}