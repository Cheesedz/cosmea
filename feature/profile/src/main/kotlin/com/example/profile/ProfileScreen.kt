package com.example.profile

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.designsystem.component.Background
import com.example.designsystem.icon.Icons
import com.example.designsystem.theme.CosmeaTheme
import com.example.model.ProfileData

@Composable
internal fun ProfileRoute(
    onLogoutClick: () -> Unit,
    onClickProfile: () -> Unit,
    onClickAccount: () -> Unit
) {
    ProfileScreen(onLogoutClick, onClickProfile, onClickAccount)
}

@Composable
fun ProfileScreen(onLogoutClick: () -> Unit, onClickProfile: () -> Unit, onClickAccount: () -> Unit) {
    val context = LocalContext.current

    Background {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val bitmap = remember { mutableStateOf<Bitmap?>(null) }
            val avatarResource = remember { mutableStateOf(R.drawable.avatar_default) }

            val coroutineScope = rememberCoroutineScope()
            val sharedPref = context.getSharedPreferences("CosmeaApp", Context.MODE_PRIVATE)
            val idUser: String? = sharedPref.getString("currentUserId", null)
            var userData by remember { mutableStateOf<ProfileData?>(null) }

            LaunchedEffect(idUser) {
                if (idUser != null) {
                    userData = getCurrentProfileData(idUser, coroutineScope)
                }
            }
            bitmap.value?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Color.Transparent)
                        .border(
                            width = 1.dp,
                            color = Color.White,
                            shape = CircleShape
                        )
                )
            }
            if (bitmap.value == null) {
                if (userData?.avatar == null) {
                    Image(
                        painter = painterResource(id = avatarResource.value),
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = CircleShape
                            ),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(userData?.avatar),
                        contentDescription = "User Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = userData?.displayName.toString(),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(32.dp, 0.dp, 16.dp, 0.dp)
//                    .clickable {
//                        darkTheme = !darkTheme
//                        context.setThemePreference(darkTheme)
//                    },
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.DarkMode,
//                    contentDescription = "Theme Icon"
//                )
//                Text("Dark Mode")
//                Switch(
//                    checked = darkTheme,
//                    onCheckedChange = {
//                        darkTheme = it
//                        context.setThemePreference(darkTheme)
//                    }
//                )
//            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, 0.dp, 16.dp, 0.dp)
                    .clickable {
                        onClickProfile()
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Person,
                    contentDescription = "Edit Profile Icon"
                )
                Text("Edit Profile")
                Icon(
                    imageVector = Icons.ArrowRight,
                    contentDescription = "Arrow Right"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

           Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp, 0.dp, 16.dp, 0.dp)
                    .clickable(onClick = onClickAccount),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Settings,
                    contentDescription = "Account Management Icon"
                )
                Text("Account Management")
                Icon(
                    imageVector = Icons.ArrowRight,
                    contentDescription = "Arrow Right"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onLogoutClick()
                    logout(context) },
            ) {
                Text("Logout")
            }
        }
    }
}

fun logout(context: Context) {
    val sharedPref = context.getSharedPreferences("CosmeaApp", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString("currentUserId", null)
        putString("currentUserName", null)
        apply()
    }
}

@Composable
fun AboutMeCard(userData : ProfileData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "About Me", style = MaterialTheme.typography.titleMedium)
            // Replace with actual About Me text
            Text(text = userData.bio.toString(), style = MaterialTheme.typography.bodyLarge)
            Friends()
        }
    }
}

@Composable
fun Friends() {
    // Replace with actual implementation
}

@Preview
@Composable
fun ProfileScreenPreview() {
    CosmeaTheme(darkTheme = false) {
        ProfileScreen({}, {}, {})
    }
}

@Preview
@Composable
fun AboutMeCardPreview() {
    CosmeaTheme(darkTheme = false) {
        AboutMeCard(ProfileData("John Doe", "This is a sample bio", "https://www.example.com/avatar.jpg", "1", "1"))
    }
}

@Preview
@Composable
fun FriendsPreview() {
    CosmeaTheme(darkTheme = false) {
        Friends()
    }
}
