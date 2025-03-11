package com.example.shifumi.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shifumi.strategy.StrategyEngine
import kotlin.math.sqrt

@Composable
fun GameStrategyScreen() {

    var shakeCount by remember { mutableStateOf(0) }
    var userWeapon by remember { mutableStateOf<String?>(null) }
    var computerWeapon by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val gyroSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val (x, y, z) = it.values
                    val magnitude = sqrt(x*x + y*y + z*z)
                    val threshold = 2.0f

                    if (magnitude > threshold) {
                        shakeCount++
                        if (shakeCount == 3) {
                            val userChoice = StrategyEngine.pickUserWeapon()
                            val compChoice = StrategyEngine.pickComputerWeapon()

                            userWeapon = userChoice
                            computerWeapon = compChoice
                            result = StrategyEngine.compare(userChoice, compChoice)

                            shakeCount = 0
                        }
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
    }

    DisposableEffect(Unit) {
        gyroSensor?.also { sensor ->
            sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("JEU STRATÉGIQUE (Ordi >70% tente de contrer)", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Secouages détectés : $shakeCount")
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Ton arme : ${userWeapon ?: "???"}")
        Text(text = "Arme ordi : ${computerWeapon ?: "???"}")
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = result?.let { "Résultat : $it" } ?: "Shake 3 fois pour jouer !",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            shakeCount = 0
            userWeapon = null
            computerWeapon = null
            result = null
        }) {
            Text("Rejouer")
        }
    }
}
