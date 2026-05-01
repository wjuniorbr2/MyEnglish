package com.example.myenglish

import com.example.myenglish.utils.prepareSplashWindow
import com.example.myenglish.utils.restoreAppWindow
import com.example.myenglish.utils.canUseHint
import com.example.myenglish.utils.cleanAnswer
import com.example.myenglish.utils.countWords
import com.example.myenglish.utils.isCorrectAnswer
import com.example.myenglish.utils.revealedHintText
import com.example.myenglish.data.HomeworkSentence
import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.compose.ui.draw.rotate
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
import java.util.StringTokenizer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareSplashWindow(window)
        overridePendingTransition(0, 0)

        setContent {
            MyEnglishTheme {
                AppWithSplash(this)
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}

private object Lesson3Homework1Audio {
    val AUDIO_RES_ID = R.raw.lesson3

    val sentences = arrayOf(
        HomeworkSentence("Sentence 1", "I like.", 0, 3000),
        HomeworkSentence("Sentence 2", "You eat meat.", 3000, 6000),
        HomeworkSentence("Sentence 3", "I don't like.", 6000, 9000)
    )
}

private object HomeworkData {
    val LESSON_1_AUDIO_RES_ID = R.raw.lesson1

    val lesson1Sentences = arrayOf(
        HomeworkSentence("Sentence 1", "I like.", 870, 2527),
        HomeworkSentence("Sentence 2", "You eat meat.", 3750, 5880),
        HomeworkSentence("Sentence 3", "I don't like.", 7610, 9426),
        HomeworkSentence("Sentence 4", "They don't drink water.", 11140, 13257),
        HomeworkSentence("Sentence 5", "Do you drink milk?", 15400, 17676),
        HomeworkSentence("Sentence 6", "Do we eat meat?", 19230, 21388),
        HomeworkSentence("Sentence 7", "I like bread with butter.", 23320, 26212),
        HomeworkSentence("Sentence 8", "I eat.", 28140, 29943),
        HomeworkSentence("Sentence 9", "You like.", 31790, 33424),
        HomeworkSentence("Sentence 10", "I don't like.", 35040, 37255),
        HomeworkSentence("Sentence 11", "You don't like.", 38820, 40794),
        HomeworkSentence("Sentence 12", "Do you like?", 42610, 44612),
        HomeworkSentence("Sentence 13", "Do they like?", 46060, 48280),
        HomeworkSentence("Sentence 14", "Don't you like?", 50060, 53014),
        HomeworkSentence("Sentence 15", "Don't they like?", 54040, 56920),
        HomeworkSentence("Sentence 16", "I like to eat.", 58100, 60369),
        HomeworkSentence("Sentence 17", "I eat in the morning.", 61660, 64021),
        HomeworkSentence("Sentence 18", "I drink milk in the morning.", 65550, 68597),
        HomeworkSentence("Sentence 19", "Do you like milk?", 70140, 72258),
        HomeworkSentence("Sentence 20", "They don't eat bread with butter.", 74010, 77491),
        HomeworkSentence("Sentence 21", "I don't like to eat in the morning.", 79160, 82187),
        HomeworkSentence("Sentence 22", "I drink water.", 83690, 85800),
        HomeworkSentence("Sentence 23", "I eat bread.", 87270, 89614),
        HomeworkSentence("Sentence 24", "I drink juice.", 91220, 93297),
        HomeworkSentence("Sentence 25", "I don't like milk.", 95200, 97238),
        HomeworkSentence("Sentence 26", "I don't drink in the morning.", 99010, 101591),
        HomeworkSentence("Sentence 27", "You eat.", 103310, 104970),
        HomeworkSentence("Sentence 28", "We like.", 106630, 108521),
        HomeworkSentence("Sentence 29", "They like to drink at night.", 110110, 112936),
        HomeworkSentence("Sentence 30", "We don't drink in the morning.", 114640, 116940),
        HomeworkSentence("Sentence 31", "Do you like butter?", 118630, 120577),
        HomeworkSentence("Sentence 32", "Do they like to drink beer?", 122320, 124916)
    )

    val lesson2Sentences = arrayOf(
        HomeworkSentence("Sentence 1", "You speak French.", 1060, 3990),
        HomeworkSentence("Sentence 2", "I don't want.", 5430, 8260),
        HomeworkSentence("Sentence 3", "We don’t want to speak french.", 10020, 14240),
        HomeworkSentence("Sentence 4", "Do they want milk?", 15690, 20000),
        HomeworkSentence("Sentence 5", "We wanna eat bread and ham", 20860, 26000),
        HomeworkSentence("Sentence 6", "I want to study there.", 26670, 31000),
        HomeworkSentence("Sentence 7", "I eat here in the morning.", 32000, 36140),
        HomeworkSentence("Sentence 8", "You study here.", 37420, 41500),
        HomeworkSentence("Sentence 9", "I study my small lesson.", 42390, 46470),
        HomeworkSentence("Sentence 10", "I don't want.", 47980, 51200),
        HomeworkSentence("Sentence 11", "We don’t wanna drink.", 51930, 55280),
        HomeworkSentence("Sentence 12", "Do you want water?", 56670, 60140),
        HomeworkSentence("Sentence 13", "Do they study german?", 61410, 65050),
        HomeworkSentence("Sentence 14", "Don’t you speak Portuguese?", 66340, 70450),
        HomeworkSentence("Sentence 15", "Don’t they want?", 71480, 75000),
        HomeworkSentence("Sentence 16", "I want to eat there.", 76180, 79960),
        HomeworkSentence("Sentence 17", "I study at night.", 81240, 85270),
        HomeworkSentence("Sentence 18", "We want milk in the afternoon.", 86310, 90790),
        HomeworkSentence("Sentence 19", "Do you want hot milk?", 91930, 95860),
        HomeworkSentence("Sentence 20", "Don’t they want to study English?", 96930, 101190),
        HomeworkSentence("Sentence 21", "I don’t want to eat in the morning.", 102690, 107120),
        HomeworkSentence("Sentence 22", "I don’t speak German.", 108530, 112220),
        HomeworkSentence("Sentence 23", "We study Spanish with you.", 113360, 117870),
        HomeworkSentence("Sentence 24", "Do you want to speak English?", 119090, 123600),
        HomeworkSentence("Sentence 25", "I don’t want to study math.", 124670, 128670),
        HomeworkSentence("Sentence 26", "They don’t speak with you.", 130120, 133980),
        HomeworkSentence("Sentence 27", "I study wine.", 135460, 139230),
        HomeworkSentence("Sentence 28", "Do you want music here?", 140980, 144810),
        HomeworkSentence("Sentence 29", "They want to drink at night.", 146460, 150650),
        HomeworkSentence("Sentence 30", "We don’t speak English in the morning.", 152010, 156480),
        HomeworkSentence("Sentence 31", "I don’t speak Portuguese with you.", 157900, 163500),
        HomeworkSentence("Sentence 32", "Do you wanna drink cold beer?", 164860, 169500)
    )

    fun sentencesForLesson(lessonName: String): Array<HomeworkSentence> {
        return if (lessonName == "Lesson 3") {
            Lesson3Homework1Audio.sentences
        } else if (lessonName == "Lesson 2") {
            lesson2Sentences
        } else {
            lesson1Sentences
        }
    }

    fun audioResIdForLesson(context: Context, lessonName: String): Int {
        if (lessonName == "Lesson 1") return LESSON_1_AUDIO_RES_ID
        if (lessonName == "Lesson 2") return context.resources.getIdentifier("lesson2", "raw", context.packageName)
        if (lessonName == "Lesson 3") return Lesson3Homework1Audio.AUDIO_RES_ID
        return 0
    }

    fun hasListeningHomework(lessonName: String): Boolean {
        return lessonName == "Lesson 1" || lessonName == "Lesson 2" || lessonName == "Lesson 3"
    }
}

fun currentDateTimeText(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(Date())
}

fun displayStudentName(fullName: String): String {
    val parts = StringTokenizer(fullName, " ")
    if (!parts.hasMoreTokens()) return "Student"

    val firstName = parts.nextToken()

    if (parts.hasMoreTokens()) {
        val secondName = parts.nextToken()
        if (secondName.isNotEmpty()) {
            return firstName + " " + Character.toUpperCase(secondName[0]) + "."
        }
    }

    return firstName
}

fun attemptPrefix(lessonName: String): String {
    return lessonName.lowercase(Locale.ROOT).replace(" ", "_") + "_listening_"
}

fun joinStrings(values: List<String>): String {
    val builder = StringBuilder()
    var i = 0
    while (i < values.size) {
        if (i > 0) builder.append("<|>")
        builder.append(values[i].replace("<|>", " "))
        i++
    }
    return builder.toString()
}

fun joinInts(values: List<Int>): String {
    val builder = StringBuilder()
    var i = 0
    while (i < values.size) {
        if (i > 0) builder.append(",")
        builder.append(values[i])
        i++
    }
    return builder.toString()
}

fun joinBooleans(values: List<Boolean>): String {
    val builder = StringBuilder()
    var i = 0
    while (i < values.size) {
        if (i > 0) builder.append(",")
        builder.append(if (values[i]) "1" else "0")
        i++
    }
    return builder.toString()
}

@Composable
fun AppWithSplash(activity: Activity) {
    var splashVisible by remember { mutableStateOf(true) }
    var alphaTarget by remember { mutableStateOf(0f) }
    val alpha by animateFloatAsState(alphaTarget, animationSpec = tween(950), label = "splashAlpha")

    DisposableEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ alphaTarget = 1f }, 420)
        handler.postDelayed({ alphaTarget = 0f }, 2750)
        handler.postDelayed({ splashVisible = false; restoreAppWindow(activity) }, 3950)
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
        AppBackground {
            AppRoot()
        }
    }
}

@Composable
fun AppBackground(content: @Composable () -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.screenbg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        content()
    }
}

@Composable
fun Header(leftTitle: String, subtitle: String? = null) {
    val context = LocalContext.current
    val titleId = remember { context.resources.getIdentifier("title", "drawable", context.packageName) }

    Box(Modifier.fillMaxWidth().height(128.dp)) {
        Column(
            Modifier
                .align(Alignment.TopStart)
                .padding(start = 14.dp, top = if (subtitle == null) 30.dp else 22.dp)
        ) {
            StrokeGlowTitle(leftTitle)
            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                StrokeGlowTitle(subtitle, fontSize = 24)
            }
        }

        if (titleId != 0) {
            Image(
                painter = painterResource(id = titleId),
                contentDescription = "My English",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 18.dp)
                    .width(306.dp)
                    .height(127.dp),
                contentScale = ContentScale.Fit
            )
        } else {
            StrokeGlowTitle("My English", Modifier.align(Alignment.TopEnd).padding(top = 30.dp), 26)
        }
    }
}

@Composable
fun StrokeGlowTitle(text: String, modifier: Modifier = Modifier, fontSize: Int = 28) {
    Box(modifier.padding(top = 6.dp)) {
        val style = LocalTextStyle.current.copy(
            fontFamily = FontFamily.Serif,
            shadow = Shadow(Color.White, Offset(0f, 0f), 13f)
        )

        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset(3.dp, 3.dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset((-3).dp, 0.dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset(3.dp, 0.dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset(0.dp, (-3).dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset(0.dp, 3.dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset((-2).dp, (-2).dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset(2.dp, (-2).dp))
        Text(text, color = Color.Black, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style, modifier = Modifier.offset((-2).dp, 2.dp))
        Text(text, color = Color.White, fontSize = fontSize.sp, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false, style = style)
    }
}

@Composable
fun StudentBadge(studentName: String, onChangeName: () -> Unit, modifier: Modifier) {
    Box(
        modifier
            .shadow(10.dp, RoundedCornerShape(14.dp))
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, Color.White, RoundedCornerShape(14.dp))
            .padding(2.dp)
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
            .clickable { onChangeName() }
            .padding(horizontal = 22.dp, vertical = 8.dp)
    ) {
        Text(
            text = studentName,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Cursive,
            style = LocalTextStyle.current.copy(
                shadow = Shadow(Color(0x99000000), Offset(1.7f, 1.7f), 2f)
            )
        )
    }
}

@Composable
fun ArtButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(0.6f),
    backgroundResId: Int = R.drawable.bluebutton,
    enabled: Boolean = true,
    heightDp: Int = 60,
    fontSize: Int = 19,
    content: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(
        modifier
            .height(heightDp.dp)
            .shadow(7.dp, RoundedCornerShape(18.dp))
            .alpha(if (enabled) 1f else 0.58f)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(Modifier.matchParentSize().clip(RoundedCornerShape(18.dp))) {
            Image(
                painter = painterResource(id = backgroundResId),
                contentDescription = null,
                modifier = Modifier.matchParentSize().graphicsLayer(scaleX = 1.42f, scaleY = 2.80f),
                contentScale = ContentScale.Crop
            )
            Canvas(Modifier.matchParentSize()) {
                drawRoundRect(Color(0x28FFFFFF), Offset(6f, 4f), Size(size.width - 12f, size.height * 0.06f), CornerRadius(18f, 18f))
                drawRoundRect(Color(0x30000000), Offset(6f, size.height * 0.91f), Size(size.width - 12f, size.height * 0.04f), CornerRadius(18f, 18f))
                drawRoundRect(Color.White, Offset(2f, 2f), Size(size.width - 4f, size.height - 4f), CornerRadius(18f, 18f), style = Stroke(width = 1.4f))
            }
        }

        if (content == null) {
            Text(
                text = text,
                color = Color.White,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(Color.Black, Offset(2f, 2f), 4f)
                )
            )
        } else {
            content()
        }
    }
}

@Composable
fun HomeworkIcon(kind: Int, done: Boolean) {
    val iconColor = if (done) {
        if (kind == 0) Color(0xFF00AEEF) else if (kind == 1) Color(0xFFFFB300) else Color(0xFFE91E63)
    } else {
        Color(0xFF888888)
    }

    Canvas(Modifier.size(23.dp)) {
        val w = size.width
        val h = size.height

        drawCircle(Color.White, w * 0.48f, Offset(w / 2f, h / 2f))
        drawCircle(Color.Black, w * 0.48f, Offset(w / 2f, h / 2f), style = Stroke(width = 1.8f))

        if (kind == 0) {
            drawArc(iconColor, 205f, 130f, false, Offset(w * 0.24f, h * 0.20f), Size(w * 0.52f, h * 0.52f), style = Stroke(width = 2.3f))
            drawRoundRect(iconColor, Offset(w * 0.22f, h * 0.50f), Size(w * 0.14f, h * 0.24f), CornerRadius(4f, 4f))
            drawRoundRect(iconColor, Offset(w * 0.64f, h * 0.50f), Size(w * 0.14f, h * 0.24f), CornerRadius(4f, 4f))
        } else if (kind == 1) {
            drawRoundRect(iconColor, Offset(w * 0.25f, h * 0.23f), Size(w * 0.50f, h * 0.56f), CornerRadius(4f, 4f), style = Stroke(width = 2f))
            drawLine(iconColor, Offset(w * 0.35f, h * 0.40f), Offset(w * 0.66f, h * 0.40f), strokeWidth = 1.7f)
            drawLine(iconColor, Offset(w * 0.35f, h * 0.54f), Offset(w * 0.66f, h * 0.54f), strokeWidth = 1.7f)
            drawLine(iconColor, Offset(w * 0.25f, h * 0.30f), Offset(w * 0.25f, h * 0.72f), strokeWidth = 2.4f)
        } else {
            drawRoundRect(iconColor, Offset(w * 0.38f, h * 0.18f), Size(w * 0.24f, h * 0.42f), CornerRadius(8f, 8f), style = Stroke(width = 2.1f))
            drawArc(iconColor, 25f, 130f, false, Offset(w * 0.25f, h * 0.40f), Size(w * 0.50f, h * 0.28f), style = Stroke(width = 2f))
            drawLine(iconColor, Offset(w * 0.50f, h * 0.62f), Offset(w * 0.50f, h * 0.78f), strokeWidth = 2f)
            drawLine(iconColor, Offset(w * 0.34f, h * 0.80f), Offset(w * 0.66f, h * 0.80f), strokeWidth = 2f)
        }
    }
}

@Composable
fun AppRoot() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("my_english_prefs", Context.MODE_PRIVATE) }

    var screen by remember { mutableStateOf("home") }
    var selectedLesson by remember { mutableStateOf("Lesson 1") }
    var activeHomeworkLesson by remember { mutableStateOf("Lesson 1") }
    var showChoices by remember { mutableStateOf(false) }
    var studentName by remember { mutableStateOf(prefs.getString("student_name", "") ?: "") }
    var showNameDialog by remember { mutableStateOf(studentName == "") }
    var lesson1Done by remember { mutableStateOf(prefs.getBoolean("lesson1_listening_done", false)) }
    var lesson2Done by remember { mutableStateOf(prefs.getBoolean("lesson2_listening_done", false)) }

    val answers = remember { mutableStateListOf<String>() }
    val plays = remember { mutableStateListOf<Int>() }
    val hints = remember { mutableStateListOf<Int>() }
    val firstCorrect = remember { mutableStateListOf<Boolean>() }

    var submitStep by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    var report by remember { mutableStateOf("") }

    fun resetAttempt(sentenceCount: Int) {
        answers.clear()
        plays.clear()
        hints.clear()
        firstCorrect.clear()

        var i = 0
        while (i < sentenceCount) {
            answers.add("")
            plays.add(0)
            hints.add(0)
            firstCorrect.add(false)
            i++
        }
    }

    fun saveAttempt() {
        val prefix = attemptPrefix(activeHomeworkLesson)
        prefs.edit()
            .putBoolean(prefix + "in_progress", submitStep != 2)
            .putString(prefix + "answers", joinStrings(answers))
            .putString(prefix + "plays", joinInts(plays))
            .putString(prefix + "hints", joinInts(hints))
            .putString(prefix + "first_correct", joinBooleans(firstCorrect))
            .putInt(prefix + "submit_step", submitStep)
            .putInt(prefix + "score", score)
            .putString(prefix + "message", message)
            .apply()
    }

    fun clearAttempt(lessonName: String) {
        val prefix = attemptPrefix(lessonName)
        prefs.edit()
            .remove(prefix + "in_progress")
            .remove(prefix + "answers")
            .remove(prefix + "plays")
            .remove(prefix + "hints")
            .remove(prefix + "first_correct")
            .remove(prefix + "submit_step")
            .remove(prefix + "score")
            .remove(prefix + "message")
            .apply()
    }

    fun restoreAttempt(lessonName: String, sentenceCount: Int): Boolean {
        val prefix = attemptPrefix(lessonName)
        if (!prefs.getBoolean(prefix + "in_progress", false)) return false

        resetAttempt(sentenceCount)

        val savedAnswers = prefs.getString(prefix + "answers", "") ?: ""
        val answerParts = if (savedAnswers == "") emptyList() else savedAnswers.split("<|>")
        val playParts = (prefs.getString(prefix + "plays", "") ?: "").split(",")
        val hintParts = (prefs.getString(prefix + "hints", "") ?: "").split(",")
        val correctParts = (prefs.getString(prefix + "first_correct", "") ?: "").split(",")

        var i = 0
        while (i < sentenceCount) {
            if (i < answerParts.size) answers[i] = answerParts[i]
            if (i < playParts.size) plays[i] = playParts[i].toIntOrNull() ?: 0
            if (i < hintParts.size) hints[i] = hintParts[i].toIntOrNull() ?: 0
            if (i < correctParts.size) firstCorrect[i] = correctParts[i] == "1"
            i++
        }

        submitStep = prefs.getInt(prefix + "submit_step", 0)
        score = prefs.getInt(prefix + "score", 0)
        message = prefs.getString(prefix + "message", "") ?: ""

        return true
    }

    LaunchedEffect(Unit) {
        resetAttempt(HomeworkData.lesson1Sentences.size)
    }

    fun listeningDoneForLesson(lessonName: String): Boolean {
        return if (lessonName == "Lesson 1") {
            lesson1Done
        } else if (lessonName == "Lesson 2") {
            lesson2Done
        } else {
            false
        }
    }

    fun markListeningDone(lessonName: String) {
        if (lessonName == "Lesson 1") {
            lesson1Done = true
            prefs.edit().putBoolean("lesson1_listening_done", true).apply()
        } else if (lessonName == "Lesson 2") {
            lesson2Done = true
            prefs.edit().putBoolean("lesson2_listening_done", true).apply()
        }

        clearAttempt(lessonName)
    }

    fun openListeningHomework() {
        val sentences = HomeworkData.sentencesForLesson(selectedLesson)
        activeHomeworkLesson = selectedLesson

        val restored = restoreAttempt(selectedLesson, sentences.size)

        if (!restored || submitStep == 2 || answers.size != sentences.size) {
            resetAttempt(sentences.size)
            submitStep = 0
            score = 0
            message = ""
            report = ""
        }

        screen = "homework"
    }

    BackHandler(enabled = screen != "home") {
        if (screen == "homework") {
            screen = "lesson"
        } else if (screen == "lesson") {
            screen = "home"
        }
    }

    if (showNameDialog) {
        StudentNameDialog(studentName) { newName ->
            studentName = newName
            prefs.edit().putString("student_name", newName).apply()
            showNameDialog = false
        }
    }

    when (screen) {
        "home" -> {
            Home(
                studentName = displayStudentName(studentName),
                onChangeName = { showNameDialog = true },
                openLesson = { lesson ->
                    selectedLesson = lesson
                    showChoices = false
                    screen = "lesson"
                }
            )
        }

        "lesson" -> {
            Lesson(
                name = selectedLesson,
                listeningDone = listeningDoneForLesson(selectedLesson),
                showChoices = showChoices,
                showHomework = { showChoices = true },
                openListening = { openListeningHomework() },
                back = { screen = "home" }
            )
        }

        "homework" -> {
            val sentences = HomeworkData.sentencesForLesson(activeHomeworkLesson)
            val audioResId = HomeworkData.audioResIdForLesson(context, activeHomeworkLesson)

            Homework(
                name = activeHomeworkLesson,
                studentName = displayStudentName(studentName),
                sentences = sentences,
                audioResId = audioResId,
                answers = answers,
                plays = plays,
                hints = hints,
                firstCorrect = firstCorrect,
                submitStep = submitStep,
                score = score,
                msg = message,
                setScore = {
                    score = it
                    saveAttempt()
                },
                setStep = {
                    submitStep = it
                    saveAttempt()
                },
                setMsg = {
                    message = it
                    saveAttempt()
                },
                setReport = {
                    report = it
                },
                onAttemptChanged = {
                    saveAttempt()
                },
                done = {
                    markListeningDone(activeHomeworkLesson)
                },
                back = {
                    screen = "lesson"
                }
            )
        }
    }
}

@Composable
fun StudentNameDialog(currentName: String, onSave: (String) -> Unit) {
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

@Composable
fun Home(studentName: String, onChangeName: () -> Unit, openLesson: (String) -> Unit) {
    val scroll = rememberScrollState()

    Box(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header("Book 1")
            Spacer(Modifier.height(12.dp))

            var lessonNumber = 1
            while (lessonNumber <= 31) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val left = lessonNumber

                    ArtButton(
                        text = "Lesson $left",
                        onClick = { openLesson("Lesson $left") },
                        modifier = Modifier.width(145.dp),
                        heightDp = 52,
                        fontSize = 16
                    )

                    if (left + 1 <= 31) {
                        val right = left + 1

                        ArtButton(
                            text = "Lesson $right",
                            onClick = { openLesson("Lesson $right") },
                            modifier = Modifier.width(145.dp),
                            heightDp = 52,
                            fontSize = 16
                        )
                    } else {
                        Spacer(Modifier.width(145.dp))
                    }
                }

                Spacer(Modifier.height(8.dp))
                lessonNumber += 2
            }

            Spacer(Modifier.height(72.dp))
        }

        StudentBadge(
            studentName = studentName,
            onChangeName = onChangeName,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
fun Lesson(
    name: String,
    listeningDone: Boolean,
    showChoices: Boolean,
    showHomework: () -> Unit,
    openListening: () -> Unit,
    back: () -> Unit
) {
    val listeningAvailable = HomeworkData.hasListeningHomework(name)

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(name)
        Spacer(Modifier.height(14.dp))

        ArtButton("Vocabulary", { })
        Spacer(Modifier.height(7.dp))

        ArtButton("Grammar", { })
        Spacer(Modifier.height(7.dp))

        ArtButton("Practice", { })
        Spacer(Modifier.height(7.dp))

        ArtButton(
            text = "Homework",
            onClick = showHomework,
            heightDp = 78,
            content = {
                Text(
                    text = "Homework",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-8).dp),
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    style = LocalTextStyle.current.copy(
                        shadow = Shadow(Color.Black, Offset(2f, 2f), 4f)
                    )
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HomeworkIcon(0, listeningDone)
                    Spacer(Modifier.width(5.dp))
                    HomeworkIcon(1, false)
                    Spacer(Modifier.width(5.dp))
                    HomeworkIcon(2, false)
                }
            }
        )

        if (showChoices) {
            Spacer(Modifier.height(20.dp))

            StrokeGlowTitle("Choose homework", fontSize = 22)

            Spacer(Modifier.height(8.dp))

            Box(contentAlignment = Alignment.Center) {
                ArtButton(
                    text = if (listeningAvailable) "Listening homework" else "Listening homework - coming soon",
                    onClick = openListening,
                    enabled = listeningAvailable,
                    fontSize = 17
                )

                if (listeningDone) {
                    Image(
                        painter = painterResource(id = R.drawable.donestamp),
                        contentDescription = "Done",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = -88.dp, y = (-16).dp)
                            .size(80.dp)
                            .rotate(-25f)
                    )
                }
            }

            Spacer(Modifier.height(7.dp))

            ArtButton(
                text = "Written homework",
                onClick = { },
                backgroundResId = R.drawable.graybutton,
                enabled = false,
                fontSize = 17
            )

            Spacer(Modifier.height(7.dp))

            ArtButton(
                text = "Spoken homework",
                onClick = { },
                backgroundResId = R.drawable.graybutton,
                enabled = false,
                fontSize = 17
            )
        }

        Spacer(Modifier.height(16.dp))

        ArtButton(
            text = "Back",
            onClick = back,
            modifier = Modifier.fillMaxWidth(0.45f),
            backgroundResId = R.drawable.redbutton
        )
    }
}

@Composable
fun Homework(
    name: String,
    studentName: String,
    sentences: Array<HomeworkSentence>,
    audioResId: Int,
    answers: MutableList<String>,
    plays: MutableList<Int>,
    hints: MutableList<Int>,
    firstCorrect: MutableList<Boolean>,
    submitStep: Int,
    score: Int,
    msg: String,
    setScore: (Int) -> Unit,
    setStep: (Int) -> Unit,
    setMsg: (String) -> Unit,
    setReport: (String) -> Unit,
    onAttemptChanged: () -> Unit,
    done: () -> Unit,
    back: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val scroll = rememberScrollState()
    var topReq by remember { mutableIntStateOf(0) }
    val handler = remember { Handler(Looper.getMainLooper()) }
    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    val focus = remember(sentences.size) { Array(sentences.size) { FocusRequester() } }

    LaunchedEffect(scroll.isScrollInProgress) {
        if (scroll.isScrollInProgress) focusManager.clearFocus()
    }

    LaunchedEffect(topReq) {
        if (topReq > 0) scroll.scrollTo(0)
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
        try {
            player?.stop()
        } catch (_: Exception) {
        }
        try {
            player?.release()
        } catch (_: Exception) {
        }
        player = null
    }

    fun play(i: Int, startMs: Int, endMs: Int) {
        stop()

        if (audioResId == 0) return

        val currentPlayer = MediaPlayer.create(context, audioResId) ?: return
        player = currentPlayer
        currentPlayer.setVolume(1f, 1f)
        currentPlayer.seekTo(startMs)
        currentPlayer.start()

        handler.postDelayed(
            {
                if (player == currentPlayer) {
                    stop()
                    focus[i].requestFocus()
                    keyboard?.show()
                }
            },
            (endMs - startMs).toLong()
        )
    }

    fun buildReport(currentScore: Int, correctionsSubmitted: Boolean): String {
        val builder = StringBuilder()
        builder.append("Student: ").append(studentName).append("\n")
        builder.append("Submitted at: ").append(currentDateTimeText()).append("\n")
        builder.append("Lesson: ").append(name).append("\n")
        builder.append("Homework: Listening homework\n")
        builder.append("Original score: ").append(currentScore).append(" / ").append(sentences.size).append("\n")
        builder.append("Student submitted answers: yes\n")
        builder.append("Student submitted corrections: ").append(if (correctionsSubmitted) "yes" else "no").append("\n")

        var i = 0
        while (i < sentences.size) {
            builder.append(sentences[i].label)
            builder.append(": plays = ")
            builder.append(plays[i])
            builder.append(", hints = ")
            builder.append(hints[i])
            builder.append("\n")
            i++
        }

        return builder.toString()
    }

    fun submitAnswers() {
        stop()
        focusManager.clearFocus()

        var currentScore = 0
        var i = 0

        while (i < sentences.size) {
            val ok = isCorrectAnswer(answers[i], sentences[i].correctText)
            firstCorrect[i] = ok
            if (ok) currentScore++
            i++
        }

        setScore(currentScore)
        setStep(1)
        setMsg("First attempt score: $currentScore / ${sentences.size}")
        setReport(buildReport(currentScore, false))
        onAttemptChanged()
        topReq++
    }

    fun submitCorrections() {
        stop()
        focusManager.clearFocus()

        var allCorrect = true
        var i = 0

        while (i < sentences.size) {
            if (!isCorrectAnswer(answers[i], sentences[i].correctText)) allCorrect = false
            i++
        }

        if (allCorrect) {
            val finalReport = buildReport(score, true)

            setMsg("Sending your lesson report to your teacher...")
            setReport(finalReport)

            sendHomeworkReportToTeacher(
                studentName = studentName,
                lessonName = "Lesson 3",
                homeworkType = "Listening homework",
                scoreText = "$score / ${sentences.size}",
                report = finalReport
            ) { success ->
                Handler(Looper.getMainLooper()).post {
                    if (success) {
                        setStep(2)
                        done()
                        setMsg("Report sent successfully.")
                    } else {
                        setMsg("Failed to send report. Please try again.")
                    }
                }
            }
        } else {
            setMsg("Some corrections are still incorrect. Please check the red X sentences.")
            setReport(buildReport(score, false))
            onAttemptChanged()
        }

        topReq++
    }

    DisposableEffect(Unit) {
        onDispose { stop() }
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(name, "Listening")

        if (msg != "") {
            Spacer(Modifier.height(8.dp))
            Text(
                text = msg,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(Color.Black, Offset(1f, 1f), 5f)
                )
            )
        }

        if (audioResId == 0) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Audio file lesson2.mp3 not found in res/raw.",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        var i = 0
        while (i < sentences.size) {
            val index = i
            val sentence = sentences[index]

            SentenceRow(
                sentence = sentence,
                answer = answers[index],
                change = {
                    answers[index] = it
                    onAttemptChanged()
                },
                playCount = plays[index],
                submitStep = submitStep,
                firstOk = firstCorrect[index],
                currentOk = isCorrectAnswer(answers[index], sentence.correctText),
                hintCount = hints[index],
                focus = focus[index],
                play = {
                    plays[index]++
                    onAttemptChanged()
                    play(index, sentence.startMs, sentence.endMs)
                },
                stop = {
                    stop()
                },
                hint = {
                    if (canUseHint(answers[index], plays[index], submitStep >= 1, hints[index], sentence.correctText)) {
                        hints[index]++
                        setReport(buildReport(score, submitStep == 2))
                        onAttemptChanged()
                    }
                }
            )

            Spacer(Modifier.height(12.dp))
            i++
        }

        if (submitStep == 0) {
            ArtButton("Submit answers", { submitAnswers() })
        } else if (submitStep == 1) {
            ArtButton("Submit corrections", { submitCorrections() })
        } else {
            Button(
                onClick = { },
                enabled = false,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color(0xFF2E7D32),
                    disabledContentColor = Color.White
                )
            ) {
                Text("Submitted")
            }
        }

        Spacer(Modifier.height(16.dp))

        ArtButton(
            text = "Back",
            onClick = {
                stop()
                back()
            },
            modifier = Modifier.fillMaxWidth(0.45f),
            backgroundResId = R.drawable.redbutton
        )
    }
}

@Composable
fun SentenceRow(
    sentence: HomeworkSentence,
    answer: String,
    change: (String) -> Unit,
    playCount: Int,
    submitStep: Int,
    firstOk: Boolean,
    currentOk: Boolean,
    hintCount: Int,
    focus: FocusRequester,
    play: () -> Unit,
    stop: () -> Unit,
    hint: () -> Unit
) {
    Card(
        Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row {
                Text(
                    text = sentence.label,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                if (submitStep >= 1) {
                    Text(
                        text = if (submitStep == 2 || currentOk) "✓" else "✕",
                        color = if (submitStep == 2 || currentOk) Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontSize = 64.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row {
                ArtButton(
                    text = "▶",
                    onClick = play,
                    modifier = Modifier.width(90.dp),
                    heightDp = 56,
                    fontSize = 20
                )

                Spacer(Modifier.width(8.dp))

                ArtButton(
                    text = "Stop",
                    onClick = stop,
                    modifier = Modifier.width(112.dp),
                    backgroundResId = R.drawable.redbutton,
                    heightDp = 56,
                    fontSize = 16
                )
            }

            Spacer(Modifier.height(8.dp))

            TextField(
                value = answer,
                onValueChange = change,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focus)
            )

            Spacer(Modifier.height(4.dp))

            Text("Plays: $playCount")

            if (submitStep >= 1) {
                Text(if (firstOk) "First attempt: correct" else "First attempt: incorrect")

                if (cleanAnswer(answer) == "") {
                    Text("Type an answer before using hints.")
                } else if (playCount < 5) {
                    Text("Hint locked: listen ${5 - playCount} more time(s).")
                } else if (submitStep < 2 && !currentOk) {
                    ArtButton(
                        text = "Reveal next word",
                        onClick = hint,
                        modifier = Modifier.fillMaxWidth(0.7f),
                        backgroundResId = R.drawable.graybutton,
                        heightDp = 56,
                        fontSize = 16
                    )
                }

                if (hintCount > 0) {
                    Text("Hint: ${revealedHintText(sentence.correctText, hintCount)}")
                    Text("Hints used: $hintCount")
                }
            }
        }
    }
}
