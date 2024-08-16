package org.justme.trackerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.justme.trackerapp.ui.forms.CalendarForm
import org.justme.trackerapp.ui.theme.TrackerAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TrackerAppTheme {
                CalendarForm().DisplayMonth()
            }
        }
    }
}