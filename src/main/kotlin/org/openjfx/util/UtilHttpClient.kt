package org.openjfx.util

import okhttp3.OkHttpClient

class UtilHttpClient {
    companion object {
        val httpClientInstance: OkHttpClient = OkHttpClient()
    }
}