@file:Suppress("TestFunctionName")

package me.saket.squiggles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class SquigglySliderTest {
  @get:Rule val paparazzi = Paparazzi(
    deviceConfig = DeviceConfig.PIXEL_5,
    showSystemUi = false,
    renderingMode = RenderingMode.SHRINK,
  )

  @Test fun `zero slider value`() {
    paparazzi.snapshot {
      Scaffold {
        SquigglySlider(
          value = 0f,
          onValueChange = {},
        )
      }
    }
  }

  @Test fun `zero amplitude`() {
    paparazzi.snapshot {
      Scaffold {
        SquigglySlider(
          value = 0.5f,
          onValueChange = {},
          squigglesSpec = SquigglySlider.SquigglesSpec(
            amplitude = 0.dp
          ),
          squigglesAnimator = SquigglySlider.SquigglesAnimator(animationProgress = mutableStateOf(0.5f)),
        )
      }
    }
  }

  @Test fun `non-zero amplitude`() {
    paparazzi.snapshot {
      Scaffold {
        SquigglySlider(
          value = 0.5f,
          onValueChange = {},
          squigglesSpec = SquigglySlider.SquigglesSpec(
            amplitude = 2.dp,
            wavelength = 24.dp,
          ),
          squigglesAnimator = SquigglySlider.SquigglesAnimator(animationProgress = mutableStateOf(0.5f)),
        )
      }
    }
  }

  @Test fun `calculate the default values of squiggles based on the stroke width`() {
    paparazzi.snapshot {
      Scaffold {
        SquigglySlider(
          value = 0.6f,
          onValueChange = {},
          squigglesSpec = SquigglySlider.SquigglesSpec(strokeWidth = 30.dp),
          squigglesAnimator = SquigglySlider.SquigglesAnimator(animationProgress = mutableStateOf(1f)),
        )
      }
    }
  }

  @Composable
  private fun Scaffold(content: @Composable BoxScope.() -> Unit) {
    MaterialTheme(
      colorScheme = dynamicDarkColorScheme(LocalContext.current)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surface),
        content = content,
        contentAlignment = Alignment.Center
      )
    }
  }
}
