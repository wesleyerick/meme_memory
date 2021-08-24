package com.wesleyerick.memememory

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wesleyerick.memememory.ui.theme.MemeMemoryTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MemeMemoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SplashScreen()
                }
            }
        }

        setup()
    }

    private fun setup() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)

            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

@Composable
fun SplashScreen(){
    MemeMemoryTheme {
//        Modifier.align(CenterHorizontally)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row{

                Icon(
                    painter = painterResource(id = R.drawable.gavin),
                    tint= Color.Unspecified,
                    contentDescription = stringResource(id = R.string.splash_img_description),
                    modifier =
                    Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(width = 2.dp, color = MaterialTheme.colors.secondary, shape = CircleShape)
                )

                Text(
                    text = stringResource(id = R.string.splash_title),
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(8.dp).align(CenterVertically),
                    fontFamily = FontFamily.Cursive
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
fun SplashPreview(){
    SplashScreen()
}