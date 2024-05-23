package com.example.bsuirjournal2.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity//(tableName = "subjects_states")
data class SubjectState(
    @ColumnInfo(name = "monday") val monday: String = "clearPainter",
    val tuesday: String = "clearPainter",
    val wednesday: String = "clearPainter",
    val thursday: String = "clearPainter",
    val friday: String = "clearPainter",
    val saturday: String = "clearPainter",
    val sunday: String = "clearPainter",
    val rollNo: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
