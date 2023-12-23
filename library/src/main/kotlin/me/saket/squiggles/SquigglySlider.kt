@file:Suppress("ConstPropertyName")

package me.saket.squiggles

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import me.saket.squiggles.SquigglySlider.SquigglesAnimator
import me.saket.squiggles.SquigglySlider.SquigglesSpec
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.sin
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
@ExperimentalMaterial3Api
fun SquigglySlider(
  value: Float,
  onValueChange: (Float) -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
  onValueChangeFinished: (() -> Unit)? = null,
  colors: SliderColors = SliderDefaults.colors(),
  squigglesSpec: SquigglesSpec = SquigglesSpec(),
  squigglesAnimator: SquigglesAnimator = SquigglySlider.rememberSquigglesAnimator(),
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
  Slider(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier,
    enabled = enabled,
    onValueChangeFinished = onValueChangeFinished,
    colors = colors,
    interactionSource = interactionSource,
    thumb = {
      SquigglySlider.Thumb(
        interactionSource = interactionSource,
        colors = colors,
        enabled = enabled,
      )
    },
    track = { sliderState ->
      SquigglySlider.Track(
        colors = colors,
        enabled = enabled,
        sliderState = sliderState,
        squigglesSpec = squigglesSpec,
        squigglesAnimator = squigglesAnimator,
      )
    },
    valueRange = valueRange
  )
}

object SquigglySlider {
  private const val SegmentsPerWavelength = 10
  private const val TwoPi = 2 * PI.toFloat()

  /**
   * ```
   *
   *       _....._                                     _....._         ▲
   *    ,="       "=.                               ,="       "=.   amplitude
   *  ,"             ".                           ,"             ".    │
   *,"                 ".,                     ,,"                 "., ▼
   *""""""""""|""""""""""|."""""""""|""""""""".|""""""""""|""""""""""|
   *                       ".               ."
   *                         "._         _,"
   *                            "-.....-"
   *◀─────────────── Wavelength ──────────────▶
   *
   * ```
   */
  @Immutable
  class SquigglesSpec(
    val strokeWidth: Dp = 4.dp,
    val wavelength: Dp = 24.dp,
    val amplitude: Dp = 2.dp,
  )

  @Stable
  class SquigglesAnimator internal constructor(
    val animationProgress: State<Float>
  )

  @Composable
  fun Thumb(
    interactionSource: MutableInteractionSource,
    colors: SliderColors,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    thumbSize: DpSize = DpSize(width = 4.dp, height = 16.dp),
    shape: Shape = RoundedCornerShape(4.dp),
  ) {
    val interactions = remember { mutableStateListOf<Interaction>() }
    LaunchedEffect(interactionSource) {
      interactionSource.interactions.collect { interaction ->
        when (interaction) {
          is PressInteraction.Press -> interactions.add(interaction)
          is PressInteraction.Release -> interactions.remove(interaction.press)
          is PressInteraction.Cancel -> interactions.remove(interaction.press)
          is DragInteraction.Start -> interactions.add(interaction)
          is DragInteraction.Stop -> interactions.remove(interaction.start)
          is DragInteraction.Cancel -> interactions.remove(interaction.start)
        }
      }
    }

    Box(
      modifier = modifier.sizeIn(minWidth = 20.dp, minHeight = 20.dp),  // Set by Slider.
      contentAlignment = Alignment.Center,
    ) {
      Spacer(
        Modifier
          .size(thumbSize)
          .indication(
            interactionSource = interactionSource,
            indication = androidx.compose.material.ripple.rememberRipple(
              bounded = false,
              radius = maxOf(thumbSize.width, thumbSize.height) + 4.dp,
            )
          )
          .hoverable(interactionSource = interactionSource)
          .background(colors.thumbColor(enabled), shape)
      )
    }
  }

  @Composable
  @ExperimentalMaterial3Api
  fun Track(
    sliderState: SliderState,
    colors: SliderColors,
    modifier: Modifier = Modifier,
    squigglesSpec: SquigglesSpec = SquigglesSpec(),
    squigglesAnimator: SquigglesAnimator = rememberSquigglesAnimator(),
    enabled: Boolean = true,
  ) {
    val sliderHeight = (squigglesSpec.amplitude + squigglesSpec.strokeWidth) * 2
    val inactiveTrackColor = colors.trackColor(enabled, active = false)
    val activeTrackColor = colors.trackColor(enabled, active = true)

    Spacer(
      modifier
        .fillMaxWidth()
        .height(sliderHeight)
        .drawWithCache {
          val path = Path()
          val pathStyle = Stroke(
            width = squigglesSpec.strokeWidth.toPx(),
            join = StrokeJoin.Round,
            cap = StrokeCap.Round,
            pathEffect = PathEffect.cornerPathEffect(
              radius = squigglesSpec.wavelength.toPx()  // For slightly smoother waves.
            ),
          )
          onDrawBehind {
            val isRtl = layoutDirection == LayoutDirection.Rtl
            val sliderLeft = Offset(0f, center.y)
            val sliderRight = Offset(size.width, center.y)
            val sliderStart = if (isRtl) sliderRight else sliderLeft
            val sliderEnd = if (isRtl) sliderLeft else sliderRight
            val sliderValueEnd = Offset(
              x = sliderStart.x + (sliderEnd.x - sliderStart.x) * sliderState.coercedValueAsFraction,
              y = center.y,
            )
            drawLine(
              color = inactiveTrackColor,
              start = sliderValueEnd,
              end = sliderEnd,
              strokeWidth = squigglesSpec.strokeWidth.toPx(),
              cap = StrokeCap.Round
            )
            path.rewind()
            path.buildSquigglesFor(
              squigglesSpec = squigglesSpec,
              startOffset = sliderStart,
              endOffset = sliderValueEnd,
              animationProgress = squigglesAnimator.animationProgress,
            )
            drawPath(
              path = path,
              color = activeTrackColor,
              style = pathStyle,
            )
          }
        }
    )
  }

  @Composable
  fun rememberSquigglesAnimator(duration: Duration = 4.seconds): SquigglesAnimator {
    val animationProgress = rememberInfiniteTransition(label = "Infinite squiggles").animateFloat(
      initialValue = 0f,
      targetValue = 1f,
      animationSpec = infiniteRepeatable(
        animation = tween(durationMillis = duration.inWholeMilliseconds.toInt(), easing = LinearEasing),
        repeatMode = RepeatMode.Restart,
      ),
      label = "Squiggles"
    )
    return remember {
      SquigglesAnimator(animationProgress)
    }
  }

  /**
   * Maths copied from [squigglyspans](https://github.com/samruston/squigglyspans).
   */
  context(DrawScope)
  private fun Path.buildSquigglesFor(
    squigglesSpec: SquigglesSpec,
    startOffset: Offset,
    endOffset: Offset,
    animationProgress: State<Float>,
  ) {
    val waveStartOffset = startOffset.x + (squigglesSpec.strokeWidth.toPx() / 2)
    val waveEndOffset = (endOffset.x - (squigglesSpec.strokeWidth.toPx() / 2)).coerceAtLeast(waveStartOffset)

    val segmentWidth = squigglesSpec.wavelength.toPx() / SegmentsPerWavelength
    val numOfPoints = ceil((waveEndOffset - waveStartOffset) / segmentWidth).toInt() + 1

    var pointX = waveStartOffset
    fastMapRange(start = 0, end = numOfPoints) { point ->
      val proportionOfWavelength = (pointX - waveStartOffset) / squigglesSpec.wavelength.toPx()
      val radiansX = proportionOfWavelength * TwoPi + (TwoPi * animationProgress.value)
      val offsetY = center.y + (sin(radiansX) * squigglesSpec.amplitude.toPx())

      when (point) {
        0 -> moveTo(pointX, offsetY)
        else -> lineTo(pointX, offsetY)
      }
      pointX = (pointX + segmentWidth).coerceAtMost(waveEndOffset)
    }
  }
}
