package com.example.twitter.presentation.bottomNavi

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.twitter.R

@Composable
fun BottomNavigation(
    navController: NavHostController,
    selectedItem: Int,
    onClick: (index: Int) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val items = listOf(
        NavigationItem(
            selectedIcon = Icons.Outlined.Home,
            unselectedIcon = Icons.Filled.Home,
            level = "Home"
        ),
        NavigationItem(
            selectedIcon = Icons.Outlined.Star,
            unselectedIcon = Icons.Filled.Star,
            level = "Update"
        ),
        NavigationItem(
            selectedIcon = Icons.Outlined.AccountCircle,
            unselectedIcon = Icons.Filled.AccountCircle,
            level = "Communities"
        ),
        NavigationItem(
            selectedIcon = Icons.Outlined.Call,
            unselectedIcon = Icons.Filled.Call,
            level = "Calls"
        )
    )


    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(80.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                icon = {
                    Icon(
                        imageVector = if (index == selectedItem) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.level,
                        tint = if (index == selectedItem) {
                            colorResource(id = R.color.black)
                        } else {
                            colorResource(id = R.color.lightGreen)
                           }
                    )
                },
                label = {
                    Text(
                        text = item.level,
                        color = if (index == selectedItem) {
                            colorResource(id = R.color.black)
                        } else {
                            colorResource(id = R.color.lightGreen)
                        }
                    )
                },
                onClick = { onClick.invoke(index) }

            )

        }
    }
}

data class NavigationItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val level: String
)