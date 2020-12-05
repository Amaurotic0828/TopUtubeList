package com.example.toplist.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class TopList (var id: String? = "", var rank: String? = "", var name: String? = "", var link: String? = "", var reason: String? = ""){
    override fun toString(): String {
        return "$name with the link of $link"
    }


}

