package com.example.twitter.presentation.main.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.twitter.R
import com.example.twitter.domain.model.ChatUI
import com.example.twitter.presentation.nav.Routes
import com.example.twitter.presentation.viewModal.BaseViewModel


@Composable
fun ChatDesign(data: ChatUI, baseViewModel: BaseViewModel, navController: NavHostController) {
    val profileImg = data?.image
    val bitmap = remember {
        profileImg?.let { baseViewModel.bitmapToString(it) }
    }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable { navController.navigate(Routes.ChatScreen(chatId = data.userId)) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = data.image,
            contentDescription = "",
            modifier = Modifier
                .size(50.dp)
                .border(2.dp, color = colorResource(R.color.darkGreen), shape = CircleShape)
                .padding(4.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.size(10.dp))
        Column {
            Text(
                text = data.name, fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black)
            )
            Text(text = data.message, color = colorResource(R.color.black).copy(0.7f))
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = data.time, color = colorResource(R.color.black))

    }
}