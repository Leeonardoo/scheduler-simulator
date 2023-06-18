package io.github.leeonardoo.so.scheduler.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.leeonardoo.so.scheduler.model.SimulatedProcess

@Composable
fun RowScope.ColorDot(
    process: SimulatedProcess,
    weight: Float
) {

    Box(
        modifier = Modifier
            .weight(weight)
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(process.color)
                .align(Alignment.CenterStart)
        )

    }
}
