@file:OptIn(ExperimentalCoroutinesApi::class)

package com.machado001.kotlin.flow.ticker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


//No good name for this one.
data class DataClassExample(val time: Duration, val randomInt: Int)


val duration = 100.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class FlowViewModel : ViewModel() {

    private val isCounting = MutableStateFlow(false)
    private val timerAccumulator = MutableStateFlow(Duration.ZERO)

    private val durationFlow: Flow<Duration> = duration.timeAndEmit()
    private val randomIntFlow = flow {
        repeat(1000) {
            delay(1000)
            emit(Random(it).nextInt(100))
        }
    }

    /**
     * This flow combines a timer with a random integer generator.
     * It emits a [DataClassExample] object containing the elapsed time and a random integer.
     * The flow is active only when [isCounting] is true.
     * It is backed by a [StateFlow] to avoid data loss when the UI is in the background.
     */
    val result = isCounting
        .flatMapLatest { isCounting ->
            if (isCounting) {
                durationFlow
                    .scan(timerAccumulator.value) { accumulator, value ->
                        val newTotal = accumulator + value
                        timerAccumulator.value = newTotal
                        newTotal
                    }
                    .filterNot { duration ->
                        duration == Duration.ZERO
                    }
                    .zip(randomIntFlow) { time, ampl ->
                        DataClassExample(time, ampl)
                    }
                    .also(::println)
            } else flowOf()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(100L),
            DataClassExample(Duration.ZERO, 0)
        )


    fun startCounting() {
        isCounting.value = true
    }

    fun stopCounting() {
        isCounting.value = false
    }
}