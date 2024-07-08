package com.example.contacts.presentation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.contacts.database.ContactDatabase
import com.example.contacts.repository.ContactRepository
import com.example.contacts.ui.ContactViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            name = "contact_database"
        ).build()
        val repository = ContactRepository(database.ContactDao())
        val viewModel: ContactViewModel by viewModels {
            ContactViewModel.ContactViewModelFactory(
                repository
            )
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "contactListScreen") {
                composable("contactListScreen") {
                    ContactListScreen(viewModel, navController)
                }
                composable("addContact") {
                    AddContact(viewModel, navController)
                }
                composable("contactDetail/{contactId}") { backStackEntry ->
                    val contentId = backStackEntry.arguments?.getString("contactId")?.toInt()
                    val contact =
                        viewModel.allContacts.observeAsState(initial = emptyList()).value.find { it.id == contentId }
                    contact?.let {
                        ContactDetailScreen(
                            it, viewModel, navController
                        )
                    }
                }
                composable("editContact/{contactId}") { backStackEntry ->
                    val contentId = backStackEntry.arguments?.getString("contactId")?.toInt()
                    val contact =
                        viewModel.allContacts.observeAsState(initial = emptyList()).value.find { it.id == contentId }
                    contact?.let {
                        EditContactScreen(
                            it, viewModel, navController
                        )
                    }
                }
            }

        }
    }
}

