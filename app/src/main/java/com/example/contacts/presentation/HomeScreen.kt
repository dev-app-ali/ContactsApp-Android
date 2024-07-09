package com.example.contacts.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.contacts.R
import com.example.contacts.database.Contact
import com.example.contacts.ui.ContactViewModel
import com.example.contacts.ui.theme.GreenJC
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(viewModel: ContactViewModel, navController: NavController) {
    val context = LocalContext.current
    val contacts by viewModel.allContacts.observeAsState(initial = emptyList())
    var searchText by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Contacts")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("addContact") }) {
                        Image(
                            Icons.Default.Add,
                            contentDescription = null, colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { isSearching = !isSearching }) {
                        Image(
                            Icons.Default.Search,
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenJC,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (isSearching) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = {
                            Text("Search here....")
                        },
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),

                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = GreenJC,
                            unfocusedBorderColor = Color.Gray,

                        )

                        )

                }
                LazyColumn(modifier = Modifier.weight(1f)) {
                    val filteredContacts = contacts.filter {
                        it.name.contains(searchText, ignoreCase = true) ||
                                it.phoneNumber.contains(searchText, ignoreCase = true) ||
                                it.email.contains(searchText, ignoreCase = true)
                    }
                    items(filteredContacts) { contact ->
                        ContactItem(contact = contact) {
                            navController.navigate("contactDetail/${contact.id}")
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ContactItem(contact: Contact, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(contact.image),
                contentDescription = contact.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = contact.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.wrapContentHeight()
            )
        }
    }
}
