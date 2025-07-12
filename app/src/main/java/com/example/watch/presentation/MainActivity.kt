package com.example.watch.presentation

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.watch.R
import com.example.watch.presentation.theme.WatchTheme

class MainActivity : ComponentActivity() {
    private var scoreState by mutableStateOf(ScoreState())
    private val history = mutableListOf<ScoreState>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ScoreApp(scoreState, onUndo = { undo() }) }
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
fun ScoreApp(score: ScoreState, onUndo: () -> Unit) {
    WatchTheme {
        ScoreScreen(score = score, onUndo = onUndo)
    }
}

@Composable
fun ScoreScreen(score: ScoreState, onUndo: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { TimeText() }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TeamScore(name = stringResource(R.string.team_a),
                        sets = score.setsA,
                        games = score.gamesA,
                        points = score.pointsA)
                    TeamScore(name = stringResource(R.string.team_b),
                        sets = score.setsB,
                        games = score.gamesB,
                        points = score.pointsB)
                }
            }
            item {
                Button(onClick = onUndo) {
                    Text(stringResource(R.string.undo))
                }
            }
        }
    }
}

@Composable
fun TeamScore(name: String, sets: Int, games: Int, points: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(name)
        Text(stringResource(R.string.sets) + " $sets")
        Text(stringResource(R.string.games) + " $games")
        Text(stringResource(R.string.points) + " ${pointsToString(points)}")
    }
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    ScoreApp(ScoreState(), onUndo = {})
}
