package com.example.ticker

import android.annotation.SuppressLint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


@SuppressLint("DefaultLocale")
fun Duration.toUiDesiredTime(): String {
    val totalSeconds = inWholeSeconds
    val hours = String.format("%02d", totalSeconds / (60 * 60))
    val minutes = String.format("%02d", (totalSeconds % 3600) / 60)
    val seconds = String.format("%02d", (totalSeconds % 60))

    return "$hours:$minutes:$seconds"
}


fun Duration.timeAndEmit(durationInMilliseconds: Long = 1000L): Flow<Duration> = flow {
    var lastEmitTime = System.currentTimeMillis()
    while (true) {
        delay(durationInMilliseconds)
        val currentTime = System.currentTimeMillis()
        val elapsedTime = currentTime - lastEmitTime
        emit(elapsedTime.milliseconds)
        lastEmitTime = currentTime
    }
}