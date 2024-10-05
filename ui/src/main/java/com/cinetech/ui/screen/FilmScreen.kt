package com.cinetech.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FilmScreen(){
    Scaffold(
        topBar = { AppBar() }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text("FilmScreen")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(){
    TopAppBar(
        title = {},
        navigationIcon = { Icons.AutoMirrored.Outlined.KeyboardArrowLeft }
    )
}