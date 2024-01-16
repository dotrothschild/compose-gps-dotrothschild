package com.inzhood.library.kotlingpsdotrothschild

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.location.LocationProvider
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inzhood.library.gpslibrary.hasPermission
import com.inzhood.library.gpslibrary.model.TransportSpeeds
import com.inzhood.library.gpslibrary.route.RouteUI
import com.inzhood.library.kotlingpsdotrothschild.ui.theme.KotlinGpsDotrothschildTheme


class MainActivity : ComponentActivity() {
   // no factory private val viewModel: MainActivityViewModel by viewModels()
   private val viewModel: MainActivityViewModel by viewModels() {
       MainActivityViewModelFactory(this.application)
   }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getLastKnownLocation()
        viewModel.startUpdatingLocation()
        viewModel.updateCandidateSpeed(getString(R.string.touch_and_select_from_list)) // Modify to view model factory when getting complex
        setContent {
            MaterialTheme {
                GpsSelectionScreen(viewModel)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }

    }
}

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun GpsSelectionScreen(viewModel: MainActivityViewModel)
// this is when not explicitly passing the viewModel as param (viewModel: MainActivityViewModel = remember { MainActivityViewModel() })
{
    val currentLocation by viewModel.currentLocation.collectAsState(initial = stringResource(R.string.location_not_found))
    // this was in setContent of class mainActivity, moved here to try stop crashing
    KotlinGpsDotrothschildTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LocationDisplay(viewModel) // Add the LocationDisplay composable

            Spacer(modifier = Modifier.height(16.dp))
            val chooseSelectionText = stringResource(R.string.touch_and_select_from_list)
            Column(
                modifier = Modifier
                    .fillMaxSize() // Expand to fill parent
                    .wrapContentSize(Alignment.Center) // Center within parent
            )
            {
                Text(
                    text = "Current Location: $currentLocation",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                //val selectionSpeeds = listOf("Speed 1", "Speed 2", "Speed 3", "Speed 4", "Speed 5")
                val selectionSpeeds = TransportSpeeds.getModeKeys()
                var buttonEnabled by remember { mutableStateOf(viewModel.candidateSpeed.value != chooseSelectionText) }
                Spacer(modifier = Modifier.height(16.dp))
                Dropdown(Modifier.wrapContentSize(),
                    list = selectionSpeeds,
                    onItemSelected = { item ->
                        viewModel.updateCandidateSpeed(item)
                        buttonEnabled = item != chooseSelectionText
                    }) //dropdown list

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.increment() // just for testing purposes, current location is updated in the viewModel
                        viewModel.updateCurrentSpeed()

                    },
                    enabled = buttonEnabled,
                    modifier = Modifier
                        .wrapContentSize()
                        .height(56.dp)
                        .padding(8.dp)
                )
                {
                    Text(text = stringResource(R.string.confirm))
                } // end of Column
                Spacer(modifier = Modifier.height(16.dp))

                val currentSpeed by viewModel.currentSpeed.collectAsState(initial = stringResource(R.string.none_selected))
                Text(
                    text = "Current Speed: $currentSpeed",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                val context = LocalContext.current

                // Simplified state management for dialog trigger
                val showSaveDialog = remember(context) { mutableStateOf(false) }

                // Button to trigger the dialog
                Button(
                    onClick = { showSaveDialog.value = true },
                    enabled = true,
                    modifier = Modifier
                        .wrapContentSize()
                        .height(56.dp)
                        .padding(8.dp)
                )
                {
                    Text(text = stringResource(R.string.save))
                }
                if (showSaveDialog.value) {
                    RouteUI.showSaveRouteDialog(context as Activity)
                    showSaveDialog.value = false
                }
                //****show load dialog
                Spacer(modifier = Modifier.height(16.dp))
                val showLoadDialog = remember(context) { mutableStateOf(false) }
                Button(
                    onClick = { showLoadDialog.value = true },
                    enabled = true,
                    modifier = Modifier
                        .wrapContentSize()
                        .height(56.dp)
                        .padding(8.dp)
                ) {
                    Text(text = stringResource(R.string.load))
                }
                if (showLoadDialog.value) {
                    RouteUI.showLoadRouteDialog(context as Activity)
                    showLoadDialog.value = false
                }


            } // end of Modifier
        }// end of Surface
    } // end of KotlinGpsDotrothschildTheme

} // end of GpsSelectionScreen


@Composable
fun LocationDisplay(viewModel: MainActivityViewModel) {
    val location by viewModel.location.observeAsState()

    LaunchedEffect(true) {
        viewModel.getLastKnownLocation()
        viewModel.startUpdatingLocation()
    }

    Text(
        text = location?.let { showLocation(it) } ?: stringResource(R.string.no_location),
        modifier = Modifier.semantics { contentDescription = "Location" }
    )// Accessibility
}

fun showLocation(location: Location): String {
    return "${location.latitude}, ${location.longitude}"
}

@Preview(showBackground = true)
@Composable
fun GpsSelectionScreenPreview() { // need this
    val context = LocalContext.current
    GpsSelectionScreen(MainActivityViewModel(context))
}
