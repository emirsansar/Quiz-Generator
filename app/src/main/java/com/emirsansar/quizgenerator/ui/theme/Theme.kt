package com.emirsansar.quizgenerator.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color.Black,
    secondary = Color(0xFF21232a),
    tertiary = Color(0xFF4b4c53),
    background = Color(0xFF21232a),
    surface = Color(0xFF797886),
    onPrimary = Color.White,
    onSecondary = Color(0xFFD9D9D9),
    onTertiary = Color(0xFF8E8F8F),
    onBackground = Color(0xFFECECEC),
    onSurface = Color(0xFFBFBCCE),
    primaryContainer = Color(0xFF4b4c53),
    onPrimaryContainer = Color(0xFFECECEC),
    secondaryContainer = Color(0xFF21232a),
    onSecondaryContainer = Color(0xFF4b4c53),
    tertiaryContainer = Color(0xFF2A2C34),
    onTertiaryContainer = Color.LightGray
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = Color(0xFFD0CDCD),
    tertiary = Color(0xFFECE8E8),
    background = Color.LightGray,
    surface = Color(0xFFFFF4F8),
    onPrimary = Color.Black,
    onSecondary = Color(0xFF1C1C1C),
    onTertiary = Color(0xFF363636),
    onBackground = Color(0xFF111111),
    onSurface = Color(0xFFE6E6E7),
    primaryContainer = Color.White,
    onPrimaryContainer = Color(0xFFE7E7E7),
    secondaryContainer = Color.LightGray,
    onSecondaryContainer = Color(0xFFE8E8EA),
    tertiaryContainer = Color(0xFFE3E3E5),
    onTertiaryContainer = Color.LightGray,
)


@Composable
fun QuizGeneratorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}