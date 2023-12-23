package me.saket.squiggles

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderState
import androidx.compose.ui.graphics.Color
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
internal inline fun <R> fastMapRange(
  start: Int,
  end: Int,
  transform: (Int) -> R
): List<R> {
  contract { callsInPlace(transform) }
  val destination = ArrayList<R>(/* initialCapacity = */ end - start + 1)
  for (i in start..end) {
    destination.add(transform(i))
  }
  return destination
}

internal fun SliderColors.thumbColor(enabled: Boolean): Color =
  if (enabled) thumbColor else disabledThumbColor

internal fun SliderColors.trackColor(enabled: Boolean, active: Boolean): Color =
  if (enabled) {
    if (active) activeTrackColor else inactiveTrackColor
  } else {
    if (active) disabledActiveTrackColor else disabledInactiveTrackColor
  }

@ExperimentalMaterial3Api
internal val SliderState.coercedValueAsFraction
  get() = calcFraction(
    valueRange.start,
    valueRange.endInclusive,
    value.coerceIn(valueRange.start, valueRange.endInclusive)
  )

// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
private fun calcFraction(a: Float, b: Float, pos: Float) =
  (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

