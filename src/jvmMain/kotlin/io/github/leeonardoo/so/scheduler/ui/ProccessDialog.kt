package io.github.leeonardoo.so.scheduler.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.leeonardoo.so.scheduler.Algorithm
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProcessDialog(
    algorithm: Algorithm,
    process: SimulatedProcess? = null,
    onDismiss: () -> Unit = {},
    onClickConfirm: (SimulatedProcess) -> Unit = {}
) {

    var arrivalTime by remember {
        mutableStateOf(process?.arrivalTime?.toString() ?: "")
    }

    var burstTime by remember {
        mutableStateOf(process?.burstTime?.toString() ?: "")
    }

    var priority by remember {
        mutableStateOf(process?.priority?.toString() ?: "")
    }

    val title = remember(process) {
        if (process == null) {
            "Novo processo"
        } else {
            "Editar processo"
        }
    }

    AlertDialog(
        shape = MaterialTheme.shapes.extraLarge,
        backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
        contentColor = MaterialTheme.colorScheme.onSurface,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val updatedProccess = process?.copy(
                        arrivalTime = arrivalTime.toIntOrNull() ?: 0,
                        burstTime = burstTime.toIntOrNull() ?: 0,
                        priority = priority.toIntOrNull() ?: 0
                    ) ?: SimulatedProcess(
                        arrivalTime = arrivalTime.toIntOrNull() ?: 0,
                        burstTime = burstTime.toIntOrNull() ?: 0,
                        priority = priority.toIntOrNull() ?: 0
                    )

                    onClickConfirm(updatedProccess)
                },
                content = {
                    Text("Salvar")
                }
            )
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                content = {
                    Text("Cancelar")
                }
            )
        },
        onDismissRequest = onDismiss,
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.alpha(0f).fillMaxWidth(),
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                TextField(
                    value = arrivalTime,
                    onValueChange = {
                        arrivalTime = it.filter { char -> char.isDigit() }
                    },
                    label = {
                        Text("Tempo de chegada")
                    },
                    placeholder = {
                        Text("0")
                    },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(Modifier.height(16.dp))

                TextField(
                    value = burstTime,
                    onValueChange = {
                        burstTime = it.filter { char -> char.isDigit() }
                    },
                    label = {
                        Text("Burst time (duração)")
                    },
                    placeholder = {
                        Text("0")
                    },
                    singleLine = true,
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                if (algorithm == Algorithm.PreemptiveStaticPriority || algorithm == Algorithm.NonPreemptiveStaticPriority) {
                    Spacer(Modifier.height(16.dp))

                    TextField(
                        value = priority,
                        onValueChange = {
                            priority = it.filter { char -> char.isDigit() }
                        },
                        label = {
                            Text("Prioridade")
                        },
                        placeholder = {
                            Text("0")
                        },
                        singleLine = true,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun ProcessDialogPreview() {
    MaterialTheme {
        ProcessDialog(
            algorithm = Algorithm.FIFO,
            process = null
        )
    }
}