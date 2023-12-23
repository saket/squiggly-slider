package me.saket.squiggles.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.sharp.Pause
import androidx.compose.material.icons.twotone.Shuffle
import androidx.compose.material.icons.twotone.SkipNext
import androidx.compose.material.icons.twotone.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vanniktech.blurhash.BlurHash
import me.saket.squiggles.SquigglySlider

@OptIn(ExperimentalMaterial3Api::class)
class SampleActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val colors = if (isSystemInDarkTheme()) dynamicDarkColorScheme(this) else dynamicLightColorScheme(this)
      MaterialTheme(colors) {
        Scaffold(
          topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
          }
        ) { contentPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(contentPadding)
              .padding(24.dp),
            contentAlignment = Alignment.Center,
          ) {
            var sliderValue by rememberSaveable { mutableStateOf(0.1f) }
            var isPlaying by rememberSaveable { mutableStateOf(true) }
            MediaNotificationScaffold(
              slider = {
                SquigglySlider(
                  value = sliderValue,
                  onValueChange = { sliderValue = it },
                  colors = SliderDefaults.colors(
                    thumbColor = LocalContentColor.current,
                    activeTrackColor = LocalContentColor.current,
                    inactiveTrackColor = LocalContentColor.current.copy(alpha = 0.2f),
                  ),
                  squigglesSpec = SquigglySlider.SquigglesSpec(
                    strokeWidth = 2.dp,
                    amplitude = if (isPlaying) 2.dp else 0.dp,
                  )
                )
              },
              isPlaying = isPlaying,
              onPlayPauseClick = { isPlaying = !isPlaying },
            )
          }
        }
      }
    }
  }

  @Composable
  private fun MediaNotificationScaffold(
    slider: @Composable () -> Unit,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
  ) {
    val blurhashBitmap = remember {
      BlurHash.decode(
        blurHash = "LfGRtaoJNxRl}+kBNaWD-mayWos.",
        width = 522,
        height = 768,
      )!!.asImageBitmap()
    }

    val colors = darkColorScheme(
      primary = Color(0xFFFBE400),
      onPrimary = Color.Black,
    )
    MaterialTheme(colors) {
      CompositionLocalProvider(LocalContentColor provides Color.White) {
        Box(
          modifier
            .fillMaxWidth()
            .height(184.dp)
            .clip(RoundedCornerShape(28.dp))
        ) {
          Image(
            modifier = Modifier.fillMaxWidth(),
            bitmap = blurhashBitmap,
            contentDescription = null,
            contentScale = ContentScale.Crop,
          )
          Column(
            Modifier
              .fillMaxSize()
              .padding(horizontal = 12.dp, vertical = 0.dp)
          ) {
            Spacer(Modifier.weight(1f))

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
              verticalAlignment = Alignment.Bottom,
              horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
              Column(Modifier.weight(1f)) {
                Text(
                  text = "Delicate Weapon",
                  style = MaterialTheme.typography.titleMedium,
                )
                Text(
                  text = "Grimes, Lizzy Wizzy",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Normal,
                  color = LocalContentColor.current.copy(alpha = 0.6f),
                )
              }
              PlayPauseIconButton(
                modifier = Modifier
                  .padding(bottom = 4.dp)
                  .size(48.dp),
                isPlaying = isPlaying,
                onClick = onPlayPauseClick,
              )
            }

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
              Icon(
                imageVector = Icons.TwoTone.SkipPrevious,
                contentDescription = null,
              )
              Box(Modifier.weight(1f)) {
                slider()
              }
              Icon(
                imageVector = Icons.TwoTone.SkipNext,
                contentDescription = null,
              )
              Icon(
                modifier = Modifier
                  .padding(end = 4.dp)
                  .size(16.dp),
                imageVector = Icons.TwoTone.Shuffle,
                contentDescription = null,
              )
            }
          }
        }
      }
    }
  }
}

@Composable
@Suppress("NAME_SHADOWING")
internal fun PlayPauseIconButton(
  isPlaying: Boolean,
  onClick: (() -> Unit)?,
  modifier: Modifier = Modifier,
) {
  val animatingShape = RoundedCornerShape(
    percent = animateIntAsState(
      targetValue = if (isPlaying) 24 else 50,
      animationSpec = tween(200),
      label = "Play/pause icon's corner size",
    ).value
  )
  AnimatedContent(
    modifier = modifier
      .clip(animatingShape)
      .background(MaterialTheme.colorScheme.primary)
      .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
      .padding(12.dp),
    targetState = isPlaying,
    transitionSpec = { scaleIn() togetherWith scaleOut() },
    label = "Play/pause icon",
  ) { isPlaying ->
    Icon(
      modifier = Modifier.fillMaxSize(),
      imageVector = if (isPlaying) Icons.Sharp.Pause else Icons.Rounded.PlayArrow,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onPrimary,
    )
  }
}
