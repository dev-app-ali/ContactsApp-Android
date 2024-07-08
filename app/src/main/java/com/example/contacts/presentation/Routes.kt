package com.example.contacts.presentation

sealed class Routes(val route: String) {
    object ContactList : Routes("contactList")
    object AddContact : Routes("addContact")
    object DetailContact : Routes("addContact")

}