//DEMO VIDEO LINK: https://drive.google.com/drive/folders/147W8TNiTJSkyeAhTKHnoCRclEBr6Brf6?usp=sharing
/*
Name: Thotagamuwa Widanage Inuka Pasandul Perera
IIT id: 20221267
UOW id: w1954040
*/

package com.example.footballapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.footballapplication.ui.theme.FootballApplicationTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Array




class MainActivity : ComponentActivity() {

    lateinit var database: FootballDatabase
    lateinit var leagueDao: LeagueDao
    lateinit var jsonArray: JSONArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        database = FootballDatabase();

        //instance of database
        database =
            Room.databaseBuilder(this, FootballDatabase::class.java, "footballDatabase").build()
        //instance of leagueDao
        leagueDao = database.getLeagueDao()

        //hardcoded json string
        val jsonString = """
            {
           "leagues":[
                  {
                     "idLeague":"4328",
                     "strLeague":"English Premier League",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Premier League, EPL"
                  },
                  {
                     "idLeague":"4329",
                     "strLeague":"English League Championship",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Championship"
                  },
                  {
                     "idLeague":"4330",
                     "strLeague":"Scottish Premier League",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Scottish Premiership, SPFL"
                  },
                  {
                     "idLeague":"4331",
                     "strLeague":"German Bundesliga",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Bundesliga, Fußball-Bundesliga"
                  },
                  {
                     "idLeague":"4332",
                     "strLeague":"Italian Serie A",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Serie A"
                  },
                  {
                     "idLeague":"4334",
                     "strLeague":"French Ligue 1",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Ligue 1 Conforama"
                  },
                  {
                     "idLeague":"4335",
                     "strLeague":"Spanish La Liga",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"LaLiga Santander, La Liga"
                  },
                   {
                     "idLeague":"4336",
                     "strLeague":"Greek Superleague Greece",
                     "strSport":"Soccer",
                     "strLeagueAlternate":""
                  },
                  {
                     "idLeague":"4337",
                     "strLeague":"Dutch Eredivisie",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Eredivisie"
                  },
                  {
                     "idLeague":"4338",
                     "strLeague":"Belgian First Division A",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Jupiler Pro League"
                  },
                  {
                     "idLeague":"4339",
                     "strLeague":"Turkish Super Lig",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Super Lig"
                  },
                  {
                     "idLeague":"4340",
                     "strLeague":"Danish Superliga",
                     "strSport":"Soccer",
                     "strLeagueAlternate":""
                  },
                  {
                     "idLeague":"4344",
                     "strLeague":"Portuguese Primeira Liga",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Liga NOS"
                  },
                  {
                     "idLeague":"4346",
                     "strLeague":"American Major League Soccer",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"MLS, Major League Soccer"
                  },
                  {
                     "idLeague":"4347",
                     "strLeague":"Swedish Allsvenskan",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Fotbollsallsvenskan"
                  },
                  {
                     "idLeague":"4350",
                     "strLeague":"Mexican Primera League",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Liga MX"
                  },
                  {
                     "idLeague":"4351",
                     "strLeague":"Brazilian Serie A",
                     "strSport":"Soccer",
                     "strLeagueAlternate":""
                  },
                  {
                     "idLeague":"4354",
                     "strLeague":"Ukrainian Premier League",
                     "strSport":"Soccer",
                     "strLeagueAlternate":""
                  },
                  {
                     "idLeague":"4355",
                     "strLeague":"Russian Football Premier League",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Чемпионат России по футболу"
                  },
                  {
                     "idLeague":"4356",
                     "strLeague":"Australian A-League",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"A-League"
                  },
                  {
                     "idLeague":"4358",
                     "strLeague":"Norwegian Eliteserien",
                     "strSport":"Soccer",
                     "strLeagueAlternate":"Eliteserien"
                  },
                  {
                     "idLeague":"4359",
                     "strLeague":"Chinese Super League",
                     "strSport":"Soccer",
                     "strLeagueAlternate":""
                  }
              ]
          }
        """.trimIndent()

        //reference for getting array of json objects: https://youtu.be/5lNQLR53UtY
        val jsonObject = JSONObject(jsonString)
        jsonArray = jsonObject.getJSONArray("leagues")
        Log.i(TAG, "json array: " + jsonArray.get(0))
//        var searchforClubsFromWebIntent = Intent(this@MainActivity, SearchforClubsFromWebActivity::class.java)
//        startActivity(searchforClubsFromWebIntent)
        setContent {
            FootballApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    HomeScreen()
                }
            }
        }
    }


    /**
     * function to add leagues to the database. turns the json object string into a json object, then turns the json object into json array.
     * each json object in the array is traversed through and each object is added to the database table called League by using the LeagueDao instance.
     */
    suspend fun addLeagues() {

//                    var id: Int = -1
//                    var strLeague: String
//                    var strSport: String
//                    var strLeagueAlternate: String
        for (i in 0..jsonArray.length() - 1) {
            val currentJSONObject = JSONObject(jsonArray[i].toString())

            //inserts a league into the League table and updates the fields of a record if a record is inserted that already exists
            leagueDao.upsertLeague(
                //instance of league
                League(
                    Integer.valueOf(currentJSONObject.get("idLeague").toString()),
                    (currentJSONObject.get("strLeague").toString()),
                    (currentJSONObject.get("strSport").toString()),
                    (currentJSONObject.get("strLeagueAlternate").toString())
                )
            )

            Log.i(TAG, "id: " + currentJSONObject.get("idLeague"))

        }


    }


    /**
     * Home Screen composable
     * contains 4 buttons where 3 of them leads to new intents
     */
    @Preview(showBackground = true)
    @Composable
    fun HomeScreen() {
//    FootballApplicationTheme {
//
//    }
        val coroutineScope = rememberCoroutineScope()


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {


            Button(
                onClick = {
                    coroutineScope.launch {
                        addLeagues()
                    }

                },
                modifier = Modifier.width(250.dp)
            ) {
                Text(text = "Add Leagues to Database")
            }
            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {
                    var searchForClubsByLeagueIntent = Intent(this@MainActivity, SearchForClubsByLeagueActivity::class.java)
                    startActivity(searchForClubsByLeagueIntent)
                },
                modifier = Modifier.width(250.dp)
            ) {
                Text(text = "Search for Clubs By League")
            }
            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {
                    var searchClubsIntent = Intent(this@MainActivity, SearchForClubsActivity::class.java)
                    startActivity(searchClubsIntent)
                },
                modifier = Modifier.width(250.dp)
            ) {
                Text(text = "Search for Clubs from Database")
            }
            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = {
                    var searchforClubsFromWebIntent = Intent(this@MainActivity, SearchforClubsFromWebActivity::class.java)
                    startActivity(searchforClubsFromWebIntent)
                },
                modifier = Modifier.width(250.dp)
            ) {
                Text(text = "Search for Clubs from Web")
            }
        }
    }
}
