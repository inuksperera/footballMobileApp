package com.example.footballapplication

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface LeagueDao {

    /**
     *  inserts a league into the League table and updates the fields of a record if a record is inserted that already exists
     * @param league instance of a league to be inserted into the database
     */
    @Upsert
    suspend fun upsertLeague(league: League)

}