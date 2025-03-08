package com.example.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.InterFont


@Composable
fun MainLayer(navController: NavController){

    Surface{
        ConstraintLayout{
            val ( bgImage,textRow , image , nameemail, listOfItem ,logOutBtn) = createRefs()

            Image(painter = painterResource(R.drawable.backgroundwithname ) ,
                contentDescription = "profile_pic",
                modifier = Modifier.constrainAs(bgImage){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.fillMaxSize(),
                alignment = Alignment.TopStart
            )

            TextRow(modifier = Modifier.constrainAs(textRow){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }.padding(top=50.dp, start = 20.dp , end = 20.dp, bottom = 60.dp),
                navController)

            Image(painter = painterResource(R.drawable.profileimage),
                contentDescription = "profile_image",
                modifier = Modifier.constrainAs(image){
                    top.linkTo(textRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }.padding(top = 30.dp , start = 20.dp , end = 20.dp).size(150.dp).clip(CircleShape)
                )

            Details(Modifier.constrainAs(nameemail){
                top.linkTo(image.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },"Shaswat")

            ListOfData(modifier = Modifier.constrainAs(listOfItem){
                top.linkTo(nameemail.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            Card(modifier = Modifier.constrainAs(logOutBtn){
                top.linkTo(listOfItem.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }.clickable{
                navController.navigate("login"){
                    popUpTo(0)
                }
            },
                colors = CardDefaults.cardColors(colorResource(R.color.bggreen))
            ) {
                Text(text = "Log Out", fontFamily = InterFont , fontSize = 20.sp , modifier = Modifier.padding(10.dp), color = colorResource(R.color.offWhite))

            }
        }
    }
}

@Composable
fun TextRow(
    modifier : Modifier,
    navController: NavController
){
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
        Image( painter =  painterResource(R.drawable.left_icon),
            contentDescription = "left_icon",
            modifier = Modifier.align(Alignment.CenterVertically).size(25.dp).clickable{
                navController.popBackStack()
            }
            )

        Text(text = "Profile",
            modifier = Modifier.align(Alignment.CenterVertically),
            fontSize = 20.sp,
            fontFamily = InterFont,
            color = Color.White,

        )

        Image(painter = painterResource(R.drawable.notification),
            contentDescription = "notification",
            modifier = Modifier.align(Alignment.CenterVertically).size(40.dp).clickable{
                navController.navigate("navigation")
            }
            )
    }
}


@Composable
fun Details(modifier: Modifier,
            username : String
            ){
    Column(modifier.padding(top = 10.dp),horizontalAlignment = Alignment.CenterHorizontally){
        Text(text = username , fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 3.dp))
        Text(text = "@$username.com", fontSize = 17.sp)
    }
}


@Composable
fun ListOfData(modifier: Modifier){
    Column(modifier.padding(50.dp).fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Row(modifier.padding(bottom = 10.dp)) {
            Image(painter = painterResource(R.drawable.invitefriendimage), contentDescription = "invite",modifier.size(25.dp))
            Spacer(modifier.padding(start = 20.dp))
            Text(text = "Invite Friends", fontSize = 20.sp, modifier = Modifier.padding(start = 10.dp), fontFamily = InterFont)
        }
        Row(modifier.padding(bottom = 10.dp)) {
            Image(painter = painterResource(R.drawable.singleuser), contentDescription = "invite",modifier.size(25.dp))
            Spacer(modifier.padding(start = 20.dp))
            Text(text = "Account Info", fontSize = 20.sp, modifier = Modifier.padding(start = 10.dp), fontFamily = InterFont)
        }
        Row(modifier.padding(bottom = 10.dp)) {
            Image(painter = painterResource(R.drawable.mutliuser), contentDescription = "invite",modifier.size(25.dp))
            Spacer(modifier.padding(start = 20.dp))
            Text(text = "Personal Profile", fontSize = 20.sp, modifier = Modifier.padding(start = 10.dp), fontFamily = InterFont)
        }
        Row(modifier.padding(bottom = 10.dp)) {
            Image(painter = painterResource(R.drawable.shield), contentDescription = "invite",modifier.size(25.dp))
            Spacer(modifier.padding(start = 20.dp))
            Text(text = "Login And Security", fontSize = 20.sp, modifier = Modifier.padding(start = 10.dp), fontFamily = InterFont)
        }
        Row(modifier.padding(bottom = 10.dp)) {
            Image(painter = painterResource(R.drawable.envelope), contentDescription = "invite",modifier.size(25.dp))
            Spacer(modifier.padding(start = 20.dp))
            Text(text = "Message Center", fontSize = 20.sp, modifier = Modifier.padding(start = 10.dp), fontFamily = InterFont)
        }
        Row(modifier.padding(bottom = 10.dp)) {
            Image(painter = painterResource(R.drawable.lock), contentDescription = "invite",modifier.size(25.dp))
            Spacer(modifier.padding(start = 20.dp))
            Text(text = "Data And Privacy", fontSize = 20.sp, modifier = Modifier.padding(start = 10.dp), fontFamily = InterFont)
        }


    }
}

@Composable
@Preview(showBackground = true)
fun PreviewMainScreen(){
    MainLayer(navController = NavController(LocalContext.current))
}