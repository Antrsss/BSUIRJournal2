package com.example.bsuirjournal2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.bsuirjournal2.roomdatabase.SubjectStateDatabase
import com.example.bsuirjournal2.ui.screens.BSUIRJournalApp
import com.example.bsuirjournal2.ui.theme.BSUIRJournal2Theme
import com.example.bsuirjournal2.viewmodels.SubjectStateViewModel

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            SubjectStateDatabase::class.java,
            "subjectState.db"
        ).build()
    }
    private val subjectStateViewModel by viewModels<SubjectStateViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SubjectStateViewModel(db.dao) as T
                }
            }
        }
    )

    //val editor = sharedPreferences.edit()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("localData", Context.MODE_PRIVATE)

        Log.d("MyUi", "Created")
        setContent {

            BSUIRJournal2Theme {

                val state by subjectStateViewModel.state.collectAsState()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BSUIRJournalApp(
                        state = state,
                        onEvent = subjectStateViewModel::onEvent,
                        sharedPreferences = sharedPreferences
                    )
                }
            }
        }
    }
}