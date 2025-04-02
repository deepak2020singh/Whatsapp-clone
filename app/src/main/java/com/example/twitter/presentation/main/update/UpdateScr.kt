package com.example.twitter.presentation.main.update

import android.graphics.BitmapFactory
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.Bitmap
import coil3.compose.AsyncImage
import com.example.twitter.R
import com.example.twitter.presentation.bottomNavi.BottomNavigation
import com.example.twitter.presentation.main.userProfile.base64ToBitmap
import com.example.twitter.presentation.nav.Routes
import com.example.twitter.presentation.viewModal.AuthViewModal


@Composable
fun UpdateScr(navController: NavHostController, authViewModal: AuthViewModal = hiltViewModel()) {
    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf<String>("") }
    var bitMapImage by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        val preferences = authViewModal.getUserPreferences()
        name = preferences?.name ?: ""
        about = preferences?.about ?: ""
        imageUrl = preferences?.profileImage ?: ""
    }

    bitMapImage = authViewModal.base64ToBitmap(imageUrl)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = colorResource(R.color.darkGreen),
                contentColor = colorResource(R.color.white),
            ) {
                Text(text = "CM")
            }
        },
        bottomBar = {
            BottomNavigation(navController, selectedItem = 1, onClick = { index ->
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
                        text = "Updates",
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 16.dp),
                        fontWeight = FontWeight.Medium
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
                                .clickable {}
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
                                    .clickable { expanded = true }
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .background(Color.White)
                                    .align(Alignment.TopEnd)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("New Group") },
                                    onClick = { expanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("New Broadcast") },
                                    onClick = { expanded = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Settings") },
                                    onClick = {
                                        expanded = false
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
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Search...", color = Color.Gray) },
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White, RoundedCornerShape(24.dp)),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black
                            ),
                            shape = RoundedCornerShape(24.dp),
                            singleLine = true
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn {
                item {
                    Text(
                        text = "Status",
                        fontSize = 18.sp,
                        style = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .padding(start = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box() {

                            if (bitMapImage != null) {
                                Image(
                                    bitmap = bitMapImage!!.asImageBitmap(),
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(50.dp).clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )

                            } else {
                                AsyncImage(
                                    model = "https://farm3.staticflickr.com/2220/1572613671_7311098b76_z_d.jpg",
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop

                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "",
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .align(Alignment.BottomEnd)
                            )
                        }
                        Spacer(modifier = Modifier.width(23.dp))
                        Column {
                            Text(
                                text = name,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold, fontSize = 15.sp
                                )
                            )
                            Text(
                                text = "Tap to add status update",
                                fontSize = 12.sp,
                                color = Color.Black.copy(0.5f)
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
                items(provideStatusList()) { status ->
                    StatusComp(status)
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "Channels",
                        fontWeight = FontWeight.Bold,
                        fontSize = 23.sp,
                        modifier = Modifier.padding(start = 12.dp),
                        color = Color.Black
                    )
                    Text(
                        text = "Stay updated on WhatsApp news and events. Find Channels to",
                        modifier = Modifier.padding(start = 12.dp),
                        color = Color.Black
                    )
                    Text(
                        text = "Follow below",
                        modifier = Modifier.padding(start = 12.dp),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    Text(
                        text = "Find Channels to follow below",
                        modifier = Modifier.padding(start = 12.dp),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                }
                items(provideStatusList1()) { channel ->
                    ChannelsComp(channel)
                }
                item{
                    Button(
                        modifier = Modifier.padding(start = 12.dp),
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(colorResource(R.color.black))
                    ) {
                        Text(text = "Explore", color = Color.White)
                    }
                }
            }
        }
    }
}


@Composable
fun ChannelsComp(channel: Status) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, bottom = 12.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = channel.image,
            contentDescription = "",
            modifier = Modifier.size(50.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(25.dp))
        Column {
            Text(
                text = channel.name,
                style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black)
            )
            Text(text = channel.time, color = Color.Gray)
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.lightGreen),
                contentColor = colorResource(R.color.white)
            )
        ) {
            Text(text = "Follow")
        }
    }
}

@Composable
fun StatusComp(status: Status) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, bottom = 12.dp)
    ) {
        AsyncImage(
            model = status.image,
            contentDescription = "",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(25.dp))
        Column {
            Text(text = status.name, color = Color.Black, fontWeight = FontWeight.Bold)
            Text(text = status.time, color = Color.Gray)
            Text(text = status.statusText, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

data class Status(
    val image: String,
    val name: String,
    val time: String,
    val statusText: String
)

fun provideStatusList(): List<Status> {
    return listOf(
        Status(
            image = "https://farm7.staticflickr.com/6089/6115759179_86316c08ff_z_d.jpg",
            name = "Prem",
            time = "12:00",
            statusText = "Feeling Happy!"
        ),
        Status(
            image = "https://farm4.staticflickr.com/3752/9684880330_9b4698f7cb_z_d.jpg",
            name = "Lashay chauhan",
            time = "12:30",
            statusText = "Busy with work"
        ),
        Status(
            image = "https://farm4.staticflickr.com/3075/3168662394_7d7103de7d_z_d.jpg",
            name = "Deepa",
            time = "1:00",
            statusText = "Out for a walk!"
        ),
        Status(
            image = "https://farm3.staticflickr.com/2220/1572613671_7311098b76_z_d.jpg",
            name = "Gourav sachar",
            time = "12:00",
            statusText = "Bullet Raja"
        )
    )
}

fun provideStatusList1(): List<Status> {
    return listOf(
        Status(
            image = "https://farm7.staticflickr.com/6089/6115759179_86316c08ff_z_d.jpg",
            name = "Prem",
            time = "12:00",
            statusText = "Feeling Happy!"
        ),
        Status(
            image = "https://farm4.staticflickr.com/3752/9684880330_9b4698f7cb_z_d.jpg",
            name = "Samson",
            time = "12:30",
            statusText = "Busy with work"
        ),
        Status(
            image = "https://farm4.staticflickr.com/3075/3168662394_7d7103de7d_z_d.jpg",
            name = "Rabbit",
            time = "1:00",
            statusText = "Out for a walk!"
        )
    )
}