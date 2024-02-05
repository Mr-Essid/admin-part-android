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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pointage_application.components.TopAppBarApplication
import com.example.pointage_application.models.ErrorMessage
import com.example.pointage_application.ui.theme.PointageapplicationTheme
import com.example.pointage_application.view_models.LoginViewModel
import com.example.pointage_application.view_models.UpdatePasswordViewModel

class AdminSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PointageapplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SettingsScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting7(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview6() {
    PointageapplicationTheme {
        SettingsScreen()
    }
}



@Composable
fun SettingsScreen( viewModel: UpdatePasswordViewModel = viewModel()) {



    val focusRequester = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current




    val context = LocalContext.current as Activity

    val id_ = context.intent.getIntExtra("id_", -1)
    val old_password = context.intent.getStringExtra("old_password")

    if (id_ == -1 || old_password == null) {
        context.finish()
    }

    // state data
    var isShowPassword by rememberSaveable {
        mutableStateOf(false)
    }


    var isShowNewPassword by rememberSaveable {
        mutableStateOf(false)
    }


    var isShowConfirmPassword by rememberSaveable {
        mutableStateOf(false)
    }



    // data observation
    val errorObserver = viewModel.isError.observeAsState()
    val dataLoading = viewModel.isLoading.observeAsState()
    val isUpdated = viewModel.isUpdated.observeAsState()







    val mainPadding = 16.dp
    val secondPadding = 8.dp
    val mainSpacer = @Composable { Spacer(modifier = Modifier.height(16.dp)) }
    val secondSpacer = @Composable { Spacer(modifier = Modifier.height(8.dp)) }



    println("The Value of viewModel is ${errorObserver.value} ${viewModel.isError.value} ")
    AnimatedVisibility(errorObserver.value != null) {
        if (errorObserver.value != null) {
            ErrorAlert(title = errorObserver.value!!.title, body = errorObserver.value!!.message) {
                focusManager.clearFocus()
                viewModel.resetError()
            }
        }
    }


    if (isUpdated.value == true) {
        Intent().also {
            it.putExtra("isUpdated", true)
            context.setResult(1, it)
            context.finish()
        }
    }




    Scaffold(
        modifier = Modifier,
        topBar =  {

            Column {

                TopAppBarApplication(title = "Settings",
                ) {
                    context.finish()
                }
                if (dataLoading.value == true) {
                    LinearProgressIndicator()
                    Log.d("TAG", "HistoryScreen: data loading... ")
                }
            }
        }
    ) { it_ -> {

    }

        Surface(
            modifier = Modifier
                .padding(it_)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            0.3f to MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            0.6f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            0.9f to MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            1f to MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    ),
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
                            painter = painterResource(id = R.drawable.software_engineer),
                            contentDescription = "image user",
                            modifier = Modifier.size(100.dp),
                        )
                    }
                    mainSpacer()
                    OutlinedTextField(
                        enabled = false,
                        value = "root",
                        onValueChange = {
                        },
                        placeholder = {
                            Text(
                                text = "username",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.secondary,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        prefix = {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = "image view",
                                Modifier.padding(0.dp, 2.dp, 4.dp, 0.dp)
                            )
                        }

                    )
                    secondSpacer()
                    OutlinedTextField(
                        value = viewModel.oldPassword,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        suffix = {
                            Box(modifier = Modifier.clickable {
                                isShowPassword = !isShowPassword
                            }) {

                                if (!isShowPassword) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                                        contentDescription = "content",
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_visibility_24),
                                        contentDescription = "content",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                            }

                        },
                        prefix = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = "lock password",
                                modifier = Modifier.padding(0.dp, 2.dp, 4.dp, 0.dp)
                            )
                        },
                        onValueChange = {
                             viewModel.oldPassword = it
                        },
                        placeholder = {
                            Text(
                                text = "old password",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        visualTransformation = if (!isShowPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.secondary,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    secondSpacer()
                    OutlinedTextField(
                        value = viewModel.newPassword,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        suffix = {
                            Box(modifier = Modifier.clickable {
                                isShowNewPassword = !isShowNewPassword
                            }) {

                                if (!isShowNewPassword) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                                        contentDescription = "content",
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_visibility_24),
                                        contentDescription = "content",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                            }

                        },
                        prefix = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = "lock password",
                                modifier = Modifier.padding(0.dp, 2.dp, 4.dp, 0.dp)
                            )
                        },
                        onValueChange = {
                            viewModel.newPassword = it
                        },

                        placeholder = {
                            Text(
                                text = "new password",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        visualTransformation = if (!isShowNewPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.secondary,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    secondSpacer()
                    OutlinedTextField(
                        value = viewModel.conFirmPassword,
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        suffix = {
                            Box(modifier = Modifier.clickable {
                                isShowConfirmPassword = !isShowConfirmPassword
                            }) {

                                if (!isShowConfirmPassword) {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                                        contentDescription = "content",
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.baseline_visibility_24),
                                        contentDescription = "content",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                            }

                        },
                        prefix = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = "lock password",
                                modifier = Modifier.padding(0.dp, 2.dp, 4.dp, 0.dp)
                            )
                        },
                        onValueChange = {
                                        viewModel.conFirmPassword = it
                        },
                        placeholder = {
                            Text(
                                text = "confirm",
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        visualTransformation = if (!isShowConfirmPassword) PasswordVisualTransformation() else VisualTransformation.None,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.secondary,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    secondSpacer()

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton (onClick = {
                            viewModel.userUpdate(id_, old_password!!)
                               }
                            , modifier = Modifier.width(120.dp))
                        {
                            Text(text = "SEND")
                        }
                        Spacer(modifier = Modifier.width(secondPadding))
                        OutlinedButton(onClick = {
                            context.finish()
                        }, Modifier.width(120.dp)) {
                            Text(text = "CANCEL")
                        }
                    }

                    mainSpacer()

                }

            }


        }
    }
}