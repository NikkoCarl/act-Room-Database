
package com.example.room_database_discussion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.room_database_discussion.ui.StudentScreen
import com.example.room_database_discussion.ui.theme.Room_database_discussionTheme
import com.example.room_database_discussion.viewmodel.StudentViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[StudentViewModel::class.java]

        setContent {
            Room_database_discussionTheme {
                StudentScreen(viewModel)
            }
        }
    }
}
