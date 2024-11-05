import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.tagginginchat.data.DataSource

@Composable
fun TagEditHandler(
    tagList: List<String>,
    onHideIcon: () -> Unit
) {
    var textState by remember { mutableStateOf(TextFieldValue()) }
    var filteredTags by remember { mutableStateOf(DataSource().users.map { it.name }) }
    var showTagList by remember { mutableStateOf(false) }
    val primaryColor = Color.Blue

    Column(modifier = Modifier.padding(16.dp)) {
        // TextField for input
        BasicTextField(
            value = textState,
            onValueChange = { newText ->
                textState = newText
                val text = newText.text
                if (text.contains('@')) {
                    val currentInput = text.substringAfterLast('@')
                    filteredTags = tagList.filter { it.contains(currentInput, ignoreCase = true) }
                    showTagList = filteredTags.isNotEmpty()
                    if (showTagList) {
                        onHideIcon()
                    }
                } else {
                    showTagList = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = MaterialTheme.shapes.small)
                .padding(8.dp),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Handle the done action if needed
                }
            )
        )

        // LazyColumn for displaying filtered tags
        if (showTagList) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            ) {
                items(filteredTags) { tag ->
                    Text(
                        text = tag,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                textState = ReplaceTagInText(tag, textState, primaryColor)
                                showTagList = false
                            },
                        color = primaryColor
                    )
                }
            }
        }
    }
}

private fun ReplaceTagInText(
    tag: String,
    textState: TextFieldValue,
    tagColor: Color,
): TextFieldValue {
    val text = textState.text
    val startIndex = text.lastIndexOf('@')
    val newText = buildAnnotatedString {
        append(text.substring(0, startIndex))
        withStyle(SpanStyle(color = tagColor)) {
            append("@$tag ")
        }
    }
    return TextFieldValue(newText)
}