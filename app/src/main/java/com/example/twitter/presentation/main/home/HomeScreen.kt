package com.example.twitter.presentation.main.home

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.twitter.R
import com.example.twitter.domain.model.ChatViewModel
import com.example.twitter.domain.model.provideChatList
import com.example.twitter.presentation.bottomNavi.BottomNavigation
import com.example.twitter.presentation.nav.Routes
import com.example.twitter.presentation.viewModal.BaseViewModel
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")


@Composable
fun HomeScreen(navController: NavHostController, baseViewModel: BaseViewModel) {
    var showPopup by remember { mutableStateOf(false) }
    val chatList = baseViewModel.chatList.collectAsState()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    if (userId != null) {
        LaunchedEffect(userId) {
            baseViewModel.getChatForUser(userId)
        }
    }

    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    val getChatList = provideChatList()
    val context = LocalContext.current
    var isCameraPermissionGranted by remember { mutableStateOf(false) }
    var isCameraScreenVisible by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) } // Store captured image URI

    val permissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                isCameraPermissionGranted = true
                isCameraScreenVisible = true
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        })

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showPopup = true },
                containerColor = Color(0xFF00AF5A),
                modifier = Modifier
                    .size(56.dp)
                    .padding(bottom = 16.dp),
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "New Chat",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        bottomBar = {
            BottomNavigation(navController, selectedItem = 0, onClick = { index ->
                when (index) {
                    0 -> {
                        navController.navigate(Routes.HomeScreen)
                    }

                    1 -> {
                        navController.navigate(Routes.UpdateScreen)
                    }

                    2 -> {
                        navController.navigate(Routes.Communities)
                    }

                    3 -> {
                        navController.navigate(Routes.CallScreen)
                    }
                }
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF00AF5A))
                    .padding(vertical = 12.dp)
            ) {
                if (!isSearching) {
                    Text(
                        text = "WhatsApp",
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.camera),
                            contentDescription = "Camera",
                            tint = Color.White,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    permissionRequest.launch(android.Manifest.permission.CAMERA)
                                }
                        )
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { isSearching = !isSearching }
                        )
                        Box {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { isExpanded = true }
                            )
                            DropdownMenu(
                                expanded = isExpanded,
                                onDismissRequest = { isExpanded = false },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("New Group") },
                                    onClick = { isExpanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("New Broadcast") },
                                    onClick = { isExpanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Settings") },
                                    onClick = {
                                        isExpanded = false
                                        navController.navigate(Routes.SettingsScreen)
                                    }
                                )
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { isSearching = false }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            trailingIcon = {Icon(imageVector = Icons.Default.Clear, contentDescription = "")},
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Search...", color = Color.Gray) },
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White, RoundedCornerShape(24.dp)),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            ),
                            shape = RoundedCornerShape(24.dp),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(colorResource(R.color.black))
                        )
                    }
                }
            }
            if (showPopup) {
                AddUser(
                    onDismiss = {showPopup = false },
                    onUserAdd = {},
                    baseViewModel
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF7F7F7)),
                content = {
                    items(getChatList) { item ->
                        ChatDesign(item, baseViewModel, navController)
                    }
                }
            )
        }
    }
}

@Composable
fun AddUser(
    onDismiss: () -> Unit,
    onUserAdd: (ChatViewModel) -> Unit,
    baseViewModel: BaseViewModel
) {
    var phoneNumber by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var userFound by remember { mutableStateOf<ChatViewModel?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Enter phone number") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Black
            )
        )
        Row {
            Button(
                onClick = { isSearching = true  },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.lightGreen))
            ) {
                Text(text = "Search")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onDismiss() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.lightGreen))
            ) {
                Text(text = "Cancel")
            }
        }
    }
}



