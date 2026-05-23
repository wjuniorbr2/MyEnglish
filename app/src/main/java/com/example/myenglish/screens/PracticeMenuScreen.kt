package com.example.myenglish.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myenglish.R
import com.example.myenglish.components.ArtButton
import com.example.myenglish.components.Header

@Composable
fun PracticeMenu(
    name: String,
    openWritten: () -> Unit,
    openListening: () -> Unit,
    openSpoken: () -> Unit,
    back: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(name, "Practice")

        Spacer(Modifier.height(22.dp))

        ArtButton(
            text = "Written practice",
            onClick = openWritten,
            modifier = Modifier.fillMaxWidth(0.72f),
            fontSize = 17
        )

        Spacer(Modifier.height(8.dp))

        ArtButton(
            text = "Listening practice",
            onClick = openListening,
            modifier = Modifier.fillMaxWidth(0.72f),
            fontSize = 17
        )

        Spacer(Modifier.height(8.dp))

        ArtButton(
            text = "Spoken practice",
            onClick = openSpoken,
            modifier = Modifier.fillMaxWidth(0.72f),
            fontSize = 17
        )

        Spacer(Modifier.height(18.dp))

        ArtButton(
            text = "Back",
            onClick = back,
            modifier = Modifier.fillMaxWidth(0.45f),
            backgroundResId = R.drawable.redbutton
        )
    }
}
