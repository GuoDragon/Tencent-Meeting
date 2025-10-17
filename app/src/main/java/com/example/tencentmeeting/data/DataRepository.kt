package com.example.tencentmeeting.data

import android.content.Context
import com.example.tencentmeeting.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class DataRepository(private val context: Context) {
    private val gson = Gson()

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
            gson.fromJson(jsonString, type)
        } catch (e: Exception) {
            emptyList()
        }
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