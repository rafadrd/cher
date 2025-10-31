package pt.isel.cher.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.cher.R
import pt.isel.cher.ui.theme.CheRTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CherTopBar(modifier: Modifier = Modifier, title: String, onNavigateBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.top_bar_back_button_description),
                )
            }
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun CherTopBarPreview() {
    CheRTheme { CherTopBar(title = "Screen Title", onNavigateBack = {}) }
}
