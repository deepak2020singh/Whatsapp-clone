package com.example.twitter.presentation.main.callScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.twitter.R
import com.example.twitter.domain.model.ChatUI
import com.example.twitter.domain.model.provideChatList
import com.example.twitter.presentation.bottomNavi.BottomNavigation
import com.example.twitter.presentation.nav.Routes


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CallScreen(navController: NavHostController, innerPadding: PaddingValues) {
    Scaffold(
        bottomBar = {
            BottomNavigation(navController, selectedItem = 3, onClick = { index ->
                when (index) {
                    0 -> {navController.navigate(Routes.HomeScreen) }
                    1 -> { navController.navigate(Routes.UpdateScreen) }
                    2 -> { navController.navigate(Routes.Communities) }
                    3 -> { navController.navigate(Routes.CallScreen) }
                }
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(color = colorResource(R.color.white))
        ) {
            val getChatList = provideChatList()
            TopBar()
            Spacer(modifier = Modifier.padding(top = 10.dp))
            TopBarUi(getChatList)
        }
    }
}

@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF00AF5A))
            .padding(vertical = 12.dp, horizontal = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Calls",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Row {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Video Call",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {},
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone Call",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {},
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun TopBarUi(getChatList: List<ChatUI>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp)
    ) {
        Text(
            text = "Favourites",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        LazyRow {
            items(getChatList) {
                RowCall(it)
            }
        }

        Button(
            onClick = {},
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.lightGreen),
                contentColor = colorResource(R.color.white)
            )
        ) {
            Text(text = "Start a new call", fontSize = 23.sp, color = Color.Black)
        }

        LazyColumn(
            modifier = Modifier.padding(top = 12.dp)
        ) {
            items(getChatList) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 14.dp)
                ) {
                    AsyncImage(
                        model = it.image,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(45.dp)
                            .clip(CircleShape),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = it.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black
                    )

                }
            }
        }
    }
}


@Composable
fun RowCall(userCall: ChatUI) {
    Column(
        modifier = Modifier
            .width(70.dp)
            .height(93.dp)
            .clickable { }
            .padding(6.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = userCall.image,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(60.dp)
                .border(2.dp, color = colorResource(R.color.lightGreen), CircleShape)
                .padding(4.dp)
                .clip(CircleShape)

        )
        Text(
            text = userCall.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black
        )
    }
}