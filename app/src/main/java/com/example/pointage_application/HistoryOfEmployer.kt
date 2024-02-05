package com.example.pointage_application

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pointage_application.components.TopAppBarApplication
import com.example.pointage_application.models.HistoryEmployerResponse
import com.example.pointage_application.models.HistoryResponse
import com.example.pointage_application.models.UserResponse
import com.example.pointage_application.ui.theme.PointageapplicationTheme
import com.example.pointage_application.view_models.HistoryOfEmployerViewModel
import com.example.pointage_application.view_models.HistoryViewModel
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.Month

class HistoryOfEmployer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PointageapplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HistoryOfEmployerScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Greeting8(name: String, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
    ) {
        repeat(5) {
            stickyHeader {
                Text(
                    text = "Header ${it + 1}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF464549))
                )
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(10) { index ->
                Text(
                    text = "Item $index",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview7() {
    PointageapplicationTheme {
        val historyEmp = HistoryEmployerResponse(
            date = "12-02-2000",
            statut = "retard",
            id_emp_hist = 1,
            id_hist = 1
        )
        HistoryOfEmployerHistory(user = historyEmp)
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryOfEmployerHistory(user: HistoryEmployerResponse) {



    OutlinedCard(onClick = {


    }) {

        Row(
            Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.calendar_1),
                        contentDescription = "xx/xx/xxxx",
                        Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = user.date, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = if (user.statut == "retard") R.drawable.iconswarning else R.drawable.icons8ok16),
                        contentDescription = "status",
                        Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = user.statut, style = MaterialTheme.typography.labelSmall)
                }

            }
            Icon(painterResource(id = R.drawable.icon_history_not_hi), contentDescription = "Open History", modifier = Modifier.size(24.dp))
        }
    }

}











@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryOfEmployerScreen(viewModel: HistoryOfEmployerViewModel  = viewModel()) {

    val calenderState = UseCaseState()
    var isFiltered by remember {
        mutableStateOf(false)
    }



    val activity = LocalContext.current as Activity
    val id_ = activity.intent.getIntExtra("id", -1)

    if (id_ == -1) {
        activity.finish()
    }

    viewModel.loadHistoryFromTheServer(id_)
    // state from view model
    val isLoading = viewModel.isLoading.observeAsState()
    val listOfHistory = viewModel.historyList.observeAsState()

    Scaffold(
        topBar = {

            Column {

                TopAppBarApplication(title = "History"
                ) {
                    activity.setResult(1)
                    activity.finish()

                }
                if (isLoading.value == true) {
                    LinearProgressIndicator()
                    Log.d("TAG", "HistoryScreen: data loading... ")
                }
            }
        }
    ) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            if (listOfHistory.value != null)
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                    listOfHistory.value!!.forEach {map_ ->

                            stickyHeader {
                                Spacer(modifier = Modifier.height(8.dp))

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                                        Column(modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 4.dp),) {
                                            Text(text = "${Month.of(map_.key.subSequence(map_.key.indexOf('-') + 1, map_.key.length).toString().toInt())} / ${map_.key.subSequence(0, map_.key.indexOf('-'))}")
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }

                        itemsIndexed(map_.value!!) { _, item ->
                            HistoryOfEmployerHistory(item)
                        }

                        }
                    }
                }

        }
}





