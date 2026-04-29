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
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myenglish.ui.theme.MyEnglishTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareSplashWindow4(window)
        overridePendingTransition(0, 0)
        setContent { MyEnglishTheme { PolishedAppWithSplash(this) } }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}

fun prepareSplashWindow4(window: Window) {
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}

fun restoreAppWindow4(activity: Activity) {
    val window = activity.window
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    window.statusBarColor = android.graphics.Color.rgb(5, 22, 52)
    window.navigationBarColor = android.graphics.Color.rgb(5, 22, 52)
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}

private object Audio4 {
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
fun PolishedAppWithSplash(activity: Activity) {
    var splashVisible by remember { mutableStateOf(true) }
    var alphaTarget by remember { mutableStateOf(0f) }
    val alpha by animateFloatAsState(alphaTarget, animationSpec = tween(950), label = "splashAlpha4")

    DisposableEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ alphaTarget = 1f }, 420)
        handler.postDelayed({ alphaTarget = 0f }, 2750)
        handler.postDelayed({ splashVisible = false; restoreAppWindow4(activity) }, 3950)
        onDispose { handler.removeCallbacksAndMessages(null) }
    }

    if (splashVisible) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(painterResource(id = R.drawable.appsplash), "My English splash", Modifier.fillMaxWidth(0.92f).alpha(alpha), contentScale = ContentScale.Fit)
        }
    } else {
        Bg4 { Root4() }
    }
}

@Composable
fun Bg4(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Image(painterResource(id = R.drawable.screenbg), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        content()
    }
}

@Composable
fun Header4(leftTitle: String) {
    val context = LocalContext.current
    val titleId = remember { context.resources.getIdentifier("title", "drawable", context.packageName) }
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        StrokeGlowTitle4(leftTitle, Modifier.weight(1f))
        if (titleId != 0) {
            Image(painterResource(id = titleId), "My English", Modifier.width(300.dp).height(124.dp), contentScale = ContentScale.Fit)
        } else {
            StrokeGlowTitle4("My English", Modifier.width(220.dp))
        }
    }
}

@Composable
fun StrokeGlowTitle4(text: String, modifier: Modifier = Modifier, fontSize: Int = 28) {
    Box(modifier.padding(top = 6.dp)) {
        val style = LocalTextStyle.current.copy(fontFamily = FontFamily.Cursive, shadow = Shadow(Color.White, Offset(0f, 0f), 13f))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, style = style, modifier = Modifier.offset(3.dp, 3.dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, style = style, modifier = Modifier.offset((-1).dp, 0.dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, style = style, modifier = Modifier.offset(1.dp, 0.dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, style = style, modifier = Modifier.offset(0.dp, (-1).dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, style = style, modifier = Modifier.offset(0.dp, 1.dp))
        Text(text, color = Color.White, fontSize = fontSize.sp, fontWeight = FontWeight.Black, style = style)
    }
}

@Composable
fun StudentBadge4(studentName: String, onChangeName: () -> Unit) {
    Box(
        Modifier
            .shadow(8.dp, RoundedCornerShape(14.dp))
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(14.dp))
            .clickable { onChangeName() }
            .padding(horizontal = 22.dp, vertical = 8.dp)
    ) {
        Text(
            studentName,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Cursive,
            style = LocalTextStyle.current.copy(shadow = Shadow(Color(0x99000000), Offset(1.7f, 1.7f), 2f))
        )
    }
}

@Composable
fun Button4(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundResId: Int = R.drawable.bluebutton,
    enabled: Boolean = true,
    heightDp: Int = 68,
    fontSize: Int = 21,
    content: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(
        modifier
            .fillMaxWidth(0.6f)
            .height(heightDp.dp)
            .shadow(9.dp, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .alpha(if (enabled) 1f else 0.58f)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(painterResource(id = backgroundResId), null, Modifier.matchParentSize().graphicsLayer(scaleX = 1.28f), contentScale = ContentScale.Crop)
        Canvas(Modifier.matchParentSize()) {
            drawRoundRect(Color(0x55FFFFFF), Offset(6f, 4f), Size(size.width - 12f, size.height * 0.33f), CornerRadius(18f, 18f))
            drawRoundRect(Color(0x66000000), Offset(6f, size.height * 0.68f), Size(size.width - 12f, size.height * 0.26f), CornerRadius(18f, 18f))
            drawRoundRect(Color.White, Offset(2f, 2f), Size(size.width - 4f, size.height - 4f), CornerRadius(18f, 18f), style = Stroke(width = 2.2f))
        }
        if (content == null) {
            Text(
                text,
                color = Color.White,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Cursive,
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(shadow = Shadow(Color.Black, Offset(2.2f, 2.2f), 4f))
            )
        } else content()
    }
}

@Composable
fun HomeworkIcon4(kind: Int, done: Boolean) {
    val iconColor = if (done) { if (kind == 0) Color(0xFF00AEEF) else if (kind == 1) Color(0xFFFFB300) else Color(0xFFE91E63) } else Color(0xFF888888)
    Canvas(Modifier.size(30.dp)) {
        val w = size.width; val h = size.height
        drawCircle(Color.White, w * 0.48f, Offset(w / 2f, h / 2f))
        drawCircle(Color.Black, w * 0.48f, Offset(w / 2f, h / 2f), style = Stroke(width = 2.1f))
        if (kind == 0) {
            drawArc(iconColor, 205f, 130f, false, Offset(w * 0.24f, h * 0.20f), Size(w * 0.52f, h * 0.52f), style = Stroke(width = 2.8f))
            drawRoundRect(iconColor, Offset(w * 0.22f, h * 0.50f), Size(w * 0.14f, h * 0.24f), CornerRadius(4f, 4f)); drawRoundRect(iconColor, Offset(w * 0.64f, h * 0.50f), Size(w * 0.14f, h * 0.24f), CornerRadius(4f, 4f))
        } else if (kind == 1) {
            drawRoundRect(iconColor, Offset(w * 0.25f, h * 0.23f), Size(w * 0.50f, h * 0.56f), CornerRadius(4f, 4f), style = Stroke(width = 2.5f)); drawLine(iconColor, Offset(w * 0.35f, h * 0.40f), Offset(w * 0.66f, h * 0.40f), strokeWidth = 2f); drawLine(iconColor, Offset(w * 0.35f, h * 0.54f), Offset(w * 0.66f, h * 0.54f), strokeWidth = 2f); drawLine(iconColor, Offset(w * 0.25f, h * 0.30f), Offset(w * 0.25f, h * 0.72f), strokeWidth = 3f)
        } else {
            drawRoundRect(iconColor, Offset(w * 0.38f, h * 0.18f), Size(w * 0.24f, h * 0.42f), CornerRadius(8f, 8f), style = Stroke(width = 2.6f)); drawArc(iconColor, 25f, 130f, false, Offset(w * 0.25f, h * 0.40f), Size(w * 0.50f, h * 0.28f), style = Stroke(width = 2.4f)); drawLine(iconColor, Offset(w * 0.50f, h * 0.62f), Offset(w * 0.50f, h * 0.78f), strokeWidth = 2.4f); drawLine(iconColor, Offset(w * 0.34f, h * 0.80f), Offset(w * 0.66f, h * 0.80f), strokeWidth = 2.4f)
        }
    }
}

@Composable
fun Root4() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("my_english_prefs", Context.MODE_PRIVATE) }
    val sentences = Audio4.sentences
    var screen by remember { mutableStateOf("home") }; var selectedLesson by remember { mutableStateOf("Lesson 1") }; var showChoices by remember { mutableStateOf(false) }
    var studentName by remember { mutableStateOf(prefs.getString("student_name", "") ?: "") }; var showNameDialog by remember { mutableStateOf(studentName == "") }; var lesson1Done by remember { mutableStateOf(prefs.getBoolean("lesson1_listening_done", false)) }
    val answers = remember { mutableStateListOf<String>() }; val plays = remember { mutableStateListOf<Int>() }; val hints = remember { mutableStateListOf<Int>() }; val firstCorrect = remember { mutableStateListOf<Boolean>() }
    fun resetAttempt() { answers.clear(); plays.clear(); hints.clear(); firstCorrect.clear(); var i = 0; while (i < sentences.size) { answers.add(""); plays.add(0); hints.add(0); firstCorrect.add(false); i++ } }
    LaunchedEffect(Unit) { resetAttempt() }
    var submitStep by remember { mutableIntStateOf(0) }; var score by remember { mutableIntStateOf(0) }; var message by remember { mutableStateOf("") }; var report by remember { mutableStateOf("") }
    fun retryIfNeeded() { if (submitStep == 2) { resetAttempt(); submitStep = 0; score = 0; message = ""; report = "" } }
    BackHandler(enabled = screen != "home") { if (screen == "homework") screen = "lesson" else if (screen == "lesson") screen = "home" }
    if (showNameDialog) StudentNameDialog(studentName) { newName -> studentName = newName; prefs.edit().putString("student_name", newName).apply(); showNameDialog = false }
    when (screen) {
        "home" -> Home4(displayStudentName(studentName), { showNameDialog = true }) { lesson -> selectedLesson = lesson; showChoices = false; screen = "lesson" }
        "lesson" -> Lesson4(selectedLesson, lesson1Done, showChoices, { showChoices = true }, { if (selectedLesson == "Lesson 1") { retryIfNeeded(); screen = "homework" } }, { screen = "home" })
        "homework" -> Homework4(selectedLesson, displayStudentName(studentName), sentences, answers, plays, hints, firstCorrect, submitStep, score, message, { score = it }, { submitStep = it }, { message = it }, { report = it }, { lesson1Done = true; prefs.edit().putBoolean("lesson1_listening_done", true).apply() }, { screen = "lesson" })
    }
}

@Composable
fun Home4(studentName: String, onChangeName: () -> Unit, openLesson: (String) -> Unit) {
    val scroll = rememberScrollState()
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().verticalScroll(scroll).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Header4("Book 1"); Spacer(Modifier.height(12.dp))
            var n = 1
            while (n <= 31) { val c = n; Button4("Lesson $c", { openLesson("Lesson $c") }); Spacer(Modifier.height(7.dp)); n++ }
            Spacer(Modifier.height(72.dp))
        }
        StudentBadge4(studentName, onChangeName, Modifier.align(Alignment.BottomEnd).padding(16.dp))
    }
}

@Composable
fun StudentBadge4(studentName: String, onChangeName: () -> Unit, modifier: Modifier) {
    Box(modifier.shadow(8.dp, RoundedCornerShape(14.dp)).background(Color.White, RoundedCornerShape(14.dp)).border(2.dp, Color.Black, RoundedCornerShape(14.dp)).clickable { onChangeName() }.padding(horizontal = 22.dp, vertical = 8.dp)) {
        Text(studentName, color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Cursive, style = LocalTextStyle.current.copy(shadow = Shadow(Color(0x99000000), Offset(1.7f, 1.7f), 2f)))
    }
}

@Composable
fun Lesson4(name: String, lesson1Done: Boolean, showChoices: Boolean, showHomework: () -> Unit, openListening: () -> Unit, back: () -> Unit) {
    val listeningAvailable = name == "Lesson 1"; val listeningDone = lesson1Done && name == "Lesson 1"
    Column(Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Header4(name); Spacer(Modifier.height(14.dp))
        Button4("Vocabulary", { }); Spacer(Modifier.height(7.dp)); Button4("Grammar", { }); Spacer(Modifier.height(7.dp)); Button4("Practice", { }); Spacer(Modifier.height(7.dp))
        Button4("Homework", showHomework, content = {
            Text("Homework", Modifier.align(Alignment.Center), color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Black, fontFamily = FontFamily.Cursive, style = LocalTextStyle.current.copy(shadow = Shadow(Color.Black, Offset(2.2f, 2.2f), 4f)))
            Row(Modifier.align(Alignment.CenterEnd).padding(end = 12.dp), verticalAlignment = Alignment.CenterVertically) { HomeworkIcon4(0, listeningDone); Spacer(Modifier.width(4.dp)); HomeworkIcon4(1, false); Spacer(Modifier.width(4.dp)); HomeworkIcon4(2, false) }
        })
        if (showChoices) {
            Spacer(Modifier.height(16.dp)); StrokeGlowTitle4("Choose homework", fontSize = 22); Spacer(Modifier.height(8.dp))
            Box(contentAlignment = Alignment.Center) { Button4(if (listeningAvailable) "Listening homework" else "Listening homework - coming soon", openListening, enabled = listeningAvailable); if (listeningDone) Image(painterResource(id = R.drawable.donestamp), "Done", Modifier.align(Alignment.CenterEnd).size(72.dp)) }
            Spacer(Modifier.height(7.dp)); Button4("Written homework", { }, backgroundResId = R.drawable.graybutton, enabled = false); Spacer(Modifier.height(7.dp)); Button4("Spoken homework", { }, backgroundResId = R.drawable.graybutton, enabled = false)
        }
        Spacer(Modifier.height(16.dp)); Button4("Back", back, backgroundResId = R.drawable.redbutton)
    }
}

@Composable
fun Homework4(name: String, studentName: String, sentences: Array<HomeworkSentence>, answers: MutableList<String>, plays: MutableList<Int>, hints: MutableList<Int>, firstCorrect: MutableList<Boolean>, submitStep: Int, score: Int, msg: String, setScore: (Int) -> Unit, setStep: (Int) -> Unit, setMsg: (String) -> Unit, setReport: (String) -> Unit, done: () -> Unit, back: () -> Unit) {
    val context = LocalContext.current; val focusManager = LocalFocusManager.current; val keyboard = LocalSoftwareKeyboardController.current; val scroll = rememberScrollState(); var topReq by remember { mutableIntStateOf(0) }; val handler = remember { Handler(Looper.getMainLooper()) }; var player by remember { mutableStateOf<MediaPlayer?>(null) }; val focus = remember { Array(sentences.size) { FocusRequester() } }
    LaunchedEffect(scroll.isScrollInProgress) { if (scroll.isScrollInProgress) focusManager.clearFocus() }; LaunchedEffect(topReq) { if (topReq > 0) scroll.scrollTo(0) }
    fun stop() { handler.removeCallbacksAndMessages(null); try { player?.stop() } catch (_: Exception) {}; try { player?.release() } catch (_: Exception) {}; player = null }
    fun play(i: Int, s: Int, e: Int) { stop(); val p = MediaPlayer.create(context, Audio4.AUDIO_RES_ID) ?: return; player = p; p.setVolume(1f, 1f); p.seekTo(s); p.start(); handler.postDelayed({ if (player == p) { stop(); focus[i].requestFocus(); keyboard?.show() } }, (e - s).toLong()) }
    fun report(sc: Int, corr: Boolean): String { val b = StringBuilder(); b.append("Student: ").append(studentName).append("\nSubmitted at: ").append(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())).append("\nLesson: ").append(name).append("\nHomework: Listening homework\nOriginal score: ").append(sc).append(" / ").append(sentences.size).append("\nStudent submitted answers: yes\nStudent submitted corrections: ").append(if (corr) "yes" else "no").append("\n"); var i = 0; while (i < sentences.size) { b.append(sentences[i].label).append(": plays = ").append(plays[i]).append(", hints = ").append(hints[i]).append("\n"); i++ }; return b.toString() }
    fun submitAnswers() { stop(); focusManager.clearFocus(); var sc = 0; var i = 0; while (i < sentences.size) { val ok = isCorrectAnswer(answers[i], sentences[i].correctText); firstCorrect[i] = ok; if (ok) sc++; i++ }; setScore(sc); setStep(1); setMsg("First attempt score: $sc / ${sentences.size}"); setReport(report(sc, false)); topReq++ }
    fun submitCorrections() { stop(); focusManager.clearFocus(); var all = true; var i = 0; while (i < sentences.size) { if (!isCorrectAnswer(answers[i], sentences[i].correctText)) all = false; i++ }; if (all) { setStep(2); setMsg("Submitted! All corrections are correct."); setReport(report(score, true)); done() } else { setMsg("Some corrections are still incorrect. Please check the red X sentences."); setReport(report(score, false)) }; topReq++ }
    DisposableEffect(Unit) { onDispose { stop() } }
    Column(Modifier.fillMaxSize().verticalScroll(scroll).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Header4("$name - Listening"); if (msg != "") { Spacer(Modifier.height(8.dp)); Text(msg, color = Color.White, fontWeight = FontWeight.Bold, style = LocalTextStyle.current.copy(shadow = Shadow(Color.Black, Offset(1f, 1f), 5f))) }; Spacer(Modifier.height(16.dp))
        var i = 0
        while (i < sentences.size) { val idx = i; val sentence = sentences[idx]; Row4(sentence, answers[idx], { answers[idx] = it }, plays[idx], submitStep, firstCorrect[idx], isCorrectAnswer(answers[idx], sentence.correctText), hints[idx], focus[idx], { plays[idx]++; play(idx, sentence.startMs, sentence.endMs) }, { stop() }, { if (canUseHint(answers[idx], plays[idx], submitStep >= 1, hints[idx], sentence.correctText)) { hints[idx]++; setReport(report(score, submitStep == 2)) } }); Spacer(Modifier.height(12.dp)); i++ }
        if (submitStep == 0) Button4("Submit answers", { submitAnswers() }) else if (submitStep == 1) Button4("Submit corrections", { submitCorrections() }) else Button(onClick = { }, enabled = false, colors = ButtonDefaults.buttonColors(disabledContainerColor = Color(0xFF2E7D32), disabledContentColor = Color.White)) { Text("Submitted") }
        Spacer(Modifier.height(16.dp)); Button4("Back", { stop(); back() }, backgroundResId = R.drawable.redbutton)
    }
}

@Composable
fun Row4(sentence: HomeworkSentence, answer: String, change: (String) -> Unit, playCount: Int, submitStep: Int, firstOk: Boolean, currentOk: Boolean, hintCount: Int, focus: FocusRequester, play: () -> Unit, stop: () -> Unit, hint: () -> Unit) {
    Card(Modifier.fillMaxWidth(), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Row { Text(sentence.label, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f)); if (submitStep >= 1) Text(if (submitStep == 2 || currentOk) "✓" else "✕", color = if (submitStep == 2 || currentOk) Color(0xFF2E7D32) else Color(0xFFC62828), fontSize = 64.sp) }
            Spacer(Modifier.height(8.dp)); Row { Button4("▶", play, Modifier.width(90.dp), heightDp = 60, fontSize = 22); Spacer(Modifier.width(8.dp)); Button4("Stop", stop, Modifier.width(112.dp), backgroundResId = R.drawable.redbutton, heightDp = 60, fontSize = 18) }
            Spacer(Modifier.height(8.dp)); TextField(answer, change, Modifier.fillMaxWidth().focusRequester(focus)); Spacer(Modifier.height(4.dp)); Text("Plays: $playCount")
            if (submitStep >= 1) { Text(if (firstOk) "First attempt: correct" else "First attempt: incorrect"); if (cleanAnswer(answer) == "") Text("Type an answer before using hints.") else if (playCount < 5) Text("Hint locked: listen ${5 - playCount} more time(s).") else if (submitStep < 2 && !currentOk) Button4("Reveal next word", hint, backgroundResId = R.drawable.graybutton, heightDp = 60, fontSize = 18); if (hintCount > 0) { Text("Hint: ${revealedHintText(sentence.correctText, hintCount)}"); Text("Hints used: $hintCount") } }
        }
    }
}
