package com.example.urbanyogi.composables

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collection.mutableVectorOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.urbanyogi.R
import com.example.urbanyogi.navigation.Screen
import com.example.urbanyogi.repository.RegisterRepository
import com.example.urbanyogi.ui.theme.primaryColor
//import androidx.navigation.compose.navigate
import com.example.urbanyogi.ui.theme.whiteBackground
import com.example.urbanyogi.viewmodel.RegisterViewModel
import com.example.urbanyogi.viewmodel.RegisterViewModelFactory

@Composable
fun LoginPage(
    navController: NavController,
    registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModelFactory(RegisterRepository())
    )
) {
    val signedIn: Boolean by registerViewModel.signedIn.observeAsState(false)

    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }

    val passwordVisible = remember {
        mutableStateOf(false)
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .padding(10.dp)
                .background(whiteBackground)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Login",
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text(text = "Email address") },
                    placeholder = { Text(text = "Email address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_visible),
                                contentDescription = "Password visibility",
                                tint = if (passwordVisible.value) primaryColor else Color.Gray
                            )
                        }
                    },
                    label = { Text(text = "Password") },
                    placeholder = { Text(text = "Password") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Button(
                    onClick = {
                        registerViewModel.signIn(email.value, password.value)
                        if (signedIn) {
                            navController.navigate(Screen.HomePage.route + "/${email.value}")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp),
                ) {
                    Text(
                        text = "Login"
                    )
                }

                Spacer(modifier = Modifier.padding(20.dp))
                Text(
                    text = "Don't have an account? Register",
                    modifier = Modifier.clickable(onClick = {
                        navController.navigate(Screen.RegisterPage.route) {
                            popUpTo = navController.graph.startDestinationId
                            launchSingleTop = true
                        }
                    })
                )
                Spacer(modifier = Modifier.padding(20.dp))
            }
        }
    }
}