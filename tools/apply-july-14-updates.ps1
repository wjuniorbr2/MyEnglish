$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest

function Read-NormalizedText {
    param([Parameter(Mandatory)][string]$Path)

    if (-not (Test-Path -LiteralPath $Path)) {
        throw "File not found: $Path"
    }

    return [System.IO.File]::ReadAllText($Path).Replace("`r`n", "`n")
}

function Write-Utf8Text {
    param(
        [Parameter(Mandatory)][string]$Path,
        [Parameter(Mandatory)][string]$Text
    )

    [System.IO.File]::WriteAllText(
        $Path,
        $Text,
        [System.Text.UTF8Encoding]::new($false)
    )
}

function Replace-ExactlyOnce {
    param(
        [Parameter(Mandatory)][string]$Text,
        [Parameter(Mandatory)][string]$Old,
        [Parameter(Mandatory)][string]$New,
        [Parameter(Mandatory)][string]$Description
    )

    $matches = [regex]::Matches($Text, [regex]::Escape($Old)).Count

    if ($matches -ne 1) {
        throw "${Description}: expected exactly one match, found $matches."
    }

    return $Text.Replace($Old, $New)
}

Write-Host "Applying homework hint wrapping..."

$hintUpdates = @(
    @{
        Path = "app/src/main/java/com/example/myenglish/components/SentenceRow.kt"
        Marker = "@Composable`nfun SentenceRow("
        OldRow = "                    Row(modifier = Modifier.fillMaxWidth()) {"
        NewRow = "                    FlowRow(modifier = Modifier.fillMaxWidth()) {"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/WrittenHomeworkScreen.kt"
        Marker = "@Composable`nprivate fun WrittenCard("
        OldRow = "                    Row(Modifier.fillMaxWidth()) {"
        NewRow = "                    FlowRow(Modifier.fillMaxWidth()) {"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/SpokenHomeworkScreen.kt"
        Marker = "@Composable`nprivate fun SpokenSentenceCard("
        OldRow = "                Row(modifier = Modifier.fillMaxWidth()) {"
        NewRow = "                FlowRow(modifier = Modifier.fillMaxWidth()) {"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/PracticeScreen.kt"
        Marker = "@Composable`nprivate fun PracticeCard("
        OldRow = "                Row(Modifier.fillMaxWidth()) {"
        NewRow = "                FlowRow(Modifier.fillMaxWidth()) {"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/WrittenPracticeScreen.kt"
        Marker = "@Composable`nprivate fun WrittenPracticeCard("
        OldRow = "                Row(Modifier.fillMaxWidth()) {"
        NewRow = "                FlowRow(Modifier.fillMaxWidth()) {"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/ListeningPracticeScreen.kt"
        Marker = "@Composable`nprivate fun ListeningPracticeCard("
        OldRow = "                Row(Modifier.fillMaxWidth()) {"
        NewRow = "                FlowRow(Modifier.fillMaxWidth()) {"
    }
)

foreach ($update in $hintUpdates) {
    $text = Read-NormalizedText $update.Path

    if (-not $text.Contains("import androidx.compose.foundation.layout.FlowRow")) {
        $text = Replace-ExactlyOnce `
            -Text $text `
            -Old "import androidx.compose.foundation.layout.Column`n" `
            -New "import androidx.compose.foundation.layout.Column`nimport androidx.compose.foundation.layout.ExperimentalLayoutApi`nimport androidx.compose.foundation.layout.FlowRow`n" `
            -Description "$($update.Path) FlowRow imports"
    }

    $optInMarker = "@OptIn(ExperimentalLayoutApi::class)`n$($update.Marker)"
    if (-not $text.Contains($optInMarker)) {
        $text = Replace-ExactlyOnce `
            -Text $text `
            -Old $update.Marker `
            -New $optInMarker `
            -Description "$($update.Path) FlowRow opt-in"
    }

    if ($text.Contains($update.OldRow)) {
        $text = Replace-ExactlyOnce `
            -Text $text `
            -Old $update.OldRow `
            -New $update.NewRow `
            -Description "$($update.Path) hint row"
    }
    elseif (-not $text.Contains($update.NewRow)) {
        throw "$($update.Path): hint row was not found."
    }

    Write-Utf8Text -Path $update.Path -Text $text
}

Write-Host "Moving the bug button below the status bar..."

$bugPath = "app/src/main/java/com/example/myenglish/components/BugReportOverlay.kt"
$bugText = Read-NormalizedText $bugPath

if (-not $bugText.Contains(".statusBarsPadding()")) {
    $bugText = Replace-ExactlyOnce `
        -Text $bugText `
        -Old "import androidx.compose.foundation.layout.offset`n" `
        -New "import androidx.compose.foundation.layout.statusBarsPadding`n" `
        -Description "Bug button status-bar import"

    $oldBugBlock = @'
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = 10.dp, y = (-14).dp)
                .width(47.dp)
                .height(47.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    clip = false
                )
                .background(
                    color = Color(0xFF111111),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .clickable { showDialog = true },
'@

    $newBugBlock = @'
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 10.dp, top = 8.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .width(47.dp)
                .height(47.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(14.dp),
                    clip = false
                )
                .background(
                    color = Color(0xFF111111),
                    shape = RoundedCornerShape(14.dp)
                )
                .clickable { showDialog = true },
'@

    $bugText = Replace-ExactlyOnce `
        -Text $bugText `
        -Old $oldBugBlock `
        -New $newBugBlock `
        -Description "Bug button layout"

    Write-Utf8Text -Path $bugPath -Text $bugText
}

Write-Host "Raising the Go to lesson buttons..."

$appRootPath = "app/src/main/java/com/example/myenglish/screens/AppRoot.kt"
$appRootText = Read-NormalizedText $appRootPath

if (-not $appRootText.Contains(".padding(bottom = 72.dp),")) {
    $oldPadding = ".padding(bottom = 18.dp),"
    $newPadding = ".padding(bottom = 72.dp),"

    for ($i = 0; $i -lt 2; $i++) {
        $index = $appRootText.IndexOf($oldPadding, [System.StringComparison]::Ordinal)
        if ($index -lt 0) {
            throw "AppRoot.kt: could not find Go to lesson padding occurrence $($i + 1)."
        }

        $appRootText = $appRootText.Remove($index, $oldPadding.Length).Insert($index, $newPadding)
    }

    Write-Utf8Text -Path $appRootPath -Text $appRootText
}

Write-Host "Adding numbers 1-20 and system voice to lessons 5-8..."

$bookPath = "app/src/main/java/com/example/myenglish/screens/BookScreen.kt"
$bookText = Read-NormalizedText $bookPath

if (-not $bookText.Contains("val useNumberSection")) {
    $oldBookDataEnd = @'
        else -> Lesson1BookData
    }

    val context = LocalContext.current
'@

    $newBookDataEnd = @'
        else -> Lesson1BookData
    }

    val lessonNumber = lessonName.removePrefix("Lesson ").toIntOrNull()
    val useNumberSection = lessonNumber != null && lessonNumber in 5..8
    val numberItems = remember {
        arrayOf(
            BookAudioItem("1", "One", 0, 0, 0, true),
            BookAudioItem("2", "Two", 0, 0, 0, true),
            BookAudioItem("3", "Three", 0, 0, 0, true),
            BookAudioItem("4", "Four", 0, 0, 0, true),
            BookAudioItem("5", "Five", 0, 0, 0, true),
            BookAudioItem("6", "Six", 0, 0, 0, true),
            BookAudioItem("7", "Seven", 0, 0, 0, true),
            BookAudioItem("8", "Eight", 0, 0, 0, true),
            BookAudioItem("9", "Nine", 0, 0, 0, true),
            BookAudioItem("10", "Ten", 0, 0, 0, true),
            BookAudioItem("11", "Eleven", 0, 0, 0, true),
            BookAudioItem("12", "Twelve", 0, 0, 0, true),
            BookAudioItem("13", "Thirteen", 0, 0, 0, true),
            BookAudioItem("14", "Fourteen", 0, 0, 0, true),
            BookAudioItem("15", "Fifteen", 0, 0, 0, true),
            BookAudioItem("16", "Sixteen", 0, 0, 0, true),
            BookAudioItem("17", "Seventeen", 0, 0, 0, true),
            BookAudioItem("18", "Eighteen", 0, 0, 0, true),
            BookAudioItem("19", "Nineteen", 0, 0, 0, true),
            BookAudioItem("20", "Twenty", 0, 0, 0, true)
        )
    }
    val bottomTitle = if (useNumberSection) {
        BookAudioItem("NUMBERS", "", 0, 0, 0, true)
    } else {
        bookData.alphabetTitle
    }
    val bottomItems = if (useNumberSection) numberItems else bookData.alphabet

    val context = LocalContext.current
'@

    $bookText = Replace-ExactlyOnce `
        -Text $bookText `
        -Old $oldBookDataEnd `
        -New $newBookDataEnd `
        -Description "BookScreen number data"

    $oldPlayEnd = @'
        currentPlayer.setOnCompletionListener { stop() }
    }

    DisposableEffect(Unit) {
'@

    $newPlayEnd = @'
        currentPlayer.setOnCompletionListener { stop() }
    }

    fun playBottomItem(item: BookAudioItem) {
        if (useNumberSection) {
            stop()
            speakText(item.translation, TextToSpeech.QUEUE_FLUSH, "book_number_${item.english}")
        } else {
            playSegment(item)
        }
    }

    fun playBottomSection() {
        if (!useNumberSection && bookData.alphabetAudioResId != 0) {
            playFull(bookData.alphabetAudioResId)
            return
        }
        if (!ttsReady) return

        stop()
        bottomItems.forEachIndexed { index, item ->
            val spokenText = if (useNumberSection) item.translation else item.english
            speakText(
                spokenText,
                if (index == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD,
                "book_bottom_$index"
            )
            textToSpeech.playSilentUtterance(
                700L,
                TextToSpeech.QUEUE_ADD,
                "book_bottom_pause_$index"
            )
        }
    }

    DisposableEffect(Unit) {
'@

    $bookText = Replace-ExactlyOnce `
        -Text $bookText `
        -Old $oldPlayEnd `
        -New $newPlayEnd `
        -Description "BookScreen number playback"

    $oldBottomSection = @'
                        .clickable { playSegment(bookData.alphabetTitle) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "ALPHABET",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                ArtButton(
                    text = "▶ ABC",
                    onClick = { playFull(bookData.alphabetAudioResId) },
                    modifier = Modifier.width(95.dp),
                    heightDp = 46,
                    fontSize = 14
                )
            }
            AlphabetGrid(bookData.alphabet, ::playSegment)
'@

    $newBottomSection = @'
                        .clickable { playSegment(bottomTitle) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (useNumberSection) "NUMBERS" else "ALPHABET",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                ArtButton(
                    text = if (useNumberSection) "▶ 1-20" else "▶ ABC",
                    onClick = { playBottomSection() },
                    modifier = Modifier.width(95.dp),
                    heightDp = 46,
                    fontSize = 14
                )
            }
            AlphabetGrid(
                items = bottomItems,
                play = ::playBottomItem,
                columns = if (useNumberSection) 5 else 7
            )
'@

    $bookText = Replace-ExactlyOnce `
        -Text $bookText `
        -Old $oldBottomSection `
        -New $newBottomSection `
        -Description "BookScreen bottom section"

    $oldAlphabetGrid = @'
private fun AlphabetGrid(items: Array<BookAudioItem>, play: (BookAudioItem) -> Unit) {
    var i = 0
    while (i < items.size) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            var col = 0
            while (col < 7) {
                val index = i + col
                if (index < items.size) AlphabetCell(items[index], play) else Spacer(Modifier.width(42.dp))
                col++
            }
        }
        Spacer(Modifier.height(5.dp))
        i += 7
    }
}
'@

    $newAlphabetGrid = @'
private fun AlphabetGrid(
    items: Array<BookAudioItem>,
    play: (BookAudioItem) -> Unit,
    columns: Int = 7
) {
    var i = 0
    while (i < items.size) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            var col = 0
            while (col < columns) {
                val index = i + col
                if (index < items.size) AlphabetCell(items[index], play) else Spacer(Modifier.width(42.dp))
                col++
            }
        }
        Spacer(Modifier.height(5.dp))
        i += columns
    }
}
'@

    $bookText = Replace-ExactlyOnce `
        -Text $bookText `
        -Old $oldAlphabetGrid `
        -New $newAlphabetGrid `
        -Description "BookScreen flexible bottom grid"

    Write-Utf8Text -Path $bookPath -Text $bookText
}

Write-Host "Requested Android source changes were applied successfully."
