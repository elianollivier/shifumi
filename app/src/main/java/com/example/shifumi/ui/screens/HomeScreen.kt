package com.example.shifumi.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    onPlaySoloClicked: () -> Unit,
    onPlayVsComputerClicked: () -> Unit,
    onPlayStrategyClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { onPlaySoloClicked() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "Mode classique (Solo)")
            }

            Button(
                onClick = { onPlayVsComputerClicked() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "Mode vs Ordinateur")
            }

            Button(
                onClick = { onPlayStrategyClicked() },
                modifier = Modifier.wrapContentSize()
            ) {
                Text(text = "Jeu Stratégique")
            }
        }
    }
}
