package com.example.myenglish.components

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myenglish.sendBugReportToTeacher

@Composable
fun BugReportOverlay(
    studentName: String,
    lessonName: String,
    currentScreen: String
) {
    var showDialog by remember { mutableStateOf(false) }
    var bugText by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var messageIsError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = 10.dp, y = (-14).dp)
                .width(62.dp)
                .height(62.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 18.dp,
                        bottomEnd = 18.dp
                    ),
                    clip = false
                )
                .background(
                    color = Color(0xFF111111),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 18.dp,
                        bottomEnd = 18.dp
                    )
                )
                .clickable { showDialog = true },
            contentAlignment = Alignment.Center
        ) {
            WhiteBugIcon()
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { if (!sending) showDialog = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF4F4F4), RoundedCornerShape(20.dp))
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Report a bug",
                    color = Color(0xFF111111),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "Tell me what went wrong. You can write in Portuguese or English.",
                    color = Color(0xFF333333),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = bugText,
                    onValueChange = { bugText = it },
                    enabled = !sending,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    label = { Text("Bug description") },
                    singleLine = false
                )

                if (message.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = message,
                        color = if (messageIsError) Color(0xFFC62828) else Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(14.dp))

                Button(
                    onClick = {
                        val text = bugText.trim()
                        if (text.isBlank()) {
                            message = "Write the bug first. The bug box is hungry."
                            messageIsError = true
                            return@Button
                        }

                        sending = true
                        message = "Sending bug report..."
                        messageIsError = false

                        sendBugReportToTeacher(
                            studentName = studentName,
                            lessonName = lessonName,
                            currentScreen = currentScreen,
                            bugText = text
                        ) { success ->
                            Handler(Looper.getMainLooper()).post {
                                sending = false
                                if (success) {
                                    bugText = ""
                                    message = "Bug report sent. Thank you!"
                                    messageIsError = false
                                    showDialog = false
                                } else {
                                    message = "Failed to send bug report. Try again."
                                    messageIsError = true
                                }
                            }
                        }
                    },
                    enabled = !sending,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF222222))
                ) {
                    Text(if (sending) "Sending..." else "Send")
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = { showDialog = false },
                    enabled = !sending,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF777777))
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
private fun WhiteBugIcon() {
    Canvas(modifier = Modifier.width(33.dp).height(33.dp)) {
        val white = Color.White
        val stroke = Stroke(width = 3.4f)
        val centerX = size.width / 2f

        drawOval(
            color = white,
            topLeft = Offset(size.width * 0.30f, size.height * 0.31f),
            size = Size(size.width * 0.40f, size.height * 0.48f)
        )
        drawCircle(
            color = white,
            radius = size.width * 0.15f,
            center = Offset(centerX, size.height * 0.23f)
        )
        drawLine(white, Offset(centerX, size.height * 0.35f), Offset(centerX, size.height * 0.78f), strokeWidth = 2.4f)

        drawLine(white, Offset(size.width * 0.30f, size.height * 0.44f), Offset(size.width * 0.10f, size.height * 0.35f), strokeWidth = stroke.width)
        drawLine(white, Offset(size.width * 0.30f, size.height * 0.56f), Offset(size.width * 0.08f, size.height * 0.58f), strokeWidth = stroke.width)
        drawLine(white, Offset(size.width * 0.30f, size.height * 0.68f), Offset(size.width * 0.12f, size.height * 0.82f), strokeWidth = stroke.width)

        drawLine(white, Offset(size.width * 0.70f, size.height * 0.44f), Offset(size.width * 0.90f, size.height * 0.35f), strokeWidth = stroke.width)
        drawLine(white, Offset(size.width * 0.70f, size.height * 0.56f), Offset(size.width * 0.92f, size.height * 0.58f), strokeWidth = stroke.width)
        drawLine(white, Offset(size.width * 0.70f, size.height * 0.68f), Offset(size.width * 0.88f, size.height * 0.82f), strokeWidth = stroke.width)
    }
}
