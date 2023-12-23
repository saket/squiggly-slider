```groovy
implementation "me.saket.squiggly-slider:squiggly-slider:1.0.0"
```

```diff
- import androidx.compose.material3.Slider
+ import me.saket.squiggles.SquigglySlider

- Slider(
+ SquigglySlider(
    value = sliderValue,
    onValueChange = { sliderValue = it },
  )
```

## License

```
Copyright 2023 Saket Narayan.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```