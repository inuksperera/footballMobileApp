/*
Name: Thotagamuwa Widanage Inuka Pasandul Perera
IIT id: 20221267
UOW id: w1954040
*/

package com.example.footballapplication

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.footballapplication.ui.theme.FootballApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchForClubsActivity : ComponentActivity() {

    lateinit var database: FootballDatabase
    lateinit var leagueDao: LeagueDao
    lateinit var teamDao: TeamDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database =
            Room.databaseBuilder(this, FootballDatabase::class.java, "footballDatabase").build()
        leagueDao = database.getLeagueDao()
        teamDao = database.getTeamDao()


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

        var buttonClickCount by rememberSaveable {
            mutableStateOf(0)
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            var textFieldValue by rememberSaveable { mutableStateOf("") }

            //the reason for using mutableStateOf(listOf<String>()) was because of the lazyColumn not updating when the list updates
            //reference: https://stackoverflow.com/questions/68046535/lazycolumn-and-mutable-list-how-to-update
            var clubsList = rememberSaveable {
                mutableStateOf(listOf<String>())
            }

            Spacer(modifier = Modifier.size(10.dp))

            Text(text = "Search for Clubs from Database")

            Spacer(modifier = Modifier.size(10.dp))

            OutlinedTextField(value = textFieldValue, onValueChange = {
                textFieldValue = it
            })
            Spacer(modifier = Modifier.size(10.dp))

            Button(onClick = {
                //used to show the "no clubs found" message only if the count is higher than one to make sure that the text doesnt show before the user hits the search button
                buttonClickCount++

                coroutineScope.launch {
                    clubsList.value = teamDao.getLeagues("%" + textFieldValue + "%")
                    Log.i(TAG, "abc leagues list: " + clubsList.value.toString())
                }
            }) {
                Text(text = "Search")
            }
            Spacer(modifier = Modifier.size(10.dp))



            LazyColumn() {
                itemsIndexed(
                    //clubs list is given as a parameter to items indexed so that it traverses through all elements in the list
                    clubsList.value
                ) { index, item ->
                    var bitmapValue: Bitmap? by rememberSaveable {
                        mutableStateOf(null)
                    }


                    LaunchedEffect(key1 = item, key2 = bitmapValue) {

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

                            //gets thumbnail image link from database
                            var thumbnailLink = teamDao.getImageLink(item)


                            withContext(Dispatchers.IO) {
                                if (thumbnailLink != "") {
                                    var url = URL(thumbnailLink)
                                    val connection =
                                        url.openConnection() as HttpURLConnection
                                    val bfstream =
                                        BufferedInputStream(connection.inputStream)
                                    //bitmap value of the image
                                    bitmapValue = BitmapFactory.decodeStream(bfstream)
                                }
//                            }

//                        }) {
//
//                        }
                            }
//                    Spacer(modifier = Modifier.size(10.dp))
                                }catch (e: Exception){
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

                            if(bitmapValue!=null){
                                Image(
                                    //renders the bitmap image if its not null
                                    bitmap = bitmapValue!!.asImageBitmap(),
                                    contentDescription = "Club logo",
                                    modifier = Modifier.size(150.dp),
                                )
                            }
                            Spacer(modifier = Modifier.size(16.dp))

                            Text(text = item)


                        }
//                        Spacer(modifier = Modifier.size(10.dp))


                    }


                }
            }
            Text(text = if (clubsList.value.isEmpty() && buttonClickCount>0) "No clubs found." else "")


        }



    }

}

