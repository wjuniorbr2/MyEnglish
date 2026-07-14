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

function Replace-OneOrConfirm {
    param(
        [Parameter(Mandatory)][string]$Text,
        [Parameter(Mandatory)][string]$Old,
        [Parameter(Mandatory)][string]$New,
        [Parameter(Mandatory)][string]$Description
    )

    $normalizedOld = $Old.Replace("`r`n", "`n")
    $normalizedNew = $New.Replace("`r`n", "`n")

    if ($Text.Contains($normalizedNew)) {
        return $Text
    }

    $matches = [regex]::Matches($Text, [regex]::Escape($normalizedOld)).Count
    if ($matches -ne 1) {
        throw "${Description}: expected one old value or an already-applied new value; found $matches old matches."
    }

    return $Text.Replace($normalizedOld, $normalizedNew)
}

Write-Host "Checking the changes already applied by the previous run..."

$requiredChecks = @(
    @{
        Path = "app/src/main/java/com/example/myenglish/components/SentenceRow.kt"
        Text = "FlowRow(modifier = Modifier.fillMaxWidth())"
        Description = "Listening-homework hint wrapping"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/WrittenHomeworkScreen.kt"
        Text = "FlowRow(Modifier.fillMaxWidth())"
        Description = "Written-homework hint wrapping"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/SpokenHomeworkScreen.kt"
        Text = "FlowRow(modifier = Modifier.fillMaxWidth())"
        Description = "Spoken-homework hint wrapping"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/PracticeScreen.kt"
        Text = "FlowRow(Modifier.fillMaxWidth())"
        Description = "Spoken-practice hint wrapping"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/WrittenPracticeScreen.kt"
        Text = "FlowRow(Modifier.fillMaxWidth())"
        Description = "Written-practice hint wrapping"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/ListeningPracticeScreen.kt"
        Text = "FlowRow(Modifier.fillMaxWidth())"
        Description = "Listening-practice hint wrapping"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/components/BugReportOverlay.kt"
        Text = ".statusBarsPadding()"
        Description = "Bug-button safe status-bar position"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/AppRoot.kt"
        Text = ".padding(bottom = 72.dp),"
        Description = "Raised Go to lesson buttons"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/BookScreen.kt"
        Text = "val useNumberSection"
        Description = "Number-section data"
    },
    @{
        Path = "app/src/main/java/com/example/myenglish/screens/BookScreen.kt"
        Text = "fun playBottomSection()"
        Description = "Number-section system voice"
    }
)

foreach ($check in $requiredChecks) {
    $source = Read-NormalizedText -Path $check.Path
    if (-not $source.Contains($check.Text)) {
        throw "$($check.Description) is missing from $($check.Path)."
    }
}

Write-Host "Finishing the number section in BookScreen..."

$bookPath = "app/src/main/java/com/example/myenglish/screens/BookScreen.kt"
$bookText = Read-NormalizedText -Path $bookPath

$bookText = Replace-OneOrConfirm `
    -Text $bookText `
    -Old '.clickable { playSegment(bookData.alphabetTitle) }' `
    -New '.clickable { playSegment(bottomTitle) }' `
    -Description "Book section title playback"

$bookText = Replace-OneOrConfirm `
    -Text $bookText `
    -Old 'text = "ALPHABET",' `
    -New 'text = if (useNumberSection) "NUMBERS" else "ALPHABET",' `
    -Description "Book section title"

$bookText = Replace-OneOrConfirm `
    -Text $bookText `
    -Old 'text = "▶ ABC",' `
    -New 'text = if (useNumberSection) "▶ 1-20" else "▶ ABC",' `
    -Description "Book play-all label"

$bookText = Replace-OneOrConfirm `
    -Text $bookText `
    -Old 'onClick = { playFull(bookData.alphabetAudioResId) },' `
    -New 'onClick = { playBottomSection() },' `
    -Description "Book play-all action"

$oldGridCall = '            AlphabetGrid(bookData.alphabet, ::playSegment)'
$newGridCall = @'
            AlphabetGrid(
                items = bottomItems,
                play = ::playBottomItem,
                columns = if (useNumberSection) 5 else 7
            )
'@

$bookText = Replace-OneOrConfirm `
    -Text $bookText `
    -Old $oldGridCall `
    -New $newGridCall `
    -Description "Book bottom grid call"

$oldGridSignature = 'private fun AlphabetGrid(items: Array<BookAudioItem>, play: (BookAudioItem) -> Unit) {'
$newGridSignature = @'
private fun AlphabetGrid(
    items: Array<BookAudioItem>,
    play: (BookAudioItem) -> Unit,
    columns: Int = 7
) {
'@

$bookText = Replace-OneOrConfirm `
    -Text $bookText `
    -Old $oldGridSignature `
    -New $newGridSignature `
    -Description "Flexible bottom-grid signature"

$bookText = Replace-OneOrConfirm `
    -Text $bookText `
    -Old '            while (col < 7) {' `
    -New '            while (col < columns) {' `
    -Description "Flexible bottom-grid columns"

$bookText = Replace-OneOrConfirm `
    -Text $bookText `
    -Old '        i += 7' `
    -New '        i += columns' `
    -Description "Flexible bottom-grid row step"

Write-Utf8Text -Path $bookPath -Text $bookText

Write-Host "Validating the completed BookScreen..."

$completed = Read-NormalizedText -Path $bookPath
$requiredBookMarkers = @(
    'text = if (useNumberSection) "NUMBERS" else "ALPHABET",',
    'text = if (useNumberSection) "▶ 1-20" else "▶ ABC",',
    'onClick = { playBottomSection() },',
    'items = bottomItems,',
    'columns = if (useNumberSection) 5 else 7',
    'columns: Int = 7',
    'while (col < columns)',
    'i += columns'
)

foreach ($marker in $requiredBookMarkers) {
    if (-not $completed.Contains($marker)) {
        throw "BookScreen validation failed. Missing: $marker"
    }
}

Write-Host "All requested Android source changes are now complete."
