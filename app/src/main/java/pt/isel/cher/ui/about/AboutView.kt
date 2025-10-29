package pt.isel.cher.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.domain.Author
import pt.isel.cher.ui.components.AuthorItem
import pt.isel.cher.ui.components.CherTopBar
import pt.isel.cher.ui.theme.CheRTheme

@Composable
fun AboutView(
    modifier: Modifier = Modifier,
    authors: List<Author>,
    onSendEmail: (List<String>) -> Unit,
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
            FloatingActionButton(onClick = { onSendEmail(authors.map { it.email }) }) {
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
fun AboutViewPreview() {
    val authors =
        listOf(
            Author("47207", "Diogo", "Ribeiro", "a47207@alunos.isel.pt"),
            Author("47236", "António", "Coelho", "a47236@alunos.isel.pt"),
            Author("49423", "Rafael", "Pegacho", "a49423@alunos.isel.pt"),
        )
    CheRTheme { AboutView(authors = authors, onSendEmail = {}, onNavigateBack = {}) }
}
