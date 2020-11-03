package com.example.rssrt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import com.example.rssrt.model.RssRepository
import com.example.rssrt.model.database.getDatabase
import kotlinx.coroutines.*

const val SHARED_PREFERENCES_NAME = "com.example.rssrt"
const val PREF_KEY_DATABASE_SETUP = "db_setup"

class MainActivity : AppCompatActivity() {

    private val scope = MainScope()

    private var isDatabaseSetup
        get() = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE).getBoolean(
            PREF_KEY_DATABASE_SETUP, false
        )
        set(newValue) {
            if (newValue && !isDatabaseSetup) {
                getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE).edit {
                    putBoolean(PREF_KEY_DATABASE_SETUP, true)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isDatabaseSetup) {
            setupDatabase()
            isDatabaseSetup = true
        }
    }

    private fun setupDatabase() {
        val repository = RssRepository(getDatabase(applicationContext))
        scope.launch(Dispatchers.IO) {
            repository.initialPopulate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        scope.cancel()
    }
}
