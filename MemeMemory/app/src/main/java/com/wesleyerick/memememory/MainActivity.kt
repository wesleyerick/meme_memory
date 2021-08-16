package com.wesleyerick.memememory

import android.content.res.Configuration
import android.os.Bundle
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesleyerick.memememory.model.Meme
import com.wesleyerick.memememory.ui.theme.MemeMemoryTheme


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
val memesSampleList = mutableListOf(
    Meme("Ricardo", R.drawable.sample_ricardo),
    Meme(
        "Confia", R.drawable.confia),
    Meme("Chico Buarque", R.drawable.chico_buarque),
    Meme("Gavin", R.drawable.gavin),
    Meme("Careta", R.drawable.laughing),
    Meme("Nando Moura", R.drawable.nando_moura),
)

class MainActivity : ComponentActivity() {


    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemeMemoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    BaseScreen()
                }
            }
        }
    }
}

var memesRandom = mutableListOf<Meme>()

private fun getRandomMemes(memesList: MutableList<Meme>): List<Meme> {
    var memesFilter = memesList

    repeat(memesFilter.size) {
        if (memesFilter.size != 6) {
            memesFilter.removeAt((0..memesFilter.size).random())
        }
    }

    memesRandom.addAll(memesList)
    memesRandom.addAll(memesList)
    memesRandom.shuffle()
    return memesRandom
}


@ExperimentalFoundationApi
@Composable
fun BaseScreen(){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selecione os Memes Iguais",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.secondary,
            modifier = Modifier.align(CenterHorizontally)
        )

        Spacer(modifier = Modifier.size(8.dp))

        Memes(getRandomMemes(memesList))

        Spacer(modifier = Modifier.size(8.dp))
        
        Button(
            onClick = { getRandomMemes(memesList) },
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Text(text = "REINICIAR")
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun Memes(memes: List<Meme>) {
    
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(3)
        ) {
            items(memes) { meme ->
                MemeCard(meme)
            }
        }
    }
}

@Composable
fun MemeCard(meme: Meme) {

    val cornerShape = RoundedCornerShape(percent = 30)

    // We keep track if the message is expanded or not in this
    // variable
    var isExpanded by remember { mutableStateOf(false) }

    // surfaceColor will be updated gradually from one color to the other
    val surfaceColor: Color by animateColorAsState(
        if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
    )

    val radius by animateDpAsState( if (isExpanded) 8.dp else 24.dp )

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
            .clickable { isExpanded = !isExpanded }
    ) {

        Column(modifier = Modifier.padding(all = 16.dp)) {

            if (isExpanded){
                Image(
                    painter = painterResource(id = meme.image),
                    contentScale = ContentScale.Crop,
                    contentDescription = meme.name,
                    modifier =
                    Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .border(2.dp, color = MaterialTheme.colors.secondary, cornerShape)
                        .clip(cornerShape)

                )

            } else {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_baseline_android_24),
//                    contentDescription = meme.name,
//                    modifier =
//                    Modifier
//                        .width(80.dp)
//                )

                Text(
                    text = "?",
                    modifier =
                    Modifier
                        .align(CenterHorizontally),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.secondaryVariant,
                    )
            }
        }
    }
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
    MemeMemoryTheme {
        BaseScreen()
    }
}