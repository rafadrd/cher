package pt.isel.cher.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import pt.isel.cher.ui.navigation.AppNavigation
import pt.isel.cher.ui.theme.CheRTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val tag: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(tag, "onCreate")
        enableEdgeToEdge()
        setContent { CheRTheme { AppNavigation(onFinish = { finish() }) } }
    }

    override fun onStart() {
        super.onStart()
        Log.v(tag, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.v(tag, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.v(tag, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.v(tag, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(tag, "onDestroy")
    }
}
