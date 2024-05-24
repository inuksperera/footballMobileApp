package com.example.footballapplication

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * League data class entity to create an instance of a league
 *
 * @param id unique id for a league used as the primary key
 * @param strLeague league name
 * @param strSport sport name
 * @param strLeagueAlternate alternate league name
 * @return an instance of a league
 */
@Entity
data class League(
    @PrimaryKey(autoGenerate = false)
    var id: Int? = null,
    var strLeague: String?,
    var strSport: String?,
    var strLeagueAlternate: String?,

)