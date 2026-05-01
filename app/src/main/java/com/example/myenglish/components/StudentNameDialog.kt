package com.example.myenglish.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.myenglish.utils.cleanAnswer

@Composable
fun StudentNameDialog(
    currentName: String,
    onSave: (String) -> Unit
) {
    var nameText by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = { },
        title = { Text("Student name") },
        text = {
            TextField(
                value = nameText,
                onValueChange = { nameText = it },
                label = { Text("First name and initial") }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (cleanAnswer(nameText) != "") {
                        onSave(nameText)
                    }
                }
            ) {
                Text("Save")
            }
        }
    )
}
