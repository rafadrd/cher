package pt.isel.cher.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = mocha_mauve,
        secondary = mocha_pink,
        tertiary = mocha_teal,
        background = mocha_base,
        surface = mocha_surface0,
        onPrimary = mocha_crust,
        onSecondary = mocha_crust,
        onTertiary = mocha_crust,
        onBackground = mocha_text,
        onSurface = mocha_text,
        error = mocha_red,
        onError = mocha_crust,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = latte_mauve,
        secondary = latte_pink,
        tertiary = latte_teal,
        background = latte_base,
        surface = latte_surface0,
        onPrimary = latte_crust,
        onSecondary = latte_crust,
        onTertiary = latte_crust,
        onBackground = latte_text,
        onSurface = latte_text,
        error = latte_red,
        onError = latte_crust,
    )

@Composable
fun CheRTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
