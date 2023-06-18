package io.github.leeonardoo.so.scheduler.ui.simulation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.leeonardoo.so.scheduler.Algorithm
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess
import io.github.leeonardoo.so.scheduler.ui.ProcessDialog
import io.github.leeonardoo.so.scheduler.ui.components.ProcessCard
import io.github.leeonardoo.so.scheduler.ui.components.ScrollbarLazyColumn
import io.github.leeonardoo.so.scheduler.ui.components.StatisticsTable
import io.github.leeonardoo.so.scheduler.ui.components.Timeline

@Composable
fun SimulationScreen(
    viewModel: SimulationViewModel,
    algorithm: Algorithm,
    isDialogVisible: Boolean,
    onDismissDialog: () -> Unit
) {

    val items by viewModel.addedProcesses.collectAsState()
    val scheduledProcesses by viewModel.scheduledProcesses.collectAsState()
    val avgTurnArround by viewModel.avgTurnArround.collectAsState()
    val avgWaiting by viewModel.avgWaiting.collectAsState()

    var selectedProcess by remember {
        mutableStateOf<SimulatedProcess?>(null)
    }

    if (isDialogVisible || selectedProcess != null) {
        ProcessDialog(
            algorithm = algorithm,
            process = selectedProcess,
            onDismiss = {
                selectedProcess = null
                onDismissDialog()
            },
            onClickConfirm = {
                if (selectedProcess != null) {
                    viewModel.updateProcess(it)
                    selectedProcess = null
                } else {
                    viewModel.addProcess(it)
                }

                onDismissDialog()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(algorithm.title)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
                )
            )
        },
        content = {
            SimulationContent(
                paddingValues = it,
                algorithm = algorithm,
                addedProcesses = items,
                onClickEdit = {
                    selectedProcess = it
                },
                onClickRemove = {
                    viewModel.removeProcess(it)
                },
                scheduledProcesses = scheduledProcesses,
                avgTurnArround = avgTurnArround,
                avgWaiting = avgWaiting
            )
        }
    )
}

@Composable
private fun SimulationContent(
    paddingValues: PaddingValues,
    algorithm: Algorithm,
    addedProcesses: List<SimulatedProcess>,
    onClickEdit: (SimulatedProcess) -> Unit,
    onClickRemove: (SimulatedProcess) -> Unit,
    scheduledProcesses: List<SimulatedProcess>,
    avgTurnArround: Double,
    avgWaiting: Double
) {
    Row(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        ScrollbarLazyColumn(
            modifier = Modifier
                .weight(0.32f)
                .fillMaxHeight(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = addedProcesses, key = { it.id }) {
                ProcessCard(
                    modifier = Modifier.animateItemPlacement(),
                    process = it,
                    showPriority = algorithm == Algorithm.PreemptiveStaticPriority || algorithm == Algorithm.NonPreemptiveStaticPriority,
                    onClickEdit = { onClickEdit(it) },
                    onClickRemove = { onClickRemove(it) }
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(top = 12.dp)
        ) {
            if (scheduledProcesses.isNotEmpty()) {

                // Timeline
                Timeline(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    scheduledProcesses = scheduledProcesses
                )

                Spacer(Modifier.height(16.dp))

                // Overall
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = "Estatísticas gerais",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = "Tempo médio de espera: ${String.format("%.02f", avgWaiting)}"
                )
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = "Tempo médio de resposta: ${String.format("%.02f", avgTurnArround)}"
                )

                // Statistics table
                StatisticsTable(addedProcesses)
            }
        }
    }
}

@Preview
@Composable
fun SimulationScreenPreview() {
    MaterialTheme(darkColorScheme()) {
        SimulationScreen(
            viewModel = SimulationViewModel(algorithm = Algorithm.FIFO),
            algorithm = Algorithm.FIFO,
            isDialogVisible = false,
            onDismissDialog = {}
        )
    }
}

