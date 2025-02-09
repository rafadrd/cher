package pt.isel.cher.ui.about

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import pt.isel.cher.R
import pt.isel.cher.domain.Author
import pt.isel.cher.util.Constants.TAG

class AboutViewModel : ViewModel() {
    val authors =
        listOf(
            Author("47207", "Diogo", "Ribeiro", "a47207@alunos.isel.pt"),
            Author("49423", "Rafael", "Pegacho", "a49423@alunos.isel.pt"),
            Author("47236", "António", "Coelho", "a47236@alunos.isel.pt"),
        )

    fun sendEmail(
        context: Context,
        emails: List<String>,
    ) {
        val intent =
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, emails.toTypedArray())
                putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_body))
            }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, context.getString(R.string.no_email_clients), e)
            Toast
                .makeText(
                    context,
                    context.getString(R.string.no_email_client_found),
                    Toast.LENGTH_SHORT,
                ).show()
        }
    }
}
