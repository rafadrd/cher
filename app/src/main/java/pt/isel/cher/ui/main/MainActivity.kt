package pt.isel.cher.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pt.isel.cher.ui.common.BaseActivity
import pt.isel.cher.ui.navigation.AppNavigation
import pt.isel.cher.ui.theme.CheRTheme

class MainActivity : BaseActivity() {
    override val tag: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { CheRTheme { AppNavigation(onFinish = { finish() }) } }
    }
}
