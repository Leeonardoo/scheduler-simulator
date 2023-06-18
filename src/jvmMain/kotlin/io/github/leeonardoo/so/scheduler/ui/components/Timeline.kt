package io.github.leeonardoo.so.scheduler.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess

@Composable
fun Timeline(contentPadding: PaddingValues, scheduledProcesses: List<SimulatedProcess>) {
    ScrollbarLazyRow(
        scrollbarPadding = contentPadding,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        itemsIndexed(scheduledProcesses) { index, item ->
            TimelineItem(
                process = item,
                index = index
            )
        }
    }
}

@Composable
fun TimelineItem(process: SimulatedProcess, index: Int) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(42.dp, 24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(process.color)
                .then(
                    if (process.color == Color.Transparent) {
                        Modifier.border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                    } else {
                        Modifier
                    }
                ),
        )

        Text(
            text = index.toString(),
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center
        )
    }
}