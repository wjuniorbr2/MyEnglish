package com.example.myenglish.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun BakingImageSelector(
    images: Array<Int>,
    imageDescriptions: Array<Int>,
    selectedImageIndex: Int,
    onImageSelected: (Int) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(images) { index, image ->
            var imageModifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .requiredSize(200.dp)
                .clickable {
                    onImageSelected(index)
                }

            if (index == selectedImageIndex) {
                imageModifier = imageModifier.border(
                    BorderStroke(4.dp, MaterialTheme.colorScheme.primary)
                )
            }

            Image(
                painter = painterResource(image),
                contentDescription = stringResource(imageDescriptions[index]),
                modifier = imageModifier
            )
        }
    }
}
