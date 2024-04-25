package com.example.bsuirjournal2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bsuirjournal2.R
//import com.example.bsuirjournal.GroupList
//import com.example.bsuirjournal.Utils
import com.example.bsuirjournal2.data.DataSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGroupScreen(
    groupNumberOptions: MutableState<List<String>>,
    onGroupCardClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText = remember { mutableStateOf("") }
    var isActive by rememberSaveable { mutableStateOf(false) }
    val painter = painterResource(id = R.drawable.ic_list)
    val description = "Info"
    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 4.dp)
            .background(color = Color.White),
        query = searchText.value,
        onQueryChange = { text ->
            searchText.value = text
            groupNumberOptions.value = DataSource.search(text)
        },
        onSearch = {text ->
            isActive = false
            groupNumberOptions.value = DataSource.search(text)
        },
        active = isActive,
        onActiveChange = {
            isActive = it
        },
        placeholder = {
            Text(text = "Номер группы...", color = Color.Black)
        },
    ) {
        GroupList(groupNumberOptions = groupNumberOptions, onClick = onGroupCardClicked, painter = painter, description = description,)
    }
}

@Composable
fun SelectGroupCard(
    group: String,
    onClick: (String) -> Unit,
    modifier: Modifier,
    painter: Painter,
    contentDescription: String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .clickable { onClick(group) },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(),
    ) {
        Row(modifier = Modifier.height(75.dp)) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(width = 75.dp, height = 75.dp)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            )
            Text(
                style = TextStyle(color = Color.Black, fontSize = 18.sp),
                text = "$group",
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 22.dp)
            )
        }
    }
}

@Composable
fun GroupList(
    groupNumberOptions: MutableState<List<String>>,
    onClick: (String) -> Unit,
    painter: Painter,
    description: String,
) {
    LazyColumn {
        items(groupNumberOptions.value) { item ->
            SelectGroupCard(
                group = item,
                painter = painter,
                contentDescription = description,
                modifier = Modifier,
                onClick = onClick
            )
        }
    }
}