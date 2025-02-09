package pt.isel.cher.ui.about

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.cher.ui.common.BaseActivity
import pt.isel.cher.ui.theme.CheRTheme

class AboutActivity : BaseActivity() {
    override val tag: String = "AboutActivity"

    private val viewModel: AboutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CheRTheme {
                AboutView(
                    authors = viewModel.authors,
                    onSendEmail = { viewModel.sendEmail(this, it) },
                    onNavigateBack = { finish() },
                )
            }
        }
    }
}
