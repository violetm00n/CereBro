package com.example.dyslexiaanalyzer

fun analyzeText(text: String): String {
    // Common letter confusions in dyslexia
    val DYSLEXIC_LETTER_CONFUSIONS = listOf(
        'b' to 'd', 'p' to 'q', 'm' to 'w', 'n' to 'u', 'n' to 'r',
        'i' to 'j', 'a' to 'e', 's' to 'z', 'f' to 't', 'c' to 'k',
        'g' to 'q', 'h' to 'n', 'v' to 'w', 'b' to 'p', 'c' to 's',
        'd' to 't', 'o' to 'e', 'a' to 'o', 'u' to 'v', 'm' to 'n'
    )

    // Common word confusions in dyslexia
    val DYSLEXIC_WORD_CONFUSIONS = listOf(
        listOf("was", "saw"), listOf("there", "their", "they're"),
        listOf("here", "hear", "hair"), listOf("you", "your", "you're", "ewe"),
        listOf("where", "wear", "were"), listOf("to", "too", "two"),
        listOf("its", "it's"), listOf("new", "knew"),
        listOf("break", "brake"), listOf("bare", "bear"),
        listOf("peace", "piece"), listOf("right", "write"),
        listOf("flower", "flour"), listOf("buy", "by", "bye"),
        listOf("no", "know"), listOf("for", "four"),
        listOf("sun", "son"), listOf("allowed", "aloud"),
        listOf("hour", "our"), listOf("blew", "blue"),
        listOf("sew", "sow"), listOf("be", "bee"),
        listOf("one", "won"), listOf("toe", "tow"),
        listOf("threw", "through"), listOf("role", "roll"),
        listOf("mail", "male"), listOf("tail", "tale")
    )

    // Normalize the text for analysis
    val normalizedText = text.lowercase().trim()
    val words = normalizedText.split(Regex("\\s+"))

    // Track scores and detected patterns
    var letterConfusionScore = 0
    var wordConfusionScore = 0
    val detectedLetterConfusions = mutableSetOf<String>()
    val detectedWordConfusions = mutableSetOf<String>()

    // Analyze individual words for letter confusions
    for (word in words) {
        for (i in 0 until word.length - 1) {
            val charPair = word.substring(i, i + 2)

            // Look for adjacent confused letters
            for ((confusionA, confusionB) in DYSLEXIC_LETTER_CONFUSIONS) {
                val pattern1 = "$confusionA$confusionB"
                val pattern2 = "$confusionB$confusionA"

                if (charPair == pattern1 || charPair == pattern2) {
                    letterConfusionScore++
                    detectedLetterConfusions.add("'$charPair' (confusion between '$confusionA' and '$confusionB')")
                }
            }
        }

        // Check for repeated letters that shouldn't be repeated
        val repeatedLetters = word.zipWithNext().filter { it.first == it.second }
        if (repeatedLetters.isNotEmpty()) {
            letterConfusionScore += repeatedLetters.size
            repeatedLetters.forEach {
                detectedLetterConfusions.add("Repeated '${it.first}' in '$word'")
            }
        }
    }

    // Check for word confusions
    for (confusionGroup in DYSLEXIC_WORD_CONFUSIONS) {
        val foundWords = confusionGroup.filter { confusionWord ->
            words.contains(confusionWord)
        }

        // If words from the same confusion group appear in the text
        if (foundWords.size > 1) {
            wordConfusionScore += foundWords.size
            detectedWordConfusions.add("Confusion between: ${foundWords.joinToString(", ") { "'$it'" }}")
        }
    }

    // Additional heuristics

    // 1. Check for word reversals (e.g., "was" used where "saw" would be correct)
    val reversalPairs = words.zipWithNext()
    for ((word1, word2) in reversalPairs) {
        if (word1.length > 2 && word2.length > 2) {
            if (word1 == word2.reversed()) {
                wordConfusionScore += 3
                detectedWordConfusions.add("Word reversal: '$word1' and '$word2'")
            }
        }
    }

    // 2. Check for missing or extra letters
    val spellingIssues = mutableListOf<String>()
    // This would require a dictionary check in a real implementation

    // Calculate final score
    val totalScore = letterConfusionScore + (wordConfusionScore * 2)
    val maxPossibleScore = 30 // Arbitrary scale for scoring
    val normalizedScore = minOf(totalScore.toFloat() / maxPossibleScore, 1.0f)
    val percentageScore = (normalizedScore * 100).toInt()

    // Prepare detailed result
    val resultBuilder = StringBuilder()
    resultBuilder.append("Dyslexia Indicator Score: $percentageScore%\n\n")

    when {
        percentageScore > 70 -> {
            resultBuilder.append("Strong indicators of dyslexic patterns detected in the text.\n\n")

            if (detectedLetterConfusions.isNotEmpty()) {
                resultBuilder.append("Letter-based patterns detected:\n")
                detectedLetterConfusions.take(5).forEach { resultBuilder.append("• $it\n") }
                if (detectedLetterConfusions.size > 5) {
                    resultBuilder.append("• And ${detectedLetterConfusions.size - 5} more letter patterns\n")
                }
                resultBuilder.append("\n")
            }

            if (detectedWordConfusions.isNotEmpty()) {
                resultBuilder.append("Word-based patterns detected:\n")
                detectedWordConfusions.take(5).forEach { resultBuilder.append("• $it\n") }
                if (detectedWordConfusions.size > 5) {
                    resultBuilder.append("• And ${detectedWordConfusions.size - 5} more word patterns\n")
                }
            }
        }
        percentageScore > 40 -> {
            resultBuilder.append("Some indicators of dyslexic patterns detected.\n\n")

            if (detectedLetterConfusions.isNotEmpty() || detectedWordConfusions.isNotEmpty()) {
                resultBuilder.append("Key patterns identified:\n")
                (detectedLetterConfusions + detectedWordConfusions).take(3).forEach {
                    resultBuilder.append("• $it\n")
                }
            }

            resultBuilder.append("\nThe text shows some patterns that may be associated with dyslexia, but the indicators are not strong.")
        }
        else -> {
            resultBuilder.append("No significant indicators of dyslexic patterns detected in this text sample.\n\n")
            resultBuilder.append("The writing patterns appear to follow typical language structure.")
        }
    }

    return resultBuilder.toString()
}