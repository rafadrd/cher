package pt.isel.cher.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.cher.R
import pt.isel.cher.domain.Author
import pt.isel.cher.ui.theme.CheRTheme

@Composable
fun AuthorItem(author: Author) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(text = author.fullName, style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.author_number, author.number),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthorItemPreview() {
    val author =
        Author(
            number = "77777",
            firstName = "Cristiano",
            lastName = "Ronaldo",
            email = "77777@cristiano.pt",
        )
    CheRTheme { AuthorItem(author) }
}
