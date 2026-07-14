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

    $oldNormalized = $Old.Replace("`r`n", "`n")
    $newNormalized = $New.Replace("`r`n", "`n")
    $count = [regex]::Matches($Text, [regex]::Escape($oldNormalized)).Count

    if ($count -ne 1) {
        throw "${Description}: expected exactly one match, found $count."
    }

    return $Text.Replace($oldNormalized, $newNormalized)
}

Write-Host "Adjusting the number grid..."

$bookPath = "app/src/main/java/com/example/myenglish/screens/BookScreen.kt"
$book = Read-NormalizedText $bookPath

if ($book.Contains("columns = if (useNumberSection) 5 else 7")) {
    $book = Replace-ExactlyOnce `
        -Text $book `
        -Old "columns = if (useNumberSection) 5 else 7" `
        -New "columns = if (useNumberSection) 4 else 7" `
        -Description "Number-grid columns"
}
elseif (-not $book.Contains("columns = if (useNumberSection) 4 else 7")) {
    throw "Number-grid column setting was not found."
}

$oldNumberSizing = @'
    val translationFontSize = if (item.english == "W") 7.sp else 9.sp
'@

$newNumberSizing = @'
    val isNumber = item.english.toIntOrNull() != null
    val translationFontSize = when {
        isNumber -> 7.sp
        item.english == "W" -> 7.sp
        else -> 9.sp
    }
    val cellWidth = if (isNumber) 48.dp else 42.dp
'@

if ($book.Contains($oldNumberSizing.Replace("`r`n", "`n"))) {
    $book = Replace-ExactlyOnce `
        -Text $book `
        -Old $oldNumberSizing `
        -New $newNumberSizing `
        -Description "Number-cell sizing"
}
elseif (-not $book.Contains("val cellWidth = if (isNumber) 48.dp else 42.dp")) {
    throw "Number-cell sizing section was not found."
}

$oldCellWidth = @'
            .width(42.dp)
            .background(darkPanelColor, RoundedCornerShape(8.dp))
'@

$newCellWidth = @'
            .width(cellWidth)
            .background(darkPanelColor, RoundedCornerShape(8.dp))
'@

if ($book.Contains($oldCellWidth.Replace("`r`n", "`n"))) {
    $book = Replace-ExactlyOnce `
        -Text $book `
        -Old $oldCellWidth `
        -New $newCellWidth `
        -Description "Number-cell width"
}
elseif (-not $book.Contains(".width(cellWidth)")) {
    throw "Number-cell width section was not found."
}

Write-Utf8Text -Path $bookPath -Text $book

Write-Host "Moving the bug button to the far left..."

$bugPath = "app/src/main/java/com/example/myenglish/components/BugReportOverlay.kt"
$bug = Read-NormalizedText $bugPath

if ($bug.Contains(".padding(start = 10.dp, top = 8.dp),")) {
    $bug = Replace-ExactlyOnce `
        -Text $bug `
        -Old ".padding(start = 10.dp, top = 8.dp)," `
        -New ".padding(top = 8.dp)," `
        -Description "Bug-button left position"
}
elseif (-not $bug.Contains(".padding(top = 8.dp),")) {
    throw "Bug-button padding was not found."
}

Write-Utf8Text -Path $bugPath -Text $bug

Write-Host "Placing the homework lesson button beside Back..."

$appRootPath = "app/src/main/java/com/example/myenglish/screens/AppRoot.kt"
$appRoot = Read-NormalizedText $appRootPath

if (-not $appRoot.Contains("import androidx.compose.foundation.layout.width")) {
    $appRoot = Replace-ExactlyOnce `
        -Text $appRoot `
        -Old "import androidx.compose.foundation.layout.padding`n" `
        -New "import androidx.compose.foundation.layout.padding`nimport androidx.compose.foundation.layout.width`n" `
        -Description "AppRoot width import"
}

$oldHomeworkButton = @'
        if (screen == "homework" || screen == "writtenHomework" || screen == "spokenHomework") {
            ArtButton(
                text = "Go to lesson",
                onClick = { openBookFromHomework(screen) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.68f)
                    .padding(bottom = 72.dp),
                backgroundResId = R.drawable.redbutton,
                heightDp = 54,
                fontSize = 17
            )
        }
'@

$newHomeworkButton = @'
        if (screen == "homework" || screen == "writtenHomework" || screen == "spokenHomework") {
            ArtButton(
                text = "Lesson",
                onClick = { openBookFromHomework(screen) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .width(66.dp)
                    .padding(end = 4.dp, bottom = 18.dp),
                backgroundResId = R.drawable.redbutton,
                heightDp = 50,
                fontSize = 12
            )
        }
'@

if ($appRoot.Contains($oldHomeworkButton.Replace("`r`n", "`n"))) {
    $appRoot = Replace-ExactlyOnce `
        -Text $appRoot `
        -Old $oldHomeworkButton `
        -New $newHomeworkButton `
        -Description "Homework lesson button"
}
elseif (-not $appRoot.Contains('text = "Lesson"')) {
    throw "Homework lesson-button block was not found."
}

Write-Utf8Text -Path $appRootPath -Text $appRoot

Write-Host "Fixing contraction highlighting in listening homework..."

$sentenceRowPath = "app/src/main/java/com/example/myenglish/components/SentenceRow.kt"
$sentenceRow = Read-NormalizedText $sentenceRowPath

$oldListeningCorrection = @'
        val annotated = buildAnnotatedString {
            append(rawText)

            var i = 0
            while (i < ranges.size) {
                val range = ranges[i]
                val studentWord = cleanAnswer(rawText.substring(range.first, range.last))
                val expectedWord = if (i < correctedWords.size) correctedWords[i] else ""

                if (studentWord != "" && studentWord != expectedWord) {
                    addStyle(
                        style = SpanStyle(color = Color(0xFFC62828)),
                        start = range.first,
                        end = range.last
                    )
                }
                i++
            }
        }
'@

$newListeningCorrection = @'
        val annotated = buildAnnotatedString {
            append(rawText)

            var expectedCursor = 0
            for (range in ranges) {
                val studentParts = cleanAnswer(rawText.substring(range.first, range.last))
                    .split(" ")
                    .filter { it.isNotBlank() }
                val endCursor = expectedCursor + studentParts.size
                val matches = studentParts.isNotEmpty() &&
                        endCursor <= correctedWords.size &&
                        correctedWords.subList(expectedCursor, endCursor) == studentParts

                if (studentParts.isNotEmpty() && !matches) {
                    addStyle(
                        style = SpanStyle(color = Color(0xFFC62828)),
                        start = range.first,
                        end = range.last
                    )
                }

                expectedCursor += studentParts.size.coerceAtLeast(1)
            }
        }
'@

if ($sentenceRow.Contains($oldListeningCorrection.Replace("`r`n", "`n"))) {
    $sentenceRow = Replace-ExactlyOnce `
        -Text $sentenceRow `
        -Old $oldListeningCorrection `
        -New $newListeningCorrection `
        -Description "Listening contraction highlighting"
}
elseif (-not $sentenceRow.Contains("var expectedCursor = 0")) {
    throw "Listening correction block was not found."
}

Write-Utf8Text -Path $sentenceRowPath -Text $sentenceRow

Write-Host "Fixing contraction highlighting in written homework..."

$writtenHomeworkPath = "app/src/main/java/com/example/myenglish/screens/WrittenHomeworkScreen.kt"
$writtenHomework = Read-NormalizedText $writtenHomeworkPath

$oldWrittenCorrection = @'
        val annotated = buildAnnotatedString {
            append(rawText)
            for (i in ranges.indices) {
                val studentWord = cleanAnswer(rawText.substring(ranges[i].first, ranges[i].last))
                val expectedWord = if (i < expectedWords.size) expectedWords[i] else ""
                if (studentWord.isNotBlank() && studentWord != expectedWord) {
                    addStyle(SpanStyle(color = writtenRed), ranges[i].first, ranges[i].last)
                }
            }
        }
'@

$newWrittenCorrection = @'
        val annotated = buildAnnotatedString {
            append(rawText)
            var expectedCursor = 0

            for (range in ranges) {
                val studentParts = cleanAnswer(rawText.substring(range.first, range.last))
                    .split(" ")
                    .filter { it.isNotBlank() }
                val endCursor = expectedCursor + studentParts.size
                val matches = studentParts.isNotEmpty() &&
                        endCursor <= expectedWords.size &&
                        expectedWords.subList(expectedCursor, endCursor) == studentParts

                if (studentParts.isNotEmpty() && !matches) {
                    addStyle(SpanStyle(color = writtenRed), range.first, range.last)
                }

                expectedCursor += studentParts.size.coerceAtLeast(1)
            }
        }
'@

if ($writtenHomework.Contains($oldWrittenCorrection.Replace("`r`n", "`n"))) {
    $writtenHomework = Replace-ExactlyOnce `
        -Text $writtenHomework `
        -Old $oldWrittenCorrection `
        -New $newWrittenCorrection `
        -Description "Written-homework contraction highlighting"
}
elseif (-not $writtenHomework.Contains("expectedWords.subList(expectedCursor, endCursor) == studentParts")) {
    throw "Written-homework correction block was not found."
}

Write-Utf8Text -Path $writtenHomeworkPath -Text $writtenHomework

Write-Host "Fixing contraction highlighting in written practice..."

$writtenPracticePath = "app/src/main/java/com/example/myenglish/screens/WrittenPracticeScreen.kt"
$writtenPractice = Read-NormalizedText $writtenPracticePath

$oldPracticeColoring = @'
private fun practiceColoredAnswer(answer: String, expected: String, wrongColor: Color) = buildAnnotatedString {
    val expectedWords = cleanAnswer(expected).split(" ").filter { it.isNotBlank() }
    val ranges = practiceWordRanges(answer)
    val studentWords = ranges.map { cleanAnswer(answer.substring(it.first, it.last)) }
    val matchMap = practiceMatchingStudentToExpected(studentWords, expectedWords)
    var expectedCursor = 0

    for (i in ranges.indices) {
        val expectedIndex = matchMap[i]
        if (expectedIndex != null && expectedIndex >= expectedCursor) {
            while (expectedCursor < expectedIndex) {
                appendPracticeMissingUnderline(wrongColor)
                expectedCursor++
            }
        }

        val start = length
        append(answer.substring(ranges[i].first, ranges[i].last))
        val end = length
        if (studentWords[i].isNotBlank() && expectedIndex == null) {
            addStyle(SpanStyle(color = wrongColor), start, end)
            if (expectedCursor < expectedWords.size) expectedCursor++
        }
        append(" ")
        if (expectedIndex != null && expectedIndex >= expectedCursor) expectedCursor = expectedIndex + 1
    }

    while (expectedCursor < expectedWords.size) {
        appendPracticeMissingUnderline(wrongColor)
        expectedCursor++
    }
}
'@

$newPracticeColoring = @'
private fun practiceColoredAnswer(answer: String, expected: String, wrongColor: Color) = buildAnnotatedString {
    val expectedWords = cleanAnswer(expected).split(" ").filter { it.isNotBlank() }
    val ranges = practiceWordRanges(answer)
    var expectedCursor = 0

    for (range in ranges) {
        val rawToken = answer.substring(range.first, range.last)
        val studentParts = cleanAnswer(rawToken).split(" ").filter { it.isNotBlank() }
        val endCursor = expectedCursor + studentParts.size
        val matches = studentParts.isNotEmpty() &&
                endCursor <= expectedWords.size &&
                expectedWords.subList(expectedCursor, endCursor) == studentParts

        val start = length
        append(rawToken)
        val end = length

        if (studentParts.isNotEmpty() && !matches) {
            addStyle(SpanStyle(color = wrongColor), start, end)
        }

        append(" ")
        expectedCursor += studentParts.size.coerceAtLeast(1)
    }

    while (expectedCursor < expectedWords.size) {
        appendPracticeMissingUnderline(wrongColor)
        expectedCursor++
    }
}
'@

if ($writtenPractice.Contains($oldPracticeColoring.Replace("`r`n", "`n"))) {
    $writtenPractice = Replace-ExactlyOnce `
        -Text $writtenPractice `
        -Old $oldPracticeColoring `
        -New $newPracticeColoring `
        -Description "Written-practice contraction highlighting"
}
elseif (-not $writtenPractice.Contains("val rawToken = answer.substring(range.first, range.last)")) {
    throw "Written-practice coloring block was not found."
}

Write-Utf8Text -Path $writtenPracticePath -Text $writtenPractice

$checks = @(
    @{ Path = $bookPath; Text = "columns = if (useNumberSection) 4 else 7"; Name = "larger number grid" },
    @{ Path = $bookPath; Text = "val cellWidth = if (isNumber) 48.dp else 42.dp"; Name = "wider number cells" },
    @{ Path = $bugPath; Text = ".padding(top = 8.dp),"; Name = "left-edge bug button" },
    @{ Path = $appRootPath; Text = ".align(Alignment.BottomEnd)"; Name = "homework lesson button position" },
    @{ Path = $sentenceRowPath; Text = "correctedWords.subList(expectedCursor, endCursor) == studentParts"; Name = "listening contraction support" },
    @{ Path = $writtenHomeworkPath; Text = "expectedWords.subList(expectedCursor, endCursor) == studentParts"; Name = "written-homework contraction support" },
    @{ Path = $writtenPracticePath; Text = "val rawToken = answer.substring(range.first, range.last)"; Name = "written-practice contraction support" }
)

foreach ($check in $checks) {
    $content = Read-NormalizedText $check.Path
    if (-not $content.Contains($check.Text)) {
        throw "Validation failed: $($check.Name)."
    }
    Write-Host "OK: $($check.Name)"
}

Write-Host "Follow-up source changes were applied successfully."
