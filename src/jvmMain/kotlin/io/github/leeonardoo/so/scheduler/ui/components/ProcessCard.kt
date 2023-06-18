package io.github.leeonardoo.so.scheduler.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProcessCard(
    modifier: Modifier = Modifier,
    process: SimulatedProcess,
    showPriority: Boolean,
    onClickEdit: () -> Unit = {},
    onClickRemove: () -> Unit = {}
) {

    var isMenuExpanded by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf<Offset?>(null) }

    Card(
        modifier = modifier
            .wrapContentHeight()
            .onPointerEvent(eventType = PointerEventType.Press) {
                if (it.type == PointerEventType.Press && it.buttons.isSecondaryPressed) {
                    it.changes.forEach { e ->
                        menuOffset = e.position
                        isMenuExpanded = true
                        e.consume()
                    }
                }
            }
    ) {
        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            offset = menuOffset?.let { DpOffset(it.x.dp, -it.y.dp) } ?: DpOffset(0.dp, 0.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Text("Editar")
                },
                leadingIcon = {
                    Icon(Icons.Outlined.Edit, null)
                },
                onClick = {
                    isMenuExpanded = false
                    onClickEdit()
                }
            )

            DropdownMenuItem(
                text = {
                    Text("Remover")
                },
                leadingIcon = {
                    Icon(Icons.Outlined.Delete, null)
                },
                onClick = {
                    isMenuExpanded = false
                    onClickRemove()
                }
            )
        }

        Row(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(process.color)
            )

            Spacer(Modifier.width(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row {
                    TitleItem(text = "Chegada")
                    InfoItem(text = process.arrivalTime.toString())
                }

                Row {
                    TitleItem(text = "Burst")
                    InfoItem(text = process.burstTime.toString())
                }

                if (showPriority) {
                    Row {
                        TitleItem(text = "Prioridade")
                        InfoItem(text = process.priority.toString())
                    }
                }
            }
        }
    }
}


@Composable
private fun RowScope.TitleItem(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        modifier = modifier.weight(1f),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun InfoItem(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
fun ProcessCardPreview() {
    MaterialTheme {
        Surface(modifier = Modifier.height(80.dp).width(200.dp)) {
            ProcessCard(
                process = SimulatedProcess(
                    color = Color.Green,
                    arrivalTime = 2,
                    burstTime = 1,
                    priority = 2
                ),
                showPriority = false
            )
        }
    }
}

