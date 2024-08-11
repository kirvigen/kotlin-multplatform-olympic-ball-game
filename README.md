# Olympic Ball Games 🏅🎱

Welcome to Olympic Ball Games, an exciting and addictive mobile game that brings the spirit of the Olympics to your fingertips!

[![Olympic Ball Games](https://raw.githubusercontent.com/kirvigen/kotlin-multplatform-olympic-ball-game/master/preview/preview.png)]()

## 📖 About The Game

Olympic Ball Games is a unique fusion of classic ball-merging mechanics and Olympic themes. Players combine balls representing different Olympic host cities to create larger, more valuable balls. As you progress, you'll unlock new cities and climb the global leaderboard!

## 🌟 Features

- **Olympic Themed**: Each ball represents a city that has hosted the Olympic Games.
- **Merge Mechanics**: Combine same-sized balls to create larger ones.
- **Dynamic Gameplay**: Gravity and physics make each game session unique.
- **Progressive Difficulty**: The game becomes more challenging as you advance.
- **Global Leaderboard**: Compete with players worldwide for the top spot.
- **Beautiful Graphics**: Enjoy smooth animations and vibrant visuals.

## 🏙️ Dataset

The game uses a customizable dataset. Here's a snippet of the current dataset:

```kotlin
val olympicSummerCities = listOf(
    BallType(
        image = Res.drawable.paris,
        name = "Paris",
        mass = 512,
        additionalInformation = "in 2024"
    ),
    BallType(
        image = Res.drawable.tokyo,
        name = "Tokyo",
        mass = 256,
        additionalInformation = "in 2020 (held in 2021)"
    ),
    BallType(
        image = Res.drawable.rio,
        name = "Rio de Janeiro",
        mass = 128,
        additionalInformation = "in 2016"
    ),
    BallType(
        image = Res.drawable.london,
        name = "London",
        mass = 64,
        additionalInformation = "in 2012"
    )
    // More cities can be added here
)
```

You can easily modify this dataset to add new cities, change masses, or update information. The `mass` value represents the ball's size and importance, with larger values for more recent games.
## 🛠️ Technologies Used

- **Kotlin**: For robust and concise code.
- **Compose Multiplatform**: For creating a responsive and beautiful UI across different platforms.
- **Coroutines**: For managing background tasks and asynchronous code.

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Kotlin 1.5.0 or later
- JDK 11 or later

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/olympic-ball-games.git
   ```
2. Open the project in Android Studio.
3. Sync the project with Gradle files.
4. Run the app on an emulator or physical device.

## 🎮 How to Play

1. Tap the screen to drop balls representing Olympic host cities.
2. Merge balls of the same size to create larger ones.
3. Try to create the largest ball possible.
4. Avoid letting balls stack up to the top of the screen.

## 📄 License

This project is licensed under the MIT License. This means you are free to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the software, subject to the following conditions:

1. The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
2. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.

For the full license text, please see the [MIT License](https://opensource.org/licenses/MIT).

---

Enjoy playing Olympic Ball Games and may the best player win! 🏆🌟