package me.saket.squiggles.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
            var sliderValue by rememberSaveable {
              mutableStateOf(0.4f)
            }
            SquigglySlider(
              value = sliderValue,
              onValueChange = { sliderValue = it },
            )
          }
        }
      }
    }
  }
}
