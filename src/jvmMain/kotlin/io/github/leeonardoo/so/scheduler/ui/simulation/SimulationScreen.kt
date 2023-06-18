package io.github.leeonardoo.so.scheduler.ui.simulation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.leeonardoo.so.scheduler.Algorithm
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess
import io.github.leeonardoo.so.scheduler.ui.ProcessDialog
import io.github.leeonardoo.so.scheduler.ui.components.ProcessCard

@Composable
fun SimulationScreen(
    viewModel: SimulationViewModel,
    algorithm: Algorithm,
    isDialogVisible: Boolean,
    onDismissDialog: () -> Unit
) {

    val items by viewModel.addedProcesses.collectAsState()

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
                    viewModel.editProcess(it)
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
                items = items,
                onClickEdit = {
                    selectedProcess = it
                },
                onClickRemove = {
                    viewModel.removeProcess(it)
                },
            )
        }
    )
}

@Composable
private fun SimulationContent(
    paddingValues: PaddingValues,
    algorithm: Algorithm,
    items: List<SimulatedProcess>,
    onClickEdit: (SimulatedProcess) -> Unit,
    onClickRemove: (SimulatedProcess) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        val processScrollState = rememberLazyListState()
        val contentScrollState = rememberScrollState()

        LazyColumn(
            modifier = Modifier
                .weight(0.42f)
                .fillMaxHeight(),
            contentPadding = PaddingValues(12.dp)
        ) {
            items(items = items, key = { it.id }) {
                ProcessCard(
                    process = it,
                    showPriority = algorithm == Algorithm.PreemptiveStaticPriority || algorithm == Algorithm.NonPreemptiveStaticPriority,
                    onClickEdit = { onClickEdit(it) },
                    onClickRemove = { onClickRemove(it) }
                )
            }
        }

        VerticalScrollbar(rememberScrollbarAdapter(processScrollState))

        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        Column(
            modifier = Modifier
                .verticalScroll(contentScrollState)
                .weight(1f)
                .background(Color.Blue)
                .fillMaxHeight()
        ) {

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