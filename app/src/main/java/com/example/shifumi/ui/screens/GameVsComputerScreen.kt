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
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun GameVsComputerScreen() {

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
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]

                    val magnitude = sqrt(x * x + y * y + z * z)

                    val threshold = 2.0f

                    if (magnitude > threshold) {
                        shakeCount++
                        if (shakeCount == 3) {
                            val userChoice = getRandomWeapon()
                            userWeapon = userChoice

                            val compChoice = getRandomWeapon()
                            computerWeapon = compChoice

                            result = compareWeapons(userChoice, compChoice)

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
        Text(text = "Version du shifumi contre l'ordinateur", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Nombre de tremblement : $shakeCount")

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Ton arme : ${userWeapon ?: "Vide"}",
            textAlign = TextAlign.Center
        )
        Text(
            text = "Arme Ordi : ${computerWeapon ?: "Vide"}",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = if (result == null) "Secoue 3 fois pour jouer" else "Résultat : $result")

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            userWeapon = null
            computerWeapon = null
            result = null
            shakeCount = 0
        }) {
            Text(text = "Rejouer")
        }
    }
}

private fun getRandomWeapon(): String {
    val weapons = listOf("Pierre", "Feuille", "Ciseaux")
    return weapons[Random.nextInt(weapons.size)]
}

private fun compareWeapons(user: String, computer: String): String {
    return when {
        user == computer -> "Égalité"
        user == "Pierre" && computer == "Ciseaux" -> "Gagné"
        user == "Ciseaux" && computer == "Feuille" -> "Gagné"
        user == "Feuille" && computer == "Pierre" -> "Gagné"
        else -> "Perdu"
    }
}
