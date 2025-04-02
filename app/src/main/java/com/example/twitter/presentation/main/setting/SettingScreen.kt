package com.example.twitter.presentation.main.setting

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.Bitmap
import coil3.compose.AsyncImage
import com.example.twitter.R
import com.example.twitter.presentation.nav.Routes
import com.example.twitter.presentation.viewModal.AuthViewModal

@Composable
fun SettingScreen(
    innerPadding: PaddingValues,
    navController: NavHostController,
    authViewModel: AuthViewModal = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var bitmapImage by remember { mutableStateOf<Bitmap?>(null) }


    LaunchedEffect(Unit) {
        val user = authViewModel.getUserPreferences()
        name = user?.name ?: ""
        imageUrl = user?.profileImage ?: ""
    }

    bitmapImage = authViewModel.base64ToBitmap(imageUrl)



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0xFFF7F7F7))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF00AF5A))
                .padding(vertical = 12.dp, horizontal = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.navigateUp() }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = colorResource(R.color.white)
                )
            }
        }

        // Profile Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
                .clickable { navController.navigate(Routes.ProfileScreen) },
            verticalAlignment = Alignment.CenterVertically
        ) {


            if (bitmapImage != null) {
                Image(
                    bitmap = bitmapImage!!.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }else {

                AsyncImage(
                    model = "https://farm4.staticflickr.com/3075/3168662394_7d7103de7d_z_d.jpg",
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "The time is start with camera",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "QR Code",
                tint = Color(0xFF00AF5A),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Settings List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            items(provideSettingList()) { item ->
                SettingItem(item)
                HorizontalDivider(
                    color = Color(0xFFE0E0E0),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun SettingItem(setting: SettingData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { /* Add item click action */ },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = setting.icon), // Fixed this line
            contentDescription = setting.title,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp) // Standard WhatsApp icon size
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = setting.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
            Text(
                text = setting.description,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

fun provideSettingList(): List<SettingData> {
    return listOf(
        SettingData(
            icon = R.drawable.key, // Corrected resource IDs
            title = "Account",
            description = "Privacy, security, change number"
        ),
        SettingData(
            icon = R.drawable.padlock,
            title = "Privacy",
            description = "Last seen, profile photo, about"
        ),
        SettingData(
            icon = R.drawable.chat,
            title = "Chats",
            description = "Theme, wallpapers, chat history"
        ),
        SettingData(
            icon = R.drawable.chart,
            title = "Notifications",
            description = "Message, group & call tones"
        ),
        SettingData(
            icon = R.drawable.chart,
            title = "Storage and data",
            description = "Network usage, auto-download"
        ),
        SettingData(
            icon = R.drawable.question_mark,
            title = "Help",
            description = "FAQ, contact us, privacy policy"
        )
    )
}

data class SettingData(
    val icon: Int,
    val title: String,
    val description: String
)
