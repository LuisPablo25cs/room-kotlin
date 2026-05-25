package cardenas.pablo.tareasapp.ui.theme

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cardenas.pablo.tareasapp.R

@Composable
fun SearchBar(
    searchInput: String,
    onSearchInputChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        value = searchInput,
        onValueChange = onSearchInputChanged,
        placeholder = {
            Text(
                text = stringResource(
                    R.string.search_placeholder
                )
            )
        },
        trailingIcon = {
            IconButton(onClick = onSearchClicked) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(
                        R.string.search_button_desc
                    )
                )
            }
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth(

        )
    )
}