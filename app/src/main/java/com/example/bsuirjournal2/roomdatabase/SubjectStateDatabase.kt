package com.example.bsuirjournal2.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bsuirjournal2.roomdatabase.SubjectSateDao
import com.example.bsuirjournal2.roomdatabase.SubjectState

@Database(
    entities = [SubjectState::class],
    version = 1
)
abstract class SubjectStateDatabase: RoomDatabase() {

    abstract val dao: SubjectSateDao

}