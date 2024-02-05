package com.example.pointage_application

import ErrorAlert
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pointage_application.components.TopAppBarApplication
import com.example.pointage_application.models.UserResponse
import com.example.pointage_application.ui.theme.PointageapplicationTheme
import com.example.pointage_application.view_models.EmployerViewModel

class EmployerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PointageapplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserListScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting4(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview3() {
    PointageapplicationTheme {
        val user = UserResponse("email@gmail.com", "m", 1, false, "00000000", "Amine Essid", "00000", "0:0:0")
        
        UserCard(user = user) {


        }

    }
}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(user: UserResponse, clickListener: () -> Unit) {

    val activity = LocalContext.current as Activity
    OutlinedCard(onClick = clickListener


                           , modifier = Modifier.fillMaxWidth()) {
        Surface {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "user card",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.uid_card),
                            contentDescription = "user card",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = user.uid,
                            fontWeight = FontWeight.Light,
                            fontSize = TextUnit(10f, TextUnitType.Sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = "user card",
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = user.email,
                            fontWeight = FontWeight.Light,
                            fontSize = TextUnit(12f, TextUnitType.Sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.call),
                            contentDescription = "user card",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+216 ${user.mobile}",
                            fontWeight = FontWeight.Light,
                            fontSize = TextUnit(12f, TextUnitType.Sp)
                        )
                    }

                }
                Box(Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "go a head and update" , modifier = Modifier.size(24.dp))

                }
            }

        }
    }

}


@Composable
fun UserListScreen(viewmodel: EmployerViewModel = viewModel()) {


    val activity = LocalContext.current as Activity
    val isLoading = viewmodel.isLoading.observeAsState()
    val activityForR = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        println("We Are Here")
        println("intent is ${it.data}")

        if (it.data != null) {
            println("We Are Here")
            if (it.data!!.getBooleanExtra("isUpdated", false)) {
                viewmodel.loadEmployersFromTheServer()
            }
        }
    }

    val addActivityLancer = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        if (it.data != null) {
            if (it.data!!.getBooleanExtra("added", false)) {
                viewmodel.loadEmployersFromTheServer()
            }
        }


    }
    val employersList = viewmodel.employerList.observeAsState()
    val isError = viewmodel.isError.observeAsState()

    val isUpdated = activity.intent.getBooleanExtra("isUpdated", false)


    if (isUpdated) {
        viewmodel.loadEmployersFromTheServer()
    }


    // error handling
    AnimatedVisibility(visible = isError.value != null) {
        if (isError.value != null) {
            ErrorAlert(title = isError.value!!.title, body = isError.value!!.message) {
                viewmodel.resetError()
            }
        }
    }
    // end error handling


    Scaffold(
        topBar = {

            Column {

                TopAppBarApplication(
                    title = "Employers",
                ) {
                    activity.setResult(1)
                    activity.finish()

                }
                if (isLoading.value == true) {
                    LinearProgressIndicator()
                    Log.d("TAG", "HistoryScreen: data loading... ")
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {

                Intent(activity, AddEmployerActivity::class.java).also {
                    addActivityLancer.launch(it)
                }

            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add employer")
            }
        }
    ) {

        Box(Modifier.padding(it)) {
            if (employersList.value != null) {
                LazyColumn(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)) {
                    itemsIndexed(viewmodel.employerList.value!!) { _, item ->
                        UserCard(user = item)
                            {
                                Intent(activity, UpdateEmployerActivity::class.java).also { it_ ->
                                    it_.putExtra("username", item.name)
                                    it_.putExtra("email", item.email)
                                    it_.putExtra("mobile", item.mobile)
                                    it_.putExtra("uid", item.uid)
                                    it_.putExtra("gender", item.gender)
                                    it_.putExtra("id", item.id_emp)
                                    activityForR.launch(it_)
                                }
                            }

                        Spacer(modifier = Modifier.height(4.dp))

                    }

                }

            }

        }

    }
}



@Preview(showSystemUi = true)
@Composable
fun showTopAppbar() {
    
    Scaffold(
        topBar =  {

            Column {

            TopAppBarApplication(title = "History",
            ) {
            }
            LinearProgressIndicator()
            }
        }
    ) {
        Box(Modifier.padding(it)) {

        }
    }
}


