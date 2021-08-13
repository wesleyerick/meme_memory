package com.wesleyerick.memememory

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.wesleyerick.memememory.model.Meme
import com.wesleyerick.memememory.ui.theme.MemeMemoryTheme


val memesList = mutableListOf(
    Meme("Ricardo", R.drawable.sample_ricardo),
    Meme(
        "we5555555555555555555555555555555555555555555555555555555555we",
        R.drawable.sample_ricardo
    ),
    Meme("wewe", R.drawable.sample_ricardo),
    Meme("wewe", R.drawable.sample_ricardo),
    Meme("wewe", R.drawable.sample_ricardo),
    Meme("wewe", R.drawable.sample_ricardo),
    Meme("Jailson", R.drawable.sample_ricardo)
)
val memesSampleList = mutableListOf(
    Meme("Ricardo", R.drawable.sample_ricardo),
    Meme("wewe", R.drawable.sample_ricardo),
    Meme("Jailson", R.drawable.sample_ricardo)
)

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemeMemoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Memes(memesList)
                }
            }
        }
    }
}

@Composable
fun Memes(memes: List<Meme>) {
    LazyColumn {
        items(memes) { meme ->
            MemeCard(meme)
        }
    }
}

@Composable
fun MemeCard(meme: Meme) {

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
            .padding(all = 16.dp)
            .animateContentSize()
            .padding(1.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            Image(
//                painter = painterResource(id = meme.image),
                painter = rememberImagePainter(
                    data = "https://developer.android.com/images/brand/Android_Robot.png"
                ),
                contentDescription = meme.name,
                modifier =
                Modifier
                    .width(80.dp)
                    .border(2.dp, color = MaterialTheme.colors.secondary, CircleShape)
                    .clip(CircleShape)

            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isExpanded){
                Text(
                    text = meme.name,
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.align(CenterHorizontally),
//                maxLines = if (isExpanded) Int.MAX_VALUE else 1, // For show 1 or all lines
                )
            }
        }
    }
}

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
        Memes(memesSampleList)
    }
}