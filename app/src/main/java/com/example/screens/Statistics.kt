package com.example.screens

import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.ui.theme.InterFont
import com.example.utils.Utils
import com.example.viewModel.StatsViewModel
import com.example.viewModel.StatsViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun Stats(navController: NavController) {
    Scaffold(
        topBar = {
            // Pass the same navController to TopBar
            TopBar(modifier = Modifier, navController = navController)
        }
    ) { paddingValues ->
        val viewModel: StatsViewModel = viewModel(factory = StatsViewModelFactory(LocalContext.current))
        val dataState = viewModel.entries.collectAsState(emptyList())
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            val chartEntries = viewModel.getEntriesForChart(dataState.value)
            LineChartView(chartEntries)

            Spacer(modifier = Modifier.padding(20.dp))

            Text("Expenses", fontSize = 20.sp , modifier = Modifier.padding(start = 20.dp, end = 20.dp), textDecoration = TextDecoration.Underline, color = Color.Gray )
            LazyColumn(modifier = Modifier.padding(20.dp)) {
                items(chartEntries){
                    Row(horizontalArrangement = Arrangement.SpaceBetween , modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                        Text(text = Utils.formatDateToHumanReadable(it.x.toLong()), fontSize = 15.sp , color = Color.Black)
                        Text(text = "₹"+it.y.toString(), fontSize = 15.sp , color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(modifier: Modifier, navController: NavController) {
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
            text = "Statistics",
            modifier = Modifier.align(Alignment.CenterVertically),
            fontSize = 20.sp,
            fontFamily = InterFont,
            color = Color.Black,
        )

        Image(
            painter = painterResource(R.drawable.notification),
            contentDescription = "notification",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(40.dp)
                .clickable{
                    navController.navigate("navigation")
                }
        )

    }
    Spacer(modifier.padding(30.dp))
}

@Composable
fun LineChartView(entries: List<Entry>) {
    val context = LocalContext.current
    AndroidView(
        factory = {
            // Inflate the XML layout for the chart
            val view = LayoutInflater.from(context).inflate(R.layout.stats_line_chart, null)
            view
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(20.dp)
    ) { view ->
        val lineChart = view.findViewById<LineChart>(R.id.chart)
        val dataset = LineDataSet(entries, "Expenses").apply {
            color = android.graphics.Color.parseColor("#FF338680")
            valueTextColor = android.graphics.Color.BLACK
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.RIGHT
            setDrawFilled(true)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextSize = 12f
            valueTextColor = android.graphics.Color.parseColor("#FF338680")
            val drawable = ContextCompat.getDrawable(context,R.drawable.char_gradient)
            fillDrawable = drawable
        }
        lineChart.xAxis.valueFormatter =
            object : com.github.mikephil.charting.formatter.ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return Utils.formatDateForChart(value.toLong()) // Ensure proper formatting
                }
            }
        lineChart.xAxis.granularity = 86400000f // 1 day in milliseconds
        lineChart.xAxis.isGranularityEnabled = true
        lineChart.data = com.github.mikephil.charting.data.LineData(dataset)
        lineChart.axisLeft.isEnabled = false
        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.invalidate() // Refresh the chart view
    }
}

@Preview(showBackground = false)
@Composable
fun PreviewStats() {
    // For preview purposes, a dummy NavController is acceptable
    Stats(navController = NavController(LocalContext.current))
}
