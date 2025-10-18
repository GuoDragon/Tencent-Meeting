package com.example.tencentmeeting.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencentmeeting.contract.ContactContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.User
import com.example.tencentmeeting.presenter.ContactPresenter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactPage() {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { ContactPresenter(dataRepository) }
    
    var contacts by remember { mutableStateOf<List<User>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showEmptyState by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var inviteMessage by remember { mutableStateOf<String?>(null) }
    
    val view = remember {
        object : ContactContract.View {
            override fun showContacts(contactList: List<User>) {
                contacts = contactList
                showEmptyState = false
            }
            
            override fun showSearchResults(results: List<User>) {
                contacts = results
                showEmptyState = false
            }
            
            override fun showEmptySearchResult() {
                contacts = emptyList()
                showEmptyState = true
            }
            
            override fun showLoading() {
                isLoading = true
            }
            
            override fun hideLoading() {
                isLoading = false
            }
            
            override fun showError(message: String) {
                errorMessage = message
            }
            
            override fun showInviteSuccess(contactName: String) {
                inviteMessage = "已邀请 $contactName 加入会议"
            }
        }
    }
    
    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadContacts()
    }
    
    LaunchedEffect(searchQuery) {
        presenter.searchContacts(searchQuery)
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
        // 顶部标题栏
        TopAppBar(
            title = {
                Text(
                    text = "通讯录",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            actions = {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "添加好友",
                        tint = Color(0xFF1976D2)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )
        
        // 搜索框
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        
        // 联系人标题
        if (contacts.isNotEmpty()) {
            Text(
                text = "我的联系人",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        
        // 联系人列表
        Box(
            modifier = Modifier.weight(1f)
        ) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF1976D2))
                    }
                }
                showEmptyState -> {
                    EmptyContactState(searchQuery = searchQuery)
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(contacts) { contact ->
                            ContactItem(
                                contact = contact,
                                onInviteClick = { presenter.inviteContact(contact) }
                            )
                        }
                    }
                }
            }
        }
        
        // 底部邀请按钮
        InviteContactButton(
            onClick = { 
                if (contacts.isNotEmpty()) {
                    inviteMessage = "已邀请所有联系人加入会议"
                }
            },
            modifier = Modifier.padding(16.dp)
        )
    }
    
    // 错误提示
    errorMessage?.let { message ->
        LaunchedEffect(message) {
            errorMessage = null
        }
    }
    
    // 邀请成功提示
    inviteMessage?.let { message ->
        LaunchedEffect(message) {
            kotlinx.coroutines.delay(2000)
            inviteMessage = null
        }
        
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(
                    text = message,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "搜索",
                color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "搜索",
                tint = Color.Gray
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "清除",
                        tint = Color.Gray
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1976D2),
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color(0xFF1976D2)
        ),
        modifier = modifier
    )
}

@Composable
private fun ContactItem(
    contact: User,
    onInviteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onInviteClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 头像
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1976D2)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.username.firstOrNull()?.toString() ?: "U",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // 联系人信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = contact.username,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                contact.phone?.let { phone ->
                    Text(
                        text = phone,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyContactState(searchQuery: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (searchQuery.isNotEmpty()) Icons.Default.SearchOff else Icons.Default.People,
            contentDescription = if (searchQuery.isNotEmpty()) "无搜索结果" else "暂无联系人",
            tint = Color.Gray,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (searchQuery.isNotEmpty()) "无搜索结果" else "暂无联系人",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun InviteContactButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1976D2)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = "邀请联系人",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "邀请联系人",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}