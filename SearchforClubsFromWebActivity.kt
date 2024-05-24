/*
Name: Thotagamuwa Widanage Inuka Pasandul Perera
IIT id: 20221267
UOW id: w1954040
*/

package com.example.footballapplication

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.footballapplication.ui.theme.FootballApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchforClubsFromWebActivity : ComponentActivity() {

    //    lateinit var database: FootballDatabase
//    lateinit var leagueDao: LeagueDao
//    lateinit var teamDao: TeamDao
    lateinit var leaguesList: MutableState<MutableList<String>>
    lateinit var clubsMap: MutableState<MutableMap<String, String>>
    lateinit var data: MutableState<MutableMap<String, String>>
    lateinit var appStateText: MutableState<String>

    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        database =
//            Room.databaseBuilder(this, FootballDatabase::class.java, "footballDatabase").build()
//        leagueDao = database.getLeagueDao()
//        teamDao = database.getTeamDao()


        setContent {
            FootballApplicationTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SearchForClubsComposable()
                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun SearchForClubsComposable() {
        var coroutineScope = rememberCoroutineScope()
        var imageCoroutineScope = rememberCoroutineScope()
        var thumbnailImageCoroutineScope = rememberCoroutineScope()
        var getLeaguesCoroutine = rememberCoroutineScope()

        var buttonClickCount by rememberSaveable {
            mutableStateOf(0)
        }

        var textFieldValue by rememberSaveable { mutableStateOf("") }
        data = rememberSaveable { mutableStateOf(mutableMapOf<String, String>()) }

        //the reason for using the data variable was because of the lazyColumn not updating when the list updates
        //reference: https://stackoverflow.com/questions/68046535/lazycolumn-and-mutable-list-how-to-update

        leaguesList = rememberSaveable {
            mutableStateOf(mutableListOf<String>())
        }
        clubsMap = rememberSaveable {
            mutableStateOf(mutableMapOf<String, String>())
        }
        appStateText = rememberSaveable{ mutableStateOf("")}

        Column(
            horizontalAlignment = Alignment.CenterHorizontally

        ) {


            Spacer(modifier = Modifier.size(10.dp))

            Text(text = "Search for Clubs from Web")

            Spacer(modifier = Modifier.size(10.dp))

            OutlinedTextField(value = textFieldValue, onValueChange = {
                textFieldValue = it
            })
            Spacer(modifier = Modifier.size(10.dp))

            Button(onClick = {
                leaguesList.value.clear()
                clubsMap.value.clear()
                data.value.clear()
                appStateText.value = "Loading..."

                //used to show the "no clubs found" message only if the count is higher than one to make sure that the text doesnt show before the user hits the search button
                buttonClickCount++
//                runBlocking {
                coroutineScope.launch {

                    try {

                        getLeagues()

                        Log.i(TAG, "abc leagues list outside: " + leaguesList.value.toString())


//                        Log.i(TAG, "abc clubs list: " + clubsList.value.toString())

                        getClubs(textFieldValue)


                        getJerseys()

                        if (clubsMap.value.isEmpty() && buttonClickCount > 0)
                        {
                            appStateText.value = "No clubs found."
                        } else {
                            appStateText.value = ""
                        }


                        data.value = (data.value + clubsMap.value) as MutableMap<String, String>


                    } catch (e: Exception) {
                        Log.i(TAG, "Error: there was an error when getting the clubs")
                        appStateText.value = "Error: there was an error when getting the clubs"
                    }
//                        }
                }
//                }
//                Log.i(TAG, "HI FREN :D " + clubsList.value.toString())

            }) {
                Text(text = "Search")
            }
            Spacer(modifier = Modifier.size(10.dp))


//            LaunchedEffect(null) {
//
//            }

            LazyColumn() {
                itemsIndexed(
                    //reference for sortedBy: https://www.youtube.com/watch?v=7lLELOTN0bw&t=301s
                    data.value.entries.toList().sortedBy { (key, value) ->
                        key.split("&&").get(1)
                    }
                ) { index, (key, value) ->
                    var bitmapValue: Bitmap? by rememberSaveable {
                        mutableStateOf(null)
                    }

//                    var a = "10 && abc"
                    var keySplitList = key.split("&&")




                    Log.i(TAG, "INSIDE LAZY COLUMN")


                    LaunchedEffect(key1 = key, key2 = data.value) {
                        Log.i(TAG, "LAUNCHED EFFECT IS RUNNING")

//                         thumbnailImageCoroutineScope.launch {
//                             var thumbnailLink = teamDao.getImageLink(item)
//                             var url =
//                                 URL(thumbnailLink)
//                         }

//                                URL("https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=English_Premier_League")

//                        Button(onClick = {

                        this.launch {
//                                val url =
//                                    URL("https://i.pinimg.com/originals/76/0d/36/760d3649962f530d8dae8224ed87a11e.jpg")
//
//                                val connection = url.openConnection() as HttpURLConnection
//                                val bfstream = BufferedInputStream(connection.inputStream)
//                                bitmapValue = BitmapFactory.decodeStream(bfstream)
//                            }

//                        imageCoroutineScope.launch {
//                        this.launch {

                            try {


                                var thumbnailLink = value

                                withContext(Dispatchers.IO) {
                                    if (thumbnailLink != "") {
                                        var url = URL(thumbnailLink)
                                        val connection =
                                            url.openConnection() as HttpURLConnection
                                        val bfstream =
                                            BufferedInputStream(connection.inputStream)
                                        bitmapValue = BitmapFactory.decodeStream(bfstream)
                                    }
//                            }

//                        }) {
//
//                        }
                                }
//                    Spacer(modifier = Modifier.size(10.dp))
                            } catch (e: Exception) {
                                Log.i(TAG, "An error occured while parsing the image")
                            }
                        }


                    }




                    Box(
                        modifier = Modifier.padding(
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 10.dp
                        )
                    ) {
                        Spacer(modifier = Modifier.size(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(Color(0xFF8591ea))
                                .fillMaxWidth()
                        ) {

                            Spacer(modifier = Modifier.size(16.dp))

                            if (bitmapValue != null) {
                                Image(
                                    bitmap = bitmapValue!!.asImageBitmap(),
                                    contentDescription = "Club Jersey",
                                    modifier = Modifier.size(150.dp),
                                )
                            }
                            Spacer(modifier = Modifier.size(16.dp))
                            Log.i(TAG, "LAZY COLUMN IS RUNNING")

                            Text(text = keySplitList.get(1))


                        }
//                        Spacer(modifier = Modifier.size(10.dp))


                    }


                }
            }
            Text(text = appStateText.value)


        }


    }

    /**
     * function to get all leagues from the api
     */
    private suspend fun getLeagues(
//        getLeaguesCoroutine: CoroutineScope,
//    ): MutableList<String> {
    ) {

//        leaguesList.value.add("harro")
        var jsonString = ""
        var url = URL("https://www.thesportsdb.com/api/v1/json/3/all_leagues.php");
        val connection = url.openConnection() as HttpURLConnection

        withContext(Dispatchers.IO) {
//


            var reader = BufferedReader(InputStreamReader(connection.inputStream))
            jsonString = reader.readLine()
//                    leaguesList.add(jsonString)
//                    leaguesList.add("jsonString")
//                    jsonString = reader.readLine()

            Log.i(ContentValues.TAG, "jsonString: " + jsonString.toString())
//
            var jsonObject = JSONObject(jsonString).get("leagues")
            Log.i(ContentValues.TAG, "harro: " + jsonObject)


            if (jsonObject.toString().equals("null")) {
//                        text = "No leagues found with the name provided."
            } else {

                val jsonArray = JSONArray(jsonObject.toString())
                Log.i(ContentValues.TAG, "jsonArray: " + jsonArray.toString())
                Log.i(TAG, "abcharro" + jsonString)
                for (i in 0..jsonArray.length() - 1) {
                    val currentJSONObject = JSONObject(jsonArray[i].toString())
//                        Log.i(TAG, "array object: " + currentJSONObject.get("strLeague"))

                    if (currentJSONObject.get("strSport").equals("Soccer")) {

                        leaguesList.value.add(currentJSONObject.get("strLeague").toString())
                    }

                }

                Log.i(TAG, "abcharro" + leaguesList.toString())


            }


//


        }

        Log.i(TAG, "HARROOOOOOOOOOOOOOOOOOOOOOOOOOOO")

//        return mutableListOf())
    }

    /**
     * function to get all clubs from api for each league
     */
    private suspend fun getClubs(textFieldValue: String): List<String> {

        Log.i(TAG, "HARROOOOOOOOOOOOOOOOOOOOOOOOOOOO FROM GET CLUBS :D")

        for (league in leaguesList.value) {
            Log.i(TAG, "Current League: " + league)

//            var a = "English Premier League"
            var url = URL(
                "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=" + league.trim()
                    .replace(" ", "_")
            )

            val connection = url.openConnection() as HttpURLConnection
            var jsonString = ""

            withContext(Dispatchers.IO) {
//                                      var reader = BufferedReader(InputStreamReader(connection.inputStream))
                var reader = BufferedReader(InputStreamReader(connection.inputStream))
                jsonString = reader.readLine()
            }

            Log.i(ContentValues.TAG, "jsonString: " + jsonString.toString())
//
            var jsonObject = JSONObject(jsonString).get("teams")
//                Log.i(TAG, "harro: " + jsonObject)


            if (jsonObject.toString().equals("null")) {
//                text = "No leagues found with the name provided."
            } else {

                val jsonArray = JSONArray(jsonObject.toString())
//                    Log.i(ContentValues.TAG, "jsonArray: " + jsonArray.toString())

                for (i in 0..jsonArray.length() - 1) {
                    val currentJSONObject = JSONObject(jsonArray[i].toString())
//                        Log.i(TAG, "CLUBS " + i + ": " + currentJSONObject.get("strTeam") + " from League: " + league)
//                        clubsList.value.add(currentJSONObject.get("strTeam").toString())

                    if (currentJSONObject.get("strTeam").toString()
                            .contains(textFieldValue.trim(), ignoreCase = true)
                    ) {
                        clubsMap.value.put(

                            "0&&" + currentJSONObject.get("strTeam").toString(),
                            currentJSONObject.get("idTeam").toString()
                        )

                        Log.i(ContentValues.TAG, "clubsMap values: " + "0&&" + currentJSONObject.get("strTeam").toString() + ", " + currentJSONObject.get("idTeam").toString())

//                        getJerseys(currentJSONObject)
                        //get all jersey links and loop through and store each jersey with the strTeam
                        //team1 -> {link1, link2, link3} - lazy column issue


                    }
                }

            }

        }

        Log.i(ContentValues.TAG, "went into getJerseys()")


        Log.i(ContentValues.TAG, "clubs list size: " + clubsMap.value.size)



        return listOf()

    }


    /**
     * function to get all jersey image links for a club
     */
    private suspend fun getJerseys() {

        Log.i(ContentValues.TAG, "inside getJerseys()")

        try {
            Log.i(ContentValues.TAG, "inside try catch")

            var keySet = clubsMap.value.keys.toSet()
            for (key in keySet) {
                var clubName = key.split("&&").get(1)
                var clubID = clubsMap.value.get(key)

                Log.i(ContentValues.TAG, "club name: " + clubName + " club id: " + clubID)
                Log.i(ContentValues.TAG, "club name. get 1: " + clubName + "clubsMap.value.get(key) : " + clubsMap.value.get(key))

                if (clubID != null) {



                    var url = URL(
                        "https://www.thesportsdb.com/api/v1/json/3/lookupequipment.php?id=" + clubID
                    )
                    val connection = url.openConnection() as HttpURLConnection
                    var jsonString = ""

                    withContext(Dispatchers.IO) {
                        var reader = BufferedReader(InputStreamReader(connection.inputStream))
                        jsonString = reader.readLine()
                    }

                    var jsonObject = JSONObject(jsonString).get("equipment")
//                Log.i(TAG, "harro: " + jsonObject)


                    if (jsonObject.toString().equals("null")) {
//                text = "No leagues found with the name provided."
                    } else {

                        val jsonArray = JSONArray(jsonObject.toString())
//                    Log.i(ContentValues.TAG, "jsonArray: " + jsonArray.toString())

                        for (i in 0..jsonArray.length() - 1) {
                            val jerseyJSONObject = JSONObject(jsonArray[i].toString())
//                            Log.i(
//                                "HARRO FREN THIS IS THE EQUIPMENT YIPPEE: ",
//                                jerseyJSONObject.get("strEquipment").toString()
//                            )
//                            Log.i(TAG,
//                                "HARRO FREN THIS IS THE EQUIPMENT YIPPEE: " +
//                                jerseyJSONObject.get("strEquipment").toString()
//                            )

                            Log.i("key: ", i.toString() + "&&" + clubName)
                            Log.i("equipment value: ", jerseyJSONObject.get("strEquipment").toString())

                            clubsMap.value.put(
                                //i.toString is added with "&&" to make the keys unique
                                i.toString() + "&&" + clubName,
                                jerseyJSONObject.get("strEquipment").toString()
                            )

                        }

                    }

                }
            }
        } catch (e: Exception) {
            Log.i(TAG, "Error: An error has occured when parsing team equipment." + e)

        }
    }

}


