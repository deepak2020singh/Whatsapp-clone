package com.example.twitter.presentation.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.twitter.R
import com.example.twitter.domain.model.provideChatList

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    chatId: String
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isMoreExpanded by remember { mutableStateOf(false) }

    val getChatList = provideChatList()
    val chat = getChatList.find { it.userId == chatId }
    val chatUserName = chat?.name ?: "No Name"
    val chatImage = chat?.image

    val messages = listOf(
        Message("Hello! How are you?", "me", "10:30 AM"),
        Message("I'm good, thanks for asking!", "other", "10:31 AM"),
        Message("What are you up to?", "me", "10:32 AM"),
        Message("Just working on some projects. How about you?", "other", "10:33 AM"),
        Message("Same here! Let's catch up soon.", "me", "10:34 AM")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 42.dp)
            .background(Color(0xFFF7F7F7))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF00AF5A))
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left section (Back + Profile)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.navigateUp() }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    AsyncImage(
                        model = chatImage,
                        contentDescription = "User Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = chatUserName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Text(
                            text = "last seen 10:25 AM",
                            fontSize = 12.sp,
                            color = Color(0xFFB0BEC5)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.videocall),
                        contentDescription = "Video Call",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Voice Call",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )

                    Box {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.White,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { isExpanded = !isExpanded }
                        )
                        // First Dropdown Menu
                        DropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Mute Notification") },
                                onClick = { isExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Wallpaper") },
                                onClick = { isExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("More") },
                                onClick = {
                                    isExpanded = false
                                    isMoreExpanded = !isMoreExpanded
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Right Arrow"
                                    )
                                }
                            )
                        }
                        // Second Dropdown Menu (Nested)
                        DropdownMenu(
                            expanded = isMoreExpanded,
                            onDismissRequest = { isMoreExpanded = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("Report") },
                                onClick = { isMoreExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Block") },
                                onClick = { isMoreExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Hide") },
                                onClick = { isMoreExpanded = false }
                            )
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
        }
        MessageInputField()
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MessageInputField() {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .background(
                    Color(0xFFF0F0F0),
                    RoundedCornerShape(24.dp)
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(R.drawable.smile), // Changed to Mood for emoji
                    contentDescription = "Emoji",
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .size(24.dp)
                )
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Type a message") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                Icon(
                    painter = painterResource(R.drawable.attach_file),
                    contentDescription = "Attach",
                    tint = Color.Gray,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFF075E54), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    val isMe = message.sender == "me"
    val backgroundColor = if (isMe) Color(0xFFDCF8C6) else Color.White // Fixed Gray to White
    val cornerRadius = 8.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    backgroundColor,
                    shape = RoundedCornerShape(
                        topStart = cornerRadius,
                        topEnd = cornerRadius,
                        bottomStart = if (isMe) cornerRadius else 0.dp,
                        bottomEnd = if (isMe) 0.dp else cornerRadius
                    )
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    color = Color.Black,
                    fontSize = 16.sp
                )
                Text(
                    text = message.timestamp,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

data class Message(
    val text: String,
    val sender: String,
    val timestamp: String
)