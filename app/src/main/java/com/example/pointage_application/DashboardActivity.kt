package com.example.pointage_application

import ErrorAlert
import android.app.Activity
import android.app.Application
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.MutableBoolean
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pointage_application.ui.theme.PointageapplicationTheme
import com.example.pointage_application.view_models.DashboardViewModel
import com.example.pointage_application.view_models.UpdatePasswordViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PointageapplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DashboardScreen()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        SessionManagements(this.application).removeToken()
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogFrom() {
    AlertDialog(onDismissRequest = { /*TODO*/ }) {
        Column {
            OutlinedTextField(
                value = "info",
                onValueChange = { },
                placeholder = {
                    Text(
                        text = "ip address",
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                prefix = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_visibility_24),
                        contentDescription = "image view",
                        Modifier
                            .padding(0.dp, 0.dp, 4.dp, 0.dp)
                            .size(24.dp)
                    )
                }

            )
        }
    }
}


@Composable
fun DashboardScreen(viewmodel: DashboardViewModel = viewModel()) {

    val mainPadding = 16.dp

    val activity = LocalContext.current as Activity

    val snackbarHostState = remember { SnackbarHostState() }

    val currentAdmin = viewmodel.currentAdmin.observeAsState()
    val errorObserver = viewmodel.error.observeAsState()





    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        if (it.data != null && it.data!!.getBooleanExtra("isUpdated", false)) {
            Toast.makeText(activity, "PASSWORD UPDATED!", Toast.LENGTH_LONG).show()
            println(it.data!!.getStringExtra("new_password"))
        }
    }

    var showDialogStream by remember {
        mutableStateOf(false)

    }




    AnimatedVisibility(errorObserver.value != null) {
        if (errorObserver.value != null) {
            ErrorAlert(title = errorObserver.value!!.title, body = errorObserver.value!!.message) {
                viewmodel.error.postValue(null)
            }
        }
    }

    if (currentAdmin.value == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else
        Scaffold(
            snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
            }
        ) { it ->
            Box(
                Modifier
                    .alpha(if (showDialogStream) 0.6f else 1f)
                    .padding(it)
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            0.3f to MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            0.6f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            0.9f to MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            1f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    )
            ) {

                Column {
                    HeaderPre(username = viewmodel.currentAdmin.value!!.name.split(' ')[0]) {
                        Intent(activity,  AdminSettingsActivity::class.java).also {
                            it.putExtra("id_", viewmodel.currentAdmin.value!!.id_emp)
                            it.putExtra("old_password", viewmodel.currentAdmin.value!!.password)
                            settingsLauncher.launch(it)
                        }
                    }

                    ElevatedCard(modifier = Modifier
                        .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                        .fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.Absolute.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Icon(painter = painterResource(id = R.drawable.reminder), contentDescription = "icon date", modifier = Modifier
                                .size(32.dp)
                                .padding(top = 5.dp))
                            Text(text = LocalDate.now().toString(), modifier = Modifier
                                .padding(vertical = 16.dp), fontWeight = FontWeight.Bold)


                        }
                    }


                    Column(Modifier.padding(mainPadding)) {



                        ItemsOfBean("History", R.drawable.history, "see history of employers present") {
                            Intent(activity, HistoryActivity::class.java).also {
                                activity.startActivity(it)
                            }
                        }
                        Spacer(modifier = Modifier.height(mainPadding / 2))
                        ItemsOfBean("Employers", R.drawable.employment, "see and add, update employers") {
                            Intent(activity, EmployerActivity::class.java).also {
                                activity.startActivity(it)
                            }
                        }
                        Spacer(modifier = Modifier.height(mainPadding / 2))
                        ItemsOfBean("Settings", R.drawable.admin_, "update your info") {

                            Intent(activity,  AdminSettingsActivity::class.java).also {
                                it.putExtra("id_", viewmodel.currentAdmin.value!!.id_emp)
                                it.putExtra("old_password", viewmodel.currentAdmin.value!!.password)
                                settingsLauncher.launch(it)
                            }
                        }

                        Spacer(modifier = Modifier.height(mainPadding / 2))
                        ItemsOfBean("Logout", R.drawable.logout, "logout and clear your session") {
                            Intent(activity, LoginActivity::class.java).also {
                                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                activity.startActivity(it)
                            }
                        }
                    }


                }

            }
        }

}


@Composable
fun HeaderPre(username: String, listener: () -> Unit) {


    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEndPercent = 16, bottomStartPercent = 16))
                .background(MaterialTheme.colorScheme.background)
                .height(110.dp)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {

            Text(
                text = "Welcome, $username",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = listener) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "setting",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsOfBean(text: String, id_: Int,description: String? = "desc", listener: () -> Unit) {


    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            listener()
        }

//        onClick = {
//            if (text == "Users"){
//                val intent = Intent(context, UserActivity::class.java)
//                context.startActivity(intent)
//            }else if (text == "Admins") {
//
//
//            }else if (text == "Cars") {
//                context.startActivity(Intent(context, CarsActivity::class.java))
//
//            }else if (text == "History") {
//                val intent = Intent(context, HistoryActivity::class.java)
//                context.startActivity(intent)
//
//            }else if (text == "Setting") {
//                val intent = Intent(context, SettingActivity::class.java)
//                context.startActivity(intent)
//            }else {
//                Log.d("TAG", "ItemsOfBean: $text")
//            }
//
//        }

    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = id_),
                contentDescription = "Hello World",
                modifier = Modifier.size(70.dp)

            )
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = description!!,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraLight,
                    color = Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(painter = painterResource(id = R.drawable.arrow), contentDescription = "show more", modifier = Modifier.size(24.dp), tint = Color(0xFFFFCB27))
            }
        }
    }

}




//@Composable
//fun BarChartDataFromDB(daysInfo: List<DayInfo>) {
//
//    val configuration = LocalConfiguration.current
//    val colors = listOf(Color.Black, Color.Yellow, Color.Cyan, Color.Green, Color.LightGray)
//    val ourList = mutableListOf<BarData>()
//    var maxNumber = 0
//    for (i in daysInfo.indices) {
//        val bar = BarData(
//            Point(
//                i.toFloat(),
//                daysInfo[i].howMany.toFloat(),
//            ),
//            label = daysInfo[i].day.toString(),
//            color = colors[i]
//        )
//        if (daysInfo[i].howMany > maxNumber) {
//            maxNumber = daysInfo[i].howMany
//        }
//        ourList.add(bar)
//    }
//
//
//    val xAxisData = AxisData.Builder()
//        .axisStepSize(400.dp)
//        .axisConfig(AxisConfig())
//
//        .steps(daysInfo.size)
//        .labelData { index -> daysInfo[index].day.split("-").last() }
//        .build()
//
//    val yAxisData = AxisData.Builder()
//        .steps(maxNumber / 10)
//        .axisStepSize(30.dp)
//        .labelAndAxisLinePadding(20.dp)
//        .labelData { index -> (index * (10)).toString() }
//        .build()
//
//
//    val barChartDataOne = BarChartData(
//        chartData = ourList,
//        xAxisData = xAxisData,
//        yAxisData = yAxisData,
//    )
//
//
//    BarChart(
//        modifier = Modifier
//            .height(400.dp)
//            .fillMaxWidth(),
//        barChartData = barChartDataOne
//    )
//
//}


//@Preview(showSystemUi = true)
//@Composable
//fun GetAllData() {
//    BarChartDataFromDB(
//        daysInfo =
//        listOf(
//            DayInfo("32/32/323", 10),
//            DayInfo("43234/3423/43234", 50),
//            DayInfo("32/32/323", 50),
//            DayInfo("32/32/323", 30),
//            DayInfo("32/32/323", 60),
//        )
//    )
//}





@Preview(showSystemUi = true)
@Composable
fun DashboardScreenPreView() {

    val mainPadding = 16.dp



    var showDialogStream by remember {
        mutableStateOf(false)
    }






        Scaffold { it ->
            Box(
                Modifier
                    .alpha(if (showDialogStream) 0.6f else 1f)
                    .padding(it)
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            0.3f to MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            0.6f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            0.9f to MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            1f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    )
            ) {

                Column {
                    HeaderPre(username = "user") {
                        println("Clicked")
                    }



                    ElevatedCard(modifier = Modifier
                        .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                        .fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.Absolute.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Icon(painter = painterResource(id = R.drawable.reminder), contentDescription = "icon date", modifier = Modifier
                                .size(32.dp)
                                .padding(top = 5.dp))
                            Text(text = LocalDate.now().toString(), modifier = Modifier
                                .padding(vertical = 16.dp), fontWeight = FontWeight.Bold)


                        }
                    }


                    Column(Modifier.padding(mainPadding)) {



                        ItemsOfBean("History", R.drawable.history, "see history of employers present") {

                        }
                        Spacer(modifier = Modifier.height(mainPadding / 2))
                        ItemsOfBean("Employers", R.drawable.employment, "see and add, update employers") {

                        }
                        Spacer(modifier = Modifier.height(mainPadding / 2))
                        ItemsOfBean("Settings", R.drawable.admin_, "update your info") {
                        }

                        Spacer(modifier = Modifier.height(mainPadding / 2))
                        ItemsOfBean("Logout", R.drawable.logout, "logout and clear your session") {
                            println("Logout Clicked")
                        }
                    }
                }

            }
        }

}
