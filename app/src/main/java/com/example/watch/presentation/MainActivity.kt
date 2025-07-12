package com.example.watch.presentation

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.watch.R
import com.example.watch.presentation.theme.WatchTheme

class MainActivity : ComponentActivity() {
    private var scoreState by mutableStateOf(ScoreState())
    private val history = mutableListOf<ScoreState>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScoreApp(
                score = scoreState,
                onAddA = { addPointA() },
                onAddB = { addPointB() },
                onUndo = { undo() }
            )
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_STEM_PRIMARY, KeyEvent.KEYCODE_STEM_1 -> {
                addPointA(); true
            }
            KeyEvent.KEYCODE_STEM_2 -> {
                addPointB(); true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_STEM_PRIMARY,
            KeyEvent.KEYCODE_STEM_1,
            KeyEvent.KEYCODE_STEM_2 -> true
            else -> super.onKeyUp(keyCode, event)
        }
    }

    private fun addPointA() {
        history.add(scoreState)
        scoreState = scoreState.addPointToA()
    }

    private fun addPointB() {
        history.add(scoreState)
        scoreState = scoreState.addPointToB()
    }

    private fun undo() {
        if (history.isNotEmpty()) {
            scoreState = history.removeLast()
        }
    }
}

@Composable
fun ScoreApp(
    score: ScoreState,
    onAddA: () -> Unit,
    onAddB: () -> Unit,
    onUndo: () -> Unit
) {
    WatchTheme {
        ScoreScreen(score = score, onAddA = onAddA, onAddB = onAddB, onUndo = onUndo)
    }
}

@Composable
fun ScoreScreen(
    score: ScoreState,
    onAddA: () -> Unit,
    onAddB: () -> Unit,
    onUndo: () -> Unit
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { TimeText() }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TeamScore(
                    name = stringResource(R.string.team_a),
                    sets = score.setsA,
                    games = score.gamesA,
                    points = score.pointsA
                )
                TeamScore(
                    name = stringResource(R.string.team_b),
                    sets = score.setsB,
                    games = score.gamesB,
                    points = score.pointsB
                )
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onAddA) { Text("➕ A") }
                Button(onClick = onAddB) { Text("➕ B") }
            }
        }
        item {
            Button(
                onClick = onUndo,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(stringResource(R.string.undo))
            }
        }
    }
}

@Composable
fun TeamScore(name: String, sets: Int, games: Int, points: Int) {
    androidx.compose.foundation.layout.Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(name, style = MaterialTheme.typography.title2)
        Text(
            "S:$sets  G:$games  P:${pointsToString(points)}",
            style = MaterialTheme.typography.body2
        )
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ScoreApp(
        score = ScoreState(pointsA = 2, pointsB = 3, gamesA = 4, gamesB = 2, setsA = 1, setsB = 1),
        onAddA = {},
        onAddB = {},
        onUndo = {}
    )
}
