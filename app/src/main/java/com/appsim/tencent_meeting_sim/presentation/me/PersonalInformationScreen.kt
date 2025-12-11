package com.appsim.tencent_meeting_sim.presentation.me

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.presentation.me.PersonalInformationContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.User
import com.appsim.tencent_meeting_sim.presentation.me.PersonalInformationPresenter

@Composable
fun PersonalInformationScreen(
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { PersonalInformationPresenter(dataRepository) }

    var currentUser by remember { mutableStateOf<User?>(null) }
    var signature by remember { mutableStateOf(context.getString(R.string.user_default_signature)) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val view = remember {
        object : PersonalInformationContract.View {
            override fun showUserInfo(user: User) {
                currentUser = user
            }

            override fun showSignature(sig: String) {
                signature = sig
            }

            override fun showError(message: String) {
                errorMessage = message
            }
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadUserInfo("user001")
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部背景区域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFB0BEC5),
                            Color(0xFFCFD8DC)
                        )
                    )
                )
        ) {
            // 退出按钮（黑色箭头）
            IconButton(
                onClick = { onNavigateBack() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 8.dp, top = 40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.icon_desc_back),
                    tint = Color.Black
                )
            }

            // 点击设置背景图文字
            Text(
                text = stringResource(R.string.user_set_background),
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // 头像区域
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-60).dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 头像
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = stringResource(R.string.icon_desc_avatar),
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 信息卡片
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column {
                        // 名称行
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* 暂不处理 */ }
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.user_name),
                                fontSize = 16.sp,
                                color = Color.Black
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentUser?.username ?: "刘承龙",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Divider(color = Color(0xFFE0E0E0), thickness = 0.5.dp)

                        // 签名行
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /* 暂不处理 */ }
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.user_signature),
                                fontSize = 16.sp,
                                color = Color.Black
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = signature,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
