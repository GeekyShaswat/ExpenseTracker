package com.example.screens
import android.util.Log
import android.view.View
import com.example.expensetracker.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.database.ExpenseTable
import com.example.utils.Utils.formatDateToHumanReadable
import com.example.utils.Utils.formatDecimalTo2Digit
import com.example.viewModel.HomeViewModel
import com.example.viewModel.HomeViewModelFactory
import com.example.viewModel.NameViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    navController: NavController,
    nameViewModel: NameViewModel
){
    val namee = nameViewModel.username.collectAsState().value
    LaunchedEffect(namee) {
        Log.d("CardForLogin", "Observed username: $namee")
    }

    val viewModel: HomeViewModel = HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)
    val list = viewModel.expenseDao.collectAsState(initial = emptyList())
    val balance = viewModel.getBalance(list.value)
    val income = viewModel.getIncome(list.value)
    val expense = viewModel.getExpense(list.value)

    Surface(modifier = Modifier.fillMaxSize()){
        ConstraintLayout(modifier = Modifier.fillMaxSize())
        {
            val (card,name , transaction , bg , addDailog) = createRefs()
            Image(painter = painterResource(R.drawable.backgroundwithname),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(bg) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxSize(),
                alignment = Alignment.TopStart
            )
            Box (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 70.dp, end = 30.dp)
                .constrainAs(name) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ){
                Column(modifier = Modifier) {

                    Text(text = "Good Afternoon," ,
                        fontSize = 16.sp ,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )

                    Text(text = namee,
                        fontSize = 20.sp ,
                        color = Color.White ,
                        fontWeight = FontWeight.Bold
                    )
                }

                Image(painter = painterResource(R.drawable.notification) ,
                    contentDescription = null ,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterEnd)
                        .clickable{
                            navController.navigate("notification")
                        }
                    )
            }
            CardDetail(modifier = Modifier.constrainAs(card) {
                top.linkTo(name.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, totalBal = balance , income = income , expense = expense,navController)

            TransactionHistory(modifier = Modifier.constrainAs(transaction){
                top.linkTo(card.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(addDailog.top)
                height= Dimension.fillToConstraints
            },
                list = list.value,
                viewModel
            )
            Image(painter = painterResource(R.drawable.add_icon) ,
                contentDescription = null ,
                alignment = Alignment.BottomCenter,
                modifier = Modifier.constrainAs(addDailog){
                    top.linkTo(transaction.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                }.size(70.dp).clickable{
                    navController.navigate("add")
                }.clipToBounds()
            )
        }
    }

}

@Composable
fun CardDetail(
    modifier: Modifier,
    totalBal: Double,
    income: Double ,
    expense: Double,
    navController: NavController
){
    Card(
        modifier = modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp) ,
        colors = CardDefaults.cardColors(Color(0xFF2F7E79)),
        shape = RoundedCornerShape(20.dp)
    ){
        Box(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 30.dp, bottom = 20.dp)
            .fillMaxWidth()){
            Column {
                Text(text = "Total Balance " ,
                    fontSize = 16.sp ,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                var bal = formatDecimalTo2Digit(totalBal)
                Text(text = "₹ $bal" ,
                    fontSize = 25.sp ,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Image(painter = painterResource(R.drawable.userprofileimage) ,
                contentDescription = null ,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopEnd).clickable{
                        navController.navigate("profile")
                    }

            )
        }
        Column(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 30.dp)
            .fillMaxWidth())
        {
            Row {
                Image(painter = painterResource(R.drawable.arrow_down) ,
                    contentDescription = null ,
                    modifier = Modifier.size(20.dp)

                    )
                Text(text = "Income" ,
                    fontSize = 14.sp,
                    color = Color(0xFFD0E5E4),
                    modifier = Modifier.padding(start = 7.dp , top= 2.dp , bottom = 5.dp)

                    )
                Spacer(modifier = Modifier.weight(1f))
                Image(painter = painterResource(R.drawable.arrow_down) ,
                    contentDescription = null ,
                    modifier = Modifier.size(20.dp)

                )
                Text(text = "Expense" ,
                    fontSize = 14.sp,
                    color = Color(0xFFD0E5E4),
                    modifier = Modifier.padding(start = 7.dp , top= 2.dp , bottom = 5.dp)

                )
            }

            Row {
                var inc = formatDecimalTo2Digit(income)
                Text(text = "₹ $inc" ,
                    fontSize = 20.sp ,
                    color = Color.White
                )
                Spacer(modifier = Modifier.weight(1f))

                var exp = formatDecimalTo2Digit(expense)
                Text(text = "₹ $exp" ,
                    fontSize = 20.sp ,
                    color = Color.White
                )
            }
            }

    }
}


@Composable
fun TransactionHistory(
    modifier: Modifier,
    list : List<ExpenseTable>,
    viewModel : ViewModel
){
    LazyColumn(modifier = modifier.padding(top = 20.dp , start = 20.dp, end = 20.dp ).height(350.dp)){
        item{
            Box(
                modifier
                    .fillMaxWidth()
                    .padding(20.dp)) {
                Text(
                    text = "Transactions History",
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Text(
                    text = "See all",
                    fontSize = 15.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
        items(list) { item ->
            TransactionItem(
                item,
                if (item.type == "Income") R.drawable.paypallogo else R.drawable.netflixlogo,
                item.name,
                item.date,
                if (item.type == "Income") "+₹ ${item.amount}" else "-₹ ${item.amount}",
                if (item.type == "Income") R.color.AmountGreen else R.color.AmountRed,
                viewModel
            )
        }


    }
}

@Composable
fun TransactionItem(
    item : ExpenseTable,
    image: Int,
    name: String,
    date: Long,
    amount: String,
    color: Int,
    homeViewModel : ViewModel
){
    var formatDate = formatDateToHumanReadable(date)
    Row (modifier = Modifier.padding(start = 20.dp , end = 20.dp , bottom = 12.dp)){
        Image(painter = painterResource(image) ,
            contentDescription = null ,
            modifier = Modifier.size(40.dp)
            )
        Column(modifier = Modifier.padding(start = 5.dp)) {
            Text(text = name ,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 3.dp)
                )
            Text(text = formatDate,
                fontSize = 13.sp,
                color = Color(0xFF666666)
                )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = amount,
            fontSize = 18.sp,
            color = colorResource(color),
            modifier = Modifier.padding(vertical = 8.dp)
            )
        Icon(painter = painterResource(R.drawable.delete_icon),
            contentDescription = "delete",
            modifier = Modifier.clickable{
                (homeViewModel as HomeViewModel).deleteExpense(item)
            }.size(30.dp).padding(start = 5.dp, bottom = 5.dp).align(Alignment.CenterVertically),
            tint = Color.Gray
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen(){
    HomeScreen(navController = NavController(LocalContext.current), nameViewModel = NameViewModel())
}