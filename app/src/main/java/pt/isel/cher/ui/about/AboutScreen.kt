package pt.isel.cher.ui.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pt.isel.cher.R
import pt.isel.cher.domain.Author
import pt.isel.cher.ui.components.AuthorItem
import pt.isel.cher.ui.components.CherTopBar
import pt.isel.cher.ui.theme.CheRTheme
import pt.isel.cher.util.Constants

@Composable
fun AboutScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    val viewModel: AboutViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sendEmailEvent.collect { emails ->
            val intent =
                Intent(Intent.ACTION_SENDTO, "mailto:".toUri()).apply {
                    putExtra(Intent.EXTRA_EMAIL, emails.toTypedArray())
                    putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
                    putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_body))
                }

            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Log.e(Constants.TAG, context.getString(R.string.no_email_clients), e)
                Toast.makeText(
                        context,
                        context.getString(R.string.no_email_client_found),
                        Toast.LENGTH_SHORT,
                    )
                    .show()
            }
        }
    }

    AboutScreenContent(
        modifier = modifier,
        authors = viewModel.authors,
        onSendEmail = viewModel::onSendEmailClicked,
        onNavigateBack = onBack,
    )
}

@Composable
private fun AboutScreenContent(
    modifier: Modifier = Modifier,
    authors: List<Author>,
    onSendEmail: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            CherTopBar(
                title = stringResource(R.string.about_authors_title),
                onNavigateBack = onNavigateBack,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onSendEmail) {
                Icon(Icons.Default.Email, stringResource(R.string.send_email_description))
            }
        },
        content = {
            Column(
                modifier = modifier.fillMaxSize().padding(it).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                authors.forEach { author -> AuthorItem(author) }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    val authors =
        listOf(
            Author("47207", "Diogo", "Ribeiro", "a47207@alunos.isel.pt"),
            Author("47236", "António", "Coelho", "a47236@alunos.isel.pt"),
            Author("49423", "Rafael", "Pegacho", "a49423@alunos.isel.pt"),
        )
    CheRTheme { AboutScreenContent(authors = authors, onSendEmail = {}, onNavigateBack = {}) }
}
