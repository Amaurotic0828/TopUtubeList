package com.example.toplist.handlers

import com.example.toplist.models.TopList
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TopListHandler {
    var database: FirebaseDatabase
    var toplistRef : DatabaseReference

    init{
        database = FirebaseDatabase.getInstance()
        toplistRef = database.getReference("toplists")
    }

    fun create(toplist: TopList): Boolean {
        val id = toplistRef.push().key
        toplist.id = id

        toplistRef.child(id!!).setValue(toplist)
        return true
    }
    fun update(toplist: TopList):Boolean{
        toplistRef.child(toplist.id!!).setValue(toplist)
        return true
    }
    fun delete(toplist: TopList): Boolean{
        toplistRef.child(toplist.id!!).removeValue()
        return true
    }
}