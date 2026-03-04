package com.example.practice_mobilki.ui.screens

import HomeWithBottomNavigation
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope.weight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoestore.R
import com.example.shoestore.ui.components.AccentButton
import com.example.shoestore.ui.components.CustomAlertDialog
import com.example.shoestore.ui.components.CustomTextField
import com.example.shoestore.ui.theme.CustomColors
import com.example.shoestore.ui.theme.TypographyApplication
import com.example.shoestore.ui.viewmodel.ProfileViewModel
import kotlin.text.isNotEmpty

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onProfile: () -> Unit = {},
    onHome: () -> Unit = {},
    onFavorite: () -> Unit = {},
    onNotification: () -> Unit = {},
    onCart: () -> Unit = {},
    onOpenCamera: () -> Unit = {},
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Используем remember для хранения состояний, основанных на uiState
    var name by remember(uiState.firstName) { mutableStateOf(uiState.firstName) }
    var surname by remember(uiState.lastname) { mutableStateOf(uiState.lastname) }
    var phoneNumber by remember(uiState.phone) { mutableStateOf(uiState.phone) }
    var address by remember(uiState.address) { mutableStateOf(uiState.address) }
    var isEdit by remember { mutableStateOf(false) }
    val profileImage by viewModel.profileImage

    val sharedPreferences = remember {
        context.getSharedPreferences("my_app_preferences", Context.MODE_PRIVATE)
    }

    val userId = sharedPreferences.getString("userId", null)

    // Используем LaunchedEffect для загрузки профиля при инициализации
    LaunchedEffect(key1 = userId) {
        if (userId != null) {
            Log.d("ProfileScreen", "Loading profile for userId: $userId")
            viewModel.loadProfile(userId)
        } else {
            Log.e("ProfileScreen", "userId is null")
            viewModel.showMessage("Не найден ID пользователя")
        }
    }

    // Обновляем локальные состояния при изменении uiState
    LaunchedEffect(uiState.firstName) {
        name = uiState.firstName
    }

    LaunchedEffect(uiState.lastname) {
        surname = uiState.lastname
    }

    LaunchedEffect(uiState.phone) {
        phoneNumber = uiState.phone
    }

    LaunchedEffect(uiState.address) {
        address = uiState.address
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(20.dp))
            Image(
                painter = painterResource(R.drawable.clock_1),
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                alpha = 1f
            )
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.profile),
                    style = TypographyApplication.headingSemiBold16
                )
            }
            if (!isEdit) {
                Box(
                    modifier = modifier
                        .size(36.dp)
                        .background(
                            color = CustomColors.accent,
                            shape = CircleShape
                        )
                        .clickable { isEdit = true },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit),
                        contentDescription = null,
                        tint = CustomColors.block,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                Box(
                    modifier = modifier
                        .size(36.dp)
                        .background(
                            color = Color.Transparent,
                            shape = CircleShape
                        )
                ) {}
            }
            Spacer(modifier = Modifier.width(20.dp))
        }
        Spacer(modifier = Modifier.weight(0.15f))
        Box(
            modifier = modifier
                .size(96.dp)
                .background(
                    color = CustomColors.subTextLight,
                    shape = CircleShape
                )
                .clickable {},
            contentAlignment = Alignment.Center
        ) {
            val currentImage = profileImage
            if (currentImage != null) {
                Image(
                    bitmap = currentImage.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.photo_camera),
                    contentDescription = null,
                    tint = CustomColors.hint,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "$name $surname", style = TypographyApplication.bodyRegular20)
        if (isEdit) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.clickable { onOpenCamera() },
                text = stringResource(R.string.change_profile_picture),
                color = CustomColors.accent,
                style = TypographyApplication.bodyRegular12
            )
        } else {
            Spacer(modifier = Modifier.height(35.dp))
            Box(modifier = Modifier.fillMaxWidth().height(65.dp)) {
                Image(
                    painter = painterResource(R.drawable.loyal_card_code),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = stringResource(R.string.your_name),
                fontSize = 16.sp,
                style = TypographyApplication.bodyMedium16
            )
            Spacer(modifier = Modifier.height(17.dp))
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = { name = it },
                placeholderText = "Emmanuel",
                isEnabled = isEdit
            )
        }
        Spacer(modifier = Modifier.height(17.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = stringResource(R.string.last_name),
                fontSize = 16.sp,
                style = TypographyApplication.bodyMedium16
            )
            Spacer(modifier = Modifier.height(17.dp))
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = surname,
                onValueChange = { surname = it },
                placeholderText = "Oyiboke",
                isEnabled = isEdit
            )
        }
        Spacer(modifier = Modifier.height(17.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = stringResource(R.string.address),
                fontSize = 16.sp,
                style = TypographyApplication.bodyMedium16
            )
            Spacer(modifier = Modifier.height(17.dp))
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = address,
                onValueChange = { address = it },
                placeholderText = "Nigeria",
                isEnabled = isEdit
            )
        }
        Spacer(modifier = Modifier.height(17.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = stringResource(R.string.phone_number),
                fontSize = 16.sp,
                style = TypographyApplication.bodyMedium16
            )
            Spacer(modifier = Modifier.height(17.dp))
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                placeholderText = "+234 811-732-5298",
                isEnabled = isEdit
            )
        }
        Spacer(modifier = Modifier.height(17.dp))
        Spacer(modifier = Modifier.weight(1f))
        if (isEdit) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.width(20.dp))
                AccentButton(modifier = Modifier.fillMaxWidth().height(50.dp), onClick = {
                    if (userId != null && name.isNotEmpty() && surname.isNotEmpty()) {
                        viewModel.upsertProfile(
                            userId = userId,
                            firstname = name,
                            lastname = surname,
                            address = address,
                            phone = phoneNumber,
                            context = context
                        )
                        isEdit = false
                    } else {
                        viewModel.showMessage("Заполните обязательные поля")
                    }
                }, text = stringResource(R.string.save_npw))
                Spacer(modifier = Modifier.width(20.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CustomColors.background),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading...",
                        style = TypographyApplication.bodyMedium16,
                        color = CustomColors.text
                    )
                }
            }
            return
        }
        HomeWithBottomNavigation(
            4,
            onHome = onHome,
            onFavorite = onFavorite,
            onNotification = onNotification,
            onCart = onCart
        )
        CustomAlertDialog(
            show = viewModel.showDialog.value,
            onDismiss = { viewModel.hideDialog() },
            text = viewModel.dialogText.value,
            title = viewModel.dialogTitle.value
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    ProfileScreen(viewModel = ProfileViewModel())
}