package com.camargo.todolist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class ToDo(
    var status: String,
    var title: String,
    var description: String) {

    var id: Int? = null
}