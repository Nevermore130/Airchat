package com.example.dengluzhuce

import android.net.Uri

interface PhotoSelectListener {
    fun onSelected(uriList:MutableList<Uri>,pathList:MutableList<String>)
}