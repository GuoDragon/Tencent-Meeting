package com.example.tencent_meeting_sim.common.utils

import com.example.tencent_meeting_sim.data.model.User
import java.text.Collator
import java.util.Locale

object UserSortUtils {
    fun sortUsers(users: List<User>): List<User> {
        val collator = Collator.getInstance(Locale.CHINA)
        return users.sortedWith(Comparator { left, right ->
            val nameResult = collator.compare(left.username, right.username)
            if (nameResult != 0) {
                nameResult
            } else {
                left.userId.compareTo(right.userId)
            }
        })
    }
}
