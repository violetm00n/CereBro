package com.example.dyslexiaanalyzer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.itextpdf.kernel.pdf.PdfDocument
import java.io.OutputStream
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.intelligence.acceleration.Analytics
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.google.firebase.ml.modeldownloader.CustomModel
//import com.google.firebase.ml.modeldownloader.FirebaseModelDownloadConditions
import org.tensorflow.lite.Interpreter
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel
import com.google.firebase.ml.modeldownloader.DownloadType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.FloatBuffer
import java.time.format.TextStyle
import kotlin.math.max
import kotlin.math.min
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import java.nio.ByteBuffer
import java.nio.ByteOrder
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.res.painterResource


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
           // DyslexiaAnalyzerApp()
            BrainHealthApp()
        }
    }
}
@Composable
fun BrainHealthApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home",
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) }) { HomeScreen(navController) }
        composable("dyslexia") { DyslexiaScreen(navController) }
        composable("alzheimers") { AurtismScreen(navController) }
        composable("Dementia") { DementiaAssessmentModule(navController,
                        onBackPressed = { HomeScreen(navController) },
                      onTestComplete = { score -> getAssessmentSummary(score) }) }
        composable("membersinfo") { makerinfo(navController) }
    }
}
@Composable
fun HomeScreen(navController: NavHostController) {
    var isVisible by remember { mutableStateOf(false) }
    var showAnimation by remember { mutableStateOf(true) } // Controls Lottie visibility

    // Trigger animations when the screen loads
    LaunchedEffect(Unit) {
        isVisible = true
        // Hide Lottie animation after 2 seconds
        kotlinx.coroutines.delay(2000)
        showAnimation = false
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image from URL
        Image(
            painter = rememberAsyncImagePainter("https://i.pinimg.com/736x/25/26/de/2526defbe2e065c19fb7c07cd05d895d.jpg"),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Show Lottie animation while loading
            if (showAnimation) {
                LottieLoader()
            } else {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(1000)) + scaleIn(initialScale = 0.8f)
                ) {
                    Text(
                        text = "Brain Disease Prediction",
                        style = MaterialTheme.typography.h5 ,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                val buttons = listOf(
                    "Dyslexia Prediction" to "dyslexia",
                    "Autism Prediction" to "alzheimers",
                    "DementiaGame" to "Dementia",
                )

                buttons.forEachIndexed { index, (label, route) ->
                    val animatedScale by animateFloatAsState(
                        targetValue = if (isVisible) 1f else 0.8f,
                        animationSpec = tween(500, delayMillis = index * 200)
                    )

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(500, delayMillis = index * 200)) +
                                scaleIn(initialScale = 0.8f)
                    ) {
                        Button(
                            onClick = { navController.navigate(route) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .graphicsLayer(scaleX = animatedScale, scaleY = animatedScale)
                                .animateContentSize(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
                        ) {
                            Text(label, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // New Icon that navigates to a new page
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(500, delayMillis = buttons.size * 200)) +
                            scaleIn(initialScale = 0.8f)
                ) {
                    IconButton(
                        onClick = { navController.navigate("membersinfo")},
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.infomem), // Change this to your preferred icon
                            contentDescription = "More Information",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun LottieLoader() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier.size(150.dp) // Adjust size as needed
    )
}

@Composable
fun DyslexiaScreen(navController: NavHostController) {
    //DyslexiaAnalyzerApp(navController)
    DyslexiaScreen0(navController)
}
@Composable
fun AurtismScreen(navController: NavHostController) {
    AutismPredictionApp(navController)
}
/*
@Composable
fun DyslexiaAnalyzerApp(navController: NavHostController) {
    var inputText by remember { mutableStateOf("") }
    var analysisResult by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var modelFile by remember { mutableStateOf<File?>(null) }
    var modelDownloadStatus by remember { mutableStateOf("Downloading model...") }

    // Download Firebase Model
    LaunchedEffect(Unit) {
        modelDownloadStatus = "Downloading model..."
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("dyslexia_model", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
            .addOnSuccessListener { model: CustomModel ->
                modelFile = model.file
                modelDownloadStatus = "Model ready"
                Log.d("FirebaseML", "Model downloaded: ${modelFile?.absolutePath}")
            }
            .addOnFailureListener { e ->
                modelDownloadStatus = "Model download failed: ${e.message}"
                Log.e("FirebaseML", "Model download failed: ${e.message}")
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Dyslexia Text Analyzer", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Model Status: $modelDownloadStatus",
            style = MaterialTheme.typography.caption,
            color = if (modelFile != null) Color.Green else Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Enter a paragraph of text:", style = MaterialTheme.typography.subtitle1)

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
            placeholder = { Text("Type or paste text here...") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (modelFile != null && inputText.isNotBlank()) {
                    isLoading = true
                    // Use a coroutine to avoid blocking the UI thread
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = runInference(modelFile!!, inputText)
                        withContext(Dispatchers.Main) {
                            analysisResult = result
                            isLoading = false
                        }
                    }
                } else if (inputText.isBlank()) {
                    analysisResult = "Please enter some text to analyze"
                } else {
                    analysisResult = "Model not downloaded yet. Try again in a moment."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = modelFile != null && !isLoading && inputText.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Analyze Text")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Analysis Result:",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(analysisResult)

            }
        }

    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}
*/
/*
@Composable
fun DyslexiaAnalyzerApp(navController: NavHostController) {
    var inputText by remember { mutableStateOf("") }
    var analysisResult by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var modelFile by remember { mutableStateOf<File?>(null) }
    var modelDownloadStatus by remember { mutableStateOf("Downloading model...") }
    val context = LocalContext.current

    // Function to download the model
    fun downloadModel() {
        modelDownloadStatus = "Downloading model..."
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("dyslexia_model", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
            .addOnSuccessListener { model: CustomModel ->
                modelFile = model.file
                modelDownloadStatus = "Model ready"
                Log.d("FirebaseML", "Model downloaded: ${modelFile?.absolutePath}")
            }
            .addOnFailureListener { e ->
                modelDownloadStatus = "Model download failed: ${e.message}"
                Log.e("FirebaseML", "Model download failed: ${e.message}")
            }
    }

    // Initial model download
    LaunchedEffect(Unit) {
        downloadModel()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Download Latest Model Button
        Button(
            onClick = { downloadModel() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Download Latest Model")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Dyslexia Text Analyzer", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Model Status: $modelDownloadStatus",
            style = MaterialTheme.typography.caption,
            color = if (modelFile != null) Color.Green else Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Enter a paragraph of text:", style = MaterialTheme.typography.subtitle1)

        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
            placeholder = { Text("Type or paste text here...") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (modelFile != null && inputText.isNotBlank()) {
                    isLoading = true
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = runInference(modelFile!!, inputText)
                        withContext(Dispatchers.Main) {
                            analysisResult = result
                            isLoading = false
                        }
                    }
                } else if (inputText.isBlank()) {
                    analysisResult = "Please enter some text to analyze"
                } else {
                    analysisResult = "Model not downloaded yet. Try again in a moment."
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = modelFile != null && !isLoading && inputText.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Analyze Text")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Analysis Result:",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(analysisResult)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}

fun runInference(modelFile: File, userInput: String): String {
    try {
        val conditions = FirebaseModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        // Pre-process the text input
        val preprocessedText = preprocessText(userInput)

        // Create interpreter with model file
        val interpreter = Interpreter(modelFile)

        // Get input and output tensors shape
        val inputShape = interpreter.getInputTensor(0).shape()
        val outputShape = interpreter.getOutputTensor(0).shape()

        Log.d("FirebaseML", "Input shape: ${inputShape.contentToString()}")
        Log.d("FirebaseML", "Output shape: ${outputShape.contentToString()}")

        // Example: For text classification, convert text to features
        // This is a placeholder - replace with actual text feature extraction based on your model
        val features = extractTextFeatures(preprocessedText, inputShape[1])

        // Create input and output tensors
        val inputBuffer = FloatBuffer.allocate(inputShape[0] * inputShape[1])
        inputBuffer.put(features)
        inputBuffer.rewind()

        val outputBuffer = FloatBuffer.allocate(outputShape[0] * outputShape[1])

        // Run inference
        interpreter.run(inputBuffer, outputBuffer)
        outputBuffer.rewind()

        // Process results
        val results = FloatArray(outputShape[1])
        outputBuffer.get(results)

        // Analyze results
        val dyslexiaScore = results[0] // Assuming first output is dyslexia probability

        // Format detailed response
        val response = StringBuilder()
        response.append("Dyslexia Indicator Score: ${(dyslexiaScore * 100).toInt()}%\n\n")

        if (dyslexiaScore > 0.7) {
            response.append("Strong indicators of dyslexic patterns detected in the text.\n\n")
            response.append("Key patterns identified:\n")
            response.append("• Frequent letter reversals or word substitutions\n")
            response.append("• Unusual spelling patterns\n")
            response.append("• Characteristic syntactic structures")
        } else if (dyslexiaScore > 0.4) {
            response.append("Some indicators of dyslexic patterns detected.\n\n")
            response.append("The text shows some patterns that may be associated with dyslexia, but the indicators are not strong.")
        } else {
            response.append("No significant indicators of dyslexic patterns detected in this text sample.\n\n")
            response.append("The writing patterns appear to follow typical language structure.")
        }

        interpreter.close()
        return response.toString()
    } catch (e: Exception) {
        Log.e("FirebaseML", "Inference Error: ${e.message}", e)
        return "Error running inference: ${e.message}"
    }
}

// Text preprocessing function
private fun preprocessText(text: String): String {
    // Remove special characters, normalize whitespace, convert to lowercase
    return text.replace("[^a-zA-Z0-9\\s]".toRegex(), " ")
        .replace("\\s+".toRegex(), " ")
        .trim()
        .lowercase()
}

// Feature extraction function - replace with actual implementation based on your model
private fun extractTextFeatures(text: String, featureSize: Int): FloatArray {
    val features = FloatArray(featureSize)

    // This is a placeholder. Implement actual feature extraction based on your model requirements.
    // For example, you might:
    // 1. Count letter reversals (b/d, p/q)
    // 2. Calculate spelling error rates
    // 3. Analyze word spacing patterns
    // 4. Count phonetically similar word substitutions

    // Simple placeholder implementation - letter frequency analysis
    val letterFrequencies = HashMap<Char, Int>()
    for (c in 'a'..'z') {
        letterFrequencies[c] = 0
    }

    // Count letters
    text.forEach { char ->
        if (char in 'a'..'z') {
            letterFrequencies[char] = letterFrequencies[char]!! + 1
        }
    }

    // Calculate error patterns (example metrics)
    val commonReversalPairs = listOf(
        Pair('b', 'd'), Pair('p', 'q'), Pair('m', 'w')
    )

    var reversalScore = 0f
    for ((char1, char2) in commonReversalPairs) {
        // Check for unusual ratios of commonly confused letters
        val ratio = if (letterFrequencies[char2]!! > 0) {
            letterFrequencies[char1]!!.toFloat() / letterFrequencies[char2]!!.toFloat()
        } else {
            0f
        }
        reversalScore += if (ratio > 1.5f || ratio < 0.5f) 0.1f else 0f
    }

    // Populate features array with our metrics
    // First 26 features could be letter frequencies
    for (i in 0 until min(26, featureSize)) {
        val letter = 'a' + i
        val frequency = letterFrequencies[letter]!! / max(text.length.toFloat(), 1f)
        features[i] = frequency
    }

    // Additional features if available
    if (featureSize > 26) {
        features[26] = reversalScore
        // Add more features as needed...
    }

    return features
}
*/
@Composable
fun DyslexiaScreen0(navController: NavHostController) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf(TextFieldValue("")) }
    var analysisResult by remember { mutableStateOf("") }
    val scrollState = rememberScrollState() // For enabling scrolling

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dyslexia Prediction") },
                backgroundColor = MaterialTheme.colors.primarySurface,
                contentColor = Color.White
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp)
                    .verticalScroll(scrollState), // Enable scrolling
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Enter a sentence:",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Type here...") },
                    singleLine = false,
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                    shape = RoundedCornerShape(8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Button(
                    onClick = { analysisResult = analyzeText(inputText.text) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Analyze Dyslexia", fontSize = 16.sp)
                }

                if (analysisResult.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        elevation = 4.dp,
                        backgroundColor = MaterialTheme.colors.surface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Analysis Result:", fontWeight = FontWeight.Bold)
                            Text(analysisResult, fontSize = 16.sp, color = MaterialTheme.colors.onSurface)
                        }
                    }
                }

                Button(
                    onClick = { generatePDF(context, inputText.text, analysisResult) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Generate PDF Report", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp)) // Space before bottom bar
            }
        },
        bottomBar = {
            BottomAppBar(backgroundColor = MaterialTheme.colors.primarySurface) {
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Text("Back to Home", color = MaterialTheme.colors.primary, fontSize = 16.sp)
                }
            }
        }
    )
}










//Aurtism section
/*
@Composable
fun AutismPredictionApp(navController: NavHostController) {
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var jaundice by remember { mutableStateOf("No") }
    var familyHistory by remember { mutableStateOf("No") }
    var screeningScore by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
     lateinit var tflite: Interpreter
    var modelFile: File? = null
    var modelDownloadStatus by remember { mutableStateOf("Downloading model...")}

     fun downloadModel1() {
        modelDownloadStatus = "Downloading model..."
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("autism_model", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
            .addOnSuccessListener { model: CustomModel ->
                modelFile = model.file
                modelDownloadStatus = "Model ready"
                Log.d("FirebaseML", "Model downloaded: ${modelFile?.absolutePath}")
                tflite = Interpreter(modelFile!!)
            }
            .addOnFailureListener { e ->
                modelDownloadStatus = "Model download failed: ${e.message}"
                Log.e("FirebaseML", "Model download failed: ${e.message}")
            }
     }
    LaunchedEffect(Unit) {
        downloadModel1()
    }


    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { downloadModel1() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Download Latest Model")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Model Status: $modelDownloadStatus",
            style = MaterialTheme.typography.caption,
            color = if (modelFile != null) Color.Green else Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text("Enter the following details:")

        TextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age (in years)") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenuField("Gender", listOf("Male", "Female"), gender) { gender = it }
        DropdownMenuField("Jaundice at birth", listOf("Yes", "No"), jaundice) { jaundice = it }
        DropdownMenuField("Family history of autism", listOf("Yes", "No"), familyHistory) { familyHistory = it }

        TextField(
            value = screeningScore,
            onValueChange = { screeningScore = it },
            label = { Text("Screening Score (0-10)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { result = runModel(age, gender, jaundice, familyHistory, screeningScore) }) {
            Text("Predict")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Result: $result")
    }
}

@Composable
fun DropdownMenuField(label: String, options: List<String>, selected: String, onSelectionChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Button(onClick = { expanded = true }) {
            Text("$label: $selected")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onSelectionChange(option)
                    expanded = false
                }) {
                    Text(text = option)
                }
            }
        }
    }
}

fun runModel(age: String, gender: String, jaundice: String, familyHistory: String, screeningScore: String): String {
    val genderValue = if (gender == "Male") 1f else 0f
    val jaundiceValue = if (jaundice == "Yes") 1f else 0f
    val familyHistoryValue = if (familyHistory == "Yes") 1f else 0f
    val ageValue = age.toFloatOrNull() ?: 0f
    val screeningValue = screeningScore.toFloatOrNull() ?: 0f

    val inputArray = floatArrayOf(ageValue, genderValue, jaundiceValue, familyHistoryValue, screeningValue)
    val byteBuffer = ByteBuffer.allocateDirect(4 * inputArray.size).order(ByteOrder.nativeOrder())
    inputArray.forEach { byteBuffer.putFloat(it) }

    val output = Array(1) { FloatArray(1) }
    // tflite.run(byteBuffer, output) <-- Uncomment this when the model is loaded
    return if (output[0][0] > 0.5) "Autism Detected" else "No Autism"
}
*/
@Composable
fun AutismPredictionApp(navController: NavHostController) {
    // Basic demographics
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var jaundice by remember { mutableStateOf("No") }
    var familyHistory by remember { mutableStateOf("No") }

    // Screening scores
    var socialResponsivenessScore by remember { mutableStateOf("") }
    var autismRatingScale by remember { mutableStateOf("") }

    // Additional clinical factors
    var speechDelay by remember { mutableStateOf("No") }
    var learningDisorder by remember { mutableStateOf("No") }
    var geneticDisorders by remember { mutableStateOf("No") }
    var depression by remember { mutableStateOf("No") }
    var intellectualDisability by remember { mutableStateOf("No") }
    var socialIssues by remember { mutableStateOf("No") }
    var anxietyDisorder by remember { mutableStateOf("No") }

    // Results and model state
    var result by remember { mutableStateOf<String?>(null) }
    var modelFile by remember { mutableStateOf<File?>(null) }
    var modelDownloadStatus by remember { mutableStateOf("Not Downloaded") }
    var isModelReady by remember { mutableStateOf(false) }
    var tflite by remember { mutableStateOf<Interpreter?>(null) }

    fun downloadModel() {
        modelDownloadStatus = "Downloading model..."
        isModelReady = false
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel("autism_model", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
            .addOnSuccessListener { model: CustomModel ->
                modelFile = model.file
                modelDownloadStatus = "Model ready"
                isModelReady = true
                Log.d("FirebaseML", "Model downloaded: ${modelFile?.absolutePath}")
                try {
                    tflite = Interpreter(modelFile!!)
                } catch (e: Exception) {
                    modelDownloadStatus = "Error initializing model: ${e.message ?: "Unknown error"}"
                    isModelReady = false
                    Log.e("FirebaseML", "Model initialization failed: ${e.message}")
                }
            }
            .addOnFailureListener { e ->
                modelDownloadStatus = "Model download failed: ${e.message ?: "Unknown error"}"
                isModelReady = false
                Log.e("FirebaseML", "Model download failed: ${e.message}")
            }
    }

    LaunchedEffect(Unit) {
        downloadModel()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comprehensive Autism Screening Tool") },
                backgroundColor = MaterialTheme.colors.primary
            )
        }

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colors.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Model Status Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp,
                    backgroundColor = when {
                        isModelReady -> Color(0xFFE8F5E9)  // Green for ready
                        modelDownloadStatus.contains("failed") || modelDownloadStatus.contains("Error") -> Color(0xFFFFEBEE)  // Red for error
                        else -> Color(0xFFFFF8E1)  // Yellow for in-progress or not started
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = modelDownloadStatus,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = { downloadModel() },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text("Refresh")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Demographics Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Demographics",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it.filter { char -> char.isDigit() } },
                            label = { Text("Age (in years)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SelectionField(
                            label = "Gender",
                            options = listOf("Male", "Female"),
                            selected = gender,
                            onSelectionChange = { gender = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SelectionField(
                            label = "Jaundice at birth",
                            options = listOf("Yes", "No"),
                            selected = jaundice,
                            onSelectionChange = { jaundice = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SelectionField(
                            label = "Family history of autism",
                            options = listOf("Yes", "No"),
                            selected = familyHistory,
                            onSelectionChange = { familyHistory = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Screening Scores Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Screening Scores",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = socialResponsivenessScore,
                            onValueChange = {
                                val newValue = it.filter { char -> char.isDigit() }
                                if (newValue.isEmpty() || newValue.toIntOrNull() in 0..10) {
                                    socialResponsivenessScore = newValue
                                }
                            },
                            label = { Text("Social Responsiveness Scale (0-10)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = autismRatingScale,
                            onValueChange = {
                                val newValue = it.filter { char -> char.isDigit() }
                                if (newValue.isEmpty() || newValue.toIntOrNull() in 0..10) {
                                    autismRatingScale = newValue
                                }
                            },
                            label = { Text("Childhood Autism Rating Scale (0-10)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Clinical Factors Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Clinical Factors",
                            style = MaterialTheme.typography.h6,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        SelectionField(
                            label = "Speech Delay/Language Disorder",
                            options = listOf("Yes", "No"),
                            selected = speechDelay,
                            onSelectionChange = { speechDelay = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SelectionField(
                            label = "Learning Disorder",
                            options = listOf("Yes", "No"),
                            selected = learningDisorder,
                            onSelectionChange = { learningDisorder = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SelectionField(
                            label = "Genetic Disorders",
                            options = listOf("Yes", "No"),
                            selected = geneticDisorders,
                            onSelectionChange = { geneticDisorders = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SelectionField(
                            label = "Depression",
                            options = listOf("Yes", "No"),
                            selected = depression,
                            onSelectionChange = { depression = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SelectionField(
                            label = "Developmental Delay/Intellectual Disability",
                            options = listOf("Yes", "No"),
                            selected = intellectualDisability,
                            onSelectionChange = { intellectualDisability = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SelectionField(
                            label = "Social/Behavioral Issues",
                            options = listOf("Yes", "No"),
                            selected = socialIssues,
                            onSelectionChange = { socialIssues = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        SelectionField(
                            label = "Anxiety Disorder",
                            options = listOf("Yes", "No"),
                            selected = anxietyDisorder,
                            onSelectionChange = { anxietyDisorder = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Prediction Button
                Button(
                    onClick = {
                        result = runModel(
                            age, gender, jaundice, familyHistory,
                            socialResponsivenessScore, autismRatingScale,
                            speechDelay, learningDisorder, geneticDisorders,
                            depression, intellectualDisability, socialIssues, anxietyDisorder,
                            tflite
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = isModelReady &&
                            age.isNotEmpty() &&
                            socialResponsivenessScore.isNotEmpty() &&
                            autismRatingScale.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.primary
                    )
                ) {
                    Text(
                        "Run Prediction",
                        style = MaterialTheme.typography.button,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        onClick = { navController.navigate("home") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Back to Home")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Results Card
                AnimatedVisibility(visible = result != null) {
                    result?.let { resultText ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = 4.dp,
                            backgroundColor = if (resultText.contains("Detected")) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Prediction Result",
                                    style = MaterialTheme.typography.h6,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = resultText,
                                    style = MaterialTheme.typography.h5,
                                    fontWeight = FontWeight.Bold,
                                    color = if (resultText.contains("Detected")) Color(0xFFF44336) else Color(0xFF4CAF50),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "This result is for screening purposes only. " +
                                            "Please consult with a healthcare professional for proper diagnosis.",
                                    style = MaterialTheme.typography.caption,
                                    textAlign = TextAlign.Center,
                                    fontStyle = FontStyle.Italic
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Button(
                                    onClick = { result = null },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                                ) {
                                    Text("Clear Result")
                                }


                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun SelectionField(
    label: String,
    options: List<String>,
    selected: String,
    onSelectionChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selected,
        onValueChange = { },
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            Text("▼", modifier = Modifier.clickable { expanded = true })
        },
        singleLine = true
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.fillMaxWidth(0.85f)
    ) {
        options.forEach { option ->
            DropdownMenuItem(onClick = {
                onSelectionChange(option)
                expanded = false
            }) {
                Text(text = option)
            }
        }
    }
}

fun runModel(
    age: String,
    gender: String,
    jaundice: String,
    familyHistory: String,
    socialResponsivenessScore: String,
    autismRatingScale: String,
    speechDelay: String,
    learningDisorder: String,
    geneticDisorders: String,
    depression: String,
    intellectualDisability: String,
    socialIssues: String,
    anxietyDisorder: String,
    tflite: Interpreter?
): String {
    try {
        // Convert basic demographics
        val ageValue = age.toFloatOrNull() ?: 0f
        val genderValue = if (gender == "Male") 1f else 0f
        val jaundiceValue = if (jaundice == "Yes") 1f else 0f
        val familyHistoryValue = if (familyHistory == "Yes") 1f else 0f

        // Convert scores
        val srsValue = socialResponsivenessScore.toFloatOrNull() ?: 0f
        val carsValue = autismRatingScale.toFloatOrNull() ?: 0f

        // Convert clinical factors
        val speechDelayValue = if (speechDelay == "Yes") 1f else 0f
        val learningDisorderValue = if (learningDisorder == "Yes") 1f else 0f
        val geneticDisordersValue = if (geneticDisorders == "Yes") 1f else 0f
        val depressionValue = if (depression == "Yes") 1f else 0f
        val intellectualDisabilityValue = if (intellectualDisability == "Yes") 1f else 0f
        val socialIssuesValue = if (socialIssues == "Yes") 1f else 0f
        val anxietyDisorderValue = if (anxietyDisorder == "Yes") 1f else 0f

        // Create input array with all features
        val inputArray = floatArrayOf(
            ageValue, genderValue, jaundiceValue, familyHistoryValue,
            srsValue, carsValue,
            speechDelayValue, learningDisorderValue, geneticDisordersValue,
            depressionValue, intellectualDisabilityValue, socialIssuesValue, anxietyDisorderValue
        )

        // Allocate ByteBuffer for input
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputArray.size).order(ByteOrder.nativeOrder())
        inputArray.forEach { byteBuffer.putFloat(it) }

        // Prepare output buffer
        val output = Array(1) { FloatArray(1) }

        // Run the model if available
        if (tflite != null) {
            tflite.run(byteBuffer, output)
            val confidence = output[0][0]

            // Return result based on confidence threshold
            return if (confidence > 0.5f) {
                val percentage = (confidence * 100).toInt()
                "Autism Traits Detected\nConfidence: $percentage%"
            } else {
                val percentage = ((1 - confidence) * 100).toInt()
                "No Autism Detected\nConfidence: $percentage%"
            }
        } else {
            return "Model not loaded properly"
        }
    } catch (e: Exception) {
        Log.e("ModelError", "Error running prediction: ${e.message}")
        return "Error running prediction: ${e.message}"
    }
}

