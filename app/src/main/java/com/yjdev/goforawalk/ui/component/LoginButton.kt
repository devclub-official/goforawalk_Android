package com.yjdev.goforawalk.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoginButton(containerColor: Color, contentColor: Color, borderColor: Color, text: String) {
    Button(
        onClick = {},
        modifier = Modifier.size(width = 240.dp, height = 54.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
        border = BorderStroke(1.dp, color = borderColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text
        )
    }
}