package com.example.footballapplication

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface TeamDao {

    /**
     *  inserts a team into the Team table and updates the fields of a record if a record is inserted that already exists
     * @param team instance of a team to be inserted into the database
     */
    @Upsert
    suspend fun upsertTeam(team: Team)


    /**
     * function to get all clubs that contain the input text as a substring in the club name or a club's league name
     * @param inputText String value of text entered in the textfield
     */
    //reference for using variables in queries: https://stackoverflow.com/questions/47893918/room-using-variable-in-query
    @Query("SELECT strTeam FROM Team WHERE (UPPER(strTeam) LIKE UPPER(:inputText)) OR (UPPER(strLeague) LIKE UPPER(:inputText)) ORDER BY strTeam ASC")
    suspend fun getLeagues(inputText: String): List<String>

    /**
     * function to get the team logo image link for a team where the team name entered matches the team name of a team in the table
     * @param teamName String value of a team's name
     */
    @Query("SELECT strTeamLogo FROM Team WHERE strTeam = :teamName")
    suspend fun getImageLink(teamName: String): String


}