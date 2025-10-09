package cn.hellozjf.project.composequiz

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.hellozjf.project.composequiz.database.entity.Chapter
import cn.hellozjf.project.composequiz.ui.screen.MainScreen
import cn.hellozjf.project.composequiz.ui.theme.ComposeQuizTheme
import cn.hellozjf.project.composequiz.util.ChapterConstant
import cn.hellozjf.project.composequiz.util.TextFileManager
import cn.hellozjf.project.composequiz.util.TextFileUtils
import cn.hellozjf.project.composequiz.viewmodel.ChapterQuizViewModel
import cn.hellozjf.project.composequiz.viewmodel.ChapterViewModel
import cn.hellozjf.project.composequiz.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.IOException

class MainActivity : ComponentActivity() {

  private val TAG = "MainActivity"

  private val mainScope = CoroutineScope(Dispatchers.Main)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      ComposeQuizTheme {
        val owner = LocalViewModelStoreOwner.current
        owner?.let {
          val chapterQuizViewModel: ChapterQuizViewModel = viewModel(
            viewModelStoreOwner = it,
            key = "ChapterQuizViewModel",
            factory = ChapterQuizViewModelFactory(
              LocalContext.current.applicationContext as Application
            )
          )
          val chapterViewModel: ChapterViewModel = viewModel(
            viewModelStoreOwner = it,
            key = "ChapterViewModel",
            factory = ChapterViewModelFactory(
              LocalContext.current.applicationContext as Application
            )
          )
          MainScreen(
            chapterQuizViewModel = chapterQuizViewModel
          )

          readCsvAndWriteToDB(chapterViewModel, chapterQuizViewModel)
        }
      }
    }
  }

  private fun readCsvAndWriteToDB(
    chapterViewModel: ChapterViewModel,
    chapterQuizViewModel: ChapterQuizViewModel
  ) {
    // 读取章节信息
    readChapterCsv(chapterViewModel)
    // 读取题库信息
    readQuizCsv()
  }

  private fun readChapterCsv(
    chapterViewModel: ChapterViewModel
  ) {
    try {
      this.assets.open(ChapterConstant.PATH).bufferedReader().use { reader ->
        val csvParser = CSVParser(reader, CSVFormat.DEFAULT.withHeader())

        for (record in csvParser) {
          val chapter = Chapter()
          chapter.index = record.get(ChapterConstant.INDEX).toInt()
          chapter.fullTitle = record.get(ChapterConstant.FULL_TITLE)
          chapter.simpleTitle = record.get(ChapterConstant.SIMPLE_TITLE)
          chapter.simpleUrl = record.get(ChapterConstant.SIMPLE_URL)
          chapter.fullUrl = record.get(ChapterConstant.FULL_URL)
          chapterViewModel.insertChapter(chapter)
        }

      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  private fun readQuizCsv() {

  }

  private fun setupTextFiles() {
    // 方式1：直接读取assets中的txt文件（适合小文件）
    val configContent = TextFileUtils.readTextFromAssets(this, "output.csv")

//    // 方式2：读取已拷贝到本地的txt文件
//    if (TextFileUtils.isTextFileExists(this, "config.txt")) {
//      val localContent = TextFileUtils.readTextFromLocal(this, "config.txt")
//      Log.d("LocalConfig", localContent ?: "文件为空")
//    }
//
//    // 方式3：按行读取
//    val lines = TextFileUtils.readTextLinesFromAssets(this, "texts/data.txt")
//    lines.forEachIndexed { index, line ->
//      Log.d("DataLine", "Line ${index + 1}: $line")
//    }
//
//    // 检查所有本地txt文件
//    val textFiles = TextFileUtils.getLocalTextFiles(this)
//    textFiles.forEach { fileName ->
//      Log.d("TextFile", "本地文件: $fileName")
//    }
//
//    // 如果需要重新拷贝文件
//    copyTextFilesWithListener()
  }

  private fun copyTextFilesWithListener() {
    TextFileManager.copyTextFilesWithProgress(this, object : TextFileManager.TextFileCopyListener {
      override fun onStart() {
        Log.d("TextFile", "开始拷贝txt文件")
      }

      override fun onProgress(fileName: String, current: Int, total: Int) {
        Log.d("TextFile", "拷贝进度: $fileName ($current/$total)")
      }

      override fun onComplete(success: Boolean, copiedCount: Int) {
        mainScope.launch {
          if (success) {
            Log.d("TextFile", "成功拷贝 $copiedCount 个文件")
          } else {
            Log.e("TextFile", "文件拷贝不完整")
          }
        }
      }

      override fun onError(fileName: String, exception: Exception) {
        Log.e("TextFile", "拷贝失败: $fileName", exception)
      }
    })
  }

  /**
   * 使用协程方式（需要在build.gradle中添加协程依赖）
   */
  private fun copyTextFilesWithCoroutine() {
    mainScope.launch {
      val success =
        TextFileManager.copyTextFilesWithCoroutine(this@MainActivity) { fileName, current, total ->
          Log.d("TextFile", "拷贝中: $fileName ($current/$total)")
        }

      if (success) {
        Log.d("TextFile", "所有文件拷贝完成")
      } else {
        Log.e("TextFile", "文件拷贝失败")
      }
    }
  }
}

//@Composable
//fun ScreenSetup(
//  modifier: Modifier = Modifier,
//  viewModel: MainViewModel
//) {
//  val allProducts by viewModel.allProducts.observeAsState(listOf())
//  val searchResults by viewModel.searchResults.observeAsState(listOf())
//  MainScreen(
//    modifier = modifier,
//    allProducts = allProducts,
//    searchResults = searchResults,
//    viewModel = viewModel
//  )
//}
//
//@Composable
//fun MainScreen(
//  modifier: Modifier = Modifier,
//  allProducts: List<Product>,
//  searchResults: List<Product>,
//  viewModel: MainViewModel
//) {
//  var productName by remember { mutableStateOf("") }
//  var productQuantity by remember { mutableStateOf("") }
//  var searching by remember { mutableStateOf(false) }
//
//  val onProductTextChange = { text: String ->
//    productName = text
//  }
//  val onQuantityTextChange = { text: String ->
//    productQuantity = text
//  }
//
//  Column(
//    horizontalAlignment = CenterHorizontally,
//    modifier = modifier.fillMaxWidth()
//  ) {
//    CustomTextField(
//      title = "Product Name",
//      textState = productName,
//      onTextChange = onProductTextChange,
//      keyboardType = KeyboardType.Text
//    )
//    CustomTextField(
//      title = "Quantity",
//      textState = productQuantity,
//      onTextChange = onQuantityTextChange,
//      keyboardType = KeyboardType.Number
//    )
//    Row(
//      horizontalArrangement = Arrangement.SpaceEvenly,
//      modifier = Modifier
//        .fillMaxWidth()
//        .padding(10.dp)
//    ) {
//      Button(onClick = {
//        if (productQuantity.isNotEmpty()) {
//          viewModel.insertProduct(
//            Product(
//              productName,
//              productQuantity.toInt()
//            )
//          )
//          searching = false
//        }
//      }) {
//        Text("Add")
//      }
//
//      Button(onClick = {
//        searching = true
//        viewModel.findProduct(productName)
//      }) {
//        Text("Search")
//      }
//
//      Button(onClick = {
//        searching = false
//        viewModel.deleteProduct(productName)
//      }) {
//        Text("Delete")
//      }
//
//      Button(onClick = {
//        searching = false
//        productName = ""
//        productQuantity = ""
//      }) {
//        Text("Clear")
//      }
//    }
//
//    LazyColumn(
//      modifier = Modifier
//        .fillMaxWidth()
//        .padding(10.dp)
//    ) {
//      val list = if (searching) searchResults else allProducts
//      item {
//        TitleRow(
//          head1 = "ID",
//          head2 = "Product",
//          head3 = "Quantity"
//        )
//      }
//      items(list) { product ->
//        ProductRow(
//          id = product.id,
//          name = product.productName,
//          quantity = product.quantity
//        )
//      }
//    }
//  }
//}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  ComposeQuizTheme {
    Greeting("Android")
  }
}

@Composable
fun TitleRow(
  head1: String,
  head2: String,
  head3: String
) {
  Row(
    modifier = Modifier
      .background(MaterialTheme.colorScheme.primary)
      .fillMaxWidth()
      .padding(5.dp)
  ) {
    Text(
      text = head1,
      color = Color.White,
      modifier = Modifier.weight(0.1f)
    )
    Text(
      text = head2,
      color = Color.White,
      modifier = Modifier.weight(0.2f)
    )
    Text(
      text = head3,
      color = Color.White,
      modifier = Modifier.weight(0.2f)
    )
  }
}

@Composable
fun ProductRow(
  id: Int,
  name: String,
  quantity: Int
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(5.dp)
  ) {
    Text(
      text = id.toString(),
      modifier = Modifier.weight(0.1f)
    )
    Text(
      text = name,
      modifier = Modifier.weight(0.2f)
    )
    Text(
      text = quantity.toString(),
      modifier = Modifier.weight(0.2f)
    )
  }
}

@Composable
fun CustomTextField(
  title: String,
  textState: String,
  onTextChange: (String) -> Unit,
  keyboardType: KeyboardType
) {
  OutlinedTextField(
    value = textState,
    onValueChange = { onTextChange(it) },
    keyboardOptions = KeyboardOptions(
      keyboardType = keyboardType
    ),
    singleLine = true,
    label = { Text(title) },
    modifier = Modifier.padding(10.dp),
    textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)
  )
}

class MainViewModelFactory(
  val application: Application
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return MainViewModel(application) as T
  }
}

class ChapterQuizViewModelFactory(
  val application: Application
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ChapterQuizViewModel(application) as T
  }
}

class ChapterViewModelFactory(
  val application: Application
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return ChapterViewModel(application) as T
  }
}