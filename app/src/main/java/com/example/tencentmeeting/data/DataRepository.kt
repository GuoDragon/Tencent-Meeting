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
            gson.fromJson(jsonString, type)
        } catch (e: Exception) {
            emptyList()
        }
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