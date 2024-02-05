package com.example.pointage_application

import ErrorAlert
import SuccessAlert
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.pointage_application.ui.theme.PointageapplicationTheme
import com.example.pointage_application.view_models.AddEmployerViewModel

class AddEmployerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PointageapplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   AddUserScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting6(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    PointageapplicationTheme {
        AddUserScreen()
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(viewModel: AddEmployerViewModel = viewModel()) {



    val activity = LocalContext.current as Activity




//     data observation
    val errorObserver = viewModel.isError.observeAsState()
    val dataLoading = viewModel.isLoading.observeAsState()
    val isAdded = viewModel.isAdded.observeAsState()





    val focusRequester = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current


    var gender by remember {
        mutableStateOf("option")
    }

    AnimatedVisibility(isAdded.value == true) {
        if (isAdded.value == true) {
            SuccessAlert("Employer Added", "Employer Add Successfully") {
                focusManager.clearFocus()
                viewModel.resetAdded()
                viewModel.resetAll()
                gender = "option"
                viewModel.isThereIsAdd = true
            }
        }
    }



    // state data













    var isExpended by remember {
        mutableStateOf(false)
    }





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

                TopAppBarApplication(title = "Add Employer",
                ) {
                    activity.setResult(1, Intent().putExtra("added", viewModel.isThereIsAdd))
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
                            painter = painterResource(id = R.drawable.add_account),
                            contentDescription = "image user",
                            modifier = Modifier.size(100.dp),
                        )
                    }

                    mainSpacer()

                    OutlinedTextField(
                        value = viewModel.name,
                        onValueChange = {
                            viewModel.name = it
                        },
                        placeholder = {
                            Text(
                                text = "name",
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
                                painter = painterResource(id = R.drawable.user),
                                contentDescription = "image view",
                                Modifier
                                    .padding(0.dp, 2.dp, 4.dp, 0.dp)
                                    .size(20.dp)
                            )
                        }

                    )

                    secondSpacer()

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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        ,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                    )


                    secondSpacer()

                    Column (
                        Modifier
                            .width(130.dp)
                            .fillMaxHeight()){

                        ExposedDropdownMenuBox(expanded = isExpended , onExpandedChange = {isExpended = !isExpended} ) {
                            OutlinedTextField(
                                value = gender ,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.menuAnchor(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpended)
                                }
                            )
                            DropdownMenu(expanded = isExpended, onDismissRequest = {  isExpended = false}, modifier = Modifier.width(130.dp)) {
                                DropdownMenuItem(text = { Text(text = "Male") }, onClick = { gender = "Male";isExpended = false;viewModel.gender = "M" }, trailingIcon = {
                                    Icon(painterResource(id = R.drawable.user) , contentDescription = "", modifier = Modifier.size(20.dp))
                                },  modifier = Modifier.fillMaxWidth())

                                DropdownMenuItem(text = { Text(text = "Female") }, onClick = { gender = "Female";isExpended = false; viewModel.gender = "F" }, trailingIcon = {
                                    Icon(painterResource(id = R.drawable.business_woman) , contentDescription = "", modifier = Modifier.size(20.dp))
                                }, modifier = Modifier.width(130.dp))
                            }
                        }
                    }

                    secondSpacer()

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton (onClick = {
                            viewModel.addEmployer()
                        }, modifier = Modifier.width(110.dp)){
                            Text(text = "SEND")
                        }
                        Spacer(modifier = Modifier.width(secondPadding), )
                        OutlinedButton(onClick = {
                            activity.setResult(1, Intent().putExtra("added", viewModel.isThereIsAdd))
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


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun ExpendedDropdown() {

    var isExpended by remember {
        mutableStateOf(false)
    }


    var gender by remember {
        mutableStateOf("option")
    }



    Column (
        Modifier
            .width(120.dp)
            .fillMaxHeight()){

        ExposedDropdownMenuBox(expanded = isExpended , onExpandedChange = {isExpended = !isExpended} ) {
            OutlinedTextField(
                value = gender ,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpended)
                }
            )
            DropdownMenu(expanded = isExpended, onDismissRequest = {  isExpended = false}, modifier = Modifier.width(120.dp)) {
                DropdownMenuItem(text = { Text(text = "Male") }, onClick = { gender = "Male" }, trailingIcon = {
                    Icon(Icons.Default.Phone , contentDescription = "")
                },  modifier = Modifier.fillMaxWidth())

                DropdownMenuItem(text = { Text(text = "Female") }, onClick = { gender = "Female" }, trailingIcon = {
                    Icon(Icons.Default.Phone , contentDescription = "" )
                }, modifier = Modifier.width(120.dp))
            }
        }
    }
}