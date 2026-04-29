package com.example.myenglish

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myenglish.ui.theme.MyEnglishTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareSplashWindow(window)
        overridePendingTransition(0, 0)
        setContent { MyEnglishTheme { BetterAppWithSplash(this) } }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}

fun prepareSplashWindow(window: Window) {
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_FULLSCREEN or
        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}

fun restoreAppWindow(activity: Activity) {
    val window = activity.window
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    window.statusBarColor = android.graphics.Color.rgb(5, 22, 52)
    window.navigationBarColor = android.graphics.Color.rgb(5, 22, 52)
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}

private object BetterLesson1Audio {
    val AUDIO_RES_ID = R.raw.lesson1
    val sentences = arrayOf(
        HomeworkSentence("Sentence 1", "I like.", 870, 2527), HomeworkSentence("Sentence 2", "You eat meat.", 3750, 5880),
        HomeworkSentence("Sentence 3", "I don't like.", 7610, 9426), HomeworkSentence("Sentence 4", "They don't drink water.", 11140, 13257),
        HomeworkSentence("Sentence 5", "Do you drink milk?", 15400, 17676), HomeworkSentence("Sentence 6", "Do we eat meat?", 19230, 21388),
        HomeworkSentence("Sentence 7", "I like bread with butter.", 23320, 26212), HomeworkSentence("Sentence 8", "I eat.", 28140, 29943),
        HomeworkSentence("Sentence 9", "You like.", 31790, 33424), HomeworkSentence("Sentence 10", "I don't like.", 35040, 37255),
        HomeworkSentence("Sentence 11", "You don't like.", 38820, 40794), HomeworkSentence("Sentence 12", "Do you like?", 42610, 44612),
        HomeworkSentence("Sentence 13", "Do they like?", 46060, 48280), HomeworkSentence("Sentence 14", "Don't you like?", 50060, 53014),
        HomeworkSentence("Sentence 15", "Don't they like?", 54040, 56920), HomeworkSentence("Sentence 16", "I like to eat.", 58100, 60369),
        HomeworkSentence("Sentence 17", "I eat in the morning.", 61660, 64021), HomeworkSentence("Sentence 18", "I drink milk in the morning.", 65550, 68597),
        HomeworkSentence("Sentence 19", "Do you like milk?", 70140, 72258), HomeworkSentence("Sentence 20", "They don't eat bread with butter.", 74010, 77491),
        HomeworkSentence("Sentence 21", "I don't like to eat in the morning.", 79160, 82187), HomeworkSentence("Sentence 22", "I drink water.", 83690, 85800),
        HomeworkSentence("Sentence 23", "I eat bread.", 87270, 89614), HomeworkSentence("Sentence 24", "I drink juice.", 91220, 93297),
        HomeworkSentence("Sentence 25", "I don't like milk.", 95200, 97238), HomeworkSentence("Sentence 26", "I don't drink in the morning.", 99010, 101591),
        HomeworkSentence("Sentence 27", "You eat.", 103310, 104970), HomeworkSentence("Sentence 28", "We like.", 106630, 108521),
        HomeworkSentence("Sentence 29", "They like to drink at night.", 110110, 112936), HomeworkSentence("Sentence 30", "We don't drink in the morning.", 114640, 116940),
        HomeworkSentence("Sentence 31", "Do you like butter?", 118630, 120577), HomeworkSentence("Sentence 32", "Do they like to drink beer?", 122320, 124916)
    )
}

@Composable
fun BetterAppWithSplash(activity: Activity) {
    var splashVisible by remember { mutableStateOf(true) }
    var alphaTarget by remember { mutableStateOf(0f) }
    val alpha by animateFloatAsState(targetValue = alphaTarget, animationSpec = tween(950), label = "betterSplashAlpha")

    DisposableEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ alphaTarget = 1f }, 420)
        handler.postDelayed({ alphaTarget = 0f }, 2750)
        handler.postDelayed({ splashVisible = false; restoreAppWindow(activity) }, 3850)
        onDispose { handler.removeCallbacksAndMessages(null) }
    }

    if (splashVisible) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.appsplash),
                contentDescription = "My English splash",
                modifier = Modifier.fillMaxWidth(0.92f).alpha(alpha),
                contentScale = ContentScale.Fit
            )
        }
    } else {
        BetterBackground { BetterLessonListScreen() }
    }
}

@Composable
fun BetterBackground(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Image(painterResource(id = R.drawable.screenbg), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        content()
    }
}

@Composable
fun BetterTopHeader(leftTitle: String, studentName: String? = null, onChangeName: (() -> Unit)? = null) {
    val context = LocalContext.current
    val titleId = remember { context.resources.getIdentifier("title", "drawable", context.packageName) }
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            BetterSmallTitle(leftTitle, Modifier.weight(1f))
            if (titleId != 0) {
                Image(painterResource(id = titleId), "My English", Modifier.width(150.dp).height(62.dp), contentScale = ContentScale.Fit)
            } else {
                Text("My English", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black, style = LocalTextStyle.current.copy(shadow = Shadow(Color.White, Offset(0f, 0f), 10f)))
            }
        }
        if (studentName != null && onChangeName != null) {
            Row(Modifier.fillMaxWidth()) {
                Spacer(Modifier.weight(1f))
                Text(
                    studentName,
                    modifier = Modifier.background(Color(0x99051022), RoundedCornerShape(50.dp)).border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(50.dp)).clickable { onChangeName() }.padding(horizontal = 14.dp, vertical = 5.dp),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BetterSmallTitle(text: String, modifier: Modifier = Modifier) {
    Box(modifier.background(Color(0x8807142F), RoundedCornerShape(18.dp)).border(1.dp, Color(0xAAFFFFFF), RoundedCornerShape(18.dp)).padding(horizontal = 14.dp, vertical = 7.dp)) {
        Text(text, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black, style = LocalTextStyle.current.copy(shadow = Shadow(Color.White, Offset(0f, 0f), 12f)))
    }
}

@Composable
fun BetterImageButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundResId: Int = R.drawable.bluebutton,
    enabled: Boolean = true,
    fontSize: Int = 18,
    heightDp: Int = 76,
    content: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(modifier.height(heightDp.dp).clip(RoundedCornerShape(18.dp)).alpha(if (enabled) 1f else 0.58f).clickable(enabled = enabled) { onClick() }, contentAlignment = Alignment.Center) {
        Image(
            painterResource(id = backgroundResId),
            null,
            Modifier.matchParentSize().graphicsLayer(scaleX = 1.28f),
            contentScale = ContentScale.Crop
        )
        if (content == null) {
            Text(text, color = Color.White, fontSize = fontSize.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center, style = LocalTextStyle.current.copy(shadow = Shadow(Color.Black, Offset(1.5f, 1.5f), 6f)))
        } else content()
    }
}

@Composable
fun BetterHomeworkCircleIcon(kind: Int, done: Boolean) {
    val iconColor = if (done) { if (kind == 0) Color(0xFF00AEEF) else if (kind == 1) Color(0xFFFFB300) else Color(0xFFE91E63) } else Color(0xFF888888)
    Canvas(Modifier.size(30.dp)) {
        val w = size.width; val h = size.height
        drawCircle(Color.White, w * 0.48f, Offset(w / 2f, h / 2f))
        drawCircle(Color.Black, w * 0.48f, Offset(w / 2f, h / 2f), style = Stroke(width = 2.1f))
        if (kind == 0) {
            drawArc(iconColor, 205f, 130f, false, Offset(w * 0.24f, h * 0.20f), Size(w * 0.52f, h * 0.52f), style = Stroke(width = 2.8f))
            drawRoundRect(iconColor, Offset(w * 0.22f, h * 0.50f), Size(w * 0.14f, h * 0.24f), CornerRadius(4f, 4f))
            drawRoundRect(iconColor, Offset(w * 0.64f, h * 0.50f), Size(w * 0.14f, h * 0.24f), CornerRadius(4f, 4f))
        } else if (kind == 1) {
            drawRoundRect(iconColor, Offset(w * 0.25f, h * 0.23f), Size(w * 0.50f, h * 0.56f), CornerRadius(4f, 4f), style = Stroke(width = 2.5f))
            drawLine(iconColor, Offset(w * 0.35f, h * 0.40f), Offset(w * 0.66f, h * 0.40f), strokeWidth = 2f)
            drawLine(iconColor, Offset(w * 0.35f, h * 0.54f), Offset(w * 0.66f, h * 0.54f), strokeWidth = 2f)
            drawLine(iconColor, Offset(w * 0.25f, h * 0.30f), Offset(w * 0.25f, h * 0.72f), strokeWidth = 3f)
        } else {
            drawRoundRect(iconColor, Offset(w * 0.38f, h * 0.18f), Size(w * 0.24f, h * 0.42f), CornerRadius(8f, 8f), style = Stroke(width = 2.6f))
            drawArc(iconColor, 25f, 130f, false, Offset(w * 0.25f, h * 0.40f), Size(w * 0.50f, h * 0.28f), style = Stroke(width = 2.4f))
            drawLine(iconColor, Offset(w * 0.50f, h * 0.62f), Offset(w * 0.50f, h * 0.78f), strokeWidth = 2.4f)
            drawLine(iconColor, Offset(w * 0.34f, h * 0.80f), Offset(w * 0.66f, h * 0.80f), strokeWidth = 2.4f)
        }
    }
}

@Composable
fun BetterLessonListScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("my_english_prefs", Context.MODE_PRIVATE) }
    val lessonSentences = BetterLesson1Audio.sentences
    var currentScreen by remember { mutableStateOf("home") }
    var selectedLesson by remember { mutableStateOf("Lesson 1") }
    var showHomeworkChoices by remember { mutableStateOf(false) }
    var studentName by remember { mutableStateOf(prefs.getString("student_name", "") ?: "") }
    var showNameDialog by remember { mutableStateOf(studentName == "") }
    var lesson1ListeningDone by remember { mutableStateOf(prefs.getBoolean("lesson1_listening_done", false)) }
    val answers = remember { mutableStateListOf<String>() }; val replayCounts = remember { mutableStateListOf<Int>() }; val hintCounts = remember { mutableStateListOf<Int>() }; val firstAttemptCorrect = remember { mutableStateListOf<Boolean>() }
    fun resetAttempt() { answers.clear(); replayCounts.clear(); hintCounts.clear(); firstAttemptCorrect.clear(); var i = 0; while (i < lessonSentences.size) { answers.add(""); replayCounts.add(0); hintCounts.add(0); firstAttemptCorrect.add(false); i = i + 1 } }
    LaunchedEffect(Unit) { resetAttempt() }
    var submitStep by remember { mutableIntStateOf(0) }; var firstAttemptScore by remember { mutableIntStateOf(0) }; var submittedMessage by remember { mutableStateOf("") }; var teacherReportToSend by remember { mutableStateOf("") }
    fun startNewAttemptIfNeeded() { if (submitStep == 2) { resetAttempt(); submitStep = 0; firstAttemptScore = 0; submittedMessage = ""; teacherReportToSend = "" } }
    BackHandler(enabled = currentScreen != "home") { if (currentScreen == "homework") currentScreen = "lesson" else if (currentScreen == "lesson") currentScreen = "home" }
    if (showNameDialog) StudentNameDialog(studentName) { newName -> studentName = newName; prefs.edit().putString("student_name", newName).apply(); showNameDialog = false }
    when (currentScreen) {
        "home" -> BetterHomeScreen(displayStudentName(studentName), { showNameDialog = true }) { lessonName -> selectedLesson = lessonName; showHomeworkChoices = false; currentScreen = "lesson" }
        "lesson" -> BetterLessonScreen(selectedLesson, lesson1ListeningDone, showHomeworkChoices, { showHomeworkChoices = true }, { if (selectedLesson == "Lesson 1") { startNewAttemptIfNeeded(); currentScreen = "homework" } }, { currentScreen = "home" })
        "homework" -> BetterHomeworkScreen(selectedLesson, displayStudentName(studentName), lessonSentences, answers, replayCounts, hintCounts, firstAttemptCorrect, submitStep, firstAttemptScore, submittedMessage, { firstAttemptScore = it }, { submitStep = it }, { submittedMessage = it }, { teacherReportToSend = it }, { lesson1ListeningDone = true; prefs.edit().putBoolean("lesson1_listening_done", true).apply() }, { currentScreen = "lesson" })
    }
}

@Composable
fun BetterHomeScreen(studentName: String, onChangeName: () -> Unit, onOpenLesson: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Column(Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp)) {
        BetterTopHeader("Book 1", studentName, onChangeName)
        Spacer(Modifier.height(16.dp))
        var n = 1
        while (n <= 31) { val current = n; BetterImageButton("Lesson $current", { onOpenLesson("Lesson $current") }, Modifier.fillMaxWidth()); Spacer(Modifier.height(8.dp)); n = n + 1 }
    }
}

@Composable
fun BetterLessonScreen(lessonName: String, lesson1ListeningDone: Boolean, showHomeworkChoices: Boolean, onShowHomeworkChoices: () -> Unit, onOpenListeningHomework: () -> Unit, onBack: () -> Unit) {
    val listeningAvailable = lessonName == "Lesson 1"; val listeningDone = lesson1ListeningDone && lessonName == "Lesson 1"
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        BetterTopHeader(lessonName)
        Spacer(Modifier.height(16.dp))
        BetterImageButton("Vocabulary", { }, Modifier.fillMaxWidth()); Spacer(Modifier.height(8.dp))
        BetterImageButton("Grammar", { }, Modifier.fillMaxWidth()); Spacer(Modifier.height(8.dp))
        BetterImageButton("Practice", { }, Modifier.fillMaxWidth()); Spacer(Modifier.height(8.dp))
        BetterImageButton("Homework", onShowHomeworkChoices, Modifier.fillMaxWidth(), content = {
            Row(Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
                Text("Homework", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black, style = LocalTextStyle.current.copy(shadow = Shadow(Color.Black, Offset(1.5f, 1.5f), 6f)))
                Spacer(Modifier.width(10.dp)); BetterHomeworkCircleIcon(0, listeningDone); Spacer(Modifier.width(4.dp)); BetterHomeworkCircleIcon(1, false); Spacer(Modifier.width(4.dp)); BetterHomeworkCircleIcon(2, false)
            }
        })
        if (showHomeworkChoices) {
            Spacer(Modifier.height(16.dp)); BetterSmallTitle("Choose homework"); Spacer(Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth()) { BetterImageButton(if (listeningAvailable) "Listening homework" else "Listening homework - coming soon", onOpenListeningHomework, Modifier.fillMaxWidth(), enabled = listeningAvailable); if (listeningDone) Image(painterResource(id = R.drawable.donestamp), "Done", Modifier.align(Alignment.CenterEnd).size(76.dp)) }
            Spacer(Modifier.height(8.dp)); BetterImageButton("Written homework", { }, Modifier.fillMaxWidth(), backgroundResId = R.drawable.graybutton, enabled = false)
            Spacer(Modifier.height(8.dp)); BetterImageButton("Spoken homework", { }, Modifier.fillMaxWidth(), backgroundResId = R.drawable.graybutton, enabled = false)
        }
        Spacer(Modifier.height(16.dp)); BetterImageButton("Back", onBack, Modifier.fillMaxWidth(), backgroundResId = R.drawable.redbutton)
    }
}

@Composable
fun BetterHomeworkScreen(
    lessonName: String, studentName: String, lessonSentences: Array<HomeworkSentence>, answers: MutableList<String>, replayCounts: MutableList<Int>, hintCounts: MutableList<Int>, firstAttemptCorrect: MutableList<Boolean>, submitStep: Int, firstAttemptScore: Int, submittedMessage: String,
    onFirstAttemptScoreChange: (Int) -> Unit, onSubmitStepChange: (Int) -> Unit, onSubmittedMessageChange: (String) -> Unit, onTeacherReportChange: (String) -> Unit, onListeningDone: () -> Unit, onBack: () -> Unit
) {
    val context = LocalContext.current; val focusManager = LocalFocusManager.current; val keyboardController = LocalSoftwareKeyboardController.current; val scrollState = rememberScrollState(); var scrollTop by remember { mutableIntStateOf(0) }; val handler = remember { Handler(Looper.getMainLooper()) }; var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }; val focusRequesters = remember { Array(lessonSentences.size) { FocusRequester() } }
    LaunchedEffect(scrollState.isScrollInProgress) { if (scrollState.isScrollInProgress) focusManager.clearFocus() }; LaunchedEffect(scrollTop) { if (scrollTop > 0) scrollState.scrollTo(0) }
    fun stopAudio() { handler.removeCallbacksAndMessages(null); try { mediaPlayer?.stop() } catch (_: Exception) { }; try { mediaPlayer?.release() } catch (_: Exception) { }; mediaPlayer = null }
    fun playAudioPart(i: Int, startMs: Int, endMs: Int) { stopAudio(); val player = MediaPlayer.create(context, BetterLesson1Audio.AUDIO_RES_ID) ?: return; mediaPlayer = player; player.setVolume(1f, 1f); player.seekTo(startMs); player.start(); handler.postDelayed({ if (mediaPlayer == player) { stopAudio(); focusRequesters[i].requestFocus(); keyboardController?.show() } }, (endMs - startMs).toLong()) }
    fun buildReport(score: Int, corrections: Boolean): String { val b = StringBuilder(); b.append("Student: ").append(studentName).append("\nSubmitted at: ").append(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())).append("\nLesson: ").append(lessonName).append("\nHomework: Listening homework\nOriginal score: ").append(score).append(" / ").append(lessonSentences.size).append("\nStudent submitted answers: yes\nStudent submitted corrections: ").append(if (corrections) "yes" else "no").append("\n"); var i = 0; while (i < lessonSentences.size) { b.append(lessonSentences[i].label).append(": plays = ").append(replayCounts[i]).append(", hints = ").append(hintCounts[i]).append("\n"); i = i + 1 }; return b.toString() }
    fun submitAnswers() { stopAudio(); focusManager.clearFocus(); var score = 0; var i = 0; while (i < lessonSentences.size) { val ok = isCorrectAnswer(answers[i], lessonSentences[i].correctText); firstAttemptCorrect[i] = ok; if (ok) score = score + 1; i = i + 1 }; onFirstAttemptScoreChange(score); onSubmitStepChange(1); onSubmittedMessageChange("First attempt score: $score / ${lessonSentences.size}"); onTeacherReportChange(buildReport(score, false)); scrollTop = scrollTop + 1 }
    fun submitCorrections() { stopAudio(); focusManager.clearFocus(); var allOk = true; var i = 0; while (i < lessonSentences.size) { if (!isCorrectAnswer(answers[i], lessonSentences[i].correctText)) allOk = false; i = i + 1 }; if (allOk) { onSubmitStepChange(2); onSubmittedMessageChange("Submitted! All corrections are correct."); onTeacherReportChange(buildReport(firstAttemptScore, true)); onListeningDone() } else { onSubmittedMessageChange("Some corrections are still incorrect. Please check the red X sentences."); onTeacherReportChange(buildReport(firstAttemptScore, false)) }; scrollTop = scrollTop + 1 }
    DisposableEffect(Unit) { onDispose { stopAudio() } }
    Column(Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp)) {
        BetterTopHeader("$lessonName - Listening")
        if (submittedMessage != "") { Spacer(Modifier.height(8.dp)); Text(submittedMessage, color = Color.White, fontWeight = FontWeight.Bold, style = LocalTextStyle.current.copy(shadow = Shadow(Color.Black, Offset(1f, 1f), 5f))) }
        Spacer(Modifier.height(16.dp)); var i = 0
        while (i < lessonSentences.size) { val index = i; val s = lessonSentences[index]; BetterSentenceRow(s, answers[index], { answers[index] = it }, replayCounts[index], submitStep, firstAttemptCorrect[index], isCorrectAnswer(answers[index], s.correctText), hintCounts[index], focusRequesters[index], { replayCounts[index] = replayCounts[index] + 1; playAudioPart(index, s.startMs, s.endMs) }, { stopAudio() }, { if (canUseHint(answers[index], replayCounts[index], submitStep >= 1, hintCounts[index], s.correctText)) { hintCounts[index] = hintCounts[index] + 1; onTeacherReportChange(buildReport(firstAttemptScore, submitStep == 2)) } }); Spacer(Modifier.height(12.dp)); i = i + 1 }
        if (submitStep == 0) BetterImageButton("Submit answers", { submitAnswers() }, Modifier.fillMaxWidth()) else if (submitStep == 1) BetterImageButton("Submit corrections", { submitCorrections() }, Modifier.fillMaxWidth()) else Button(onClick = { }, enabled = false, colors = ButtonDefaults.buttonColors(disabledContainerColor = Color(0xFF2E7D32), disabledContentColor = Color.White)) { Text("Submitted") }
        Spacer(Modifier.height(16.dp)); BetterImageButton("Back", { stopAudio(); onBack() }, Modifier.fillMaxWidth(), backgroundResId = R.drawable.redbutton)
    }
}

@Composable
fun BetterSentenceRow(sentence: HomeworkSentence, answer: String, onAnswerChange: (String) -> Unit, replayCount: Int, submitStep: Int, firstAttemptCorrect: Boolean, currentCorrect: Boolean, hintCount: Int, focusRequester: FocusRequester, onPlay: () -> Unit, onStop: () -> Unit, onHint: () -> Unit) {
    Card(Modifier.fillMaxWidth(), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Row { Text(sentence.label, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f)); if (submitStep >= 1) Text(if (submitStep == 2 || currentCorrect) "✓" else "✕", color = if (submitStep == 2 || currentCorrect) Color(0xFF2E7D32) else Color(0xFFC62828), fontSize = 64.sp) }
            Spacer(Modifier.height(8.dp)); Row { BetterImageButton("▶", onPlay, Modifier.width(90.dp), heightDp = 64, fontSize = 20); Spacer(Modifier.width(8.dp)); BetterImageButton("Stop", onStop, Modifier.width(112.dp), backgroundResId = R.drawable.redbutton, heightDp = 64, fontSize = 16) }
            Spacer(Modifier.height(8.dp)); TextField(answer, onAnswerChange, Modifier.fillMaxWidth().focusRequester(focusRequester)); Spacer(Modifier.height(4.dp)); Text("Plays: $replayCount")
            if (submitStep >= 1) { Text(if (firstAttemptCorrect) "First attempt: correct" else "First attempt: incorrect"); if (cleanAnswer(answer) == "") Text("Type an answer before using hints.") else if (replayCount < 5) Text("Hint locked: listen ${5 - replayCount} more time(s).") else if (submitStep < 2 && !currentCorrect) BetterImageButton("Reveal next word", onHint, Modifier.fillMaxWidth(), backgroundResId = R.drawable.graybutton, heightDp = 64, fontSize = 16); if (hintCount > 0) { Text("Hint: ${revealedHintText(sentence.correctText, hintCount)}"); Text("Hints used: $hintCount") } }
        }
    }
}
