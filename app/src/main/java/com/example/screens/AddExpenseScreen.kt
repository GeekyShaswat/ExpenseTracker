package com.example.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.database.ExpenseTable
import com.example.expensetracker.R
import com.example.utils.Utils
import com.example.viewModel.AddExpenseViewModel
import com.example.viewModel.AddExpenseViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AddData(navController: NavController) {
    val addExpenseViewModel: AddExpenseViewModel =
        AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)
    var expenseEntity by remember { mutableStateOf(ExpenseTable( name =  "", amount = 0.0, date =  0L, category = "", type =  "Income")) }
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableDoubleStateOf(0.0) }
    var date by remember { mutableLongStateOf(0L) }
    var dateDialogVisibility by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("") }
    val options = listOf("Income", "Expense")
    var type by remember { mutableStateOf(options[0]) }

    Log.d("log in add Data", "$expenseEntity")

    if (dateDialogVisibility) {
        ExpenseDatePicker(
            onDismiss = { dateDialogVisibility = false },
            onDateSelected = {
                date = it
                dateDialogVisibility = false
            }
        )
    }

    LaunchedEffect(name, amount, date, category, type) {
        expenseEntity = ExpenseTable(
            name = name, amount = amount, date =  date, category =  category, type =  type
        )
        Log.d("entity", expenseEntity.toString())
    }

    Surface {
        ConstraintLayout {
            val (card, button, bg, add) = createRefs()
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
            Statement(modifier = Modifier
                .constrainAs(add) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 25.dp),
                navController
            )
            CardWithDetails(
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(add.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                name = name,
                onNameChange = { name = it },
                amount = amount,
                onAmountChange = { amount = it },
                date = date,
                category = category,
                onCategoryChange = { category = it },
                type = type,
                onTypeChange = { type = it },
                onShowDateDialog = { dateDialogVisibility = true }
            )

            Buttons(
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(top = 20.dp),
                expenseEntity,
                addExpenseViewModel,
                buttonClicked = { clicked ->
                    if (clicked) {
                        name = ""
                        amount = 0.0
                        date = 0L
                        category = ""
                        type = options[0]
                    }
                },
                navController
            )
        }
    }
}

@Composable
fun Statement(modifier: Modifier , navController : NavController){
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(20.dp) ){
        Image(painter = painterResource(R.drawable.left_icon) ,
            contentDescription = null,
            Modifier.size(35.dp).clickable{ navController.popBackStack()}
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Add Expense" ,
            fontSize = 21.sp,
            modifier = Modifier.align(Alignment.CenterVertically),
            color = colorResource(R.color.white)
            )
        Spacer(modifier = Modifier.weight(1f))
        Image(painter = painterResource(R.drawable.threedot),
            contentDescription = null,
            Modifier
                .size(25.dp)
                .align(Alignment.Bottom)
            )
    }
}
@Composable
fun CardWithDetails(
    modifier: Modifier,
    name: String,
    onNameChange: (String) -> Unit,
    amount: Double,
    onAmountChange: (Double) -> Unit,
    date: Long,
    category: String,
    onCategoryChange: (String) -> Unit,
    type: String,
    onTypeChange: (String) -> Unit,
    onShowDateDialog: () -> Unit
) {
    val options = listOf("Income", "Expense")

    Card(
        modifier
            .padding(start = 20.dp, end = 20.dp, top = 40.dp)
            .height(550.dp),
        colors = CardDefaults.cardColors(Color.White),
        border = BorderStroke(4.dp, colorResource(R.color.bggreen))
    ) {
        Column(modifier.padding(20.dp)) {
            Text(text = "Name", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(value = name, onValueChange = onNameChange, modifier.padding(bottom = 20.dp))

            Text(text = "Amount", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value =if(amount != 0.0) "$amount" else "",
                onValueChange = { onAmountChange(it.toDoubleOrNull() ?: 0.0) },
                modifier.padding(bottom = 20.dp),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )

            Text(text = "Date", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = if (date == 0L) "" else Utils.formatDateToHumanReadable(date),
                onValueChange = {},
                modifier
                    .padding(bottom = 20.dp)
                    .clickable { onShowDateDialog() },
                enabled = false
            )

            Text(text = "Category", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            ExpenseDropDown(
                listOf("Netflix", "Paypal", "Salary", "Starbucks", "Upwork"),
                selectedItem = category,
                onSelectedItem = onCategoryChange,
                modifier = modifier
            )

            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                options.forEach { option ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        RadioButton(selected = (option == type), onClick = { onTypeChange(option) })
                        Text(text = option, modifier = Modifier.padding(start = 5.dp))
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropDown(listOfItems: List<String>, onSelectedItem: (String) -> Unit ,modifier: Modifier, selectedItem: String) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            modifier = modifier
                .fillMaxWidth()
                .padding(end = 35.dp)
                .menuAnchor(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOfItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item , fontSize = 15.sp) },
                    onClick = {
                        onSelectedItem(item)
                        expanded = false // Correctly close the menu
                    },
                    modifier
                        .background(color = Color.White)
                        .border(1.dp, color = Color.Gray, shape = RectangleShape)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePicker(
    onDateSelected : (date : Long) -> Unit,
    onDismiss : () -> Unit
){
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L
    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = { TextButton(onClick = {onDateSelected(selectedDate)}){
            Text(text = "Save", color = colorResource(R.color.AmountRed))
        } },
        dismissButton = {
            TextButton(onClick = { onDateSelected(selectedDate) }) {
                Text(text = "Cancel" , color = colorResource(R.color.AmountGreen))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun Buttons(
    modifier: Modifier,
    entity :ExpenseTable,
    addExpenseViewModel: AddExpenseViewModel,
    buttonClicked: (Boolean) -> Unit,
    navController : NavController
)
{
    var context = LocalContext.current
    Row(
        modifier = modifier
            .padding(27.dp)
            .fillMaxWidth()
    ) {
        // Save Card
        Card(
            colors = CardDefaults.cardColors(colorResource(R.color.bggreen)),
            modifier = Modifier
                .weight(1f)
                .clickable {
                    if (entity.type.isNotEmpty()
                        && entity.name.isNotEmpty()
                        && entity.amount > 0.0
                        && entity.category.isNotEmpty()
                        && entity.date != 0L
                    ) {
                        CoroutineScope(Dispatchers.Main).launch {
                            if(addExpenseViewModel.addExpense(entity)){
                                navController.popBackStack()
                            }
                        }
                        Toast.makeText(context, "Data Added", Toast.LENGTH_SHORT).show()
                        buttonClicked(true)


                    } else {
                        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    }
                }
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Save", color = Color.White,fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.width(16.dp)) // Add spacing between cards

        // Cancel Card
        Card(
            colors = CardDefaults.cardColors(colorResource(R.color.bggreen)),
            modifier = Modifier
                .weight(1f)
                .clickable { navController.popBackStack() }
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Cancel", color = Color.White,fontSize = 14.sp)
            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun PreviewAddData(){
    AddData(navController = NavController(LocalContext.current))
}