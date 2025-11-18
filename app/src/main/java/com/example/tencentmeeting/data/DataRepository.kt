package com.example.tencentmeeting.data

import android.content.Context
import com.example.tencentmeeting.model.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.IOException

class DataRepository(private val context: Context) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    // 内存中的会议列表（用于临时保存新创建的会议）
    private val inMemoryMeetings = mutableListOf<Meeting>()

    // 内存中的消息列表（用于临时保存新发送的消息）
    private val inMemoryMessages = mutableListOf<Message>()

    // 内存中的邀请列表（用于临时保存新发送的邀请）
    private val inMemoryInvitations = mutableListOf<MeetingInvitation>()

    // 内存中的个人会议室信息（用于临时保存个人会议室设置）
    private var inMemoryPersonalMeetingRooms = mutableMapOf<String, PersonalMeetingRoom>()

    // 内存中的举手记录列表
    private val inMemoryHandRaiseRecords = mutableListOf<HandRaiseRecord>()

    // 内存中的参会人列表
    private val inMemoryParticipants = mutableListOf<MeetingParticipant>()

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
            val jsonParticipants: List<MeetingParticipant> = gson.fromJson(jsonString, type)
            // 合并JSON文件中的参会人和内存中的参会人
            jsonParticipants + inMemoryParticipants
        } catch (e: Exception) {
            inMemoryParticipants.toList()
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
            val jsonRecords: List<HandRaiseRecord> = gson.fromJson(jsonString, type)
            // 合并JSON文件中的记录和内存中的记录
            jsonRecords + inMemoryHandRaiseRecords
        } catch (e: Exception) {
            inMemoryHandRaiseRecords.toList()
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

    // ==================== 文件写入方法 ====================

    /**
     * 初始化数据文件：首次启动时将assets中的JSON复制到filesDir
     */
    fun initializeDataFiles() {
        val fileNames = listOf(
            "users.json", "meetings.json", "meeting_participants.json",
            "messages.json", "hand_raise_records.json",
            "meeting_invitations.json", "personal_meeting_rooms.json"
        )
        fileNames.forEach { fileName ->
            val file = File(context.filesDir, fileName)
            if (!file.exists()) {
                try {
                    val assetsData = readJsonFromAssets("data/$fileName")
                    file.writeText(assetsData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 通用JSON写入方法：将数据序列化并写入filesDir
     */
    private fun writeJsonToFile(fileName: String, data: Any) {
        try {
            val file = File(context.filesDir, fileName)
            val json = gson.toJson(data)
            file.writeText(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 从filesDir读取JSON文件，如果不存在则从assets读取
     */
    private fun readJsonFromFilesDir(fileName: String, assetsPath: String): String {
        val file = File(context.filesDir, fileName)
        return if (file.exists()) {
            file.readText()
        } else {
            readJsonFromAssets(assetsPath)
        }
    }

    /**
     * 更新会议信息并保存到文件
     */
    fun updateMeeting(meetingId: String, updateFn: (Meeting) -> Meeting) {
        val index = inMemoryMeetings.indexOfFirst { it.meetingId == meetingId }
        if (index >= 0) {
            inMemoryMeetings[index] = updateFn(inMemoryMeetings[index])
        }
        saveMeetingsToFile()
    }

    /**
     * 保存会议列表到文件
     */
    fun saveMeetingsToFile() {
        val allMeetings = getMeetings()
        writeJsonToFile("meetings.json", allMeetings)
    }

    /**
     * 保存参会人列表到文件
     */
    fun saveMeetingParticipantsToFile(participants: List<MeetingParticipant>) {
        writeJsonToFile("meeting_participants.json", participants)
    }

    /**
     * 添加或更新参会人信息并保存到文件
     */
    fun addOrUpdateParticipant(participant: MeetingParticipant) {
        // 从内存中找
        val index = inMemoryParticipants.indexOfFirst {
            it.userId == participant.userId && it.meetingId == participant.meetingId
        }
        if (index >= 0) {
            inMemoryParticipants[index] = participant
        } else {
            inMemoryParticipants.add(participant)
        }

        // 合并所有数据并保存
        val allParticipants = getMeetingParticipants() + inMemoryParticipants
        // 去重
        val uniqueParticipants = allParticipants.distinctBy { "${it.userId}_${it.meetingId}" }
        saveMeetingParticipantsToFile(uniqueParticipants)
    }

    /**
     * 保存消息列表到文件
     */
    fun saveMessagesToFile() {
        val allMessages = getMessages()
        writeJsonToFile("messages.json", allMessages)
    }

    /**
     * 添加举手记录并保存到文件
     */
    fun addHandRaiseRecord(record: HandRaiseRecord) {
        inMemoryHandRaiseRecords.add(record)
        val allRecords = getHandRaiseRecords() + inMemoryHandRaiseRecords
        writeJsonToFile("hand_raise_records.json", allRecords)
    }

    /**
     * 更新举手记录的lowerTime（放下手）
     */
    fun updateHandRaiseLowerTime(recordId: String, lowerTime: Long) {
        val allRecords = (getHandRaiseRecords() + inMemoryHandRaiseRecords).toMutableList()
        val index = allRecords.indexOfFirst { it.recordId == recordId }
        if (index >= 0) {
            allRecords[index] = allRecords[index].copy(lowerTime = lowerTime)
            writeJsonToFile("hand_raise_records.json", allRecords)
        }
    }

    /**
     * 保存邀请列表到文件
     */
    fun saveInvitationsToFile() {
        val allInvitations = getMeetingInvitations()
        writeJsonToFile("meeting_invitations.json", allInvitations)
    }

    /**
     * 保存个人会议室列表到文件
     */
    fun savePersonalMeetingRoomsToFile() {
        val rooms = inMemoryPersonalMeetingRooms.values.toList()
        writeJsonToFile("personal_meeting_rooms.json", rooms)
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