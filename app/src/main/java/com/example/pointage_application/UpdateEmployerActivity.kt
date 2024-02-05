package com.example.pointage_application

import ErrorAlert
import SuccessAlert
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pointage_application.components.TopAppBarApplication
import com.example.pointage_application.models.UserResponse
import com.example.pointage_application.ui.theme.PointageapplicationTheme
import com.example.pointage_application.view_models.UpdateUserViewModel

class UpdateEmployerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PointageapplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UpdateUserScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting5(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun GreetingPreview4() {

    PointageapplicationTheme {
        UpdateUserScreen()

    }

}







@SuppressLint("SuspiciousIndentation")
@Composable
fun UpdateUserScreen(viewModel: UpdateUserViewModel = viewModel()) {



    val activity = LocalContext.current as Activity




    val errorObserver = viewModel.isError.observeAsState()
    val dataLoading = viewModel.isLoading.observeAsState()
    val isUpdatedObserver = viewModel.isUpdated.observeAsState()

    val focusRequester = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    AnimatedVisibility(isUpdatedObserver.value == true) {
        if (isUpdatedObserver.value == true) {
            SuccessAlert("Employer Updated", "Employer Updated Successfully") {
                viewModel.resetUpdate()
                viewModel.isThereIsUpdate = true
                focusManager.clearFocus()
            }
        }
    }




    val idEmployer = activity.intent.getIntExtra("id", -1)
    val username = activity.intent.getStringExtra("username")
    val email = activity.intent.getStringExtra("email")
    val uid = activity.intent.getStringExtra("uid")
    val mobile = activity.intent.getStringExtra("mobile")
    val gender = activity.intent.getStringExtra("gender")


    if (username.isNullOrEmpty() || email.isNullOrEmpty() || uid.isNullOrEmpty() || mobile.isNullOrEmpty() || gender.isNullOrEmpty() || idEmployer == -1) {
        ErrorAlert(title = "Employer Error", body = "No Employer Found, Same Things Went Wrong") {
            activity.finish()
        }
    }

    if (viewModel.currentUser.value == null) {
        viewModel.setUser(UserResponse(
            email = email!!,
            uid = uid!!,
            mobile = mobile!!,
            gender = gender!!,
            id_emp = idEmployer,
            name = username!!
        ))

    }


    println("user updated from here")








    val mainPadding = 16.dp
    val secondPadding = 8.dp
    val mainSpacer = @Composable { Spacer(modifier = Modifier.height(16.dp)) }
    val secondSpacer = @Composable { Spacer(modifier = Modifier.height(8.dp)) }



    AnimatedVisibility(errorObserver.value != null) {
        if (errorObserver.value != null) {
            ErrorAlert(title = errorObserver.value!!.title, body = errorObserver.value!!.message) {
                viewModel.resetError()
                focusManager.clearFocus()
            }
        }

    }






    Scaffold(
        modifier = Modifier,
        topBar = {
            Column {

                TopAppBarApplication(title = "Details", {
                    IconButton(onClick = { Intent(activity, HistoryOfEmployer::class.java).also {
                        it.putExtra("id", viewModel.currentUser.value!!.id_emp)
                        activity.startActivity(it)
                    } }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "History")
                    }
                }
                ) {
                    val intent = Intent()
                    intent.putExtra("isUpdated", viewModel.isThereIsUpdate)
                    activity.setResult(1, intent)
                    activity.finish()

                }
                if (dataLoading.value == true) {
                    LinearProgressIndicator()
                    Log.d("TAG", "HistoryScreen: data loading... ")
                }
            }
        }
    ) { it_ ->

        Surface(
            modifier = Modifier
                .padding(it_)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier
                        .padding(mainPadding)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center
                ) {

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.update),
                            contentDescription = "image user",
                            modifier = Modifier.size(100.dp),
                        )
                    }
                    mainSpacer()
                    OutlinedTextField(
                        value = viewModel.email,
                        onValueChange = {
                            viewModel.email = it
                        },
                        placeholder = {
                            Text(
                                text = "email",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.secondary,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        prefix = {
                            Icon(
                                painter = painterResource(id = R.drawable.email),
                                contentDescription = "image view",
                                Modifier
                                    .padding(0.dp, 2.dp, 4.dp, 0.dp)
                                    .size(20.dp)
                            )
                        }

                    )
                    secondSpacer()
                    OutlinedTextField(
                        value = viewModel.uid,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        prefix = {
                            Icon(
                                painter = painterResource(id = R.drawable.uid_card) ,
                                contentDescription = "lock password",
                                modifier = Modifier
                                    .padding(0.dp, 2.dp, 4.dp, 0.dp)
                                    .size(20.dp)
                            )
                        },
                        onValueChange = {
                            viewModel.uid = it
                        },
                        placeholder = {
                            Text(
                                text = "uid",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.secondary,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                    )

                    secondSpacer()

                    OutlinedTextField(
                        value = viewModel.mobile,
                        maxLines = 1,
                        prefix = {
                            Icon(
                                painter = painterResource(id = R.drawable.mobile) ,
                                contentDescription = "lock password",
                                modifier = Modifier
                                    .padding(0.dp, 2.dp, 4.dp, 0.dp)
                                    .size(20.dp)
                            )
                        },
                        onValueChange = {
                            viewModel.mobile = it
                        },
                        placeholder = {
                            Text(
                                text = "mobile",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.secondary,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                    )

                    secondSpacer()

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton (onClick = {
                            viewModel.userUpdate()
                        }, modifier = Modifier.width(110.dp)){
                            Text(text = "SEND")
                        }
                        Spacer(modifier = Modifier.width(secondPadding), )
                        OutlinedButton(onClick = {
                            val intent = Intent()
                            intent.putExtra("isUpdated", viewModel.isThereIsUpdate)
                            activity.setResult(1, intent)
                            activity.finish()

                        }, Modifier.width(110.dp)) {
                            Text(text = "CANCEL")
                        }
                    }
                }

            }
        }
    }
}