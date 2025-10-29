package pt.isel.cher.ui.common

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseActivity : ComponentActivity() {
    protected abstract val tag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(tag, "${this::class.simpleName}.onCreate")
        enableEdgeToEdge()
    }

    override fun onStart() {
        super.onStart()
        Log.v(tag, "${this::class.simpleName}.onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.v(tag, "${this::class.simpleName}.onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.v(tag, "${this::class.simpleName}.onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.v(tag, "${this::class.simpleName}.onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(tag, "${this::class.simpleName}.onDestroy")
    }
}
