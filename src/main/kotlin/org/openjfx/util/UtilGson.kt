package org.openjfx.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class UtilGson {
    companion object {
        val gsonInstance: Gson = GsonBuilder().create()
    }
}