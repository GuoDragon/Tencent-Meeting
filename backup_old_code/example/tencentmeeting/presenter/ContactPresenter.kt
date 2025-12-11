package com.example.tencentmeeting.presenter

import com.example.tencentmeeting.contract.ContactContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactPresenter(
    private val dataRepository: DataRepository
) : ContactContract.Presenter {
    
    private var view: ContactContract.View? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + Job())
    private var allContacts: List<User> = emptyList()
    
    override fun attachView(view: ContactContract.View) {
        this.view = view
    }
    
    override fun detachView() {
        this.view = null
    }
    
    override fun loadContacts() {
        presenterScope.launch {
            try {
                view?.showLoading()
                val contacts = withContext(Dispatchers.IO) {
                    dataRepository.getUsers()
                        .filter { it.userId != "user001" } // 过滤当前用户"刘承龙"
                }

                allContacts = contacts
                view?.showContacts(contacts)
            } catch (e: Exception) {
                view?.showError("加载联系人失败: ${e.message}")
            } finally {
                view?.hideLoading()
            }
        }
    }
    
    override fun searchContacts(query: String) {
        if (query.isBlank()) {
            view?.showContacts(allContacts)
            return
        }
        
        val filteredContacts = allContacts.filter { contact ->
            contact.username.contains(query, ignoreCase = true) ||
            contact.phone?.contains(query) == true
        }
        
        if (filteredContacts.isNotEmpty()) {
            view?.showSearchResults(filteredContacts)
        } else {
            view?.showEmptySearchResult()
        }
    }
    
    override fun inviteContact(contact: User) {
        view?.showInviteSuccess(contact.username)
    }
    
    override fun onDestroy() {
        detachView()
    }
}