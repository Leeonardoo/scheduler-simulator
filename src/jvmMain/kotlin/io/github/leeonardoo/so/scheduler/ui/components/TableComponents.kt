package io.github.leeonardoo.so.scheduler.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess

@Composable
fun StatisticsTable(addedProcesses: List<SimulatedProcess>) {
    val column1Weight = .085f
    val column2Weight = .13f
    val column3Weight = .13f
    val column4Weight = .13f
    val column5Weight = .16f
    val column6Weight = .16f


    ScrollbarLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TableCell(
                    text = "Processo",
                    weight = column1Weight,
                    alignment = TextAlign.Left,
                    title = true
                )

                TableCell(
                    text = "Chegada em",
                    weight = column2Weight,
                    alignment = TextAlign.Right,
                    title = true
                )

                TableCell(
                    text = "Burst time",
                    weight = column3Weight,
                    alignment = TextAlign.Right,
                    title = true
                )

                TableCell(
                    text = "Finalizado em",
                    weight = column4Weight,
                    alignment = TextAlign.Right,
                    title = true
                )

                TableCell(
                    text = "Tempo de resposta",
                    weight = column5Weight,
                    alignment = TextAlign.Right,
                    title = true
                )

                TableCell(
                    text = "Tempo em espera",
                    weight = column6Weight,
                    alignment = TextAlign.Right,
                    title = true
                )
            }
            Divider(
                color = Color.LightGray,
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxHeight()
                    .fillMaxWidth()
            )
        }

        items(
            items = addedProcesses,
            key = { it.id }
        ) { process ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(0.5.dp))
                    .animateItemPlacement(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ColorDot(
                    modifier = Modifier.weight(column1Weight),
                    process = process
                )

                TableCell(
                    text = process.arrivalTime.toString(),
                    weight = column2Weight,
                    alignment = TextAlign.Right,
                    title = false
                )

                TableCell(
                    text = process.burstTime.toString(),
                    weight = column3Weight,
                    alignment = TextAlign.Right,
                    title = false
                )

                TableCell(
                    text = process.completionInfo?.completedAt?.toString() ?: "",
                    weight = column4Weight,
                    alignment = TextAlign.Right,
                    title = false
                )

                TableCell(
                    text = process.completionInfo?.turnAroundTime?.toString() ?: "",
                    weight = column5Weight,
                    alignment = TextAlign.Right,
                    title = false
                )

                TableCell(
                    text = process.completionInfo?.waitingTime?.toString() ?: "",
                    weight = column6Weight,
                    alignment = TextAlign.Right,
                    title = false
                )
            }
            Divider(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun ColorDot(
    modifier: Modifier = Modifier,
    process: SimulatedProcess
) {
    Box(modifier = modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(process.color)
                .align(Alignment.CenterStart)
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
            .padding(8.dp),
        style = if (title) {
            MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        } else {
            MaterialTheme.typography.bodyMedium
        },
        textAlign = alignment,
    )
}