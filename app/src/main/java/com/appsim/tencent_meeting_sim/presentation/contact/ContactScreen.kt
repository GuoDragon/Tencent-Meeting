package com.appsim.tencent_meeting_sim.presentation.contact

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.presentation.contact.ContactContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.User
import com.appsim.tencent_meeting_sim.presentation.contact.ContactPresenter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(onNavigateToAddFriends: () -> Unit = {}, onNavigateToFriendDetails: (User) -> Unit = {}) {
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
            override fun showContacts(contactList: List<User>) { contacts = contactList; showEmptyState = false }
            override fun showSearchResults(results: List<User>) { contacts = results; showEmptyState = false }
            override fun showEmptySearchResult() { contacts = emptyList(); showEmptyState = true }
            override fun showLoading() { isLoading = true }
            override fun hideLoading() { isLoading = false }
            override fun showError(message: String) { errorMessage = message }
            override fun showInviteSuccess(contactName: String) { inviteMessage = context.getString(R.string.msg_invited_contact, contactName) }
        }
    }

    LaunchedEffect(Unit) { presenter.attachView(view); presenter.loadContacts() }
    LaunchedEffect(searchQuery) { presenter.searchContacts(searchQuery) }
    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

    Column(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFE3F2FD), Color(0xFFF5F5F5))))) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.contact_title), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black) },
            actions = {
                IconButton(onClick = onNavigateToAddFriends) {
                    Icon(imageVector = Icons.Default.PersonAdd, contentDescription = stringResource(R.string.contact_add), tint = Color(0xFF1976D2))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE3F2FD))
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            placeholder = { Text(text = stringResource(R.string.contact_search), color = Color.Gray) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = stringResource(R.string.icon_desc_clear), tint = Color.Gray)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1976D2),
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                cursorColor = Color(0xFF1976D2),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        if (contacts.isNotEmpty()) {
            Text(text = stringResource(R.string.contact_my_contacts), fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Gray, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        }

        Box(modifier = Modifier.weight(1f)) {
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF1976D2))
                    }
                }
                showEmptyState -> {
                    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Icon(imageVector = if (searchQuery.isNotEmpty()) Icons.Default.SearchOff else Icons.Default.People, contentDescription = if (searchQuery.isNotEmpty()) stringResource(R.string.msg_empty_search_result) else stringResource(R.string.msg_empty_contacts), tint = Color.Gray, modifier = Modifier.size(80.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = if (searchQuery.isNotEmpty()) stringResource(R.string.msg_empty_search_result) else stringResource(R.string.msg_empty_contacts), fontSize = 16.sp, color = Color.Gray)
                    }
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 16.dp)) {
                        items(contacts) { contact ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onNavigateToFriendDetails(contact) },
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFF1976D2)), contentAlignment = Alignment.Center) {
                                        Text(text = contact.username.firstOrNull()?.toString() ?: "U", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = contact.username, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        contact.phone?.let { phone ->
                                            Text(text = phone, fontSize = 14.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = { if (contacts.isNotEmpty()) { inviteMessage = context.getString(R.string.msg_invited_all_contacts) } },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = stringResource(R.string.contact_invite), tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.contact_invite), fontSize = 16.sp, color = Color.White)
            }
        }
    }

    errorMessage?.let { message -> LaunchedEffect(message) { errorMessage = null } }

    inviteMessage?.let { message ->
        LaunchedEffect(message) { kotlinx.coroutines.delay(2000); inviteMessage = null }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Card(modifier = Modifier.padding(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))) {
                Text(text = message, color = Color.White, modifier = Modifier.padding(12.dp))
            }
        }
    }
}
