package com.inzhood.library.kotlingpsdotrothschild

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    list: List<String>,
    onItemSelected: (String) -> Unit, // the consumer of the function defines what happens when an item is selected
    disabledValue: String = ""
) {
    val modifiedList = mutableListOf(stringResource(R.string.touch_and_select_from_list))
    if (list.isEmpty()) {
        return
    } else {
        modifiedList += list
    }

    var expanded by remember { mutableStateOf(false) }
    val selectedIndex = remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
           // .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            modifiedList[selectedIndex.intValue],
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(Color.Gray)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            modifiedList.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex.intValue = index
                        expanded = false
                        onItemSelected(item)
                    },
                    enabled = item != disabledValue,
                    text = { Text(text = item) }
                )
            }
        }
    }
}