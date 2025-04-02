package com.example.twitter.presentation.main.userProfile

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.twitter.presentation.nav.Routes
import com.example.twitter.presentation.viewModal.AuthViewModal
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetUserProfile(
    innerPadding: PaddingValues,
    navController: NavHostController,
    phoneAuthViewModal: AuthViewModal = hiltViewModel()
) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser


    val phoneNumber = user?.phoneNumber ?: ""

    var name by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }

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
            if (bitMapImage != null) {
                Image(
                    bitmap = bitMapImage!!.asImageBitmap(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = "No Image",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Text(text = phoneNumber)

        Spacer(modifier = Modifier.height(24.dp))

        // Name Section
        ProfileSection(
            title = "Name",
            content = name,
            onSave = { name = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // About Section
        ProfileSection(
            title = "About",
            content = about,
            onSave = { about = it }
        )
        Spacer(modifier = Modifier.height(32.dp))
        // Save Button
        Button(
            onClick = {
                if (bitMapImage != null) {
                    val encodedImage = phoneAuthViewModal.convertBitmapToBase64(bitMapImage!!)
                    phoneAuthViewModal.saveUserPreferences(
                        name = name,
                        about = about,
                        link = link,
                        profileImage = encodedImage
                    )
                    phoneAuthViewModal.saveUserProfile(
                        userId = user?.uid ?: "",
                        name = name,
                        about = about,
                        link = link,
                        phoneNumber = user?.phoneNumber ?: "",
                        profilePictureUrl = bitMapImage
                    )
                    navController.navigate(Routes.HomeScreen) {
                        popUpTo(Routes.ProfileScreen) {
                            inclusive = true
                        }
                    }
                    Toast.makeText(context, "Profile saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please select a profile picture", Toast.LENGTH_SHORT).show()
                }
            },
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00AF5A),
                contentColor = Color.White
            ),
            modifier = Modifier
                .width(120.dp)
                .height(48.dp),
        ) {
            Text("Save", fontSize = 16.sp)
        }

    }
}
