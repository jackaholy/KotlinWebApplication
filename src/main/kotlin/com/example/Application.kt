// Jack Holy
// This kotlin program keeps track of a list of scores using Ktor.
// I use Mustache to help with the GET and POST methods. Using Mustache allows it
// to be used similarly to python Flask.
// The user can see their average, best, and worst score, and see the total number of scores
// they have entered.

package com.example

import com.example.plugins.*
import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File
import io.ktor.server.mustache.*

/**
 * Runs the application itself.
 */
fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

/**
 * Installs Mustache, a template engine to help with the HTML POST method.
 */
fun Application.module() {
    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("mycontent")
    }
    configureRouting()
}

/**
 * Adds a new score to the csv file.
 * @param data - The score which is to be added.
 */
fun newScore(data: Int) {
    // Add score to the csv file
    if (data == null) {
        return
    } else {
        File("src/main/resources/scores.csv").appendText("$data\n")
    }
}

/**
 * Opens and reads the specified file removing whitespace.
 * @param fileName - the name of the file to be read.
 * @return list - a list of all the scores that are read.
 */
fun openFile(fileName: String): List<Int> {
    return File(fileName).useLines { lines ->
        lines.mapNotNull { line ->
            line.trim().toIntOrNull()
        }.toList()
    }
}

/**
 * Gets the mean (average) score from all the scores in the csv file.
 * @return Double.
 */
fun getMean(): Double? {
    val scores = openFile("src/main/resources/scores.csv")
    return if (scores.isNotEmpty()) {
        scores.average()
    } else {
        null
    }
}

/**
 * Gets the worst score from all the scores in the csv file.
 * @return the first (lowest) score in the list.
 */
fun getWorstScore(): Int {
    val scores = openFile("src/main/resources/scores.csv").sorted()
    return scores.first()
}

/**
 * Gets the best score from all the scores in the csv file.
 * @return the last (highest) score in the list.
 */
fun getBestScore(): Int {
    val scores = openFile("src/main/resources/scores.csv").sorted()
    return scores.last()
}

/**
 * Gets the total number of entries in the file.
 * @return the number of entries in the file.
 */
fun getTotalEntries(): Int {
    return openFile("src/main/resources/scores.csv").size
}

// Placeholder to store a single score to be stored in a List.
data class Score(val value: Int)

/**
 * Gets all the scores in the file. I got this function with the help of chat.openai.com
 * @return scores.
 */
fun getScores(): List<Score> {
    val scores = mutableListOf<Score>()
    File("src/main/resources/scores.csv").forEachLine { line ->
        line.trim().toIntOrNull()?.let { scoreValue ->
            scores.add(Score(scoreValue))
        }
    }
    return scores
}