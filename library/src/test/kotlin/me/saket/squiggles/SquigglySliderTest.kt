@file:Suppress("TestFunctionName")

package me.saket.squiggles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import org.junit.Rule
import org.junit.Test

class SquigglySliderTest {
  @get:Rule val paparazzi = Paparazzi(
    deviceConfig = DeviceConfig.PIXEL_5,
    showSystemUi = false,
    renderingMode = RenderingMode.SHRINK,
  )

  @Test fun `zero slider value`() {
    paparazzi.snapshot {
      Scaffold {
        TODO()
      }
    }
  }

  @Test fun `zero amplitude`() {
    paparazzi.snapshot {
      Scaffold {
        TODO()
      }
    }
  }

  @Test fun `non-zero amplitude`() {
    paparazzi.snapshot {
      Scaffold {
        TODO()
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

val Color.Companion.Whisper get() = Color(0XFFF8F5FA)
val Color.Companion.SeaBuckthorn get() = Color(0xFFF9A825)
val Color.Companion.Perfume get() = Color(0xFFD0BCFF)
val Color.Companion.GraySuit get() = Color(0xFFC1BAC9)
