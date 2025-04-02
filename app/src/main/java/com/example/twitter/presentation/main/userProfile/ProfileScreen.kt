package com.example.twitter.presentation.main.userProfile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.twitter.presentation.viewModal.AuthState
import com.example.twitter.presentation.viewModal.AuthViewModal
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    innerPadding: PaddingValues,
    navController: NavHostController,
    phoneAuthViewModal: AuthViewModal = hiltViewModel()
) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    val fetchUserProfile by phoneAuthViewModal.authState.collectAsState(initial = AuthState.Idle)

    var about by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }

    var nameFetched by remember { mutableStateOf("") }
    var fetchedProfilePictureBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var imageUrl by remember { mutableStateOf<Uri?>(null) }
    var bitMapImage by remember { mutableStateOf<Bitmap?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUrl = uri
            uri?.let {
                bitMapImage = if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
            }
        }
    )

    LaunchedEffect(user) {
        user?.uid?.let {
            phoneAuthViewModal.fetchUserProfile(it)
        }
    }

    // Handle fetched user profile
    when (fetchUserProfile) {
        AuthState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF00AF5A))
            }
        }
        is AuthState.Success -> {
            val user = (fetchUserProfile as AuthState.Success).user
            nameFetched = user.name
            about = user.about
            link = user.link

            // Convert Base64 string to Bitmap
            user.profilePictureUrl?.let { base64String ->
                fetchedProfilePictureBitmap = base64ToBitmap(base64String)
            }
        }
        is AuthState.Error -> {
            val errorMessage = (fetchUserProfile as AuthState.Error).message
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        else -> {
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        color = Color.White,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF075E54)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 12.dp, end = 12.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Profile Picture Section
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0))
                    .clickable { imageLauncher.launch("image/*") }
            ) {
                when {
                    bitMapImage != null -> {
                        // Display newly selected image
                        Image(
                            bitmap = bitMapImage!!.asImageBitmap(),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    fetchedProfilePictureBitmap != null -> {
                        // Display fetched image from Firestore
                        Image(
                            bitmap = fetchedProfilePictureBitmap!!.asImageBitmap(),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        Text(
                            text = "No Image",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile Picture",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(36.dp)
                        .background(Color(0xFF00AF5A), CircleShape)
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Name Section
            ProfileSection(
                title = "Name",
                content = nameFetched,
                onSave = { nameFetched = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            // About Section
            ProfileSection(
                title = "About",
                content = about,
                onSave = { about = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Link Section
            ProfileSection(
                title = "Link",
                content = link,
                onSave = { link = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = {
                    user?.uid?.let { userId ->
                        phoneAuthViewModal.saveUserProfile(
                            userId = userId,
                            name = nameFetched,
                            about = about,
                            link = link,
                            phoneNumber = user.phoneNumber ?: "",
                            profilePictureUrl = bitMapImage
                        )
                    }
                    Toast.makeText(context, "Profile saved", Toast.LENGTH_SHORT).show()
                },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00AF5A),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .width(120.dp)
                    .height(48.dp)
            ) {
                Text("Save", fontSize = 16.sp)
            }
        }
    }
}


 fun base64ToBitmap(base64Image: String): Bitmap? {
    return try {
        val decodedByte = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
    } catch (e: Exception) {
        null
    }
}


@Composable
fun ProfileSection(
    title: String,
    content: String,
    onSave: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(content) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit $title",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { isEditing = true }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (isEditing) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.DarkGray,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp, color = Color.Black)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        onSave(text)
                        isEditing = false
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Save")
                }
            } else {
                Text(
                    text = content,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
        }
    }
}




