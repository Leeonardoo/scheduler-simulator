package io.github.leeonardoo.so.scheduler.ui.simulation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.leeonardoo.so.scheduler.Algorithm
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess
import io.github.leeonardoo.so.scheduler.ui.ProcessDialog
import io.github.leeonardoo.so.scheduler.ui.components.ProcessCard
import io.github.leeonardoo.so.scheduler.ui.components.ScrollbarLazyColumn
import io.github.leeonardoo.so.scheduler.ui.components.Timeline

data class Invoice(val invoice: String, val date: String, val status: String, val amount: String)

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
                items = items,
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
    items: List<SimulatedProcess>,
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
            items(items = items, key = { it.id }) {
                ProcessCard(
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
            }
        }

//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxHeight()
//        ) {
//            val contentScrollState = rememberLazyListState()
//
//            val invoiceList = listOf(
//                Invoice("51023", "15/04/2023", "Unpaid", amount = "$2,600"),
//                Invoice("51024", "17/04/2023", "Pending", amount = "$900"),
//                Invoice("51025", "20/04/2023", "Paid", amount = "$7,560"),
//                Invoice("51026", "23/04/2023", "Pending", amount = "$300"),
//                Invoice("51027", "30/04/2023", "Paid", amount = "$5,890"),
//            )
//            val column1Weight = .2f
//            val column2Weight = .3f
//            val column3Weight = .25f
//            val column4Weight = .25f
//
//
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                state = contentScrollState,
//                contentPadding = PaddingValues(12.dp),
//                verticalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                item {
//                    Row(
//                        Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        TableCell(
//                            text = "Invoice",
//                            weight = column1Weight,
//                            alignment = TextAlign.Left,
//                            title = true
//                        )
//                        TableCell(text = "Date", weight = column2Weight, title = true)
//                        TableCell(text = "Status", weight = column3Weight, title = true)
//                        TableCell(
//                            text = "Amount",
//                            weight = column4Weight,
//                            alignment = TextAlign.Right,
//                            title = true
//                        )
//                    }
//                    Divider(
//                        color = Color.LightGray,
//                        modifier = Modifier
//                            .height(1.dp)
//                            .fillMaxHeight()
//                            .fillMaxWidth()
//                    )
//                }
//
//                itemsIndexed(invoiceList) { _, invoice ->
//                    Row(
//                        Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        TableCell(
//                            text = invoice.invoice,
//                            weight = column1Weight,
//                            alignment = TextAlign.Left
//                        )
//                        TableCell(text = invoice.date, weight = column2Weight)
//                        //StatusCell(text = invoice.status, weight = column3Weight)
//                        TableCell(
//                            text = invoice.amount,
//                            weight = column4Weight,
//                            alignment = TextAlign.Right
//                        )
//                    }
//                    Divider(
//                        color = Color.LightGray,
//                        modifier = Modifier
//                            .height(1.dp)
//                            .fillMaxHeight()
//                            .fillMaxWidth()
//                    )
//                }
//            }
//
//            VerticalScrollbar(
//                modifier = Modifier.align(Alignment.CenterEnd),
//                adapter = rememberScrollbarAdapter(scrollState = contentScrollState),
//                style = LocalScrollbarStyle.current.copy(
//                    unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
//                    hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
//                )
//            )
//        }
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

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    alignment: TextAlign = TextAlign.Center,
    title: Boolean = false
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(10.dp),
        fontWeight = if (title) FontWeight.Bold else FontWeight.Normal,
        textAlign = alignment,
    )
}
