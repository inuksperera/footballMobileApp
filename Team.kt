package com.example.footballapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * League data class entity to create an instance of a team/club
 *
 * @param idTeam unique id for team that serves as primary key
 * @param strTeam team name
 * @param strTeamShort short version of team name
 * @param strAlternate alternate team name
 * @param intFormedYear team formed year
 * @param strLeague league name of team
 * @param idLeague id of league
 * @param strStadium stadium name
 * @param strKeywords team keywords
 * @param strStadiumThumb thumbnail image link of stadium
 * @param strStadiumLocation location of stadium
 * @param intStadiumCapacity capacity of stadium
 * @param strWebsite link of team website
 * @param strTeamJersey link of team jersey image
 * @param strTeamLogo link of team logo image
 * @return an instance of a team
 */
@Entity
data class Team(
    @PrimaryKey(autoGenerate = false)
    var idTeam: Int? = null,
    var strTeam: String?,
    var strTeamShort: String?,
    var strAlternate: String?,
    var intFormedYear: Int?,
    var strLeague: String?,
    var idLeague: Int?,
    var strStadium: String?,
    var strKeywords: String?,
    var strStadiumThumb: String?,
    var strStadiumLocation: String?,
    var intStadiumCapacity: Int?,
    var strWebsite: String?,
    var strTeamJersey: String?,
    var strTeamLogo: String?
)
