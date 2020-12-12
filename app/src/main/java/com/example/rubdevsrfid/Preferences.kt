package com.example.rubdevsrfid

import android.content.Context
import android.content.SharedPreferences

class Preferences(context : Context) {
    val sp_api_url = "api_url"
    val sp_api_puerto = "api_port"


    val preference_puerto = context.getSharedPreferences(sp_api_puerto,Context.MODE_PRIVATE)
    val preference_url = context.getSharedPreferences(sp_api_url,Context.MODE_PRIVATE)

    fun get_api_url(url:SharedPreferences) : String? {
        return url.getString(sp_api_url,"")
    }

    fun set_api_url(api_url : String){
        val editor = preference_url.edit()
        editor.putString(sp_api_url,api_url)
        editor.apply()
    }

    fun get_api_puerto(puerto: SharedPreferences) : String? {
        return  puerto.getString(sp_api_puerto,"")
    }

    fun set_api_puerto(puerto : String) {
        val editor = preference_puerto.edit()
        editor.putString(sp_api_puerto,puerto)
        editor.apply()
    }
}