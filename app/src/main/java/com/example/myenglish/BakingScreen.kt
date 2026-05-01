package com.example.myenglish

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myenglish.components.BakingImageSelector
import com.example.myenglish.components.BakingPromptInput
import com.example.myenglish.components.BakingResultText
import com.example.myenglish.data.bakingImageDescriptions
import com.example.myenglish.data.bakingImages

@Composable
fun BakingScreen(
    bakingViewModel: BakingViewModel = viewModel()
) {
    val selectedImage = remember { mutableIntStateOf(0) }
    val placeholderPrompt = stringResource(R.string.prompt_placeholder)
    val placeholderResult = stringResource(R.string.results_placeholder)
    var prompt by rememberSaveable { mutableStateOf(placeholderPrompt) }
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by bakingViewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.baking_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        BakingImageSelector(
            images = bakingImages,
            imageDescriptions = bakingImageDescriptions,
            selectedImageIndex = selectedImage.intValue,
            onImageSelected = { index ->
                selectedImage.intValue = index
            }
        )

        BakingPromptInput(
            prompt = prompt,
            onPromptChange = { newPrompt ->
                prompt = newPrompt
            },
            onGoClick = {
                val bitmap = BitmapFactory.decodeResource(
                    context.resources,
                    bakingImages[selectedImage.intValue]
                )
                bakingViewModel.sendPrompt(bitmap, prompt)
            }
        )

        when (val state = uiState) {
            UiState.Initial -> {
                BakingResultText(
                    result = result,
                    isError = false,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is UiState.Error -> {
                result = state.errorMessage
                BakingResultText(
                    result = result,
                    isError = true,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is UiState.Success -> {
                result = state.outputText
                BakingResultText(
                    result = result,
                    isError = false,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BakingScreenPreview() {
    BakingScreen()
}
