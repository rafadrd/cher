package pt.isel.cher.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.ui.components.CherButton
import pt.isel.cher.ui.theme.CheRTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onPlay: () -> Unit,
    onFavorites: () -> Unit,
    onAbout: () -> Unit,
    onLeave: () -> Unit,
) {
    Box(modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = onAbout, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(R.string.main_button_about_description),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
            IconButton(onClick = onLeave, modifier = Modifier.size(48.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = stringResource(R.string.main_button_leave_description),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.main_screen_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp),
            )

            CherButton(
                text = stringResource(R.string.main_button_play),
                onClick = onPlay,
                modifier = Modifier.width(240.dp).padding(vertical = 8.dp),
            )

            CherButton(
                text = stringResource(R.string.main_button_favorites),
                onClick = onFavorites,
                modifier = Modifier.width(240.dp).padding(vertical = 8.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    CheRTheme { MainScreen(onPlay = {}, onFavorites = {}, onAbout = {}, onLeave = {}) }
}
