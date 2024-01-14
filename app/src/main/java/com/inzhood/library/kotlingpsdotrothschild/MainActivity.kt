package com.inzhood.library.kotlingpsdotrothschild

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.inzhood.library.kotlingpsdotrothschild.ui.theme.KotlinGpsDotrothschildTheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.liveData


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinGpsDotrothschildTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GpsSelectionScreen()
                }
            }
        }
    }
}

@Composable
fun GpsSelectionScreen(viewModel: MainActivityViewModel = remember { MainActivityViewModel() })
{
    val currentLocation = "test string"

    Spacer(modifier = Modifier.height(16.dp))
    val chooseSelectionText = stringResource(R.string.touch_and_select_from_list)
    Column(
        modifier = Modifier
            .fillMaxSize() // Expand to fill parent
            .wrapContentSize(Alignment.Center) // Center within parent
    )
    {
        var locationText by remember { mutableStateOf("Current location: $currentLocation") }
        Text(
            text = locationText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        val selectionItems = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        var selectedItem by remember { mutableStateOf(selectionItems[0]) }
        selectedItem = stringResource(id = R.string.touch_and_select_from_list)
        var buttonEnabled by remember { mutableStateOf(selectedItem != chooseSelectionText) }

        Spacer(modifier = Modifier.height(16.dp))
        Dropdown(Modifier.wrapContentSize(),
            list = selectionItems,
            onItemSelected = { item->
                selectedItem = item
                buttonEnabled = selectedItem!= chooseSelectionText
            }) //dropdown list

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.increment()
                locationText = viewModel.currentLocation.value
            },
            enabled = buttonEnabled,
            modifier = Modifier
                .wrapContentSize()
                .height(56.dp)
                .padding(8.dp)
        )
        {
            Text(text = "Confirm")
        } // end of Column
    } // end of Modifier

} // end of GpsSelectionScreen

