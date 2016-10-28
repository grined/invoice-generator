package com.grined.toptal.invoice.toptal

import com.grined.toptal.invoice.PropertyHolder
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request

class ToptalAccessor {
    fun getInvoice(url : String) : String{
        val client = OkHttpClient()
        val credentials = Credentials.basic(
                PropertyHolder.getProperty("login"),
                PropertyHolder.getProperty("password"))
        val request = Request.Builder()
                .addHeader("Authorization", credentials)
                .url(url)
                .build()

        val response = client.newCall(request).execute()
        return response.body().string()
    }
}