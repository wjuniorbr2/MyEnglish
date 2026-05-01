package com.example.myenglish.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myenglish.R

@Composable
fun BakingPromptInput(
    prompt: String,
    onPromptChange: (String) -> Unit,
    onGoClick: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(all = 16.dp)
    ) {
        TextField(
            value = prompt,
            label = { Text(stringResource(R.string.label_prompt)) },
            onValueChange = onPromptChange,
            modifier = Modifier
                .weight(0.8f)
                .padding(end = 16.dp)
                .align(Alignment.CenterVertically)
        )

        Button(
            onClick = onGoClick,
            enabled = prompt.isNotEmpty(),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(text = stringResource(R.string.action_go))
        }
    }
}
