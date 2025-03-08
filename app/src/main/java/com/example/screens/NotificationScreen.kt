package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.InterFont

@Composable
fun NotificationScreen(navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier, navController = navController)
        }
    ) {
        Box(modifier = Modifier.padding(it),contentAlignment = Alignment.Center){
            Column(horizontalAlignment = Alignment.CenterHorizontally , verticalArrangement = Arrangement.Center) {
                Text("No Notification", color = Color.Gray, fontSize = 25.sp,fontFamily = InterFont)
            }

        }
    }
}


@Composable
fun TopAppBar(modifier: Modifier, navController: NavController) {
    Row(
        modifier
            .fillMaxWidth()
            .background((colorResource(R.color.bggreen))).padding(top = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Image(
            painter = painterResource(R.drawable.left_icon),
            contentDescription = "left_icon",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(25.dp)
                .clickable {
                    navController.popBackStack()
                }
        )

        Text(
            text = "Notification",
            modifier = Modifier.align(Alignment.CenterVertically),
            fontSize = 20.sp,
            fontFamily = InterFont,
            color = Color.Black,
        )
        Text("")
    }
    Spacer(modifier.padding(30.dp))
}

@Preview
@Composable
fun PreviewNotificationScreen(){
    NotificationScreen(navController = NavController(LocalContext.current))
}