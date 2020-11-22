package com.daniilshevtsov.deduplication

import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@UseExperimental(ExperimentalTime::class)
fun main(args: Array<String>) {
    val app = DeduplicationApp()
    val executionDuration = measureTime {
        app.start(args = args)
    }
    println("execution time: ${executionDuration.inSeconds} seconds")
}