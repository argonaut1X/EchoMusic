package iad1tya.echo.music.ui.theme

import android.graphics.Bitmap

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import com.materialkolor.score.Score

val DefaultThemeColor = Color(0xFFD71921) // Nothing OS signature red

// Nothing OS dark color scheme — pure black with red accent
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD71921),            // Nothing red accent
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF121212),    // Dark gray card/surface
    onPrimaryContainer = Color(0xFFFFFFFF),
    secondary = Color(0xFF9E9E9E),          // Medium gray muted text
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF121212),
    onSecondaryContainer = Color(0xFF9E9E9E),
    tertiary = Color(0xFF9E9E9E),
    onTertiary = Color(0xFF000000),
    tertiaryContainer = Color(0xFF121212),
    onTertiaryContainer = Color(0xFF9E9E9E),
    error = Color(0xFFD71921),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFF5A0000),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF000000),         // Pure black background
    onBackground = Color(0xFFFFFFFF),       // White text
    surface = Color(0xFF000000),            // Pure black surface
    onSurface = Color(0xFFFFFFFF),          // White text
    surfaceVariant = Color(0xFF121212),     // Dark gray
    onSurfaceVariant = Color(0xFF9E9E9E),  // Medium gray
    surfaceTint = Color(0xFFD71921),        // Nothing red tint
    inverseSurface = Color(0xFFFFFFFF),
    inverseOnSurface = Color(0xFF000000),
    outline = Color(0xFF333333),            // Dark gray disabled/inactive
    outlineVariant = Color(0xFF1A1A1A),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFF1A1A1A),
    surfaceContainer = Color(0xFF121212),   // Dark gray card bg
    surfaceContainerHigh = Color(0xFF1A1A1A),
    surfaceContainerHighest = Color(0xFF222222),
    surfaceContainerLow = Color(0xFF0A0A0A),
    surfaceContainerLowest = Color(0xFF000000),
    surfaceDim = Color(0xFF000000)
)

// Nothing OS light color scheme — white with red accent
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFD71921),            // Nothing red accent
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFF5F5F5),
    onPrimaryContainer = Color(0xFF1A1A1A),
    secondary = Color(0xFF666666),          // Muted text
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF0F0F0),
    onSecondaryContainer = Color(0xFF1A1A1A),
    tertiary = Color(0xFF666666),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE8E8E8),
    onTertiaryContainer = Color(0xFF1A1A1A),
    error = Color(0xFFD71921),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFF5A0000),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFFAFAFA),
    onSurfaceVariant = Color(0xFF333333),
    surfaceTint = Color(0xFFD71921),        // Nothing red tint
    inverseSurface = Color(0xFF1A1A1A),
    inverseOnSurface = Color(0xFFFFFFFF),
    outline = Color(0xFFBDBDBD),
    outlineVariant = Color(0xFFE0E0E0),
    scrim = Color(0xFF000000),
    surfaceBright = Color(0xFFFFFFFF),
    surfaceContainer = Color(0xFFFAFAFA),
    surfaceContainerHigh = Color(0xFFF5F5F5),
    surfaceContainerHighest = Color(0xFFF0F0F0),
    surfaceContainerLow = Color(0xFFFDFDFD),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFF5F5F5)
)

@Composable
fun EchoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    pureBlack: Boolean = false,
    themeColor: Color = DefaultThemeColor,
    isDynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = when {
        isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme.pureBlack(pureBlack),
        typography = AppTypography,
        content = content
    )
}

fun Bitmap.extractThemeColor(): Color {
    val colorsToPopulation = Palette.from(this)
        .maximumColorCount(8)
        .generate()
        .swatches
        .associate { it.rgb to it.population }
    val rankedColors = Score.score(colorsToPopulation)
    return Color(rankedColors.first())
}

fun Bitmap.extractGradientColors(): List<Color> {
    val extractedColors = Palette.from(this)
        .maximumColorCount(64)
        .generate()
        .swatches
        .associate { it.rgb to it.population }

    val orderedColors = Score.score(extractedColors, 2, 0xff4285f4.toInt(), true)
        .sortedByDescending { Color(it).luminance() }

    return if (orderedColors.size >= 2)
        listOf(Color(orderedColors[0]), Color(orderedColors[1]))
    else
        listOf(Color(0xFF595959), Color(0xFF0D0D0D))
}

fun ColorScheme.pureBlack(apply: Boolean) =
    if (apply) copy(
        surface = Color.Black,
        background = Color.Black
    ) else this

val ColorSaver = object : Saver<Color, Int> {
    override fun restore(value: Int): Color = Color(value)
    override fun SaverScope.save(value: Color): Int = value.toArgb()
}
