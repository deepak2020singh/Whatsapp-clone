package com.example.twitter.presentation.auth

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.twitter.R
import com.example.twitter.presentation.nav.Routes


@Composable
fun WelcomeScreen(navController: NavHostController, innerPadding: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome to WhatsApp",
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Main Content Column
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Image
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "WhatsApp Logo",
                modifier = Modifier
                    .size(230.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(25.dp))
            Row(
                modifier = Modifier.padding(top = 32.dp)
            ) {
                Text(text = "Read our")
                Text(
                    text = " Privacy Policy",
                    color = Color.Green,
                    modifier = Modifier.clickable { })
                Text(text = " Tap Agree to Continue to")
            }
            Row {
                Text(text = "accept to condition")
                Text(
                    text = " Terms of Service",
                    color = Color.Green,
                    modifier = Modifier.clickable { })
            }
            Spacer(modifier = Modifier.height(22.dp))

            // Continue Button
            Button(
                onClick = {
                    navController.navigate(Routes.AuthScreen) {
                        popUpTo(Routes.WelcomeScreen) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.lightGreen),
                    contentColor = colorResource(R.color.white)
                ),
            ) {
                Text(text = "Tap to Continue", fontSize = 27.sp)
            }
        }

        // Footer Section
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "From", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.meta),
                    contentDescription = "",
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Meta")
            }
        }
    }
}
