package com.example.mobilenetv2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.mobilenetv2.ui.theme.Mobilenetv2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mobilenetv2Theme {
                App()
            }
        }
    }
}

@Preview(backgroundColor = 0xff0000)
@Composable
fun App() {
    GetContentExample(
        modifier = Modifier
            .fillMaxSize()
    )
}

@Composable
fun GetContentExample(modifier: Modifier = Modifier) {
    var context = LocalContext.current;
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var resultClass by remember { mutableStateOf<String>("unknown") }
    var resultScore by remember { mutableStateOf<String>("0.00") }
    var model = Classifier(Utils.assetFilePath(context, "mobilenet-v2.pt"));
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    Column(
        modifier = modifier.padding(5.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        if (imageUri == null) {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .border(
                        BorderStroke(
                            2.dp, SolidColor(Color.Black)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Please upload an image.",
                    textAlign = TextAlign.Center,
                )
            }

        } else {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Detect Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .border(
                        BorderStroke(
                            2.dp, SolidColor(Color.Black)
                        )
                    ),
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = "Load Image")
            }
            Button(onClick = {
                if (imageUri == null) {
                    Toast.makeText(context, "Please upload an image first", Toast.LENGTH_LONG)
                        .show()
                } else {

                    bitmap = BitmapFactory.decodeFile(imageUri.toString());
                    val bitmap =
                        BitmapFactory.decodeStream(
                            context.getContentResolver().openInputStream(imageUri!!)
                        )
                    var result = model.predict(bitmap)
                    resultClass = result.className;
                    resultScore = String.format("%.2f", result.score * 100)
                }
            }) {
                Text(text = "Detect")
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Class: " + resultClass, modifier = Modifier.padding(10.dp))
            Text(text = "Confidence: " + resultScore + "%", modifier = Modifier.padding(10.dp))
        }
    }
}

