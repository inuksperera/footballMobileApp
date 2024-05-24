package com.example.footballapplication

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The database has two entities that are the two tables of classes League and Team
 */
@Database(
    entities = [League::class, Team::class],
    version = 1
)
abstract class FootballDatabase: RoomDatabase() {


    //dao for leagues
    abstract fun getLeagueDao(): LeagueDao

    //dao for teams
    abstract fun getTeamDao(): TeamDao
}