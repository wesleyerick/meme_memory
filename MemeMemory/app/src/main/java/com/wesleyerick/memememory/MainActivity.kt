package com.wesleyerick.memememory

import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesleyerick.memememory.model.Meme
import com.wesleyerick.memememory.ui.theme.MemeMemoryTheme
import kotlinx.coroutines.*


val memesList = mutableListOf(
    Meme("Ricardo Milos", R.drawable.sample_ricardo),
    Meme("Confia", R.drawable.confia),
    Meme("Chico Buarque", R.drawable.chico_buarque),
    Meme("Gavin", R.drawable.gavin),
    Meme("Careta", R.drawable.laughing),
    Meme("Nando Moura", R.drawable.nando_moura),
    Meme("Mo paz", R.drawable.mo_paz),
    Meme("Pensa", R.drawable.think_about_it),
    Meme("Turn Up", R.drawable.turn_up),
    Meme("Stilo", R.drawable.style),
    Meme("Pai de família", R.drawable.pai_de_familia_sucodelaranja),
    Meme("Okay", R.drawable.okay),
)

var memeMatch: MutableList<Meme> = mutableListOf()

class MainActivity : ComponentActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartGameScreen()
        }
    }
}

var memesRandom = mutableListOf<Meme>()

private fun getRandomMemes(memesList: MutableList<Meme>): List<Meme> {
    var memesFilter = memesList

    repeat(memesFilter.size) {
        if (memesFilter.size != 6) {
            memesFilter.removeAt((0 until memesFilter.size).random())
        }
    }

    memesRandom.addAll(memesList)
    memesRandom.addAll(memesList)
    memesRandom.shuffle()
    return memesRandom
}

@ExperimentalFoundationApi
@Composable
fun StartGameScreen() {
    var isStartGame by remember { mutableStateOf(true) }

    val ramdomMemesList = getRandomMemes(memesList)

    MemeMemoryTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            BaseScreen(isStartGame = isStartGame, ramdomMemesList)
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                isStartGame = false
            }
        }
    }
}


@ExperimentalFoundationApi
@Composable
fun BaseScreen(isStartGame: Boolean, ramdomMemesList: List<Meme>) {

    val textTimer by remember { mutableStateOf(getCountDownStart()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selecione os Memes Iguais",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        Memes(ramdomMemesList, isStartGame)

//        Spacer(modifier = Modifier.size(8.dp))

        if (isStartGame)
            Text(
                text = textTimer,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(16.dp)
            )

//        Spacer(modifier = Modifier.size(8.dp))
//
//        Button(
//            onClick = {},
//            modifier =
//            Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ){
//            Text(text = "REINICIAR")
//        }
    }
}

fun getCountDownStart(): String {

    var timer = ""

    val countDown = object : CountDownTimer(3000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timer = "${millisUntilFinished / 1000}"
        }

        override fun onFinish() {
            cancel()
        }
    }

    return timer

}

@ExperimentalFoundationApi
@Composable
fun Memes(memes: List<Meme>, isStartGame: Boolean) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(3)
        ) {
            items(memes) { meme ->
                MemeCard(meme, isStartGame)
            }
        }
    }
}

@Composable
fun MemeCard(meme: Meme, isStartGame: Boolean) {

    // We keep track if the message is expanded or not in this
    // variable
    var isExpanded by remember { mutableStateOf(false) }

    // surfaceColor will be updated gradually from one color to the other
    val surfaceColor: Color by animateColorAsState(
        if (isExpanded) MaterialTheme.colors.secondary else MaterialTheme.colors.surface,
    )

    val radius by animateDpAsState(if (isExpanded) 8.dp else 48.dp)

    Surface(
        shape = RoundedCornerShape(radius),
        elevation = 1.dp,
        color = surfaceColor,
        // animateContentSize will change the Surface size gradually
        modifier =
        Modifier
            .padding(all = 8.dp)
            .animateContentSize()
            .padding(1.dp)
            .clickable {
                isExpanded = !isExpanded

                memeMatch.add(meme)

                if (!validateMatch()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        isExpanded = !isExpanded
                    }
                }
            }
    ) {

        Column {

            when {
                isStartGame -> {
                    Image(
                        painter = painterResource(id = meme.image),
                        contentScale = ContentScale.Crop,
                        contentDescription = meme.name,
                        modifier =
                        Modifier
                            .height(80.dp)
                            .fillMaxWidth()
                            .border(
                                2.dp,
                                color = MaterialTheme.colors.secondaryVariant,
                                shape = CircleShape
                            )
                            .clipToBounds()

                    )
                }
                isExpanded -> {
                    Image(
                        painter = painterResource(id = meme.image),
                        contentScale = ContentScale.Crop,
                        contentDescription = meme.name,
                        modifier =
                        Modifier
                            .height(80.dp)
                            .fillMaxWidth()
                            .clipToBounds()

                    )
                }
                else -> {
                    Text(
                        text = "?",
                        modifier =
                        Modifier
                            .align(CenterHorizontally)
                            .padding(16.dp),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.secondaryVariant,
                    )
                }
            }
        }
    }
}

fun validateMatch(): Boolean {

    var isMatched = false

    if (memeMatch.size != 2) {
        return !isMatched
    }

    if (memeMatch.first() == memeMatch.last()) {
        println("Match!")
        isMatched = true
        memeMatch.clear()
    } else {
        println("Not Match!")
        isMatched = false
        memeMatch.removeAt(1)
    }

    return isMatched
}

@ExperimentalFoundationApi
@Preview(
    showBackground = true,
    name = "Light Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun DefaultPreview() {
    StartGameScreen()
}