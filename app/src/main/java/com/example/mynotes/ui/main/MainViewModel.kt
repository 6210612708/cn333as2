package com.example.mynotes.ui.main

import android.content.ContentValues
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mynotes.models.Noted

class MainViewModel(val sharedPreferences: SharedPreferences) : ViewModel() {

    lateinit var list: Noted

    lateinit var onListAdded: () -> Unit
    lateinit var onListRemoved: () -> Unit
    var whereRemoved: Int = -1

    val lists: MutableList<Noted> by lazy {
        retrieveLists()
    }

    private fun retrieveLists(): MutableList<Noted> {

        val sharedPreferencesContents = sharedPreferences.all
        val noteLists = ArrayList<Noted>()

        for (noteList in sharedPreferencesContents) {

            val note = Noted(noteList.key, noteList.value as String)
            noteLists.add(note)
        }

        return noteLists
    }

    fun create(list: Noted) {
        val editor = sharedPreferences.edit()
        val text: String = list.content
        editor.putString(list.name, text)
        editor.apply()
        lists.add(list)
        onListAdded.invoke()
    }

    fun saveNoted(list: Noted) {
        val editor = sharedPreferences.edit()
        val text: String = list.content
        editor.putString(list.name, text)
        editor.apply()
        Log.d(ContentValues.TAG, list.content)
    }

    fun updateNoted(list: Noted) {
        val editor = sharedPreferences.edit()
        val text: String = list.content
        editor.putString(list.name, text)
        editor.apply()
        Log.d(ContentValues.TAG, list.content)
        refreshNoted()
    }
    fun removeNoted(list: Noted){
        val index = lists.indexOf(list)
        whereRemoved = index
        lists.remove(list)
        onListRemoved.invoke()

        val editor = sharedPreferences.edit()
        editor.remove(list.name)
        editor.apply()
    }

    fun findNoted(key: String): Boolean{
        return sharedPreferences.contains(key)
    }

    private fun refreshNoted() {
        lists.clear()
        lists.addAll(retrieveLists())
    }

}
