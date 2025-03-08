package com.example.screens


import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.InterFont
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController
import com.example.viewModel.UserViewModel
import com.example.viewModel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SignUp(navController: NavController){

    var userViewModel : UserViewModel = UserViewModelFactory(LocalContext.current).create(UserViewModel::class.java)
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Surface {
        ConstraintLayout {
            var ( bgImg,text , card,logo ) = createRefs()

            Image(painter = painterResource(R.drawable.backgroundwithname),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(bgImg) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxSize(),
                alignment = Alignment.TopStart
            )
            Image(
                painter = painterResource(R.mipmap.logo_image_foreground),
                contentDescription = "logo image",
                modifier = Modifier
                    .constrainAs(logo) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    }
                    .padding(vertical = 20.dp)
                    .size(80.dp)
            )
            Text(text= "Create your Account !",
                fontSize = 18.sp,
                fontFamily = InterFont,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .constrainAs(text) {
                        top.linkTo(logo.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(bottom = 10.dp)
            )
            CardForSignUp(
                modifier = Modifier.constrainAs(card){
                    top.linkTo(text.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                name.trim(),
                onNameChange = { name = it },
                password,
                onPasswordChange = { password = it },
                userViewModel,
                navController
            )

        }
    }
}

@Composable
fun CardForSignUp(
    modifier: Modifier,
    username: String,
    onNameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    userViewModel: UserViewModel,
    navController: NavController
){
    var showPassword by remember { mutableStateOf(false) }
    var context = LocalContext.current
    Card(
        modifier
            .padding(start = 20.dp, top = 50.dp, end = 20.dp)
            .height(400.dp),
        colors = CardDefaults.cardColors(Color.White),
        border = BorderStroke(4.dp, colorResource(R.color.bggreen)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Spacer(modifier.height(20.dp))
        Column(modifier.padding(20.dp)) {
            Text(text = "Username", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,modifier = modifier.padding(bottom = 12.dp))
            OutlinedTextField(
                value = username,
                onValueChange = onNameChange,
                modifier.padding(bottom = 20.dp)
            )

            Text(text = "Password", fontSize = 16.sp, fontWeight = FontWeight.SemiBold ,modifier = modifier.padding(bottom = 12.dp))
            OutlinedTextField(
                value =password,
                onValueChange = onPasswordChange,
                modifier.padding(bottom = 20.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                visualTransformation =
                if(showPassword){
                    VisualTransformation.None
                }else{
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    if(showPassword){
                        Image(painter = painterResource(R.drawable.eye_close) ,
                            contentDescription = null,
                            modifier= Modifier
                                .clickable {
                                    showPassword = !showPassword
                                }
                                .size(20.dp)
                                .fillMaxSize()
                        )
                    }
                    else{
                        Image(painter = painterResource(R.drawable.eye_open),
                            contentDescription = null,
                            modifier= Modifier
                                .clickable {
                                    showPassword = !showPassword
                                }
                                .size(20.dp)
                                .fillMaxSize()
                        )
                    }
                }
            )
            Card(
                colors = CardDefaults.cardColors(colorResource(R.color.bggreen)),
                modifier = Modifier
                    .padding(top = 10.dp, start = 50.dp, end = 50.dp, bottom = 8.dp)
                    .clickable {
                        CoroutineScope(Dispatchers.Main).launch {
                            if(userViewModel.isUsernameAvailable(username)){
                                if (userViewModel.checkUser(username, password)) {
                                    userViewModel.insertUser(username, password)
                                    // navigation code for home screen
                                    navController.navigate("login"){
                                        popUpTo(0)
                                    }

                                    //toast for user added
                                    Toast
                                        .makeText(context, "User added", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                            else{
                                Toast
                                    .makeText(context, "Username already exist", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Sign Up", color = Color.White,fontSize = 14.sp)
                }
            }
            Text(text = "Already have an account ! Sign In ?"
                , fontSize = 12.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        //navigation code for login screen
                        navController.navigate("login")
                    }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewSignUpScreen(){
    SignUp(navController = NavController(LocalContext.current))
}