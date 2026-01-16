package com.appsim.tencent_meeting_sim.presentation.me

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.presentation.me.RecordContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.presentation.me.RecordPresenter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToPlaceholder: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { RecordPresenter(dataRepository) }

    var usedMB by remember { mutableStateOf(0) }
    var totalMB by remember { mutableStateOf(1024) }
    var fileCount by remember { mutableStateOf(0) }
    var showEmptyState by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(0) }

    val view = remember {
        object : RecordContract.View {
            override fun showStorageInfo(mb: Int, total: Int, count: Int) {
                usedMB = mb
                totalMB = total
                fileCount = count
            }

            override fun showEmptyState() {
                showEmptyState = true
            }

            override fun showError(message: String) {
                errorMessage = message
            }
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadStorageInfo()
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.me_recording),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.icon_desc_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToPlaceholder("搜索录制") }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = stringResource(R.string.icon_desc_search)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToPlaceholder("开始录制") },
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = stringResource(R.string.btn_start_recording),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // 存储信息栏
            StorageInfoSection(
                usedMB = usedMB,
                totalMB = totalMB,
                fileCount = fileCount,
                onNavigateToPlaceholder = onNavigateToPlaceholder
            )

            // Tab栏
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFF1976D2)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text(stringResource(R.string.label_all_files)) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text(stringResource(R.string.label_recent_viewed)) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text(stringResource(R.string.label_my_recordings)) }
                )
            }

            // 右侧过滤按钮
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = stringResource(R.string.icon_desc_filter),
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onNavigateToPlaceholder("过滤录制") }
                )
            }

            // 内容区域 - 空状态
            if (showEmptyState) {
                EmptyStateSection()
            }
        }
    }
}

@Composable
private fun StorageInfoSection(
    usedMB: Int,
    totalMB: Int,
    fileCount: Int,
    onNavigateToPlaceholder: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.label_storage_used, usedMB, totalMB / 1024, fileCount),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToPlaceholder("扩容存储") },
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.btn_expand),
                fontSize = 14.sp,
                color = Color(0xFF1976D2)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun EmptyStateSection() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 空状态图标
            Icon(
                imageVector = Icons.Default.FolderOpen,
                contentDescription = null,
                tint = Color(0xFFB0BEC5),
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.msg_no_recording_files),
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}
