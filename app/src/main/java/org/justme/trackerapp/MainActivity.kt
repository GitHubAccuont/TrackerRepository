package org.justme.trackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import org.justme.trackerapp.ui.forms.CalendarForm
import org.justme.trackerapp.ui.theme.TrackerAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        TrackerApp()
        setContent {
            TrackerAppTheme {
                CalendarForm().DisplayMonth()
            }
        }
    }
}