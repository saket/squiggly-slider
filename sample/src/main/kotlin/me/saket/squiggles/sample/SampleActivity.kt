package me.saket.squiggles.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
class SampleActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val uiController = rememberSystemUiController()
      val systemInDarkTheme = isSystemInDarkTheme()
      LaunchedEffect(Unit) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        uiController.setSystemBarsColor(Color.Transparent, darkIcons = !systemInDarkTheme)
        uiController.setNavigationBarColor(Color.Transparent)
      }

      val colors = if (systemInDarkTheme) dynamicDarkColorScheme(this) else dynamicLightColorScheme(this)

      MaterialTheme(colors) {
        Scaffold(
          topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
          }
        ) { contentPadding ->
          TODO()
        }
      }
    }
  }
}
