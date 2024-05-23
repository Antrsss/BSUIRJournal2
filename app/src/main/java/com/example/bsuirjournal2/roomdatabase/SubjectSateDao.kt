package com.example.bsuirjournal2.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.bsuirjournal2.roomdatabase.SubjectState
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectSateDao {

    @Insert
    suspend fun insertSubjectState(subjectState: SubjectState)

    @Upsert
    suspend fun upsertSubjectState(subjectState: SubjectState)

    @Query("UPDATE subjectstate SET monday=:monday WHERE rollNo LIKE :roll")
    suspend fun updateMonday(monday : String, roll : Int)
    @Query("UPDATE subjectstate SET tuesday=:tuesday WHERE rollNo LIKE :roll")
    suspend fun updateTuesday(tuesday : String, roll : Int)
    @Query("UPDATE subjectstate SET wednesday=:wednesday WHERE rollNo LIKE :roll")
    suspend fun updateWednesday(wednesday : String, roll : Int)
    @Query("UPDATE subjectstate SET thursday=:thursday WHERE rollNo LIKE :roll")
    suspend fun updateThursday(thursday : String, roll : Int)
    @Query("UPDATE subjectstate SET friday=:friday WHERE rollNo LIKE :roll")
    suspend fun updateFriday(friday : String, roll : Int)
    @Query("UPDATE subjectstate SET saturday=:saturday WHERE rollNo LIKE :roll")
    suspend fun updateSaturday(saturday : String, roll : Int)
    @Query("UPDATE subjectstate SET sunday=:sunday WHERE rollNo LIKE :roll")
    suspend fun updateSunday(sunday : String, roll : Int)

    @Delete
    suspend fun deleteSubjectState(subjectState: SubjectState)

    @Query("SELECT * FROM subjectstate")
    fun getSubjectsStates() : Flow<List<SubjectState>>

    @Query("DELETE FROM subjectstate")
    suspend fun deleteAllSubjectsStates()
}