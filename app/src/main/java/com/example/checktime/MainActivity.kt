package com.example.checktime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.checktime.ui.theme.CheckTimeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = Application.database.checkTimeDao()
        val repository = CheckTimeRepository(dao)
        val viewModel = CheckTimeViewModel(repository)
        setContent {
            CheckTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CheckTimerView(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun CheckTimerView(
    modifier: Modifier = Modifier,
    viewModel: CheckTimeViewModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var textTime by remember { mutableStateOf("") }
        var textTimeRange by remember { mutableStateOf("") }
        var resultText by remember { mutableStateOf("") }
        var isShowResultButtonEnabled by remember { mutableStateOf(false) }
        var isResultTextVisible by remember { mutableStateOf(false) }

        TextButton(
            onClick = {
                viewModel.setTime()
                textTime = "${viewModel.time.value.toString()}時"
                textTimeRange =
                    "${viewModel.startTime.value.toString()} ~ ${viewModel.endTime.value.toString()}"
                isResultTextVisible = false
                isShowResultButtonEnabled = true
            },
            modifier = modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "時刻を取得",
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "指定された時刻",
            modifier = modifier.padding(4.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = textTime,
            modifier = modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = modifier.height(16.dp))

        Text(
            text = "時間の範囲",
            modifier = modifier.padding(4.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = textTimeRange,
            modifier = modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = modifier.height(16.dp))

        Button(
            onClick = {
                isResultTextVisible = true
                resultText =
                    if (viewModel.isTimeOn()) "指定の時刻は時間の範囲に含まれています" else "指定の時刻は時間の範囲に含まれていません"
                viewModel.recordHistory(resultText)
                isShowResultButtonEnabled = false
            },
            modifier = modifier.align(Alignment.CenterHorizontally),
            enabled = isShowResultButtonEnabled
        ) {
            Text(text = "結果表示")
        }

        Spacer(modifier = modifier.height(16.dp))

        if (isResultTextVisible) {
            Text(
                text = resultText,
                modifier = modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold,
                color = if (viewModel.isTimeOn()) Color.Black else Color.Red
            )
        }

        Spacer(modifier = modifier.height(32.dp))

        Divider()
        Text(
            text = "履歴",
            modifier = modifier.padding(4.dp),
            fontWeight = FontWeight.Bold
        )

        Button(
            onClick = { viewModel.updateHistory() },
            modifier = modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "履歴更新")
        }

        Spacer(modifier = modifier.height(16.dp))

        val historyList = viewModel.historyList.observeAsState().value?.toList() ?: emptyList()
        LazyColumn(modifier = modifier.fillMaxWidth()) {
            items(historyList) {
                Surface {
                    Column {
                        Row {
                            Text(text = "開始時刻 : ${it.startTime}")
                            Spacer(modifier = modifier.width(16.dp))
                            Text(text = "終了時刻 : ${it.endTime}")
                        }
                        Text(text = "指定の時刻 : ${it.time}時")
                        Text(text = "判断結果 : ${it.resultMsg}")
                    }
                    Divider()
                }
            }
        }
    }
}