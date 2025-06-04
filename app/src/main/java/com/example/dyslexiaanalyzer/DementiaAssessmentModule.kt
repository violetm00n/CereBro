package com.example.dyslexiaanalyzer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.os.SystemClock
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Main entry point for the dementia assessment module.
 * This composable can be integrated into any existing app.
 */
@Composable
fun DementiaAssessmentModule(navController: NavHostController,
                             onBackPressed: @Composable () -> Unit,
                             onTestComplete: (score: Float) -> Unit
) {
    val navController = rememberNavController()
    val testScores = remember { mutableStateOf(mapOf<String, Float>()) }
    val startTime = remember { mutableStateOf(SystemClock.elapsedRealtime()) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(navController = navController, startDestination = "dementia_test_home") {
            composable("dementia_test_home") {
                DementiaTestHome(navController,
                    onBackPressed = {HomeScreen(navController)},
                    onStartTest = {
                        startTime.value = SystemClock.elapsedRealtime()
                        navController.navigate("memory_test")
                    }
                )
            }
            composable("home",
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(100)) }) { HomeScreen(navController) }

            composable("memory_test") {
                MemoryTestScreen(navController,
                    onComplete = { score ->
                        testScores.value = testScores.value.plus("memory" to score)
                        navController.navigate("orientation_test")
                    }
                )
            }

            composable("orientation_test") {
                OrientationTestScreen(
                    onComplete = { score ->
                        testScores.value = testScores.value.plus("orientation" to score)
                        navController.navigate("attention_test")
                    }
                )
            }

            composable("attention_test") {
                AttentionTestScreen(
                    onComplete = { score ->
                        testScores.value = testScores.value.plus("attention" to score)
                        navController.navigate("test_results")
                    }
                )
            }

            composable("test_results") {
                val testTime = (SystemClock.elapsedRealtime() - startTime.value) / 1000
                val averageScore = testScores.value.values.average().toFloat()

                TestResultsScreen(navController,
                    scores = testScores.value,
                    testTimeSeconds = testTime,
                    onFinish = {
                        HomeScreen(navController)
                    }
                )
            }
        }
    }
}

@Composable
fun DementiaTestHome(navController: NavHostController,onBackPressed: @Composable () -> Unit, onStartTest: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cognitive Assessment",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.width(48.dp)) // For balance
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Test Overview",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "This assessment includes three short activities to evaluate different cognitive areas:",
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        //Icon(Icons.Default.Memory, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Memory: Card matching game")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        //Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Orientation: Time and place questions")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        //Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Attention: Pattern sequence test")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "This will take approximately 5-7 minutes to complete.",
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onStartTest,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Start Assessment")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        androidx.compose.material.Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material.Text("Back to Home")
        }
    }


}

/**
 * Memory Test Screen - Card matching game
 */
@Composable
fun MemoryTestScreen(navController: NavHostController,onComplete: (score: Float) -> Unit) {
    var score by remember { mutableStateOf(0) }
    var attempts by remember { mutableStateOf(0) }
    var gameCompleted by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Create paired memory cards with different icons
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Star,
        Icons.Default.Email,
        Icons.Default.Phone,
        Icons.Default.Person,
        Icons.Default.Settings
    )

    data class MemoryCard(
        val id: Int,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val isFlipped: Boolean = false,
        val isMatched: Boolean = false
    )

    val cardPairs = icons.flatMap { icon ->
        listOf(
            MemoryCard(id = icons.indexOf(icon) * 2, icon = icon),
            MemoryCard(id = icons.indexOf(icon) * 2 + 1, icon = icon)
        )
    }

    var cards by remember { mutableStateOf(cardPairs.shuffled()) }
    var flippedCards by remember { mutableStateOf(listOf<Int>()) }
    var canFlip by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Memory Test",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Match the cards by finding pairs",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text("Score: $score")
            Text("Attempts: $attempts")
        }

        AnimatedVisibility(
            visible = gameCompleted,
            enter = fadeIn(animationSpec = tween(500)),
            exit = fadeOut(animationSpec = tween(500))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Memory Test Complete!",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Calculate score as percentage
                val maxScore = icons.size
                val percentage = (score.toFloat() / maxScore) * 100

                Text("Score: ${percentage.toInt()}%")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { onComplete(percentage) }) {
                    Text("Continue")
                }
            }
        }

        if (!gameCompleted) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(cards) { card ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                            .background(
                                if (card.isFlipped || card.isMatched) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(1.dp, MaterialTheme.colorScheme.outline)
                            .clickable(
                                enabled = !card.isFlipped && !card.isMatched && canFlip,
                                onClick = {
                                    // Flip the card
                                    cards = cards.map {
                                        if (it.id == card.id) it.copy(isFlipped = true) else it
                                    }

                                    // Add to flipped cards
                                    val newFlippedCards = flippedCards + card.id
                                    flippedCards = newFlippedCards

                                    // Check for matches when two cards are flipped
                                    if (newFlippedCards.size == 2) {
                                        canFlip = false
                                        attempts++

                                        val card1 = cards.first { it.id == newFlippedCards[0] }
                                        val card2 = cards.first { it.id == newFlippedCards[1] }

                                        // Check if icons match
                                        if (card1.icon == card2.icon) {
                                            // Match found
                                            score++
                                            cards = cards.map {
                                                if (it.id == card1.id || it.id == card2.id) {
                                                    it.copy(isMatched = true)
                                                } else {
                                                    it
                                                }
                                            }
                                            flippedCards = emptyList()
                                            canFlip = true

                                            // Check if game completed
                                            if (cards.all { it.isMatched }) {
                                                gameCompleted = true
                                            }
                                        } else {
                                            // No match, flip back after delay
                                            coroutineScope.launch {
                                                delay(1000)
                                                cards = cards.map {
                                                    if (it.id == card1.id || it.id == card2.id) {
                                                        it.copy(isFlipped = false)
                                                    } else {
                                                        it
                                                    }
                                                }
                                                flippedCards = emptyList()
                                                canFlip = true
                                            }
                                        }
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (card.isFlipped || card.isMatched) {
                            Icon(
                                imageVector = card.icon,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        androidx.compose.material.Button(
            onClick = { navController.navigate("dementia_test_home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material.Text("Home")
        }
    }

}

/**
 * Orientation Test Screen - Time and place awareness
 */
@Composable
fun OrientationTestScreen(onComplete: (score: Float) -> Unit) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var testCompleted by remember { mutableStateOf(false) }
    var selectedAnswer by remember { mutableStateOf("") }
    var answered by remember { mutableStateOf(false) }

    // Get current date information for questions
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR).toString()
    val currentMonth = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH).toString()
    val currentDayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(calendar.time)
    val currentSeason = when (calendar.get(Calendar.MONTH)) {
        in 2..4 -> "Spring"
        in 5..7 -> "Summer"
        in 8..10 -> "Fall"
        else -> "Winter"
    }

    data class OrientationQuestion(
        val question: String,
        val options: List<String>,
        val correctAnswer: String
    )

    // Create orientation questions
    val questions = listOf(
        OrientationQuestion(
            question = "What year is it now?",
            options = listOf(
                (currentYear.toInt() - 1).toString(),
                currentYear,
                (currentYear.toInt() + 1).toString(),
                (currentYear.toInt() - 2).toString()
            ),
            correctAnswer = currentYear
        ),
        OrientationQuestion(
            question = "What month is it now?",
            options = listOf(
                currentMonth,
                getRandomMonth(currentMonth),
                getRandomMonth(currentMonth),
                getRandomMonth(currentMonth)
            ),
            correctAnswer = currentMonth
        ),
        OrientationQuestion(
            question = "What day of the week is it today?",
            options = listOf(
                currentDayOfWeek,
                getRandomDay(currentDayOfWeek),
                getRandomDay(currentDayOfWeek),
                getRandomDay(currentDayOfWeek)
            ),
            correctAnswer = currentDayOfWeek
        ),
        OrientationQuestion(
            question = "What season is it currently?",
            options = listOf("Spring", "Summer", "Fall", "Winter"),
            correctAnswer = currentSeason
        ),
        OrientationQuestion(
            question = "Which of these objects would you write with?",
            options = listOf("Pen", "Fork", "Pillow", "Cup"),
            correctAnswer = "Pen"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Orientation Test",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Question ${currentQuestionIndex + 1}/${questions.size}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AnimatedVisibility(
            visible = testCompleted
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Orientation Test Complete!",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Calculate score as percentage
                val percentage = (score.toFloat() / questions.size) * 100

                Text("Score: ${percentage.toInt()}%")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { onComplete(percentage) }) {
                    Text("Continue")
                }
            }
        }

        if (!testCompleted) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = questions[currentQuestionIndex].question,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    questions[currentQuestionIndex].options.shuffled().forEach { option ->
                        val isSelected = selectedAnswer == option
                        val isCorrect = option == questions[currentQuestionIndex].correctAnswer

                        Button(
                            onClick = {
                                if (!answered) {
                                    selectedAnswer = option
                                    answered = true

                                    if (isCorrect) {
                                        score++
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    !answered -> MaterialTheme.colorScheme.primaryContainer
                                    isSelected && isCorrect -> Color(0xFF4CAF50) // Green for correct
                                    isSelected && !isCorrect -> Color(0xFFF44336) // Red for incorrect
                                    !isSelected && isCorrect && answered -> Color(0xFF4CAF50) // Show correct answer
                                    else -> MaterialTheme.colorScheme.primaryContainer
                                }
                            )
                        ) {
                            Text(
                                text = option,
                                color = if (answered && (isSelected || isCorrect)) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (answered) {
                        Button(
                            onClick = {
                                if (currentQuestionIndex < questions.size - 1) {
                                    currentQuestionIndex++
                                    answered = false
                                    selectedAnswer = ""
                                } else {
                                    testCompleted = true
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(if (currentQuestionIndex < questions.size - 1) "Next Question" else "Finish")
                        }
                    }
                }
            }
        }
    }
}

fun getRandomMonth(exclude: String): String {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    return months.filter { it != exclude }.random()
}

fun getRandomDay(exclude: String): String {
    val days =
        listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    return days.filter { it != exclude }.random()
}

/**
 * Attention Test Screen - Pattern sequence test
 */
@Composable
fun AttentionTestScreen(onComplete: (score: Float) -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    var sequenceToFollow by remember { mutableStateOf(listOf<Int>()) }
    var userSequence by remember { mutableStateOf(listOf<Int>()) }
    var currentLevel by remember { mutableStateOf(1) }
    var isShowingSequence by remember { mutableStateOf(false) }
    var isUserTurn by remember { mutableStateOf(false) }
    var testCompleted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    val maxLevel = 5 // Total number of levels

    // Colors for the pattern squares
    val colors = listOf(
        Color(0xFFF44336), // Red
        Color(0xFF2196F3), // Blue
        Color(0xFF4CAF50), // Green
        Color(0xFFFFEB3B)  // Yellow
    )

    // Generate a random sequence for the current level
    fun generateSequence() {
        val newSequence = List(currentLevel + 2) { (0..3).random() }
        sequenceToFollow = newSequence
        isShowingSequence = true
        isUserTurn = false
        userSequence = emptyList()

        // Show the sequence to the user
        coroutineScope.launch {
            delay(1000) // Wait before starting

            sequenceToFollow.forEachIndexed { index, colorIndex ->
                delay(800) // Gap between highlights
                // This will trigger a recomposition to highlight the button
                sequenceToFollow = sequenceToFollow.toMutableList().apply {
                    this[index] = colorIndex + 100 // Add 100 to indicate highlighting
                }

                delay(600) // Highlight duration
                // Reset the highlighting
                sequenceToFollow = sequenceToFollow.toMutableList().apply {
                    this[index] = colorIndex
                }
            }

            delay(500)
            isShowingSequence = false
            isUserTurn = true
        }
    }

    // Start the game
    LaunchedEffect(currentLevel) {
        if (!testCompleted) {
            generateSequence()
        }
    }

    // Check if user's sequence matches
    fun checkSequence() {
        if (userSequence.size == sequenceToFollow.size) {
            val isCorrect =
                userSequence.zip(sequenceToFollow).all { (user, correct) -> user == correct }

            if (isCorrect) {
                score++
                if (currentLevel < maxLevel) {
                    currentLevel++
                    generateSequence()
                } else {
                    testCompleted = true
                }
            } else {
                // Incorrect sequence - test ends
                testCompleted = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Attention Test",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (isShowingSequence) {
            Text(
                text = "Watch the pattern...",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        } else if (isUserTurn) {
            Text(
                text = "Repeat the pattern",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Text(
            text = "Level: $currentLevel/$maxLevel",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AnimatedVisibility(
            visible = testCompleted
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Attention Test Complete!",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Calculate score as percentage
                val percentage = (score.toFloat() / maxLevel) * 100

                Text("Score: ${percentage.toInt()}%")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { onComplete(percentage) }) {
                    Text("Continue")
                }
            }
        }

        if (!testCompleted) {
            Spacer(modifier = Modifier.height(32.dp))

            // Grid of colored squares
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f)
            ) {
                items(4) { index ->
                    val isHighlighted = sequenceToFollow.any { it == index + 100 }

                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize()
                            .aspectRatio(1f)
                            .background(
                                if (isHighlighted) colors[index].copy(alpha = 0.7f) else colors[index].copy(
                                    alpha = 0.3f
                                )
                            )
                            .clickable(
                                enabled = isUserTurn,
                                onClick = {
                                    if (isUserTurn) {
                                        // Briefly highlight when user taps
                                        coroutineScope.launch {
                                            sequenceToFollow = sequenceToFollow.toMutableList()

                                            userSequence = userSequence + index

                                            // Check if the sequence is complete
                                            checkSequence()
                                        }
                                    }
                                }
                            ),
                        // contentAlignment = Alignment.Center

                    )
                }
            }
        }
    }
}

/**
 * Results Screen - Shows the overall test results
 */
@Composable
fun TestResultsScreen(navController: NavHostController,
    scores: Map<String, Float>,
    testTimeSeconds: Long,
    onFinish: @Composable () -> Unit
) {
    val averageScore = scores.values.average().toFloat()
    Spacer(modifier = Modifier.height(25.dp))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Assessment Complete",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Your Results",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                scores.forEach { (test, score) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = when (test) {
                                "memory" -> "Memory Test"
                                "orientation" -> "Orientation Test"
                                "attention" -> "Attention Test"
                                else -> test
                            }
                        )
                        Text(
                            text = "${score.toInt()}%",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Overall Score",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${averageScore.toInt()}%",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Time taken: ${formatTime(testTimeSeconds)}",
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = getAssessmentSummary(averageScore),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))


        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            androidx.compose.material.Button(
                onClick = { navController.navigate("dementia_test_home") },
                modifier = Modifier.fillMaxWidth()
            ) {
                androidx.compose.material.Text("Home")
            }
        }
    }
}

// Helper function to format time in minutes and seconds
fun formatTime(seconds: Long): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "${minutes}m ${remainingSeconds}s"
}

// Helper function to generate assessment summary
fun getAssessmentSummary(score: Float): String {
    return when {
        score >= 90 -> "Excellent performance across all cognitive areas."
        score >= 75 -> "Good performance. Most cognitive areas appear strong."
        score >= 60 -> "Moderate performance. Some aspects may benefit from further assessment."
        else -> "Performance indicates potential areas of concern. Follow-up with a healthcare provider is recommended."
    }
}
