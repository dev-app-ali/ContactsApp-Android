package com.example.contacts.presentation
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.contacts.database.Contact
import com.example.contacts.ui.ContactViewModel
import com.example.contacts.ui.theme.GreenJC

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ContactDetailScreen(
    contact: Contact,
    viewModel: ContactViewModel,
    navController: NavController
) {
    val context = LocalContext.current.applicationContext
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Contact", fontSize = 18.sp, modifier = Modifier.clickable { })
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("editContact/${contact.id}")
                    }) {
                        Image(
                            Icons.Default.Edit,
                            contentDescription = null, colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenJC,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(contact.image),
                contentDescription = null,
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            ContactDetailItem(label = "Name", value = contact.name)
            Spacer(modifier = Modifier.height(16.dp))
            ContactDetailItem(label = "Phone Number", value = contact.phoneNumber)
            Spacer(modifier = Modifier.height(16.dp))
            ContactDetailItem(label = "Email", value = contact.email)

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                colors = ButtonDefaults.buttonColors(GreenJC),
                onClick = {
                    showDialog = true
                }
            ) {
                Text(text = "Delete", fontWeight = FontWeight.Bold, color = Color.White)
            }

            if (showDialog) {
                AlertDialog(

                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Confirm Delete") },
                    text = { Text(text = "Are you sure you want to delete this contact?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.deleteContact(contact)
                                navController.navigate("contactListScreen") {
                                    popUpTo("contactListScreen") { inclusive = true }
                                }

                            },
                            colors = ButtonDefaults.buttonColors(GreenJC)

                        ) {
                            Text(text = "Delete")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false },

                            colors = ButtonDefaults.buttonColors(Color.Gray)
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ContactDetailItem(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Normal)
    }
}
