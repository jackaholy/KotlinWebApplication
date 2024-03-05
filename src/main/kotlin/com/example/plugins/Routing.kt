package com.example.plugins

import com.example.*
import io.ktor.server.application.*
import io.ktor.server.mustache.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Handles the GET and POST methods.
 */
fun Application.configureRouting() {
    routing {

        get("/") {
            call.respond(
                MustacheContent(
                    "index.html",
                    // Tie each of the score displays to it's appropriate function.
                    mapOf(
                        "mean_score" to getMean().toString(),
                        "best_score" to getBestScore().toString(),
                        "worst_score" to getWorstScore().toString(),
                        "total_entries" to getTotalEntries().toString(),
                        "scores" to getScores()
                    )
                )
            )
        }
        // Allow the user to enter new scores
        post("/submit_score") {
            val formParameters = call.receiveParameters()
            val score = formParameters["data"].toString()
            if (score != "") {
                newScore(score.toInt())
            }
            // Always go back to the same page.
            call.respondRedirect("/")
        }
    }
}