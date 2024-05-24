/*
Name: Thotagamuwa Widanage Inuka Pasandul Perera
IIT id: 20221267
UOW id: w1954040
*/

package com.example.footballapplication

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.footballapplication.ui.theme.FootballApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchForClubsByLeagueActivity : ComponentActivity() {

    lateinit var database: FootballDatabase
    lateinit var teamDao: TeamDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        database =
            Room.databaseBuilder(this, FootballDatabase::class.java, "footballDatabase").build()
        teamDao = database.getTeamDao()

        setContent {
            FootballApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchForClubsByLeagueComposable()

                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun SearchForClubsByLeagueComposable() {
        var textFieldValue by rememberSaveable { mutableStateOf("") }
        var retreiveValuesCoroutine = rememberCoroutineScope()
        var setValuesCoroutine = rememberCoroutineScope()

        var text by rememberSaveable { mutableStateOf("") }
        var jsonString by rememberSaveable { mutableStateOf("") }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally


        ) {
            Spacer(modifier = Modifier.size(10.dp))

            Text(text = "Search for Clubs By League")

            Spacer(modifier = Modifier.size(10.dp))

            OutlinedTextField(value = textFieldValue, onValueChange = {
                textFieldValue = it
            })
            Spacer(modifier = Modifier.size(10.dp))

            Row {
                Spacer(modifier = Modifier.size(7.dp))
                Button(
                    onClick = {
                        text = ""

                        //coroutine is launched
                        retreiveValuesCoroutine.launch {

                            //url of league where spaces in the textfield value is replaced by underscores to access the api
                            var url =
                                URL(
                                    "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=" + textFieldValue.trim()
                                        .replace(" ", "_")
                                )

//                                URL("https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=English_Premier_League")

                            val connection = url.openConnection() as HttpURLConnection

                            withContext(Dispatchers.IO) {
//                                      var reader = BufferedReader(InputStreamReader(connection.inputStream))
                                var reader =
                                    BufferedReader(InputStreamReader(connection.inputStream))
                                jsonString = reader.readLine()

                                Log.i(ContentValues.TAG, "jsonString: " + jsonString.toString())
//
                                var jsonObject = JSONObject(jsonString).get("teams")
//                                Log.i(TAG, "harro: " + jsonObject)


                                if (jsonObject.toString().equals("null")) {
                                    text = "No football leagues found with the name provided."
                                } else {

                                    val jsonArray = JSONArray(jsonObject.toString())
                                    Log.i(ContentValues.TAG, "jsonArray: " + jsonArray.toString())

                                    //filtering out leagues related to football
                                    if (JSONObject(jsonArray[0].toString()).get("strSport").equals("Soccer")) {
                                        for (i in 0..jsonArray.length() - 1) {
                                            val currentJSONObject =
                                                JSONObject(jsonArray[i].toString())
                                            text += """
"idTeam":"${currentJSONObject.get("idTeam")}",
"Name":"${currentJSONObject.get("strTeam")}",
"strTeamShort":"${currentJSONObject.get("strTeamShort")}",
"strAlternate":"${currentJSONObject.get("strAlternate")}",
"intFormedYear":"${currentJSONObject.get("intFormedYear")}",
"strLeague":"${currentJSONObject.get("strLeague")}",
"idLeague":"${currentJSONObject.get("idLeague")}",
"strStadium":"${currentJSONObject.get("strStadium")}",
"strKeywords":"${currentJSONObject.get("strKeywords")}",
"strStadiumThumb":"${currentJSONObject.get("strStadiumThumb")}",
"strStadiumLocation":"${currentJSONObject.get("strStadiumLocation")}",
"intStadiumCapacity":"${currentJSONObject.get("intStadiumCapacity")}",
"strWebsite":"${currentJSONObject.get("strWebsite")}",
"strTeamJersey":"${currentJSONObject.get("strTeamJersey")}",
"strTeamLogo":"${currentJSONObject.get("strTeamLogo")}",

                                          """
                                        }

                                    } else {
                                        text = "No football leagues found with the name provided."

                                    }
                                }
                            }
//                                      reader.forEachLine {
//                                          text += "\n" + it
//                                      }
//                                    var string = reader.readLine()
//                                    while (string != null){
//                                        text += "\n" + string
//                                        string = reader.readLine()
//                                    }
//                                    var char = reader.read()
//
//                                      var string = ""
//
////                                      text += "lakdsfjdksl\n" + string
//
//                                      while (char != -1){
//
//                                          string += char.toChar()
//                                          text = string.toString()
//                                        Log.i(TAG, "char: " + char.toChar())
//                                        if (char.toChar() == '\n') {
//                                            text += "\n" + string
//                                            string = ""
//                                        }
////                                          char = reader.read()
//
//                                      }
//
////                                      text += "aaaaaaaaa\n" + string
//
//                                  }
//                                  var stb = StringBuilder()
//
//                                  withContext(Dispatchers.IO){
//                                      var bf = BufferedReader(InputStreamReader(connection.inputStream))
//                                      var line: String? = bf.readLine()
//                                      while (line != null) { // keep reading until no more lines of text
//                                          stb.append(line + "\n")
//                                          line = bf.readLine()
//                                      }
//
//
//                                  }
//
//                                  text = stb.toString()
                        }


                    },
                    modifier = Modifier.width(170.dp)
                ) {
                    Text(text = "Retrieve Clubs")
                }
                Spacer(modifier = Modifier.size(10.dp))

                Button(
                    onClick = {

                        if (!jsonString.equals("")) {

                            setValuesCoroutine.launch {


                                var jsonObject = JSONObject(jsonString).get("teams")

                                if (!jsonObject.toString().equals("null")) {

                                    val jsonArray = JSONArray(jsonObject.toString())

                                    for (i in 0..jsonArray.length() - 1) {
                                        val currentJSONObject = JSONObject(jsonArray[i].toString())

                                        //inserts all the teams retreived from the api to the database using the TeamDao instance
                                        teamDao.upsertTeam(

                                            //instance of team object
                                            Team(
                                                currentJSONObject.get("idTeam").toString().toInt(),
                                                currentJSONObject.get("strTeam").toString(),
                                                currentJSONObject.get("strTeamShort").toString(),
                                                currentJSONObject.get("strAlternate").toString(),
                                                currentJSONObject.get("intFormedYear").toString()
                                                    .toInt(),
                                                currentJSONObject.get("strLeague").toString(),
                                                currentJSONObject.get("idLeague").toString()
                                                    .toInt(),
                                                currentJSONObject.get("strStadium").toString(),
                                                currentJSONObject.get("strKeywords").toString(),
                                                currentJSONObject.get("strStadiumThumb").toString(),
                                                currentJSONObject.get("strStadiumLocation")
                                                    .toString(),
                                                currentJSONObject.get("intStadiumCapacity")
                                                    .toString()
                                                    .toInt(),
                                                currentJSONObject.get("strWebsite").toString(),
                                                currentJSONObject.get("strTeamJersey").toString(),
                                                currentJSONObject.get("strTeamLogo").toString()
                                            )
                                        )


                                    }
                                }
                            }

                        }


                    },
                    modifier = Modifier.width(200.dp)
                ) {
                    Text(text = " Save clubs to Database")
                }
                Spacer(modifier = Modifier.size(7.dp))

            }
            Spacer(modifier = Modifier.size(10.dp))

            Box(
                modifier = Modifier.padding(10.dp)
            ) {

                Text(
                    text =  text,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                )
            }


        }

    }
}