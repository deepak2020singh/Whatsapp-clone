package com.example.twitter.presentation.main.communities

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.twitter.R
import com.example.twitter.presentation.bottomNavi.BottomNavigation
import com.example.twitter.presentation.nav.Routes


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Communities(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomNavigation(navController, selectedItem = 2, onClick = { index ->
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
        },
    ) { innerPadding ->
        CommunitiesUi(innerPadding)
    }
}

@Composable
fun CommunitiesUi(innerPadding: PaddingValues) {
    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    var isExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF00AF5A))
                .padding(vertical = 12.dp)
        ) {
            if (!isSearching) {
                Text(
                    text = "Communities",
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
                            .size(24.dp) // Standard icon size
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
                                .clickable { isExpanded = true }
                        )
                        DropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false },
                            modifier = Modifier
                                .background(Color.White)
                                .align(Alignment.TopEnd)
                        ) {
                            DropdownMenuItem(
                                text = { Text("New Group", color = Color.Black) },
                                onClick = { isExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("New Broadcast", color = Color.Black) },
                                onClick = { isExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Settings", color = Color.Black) },
                                onClick = {
                                    isExpanded = false
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

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = "https://www.kasandbox.org/programming-images/avatars/spunky-sam.png",
                contentDescription = "",
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Button(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)
                .fillMaxWidth(),
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.lightGreen),
                contentColor = colorResource(R.color.white)
            )
        ) {
            Text(
                text = "Start new Communities",
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Your Communities",
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp,
            color = Color.Black,
            modifier = Modifier.padding(start = 12.dp)
        )
        Spacer(modifier = Modifier.height(22.dp))
        CommunityUser()
        Spacer(modifier = Modifier.height(12.dp))
        CommunityUser()
        Spacer(modifier = Modifier.height(12.dp))
        CommunityUser()
    }
}


@Composable
fun CommunityUser(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "",
            modifier = Modifier
                .size(45.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "Jonny Babu",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(text = "@Jonny Babu", fontSize = 15.sp, color = Color.Black.copy(0.7f))
        }
    }
}