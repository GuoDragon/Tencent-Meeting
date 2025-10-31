package com.example.tencentmeeting.data

import android.content.Context
import com.example.tencentmeeting.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class DataRepository(private val context: Context) {
    private val gson = Gson()

    // 内存中的会议列表（用于临时保存新创建的会议）
    private val inMemoryMeetings = mutableListOf<Meeting>()

    // 内存中的消息列表（用于临时保存新发送的消息）
    private val inMemoryMessages = mutableListOf<Message>()

    // 内存中的邀请列表（用于临时保存新发送的邀请）
    private val inMemoryInvitations = mutableListOf<MeetingInvitation>()

    // 内存中的个人会议室信息（用于临时保存个人会议室设置）
    private var inMemoryPersonalMeetingRooms = mutableMapOf<String, PersonalMeetingRoom>()

    fun getUsers(): List<User> {
        return try {
            val jsonString = readJsonFromAssets("data/users.json")
            val type = object : TypeToken<List<User>>() {}.type
            gson.fromJson(jsonString, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getMeetings(): List<Meeting> {
        return try {
            val jsonString = readJsonFromAssets("data/meetings.json")
            val type = object : TypeToken<List<Meeting>>() {}.type
            val jsonMeetings: List<Meeting> = gson.fromJson(jsonString, type)
            // 合并JSON文件中的会议和内存中的会议
            jsonMeetings + inMemoryMeetings
        } catch (e: Exception) {
            inMemoryMeetings.toList()
        }
    }

    /**
     * 保存会议到内存
     * 注意：这只会保存到内存中，不会写入JSON文件
     */
    fun saveMeeting(meeting: Meeting) {
        inMemoryMeetings.add(meeting)
    }

    fun getMeetingParticipants(): List<MeetingParticipant> {
        return try {
            val jsonString = readJsonFromAssets("data/meeting_participants.json")
            val type = object : TypeToken<List<MeetingParticipant>>() {}.type
            gson.fromJson(jsonString, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getMessages(): List<Message> {
        return try {
            val jsonString = readJsonFromAssets("data/messages.json")
            val type = object : TypeToken<List<Message>>() {}.type
            val jsonMessages: List<Message> = gson.fromJson(jsonString, type)
            // 合并JSON文件中的消息和内存中的消息
            jsonMessages + inMemoryMessages
        } catch (e: Exception) {
            inMemoryMessages.toList()
        }
    }

    /**
     * 根据会议ID获取该会议的所有消息
     * @param meetingId 会议ID
     * @return 该会议的消息列表，按时间戳排序
     */
    fun getMessagesByMeetingId(meetingId: String): List<Message> {
        return getMessages()
            .filter { it.meetingId == meetingId }
            .sortedBy { it.timestamp }
    }

    /**
     * 添加新消息到内存
     * 注意：这只会保存到内存中，不会写入JSON文件
     * @param message 要添加的消息
     */
    fun addMessage(message: Message) {
        inMemoryMessages.add(message)
    }

    fun getHandRaiseRecords(): List<HandRaiseRecord> {
        return try {
            val jsonString = readJsonFromAssets("data/hand_raise_records.json")
            val type = object : TypeToken<List<HandRaiseRecord>>() {}.type
            gson.fromJson(jsonString, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * 获取所有会议邀请
     */
    fun getMeetingInvitations(): List<MeetingInvitation> {
        return try {
            val jsonString = readJsonFromAssets("data/meeting_invitations.json")
            val type = object : TypeToken<List<MeetingInvitation>>() {}.type
            val jsonInvitations: List<MeetingInvitation> = gson.fromJson(jsonString, type)
            // 合并JSON文件中的邀请和内存中的邀请
            jsonInvitations + inMemoryInvitations
        } catch (e: Exception) {
            inMemoryInvitations.toList()
        }
    }

    /**
     * 根据会议ID获取该会议的所有邀请
     */
    fun getInvitationsByMeetingId(meetingId: String): List<MeetingInvitation> {
        return getMeetingInvitations()
            .filter { it.meetingId == meetingId }
    }

    /**
     * 获取可以邀请的用户（未在会议中的用户）
     * @param meetingId 会议ID
     * @param currentUserId 当前用户ID（主持人ID，不显示在可邀请列表中）
     */
    fun getAvailableUsersToInvite(meetingId: String, currentUserId: String): List<User> {
        val allUsers = getUsers()
        val participants = getMeetingParticipants().filter { it.meetingId == meetingId }
        val participantIds = participants.map { it.userId }
        val invitations = getInvitationsByMeetingId(meetingId)
        val invitedUserIds = invitations.map { it.inviteeId }

        return allUsers.filter { user ->
            // 排除当前用户（主持人）、已在会议中的用户、已被邀请的用户
            user.userId != currentUserId &&
            !participantIds.contains(user.userId) &&
            !invitedUserIds.contains(user.userId)
        }
    }

    /**
     * 添加新的会议邀请到内存
     * @param invitation 要添加的邀请
     */
    fun addInvitation(invitation: MeetingInvitation) {
        inMemoryInvitations.add(invitation)
    }

    /**
     * 获取个人会议室信息
     * @param userId 用户ID
     * @return 个人会议室信息，如果不存在则返回null
     */
    fun getPersonalMeetingRoom(userId: String): PersonalMeetingRoom? {
        // 先检查内存中是否有
        inMemoryPersonalMeetingRooms[userId]?.let { return it }

        // 从JSON文件加载
        return try {
            val jsonString = readJsonFromAssets("data/personal_meeting_rooms.json")
            val type = object : TypeToken<List<PersonalMeetingRoom>>() {}.type
            val rooms: List<PersonalMeetingRoom> = gson.fromJson(jsonString, type)
            val room = rooms.find { it.userId == userId }
            // 缓存到内存
            room?.let { inMemoryPersonalMeetingRooms[userId] = it }
            room
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 保存个人会议室信息到内存
     * 注意：这只会保存到内存中，不会写入JSON文件
     * @param roomInfo 个人会议室信息
     */
    fun savePersonalMeetingRoom(roomInfo: PersonalMeetingRoom) {
        inMemoryPersonalMeetingRooms[roomInfo.userId] = roomInfo
    }

    private fun readJsonFromAssets(fileName: String): String {
        return try {
            context.assets.open(fileName).use { inputStream ->
                inputStream.bufferedReader().use { it.readText() }
            }
        } catch (ex: IOException) {
            ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(context: Context): DataRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}