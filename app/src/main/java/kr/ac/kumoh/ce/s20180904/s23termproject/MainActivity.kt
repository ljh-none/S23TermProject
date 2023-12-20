package kr.ac.kumoh.ce.s20180904.s23termproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavType
import kr.ac.kumoh.ce.s20180904.s23termproject.ui.theme.S23TermProjectTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage

enum class WordScreen{
    WordList,
    WordDetail
}

class MainActivity : ComponentActivity() {
    //variable space
    private val viewModel: WordViewModel by viewModels()

    //override space
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(viewModel)
        }
    }
}


@Composable
fun MainScreen(viewModel:WordViewModel){
    val wordList by viewModel.wordList.observeAsState(emptyList())

    S23TermProjectTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WordApp(wordList)
        }
    }
}

@Composable
fun WordApp(wordList: List<Word>) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WordScreen.WordList.name,
    ) {
        composable(route = WordScreen.WordList.name) {
            WordList(navController, wordList)
        }
        composable(
            route = WordScreen.WordDetail.name + "/{id}",
            arguments = listOf(navArgument("id") {
                    type = NavType.IntType
                }
            )
            ) {
            val id = it.arguments?.getInt("id") ?: -1
            if (id >= 0)
                WordDetail(wordList[id])
        }
    }
}

@Composable
fun WordList(navController: NavController, list: List<Word>){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(list.size) {
            WordItem(navController, list, it)
        }
    }
}

@Composable
fun WordItem(navController: NavController, wordList: List<Word>, id: Int) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextTitle(wordList[id].word)
            Button(
                onClick = { navController.navigate(WordScreen.WordDetail.name+"/$id") }
            ) {
                Text(text = "go")
            }

        }
        AnimatedVisibility(visible = expanded) {
            Text(wordList[id].meaning)
        }
    }
}

@Composable
fun TextTitle(title: String) {
    Text(title, fontSize = 30.sp)
}

@Composable
fun WordDetail(word: Word){
    val webUri= Uri.parse("https://dictionary.cambridge.org/ko/%EC%82%AC%EC%A0%84/%EC%98%81%EC%96%B4-%ED%95%9C%EA%B5%AD%EC%96%B4/${word.word}")
    val context = LocalContext.current

    Column (
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            AsyncImage(
                model = word.image,
                contentDescription = "word image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(400.dp)
                    .clip(RoundedCornerShape(percent = 10)),
            )
            Spacer(modifier = Modifier.height(15.dp))
            TextTitle(title = word.sentence)
        }
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, webUri)
                startActivity(context, intent, null)
                      },
            modifier=Modifier.fillMaxWidth()
        ) {
            Text("goto Dictionary")
        }
    }
}

//https://www.collinsdictionary.com/images/full/apple_158989157.jpg

//UPDATE word SET image='https://www.collinsdictionary.com/images/full/apple_158989157.jpg' WHERE word='apple';
//UPDATE word SET image='https://www.thoughtco.com/thmb/g8h6NnWWWVkm-KXNBgMx-0Edd2U=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/GettyImages_482194715-56a1329e5f9b58b7d0bcf666.jpg' WHERE word='ocean';
//UPDATE word SET image='https://upload.wikimedia.org/wikipedia/commons/1/1a/Crystal_Project_computer.png' WHERE word='computer';
//UPDATE word SET image='https://upload.wikimedia.org/wikipedia/commons/e/e7/Everest_North_Face_toward_Base_Camp_Tibet_Luca_Galuzzi_2006.jpg' WHERE word='mountain';
