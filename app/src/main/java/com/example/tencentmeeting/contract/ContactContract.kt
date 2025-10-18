package com.example.tencentmeeting.contract

import com.example.tencentmeeting.model.User

interface ContactContract {
    
    interface View {
        fun showContacts(contacts: List<User>)
        fun showSearchResults(results: List<User>)
        fun showEmptySearchResult()
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun showInviteSuccess(contactName: String)
    }
    
    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadContacts()
        fun searchContacts(query: String)
        fun inviteContact(contact: User)
        fun onDestroy()
    }
}