package com.example.shifumi.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shifumi.score.ScoresManager
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun GameVsComputerScreen() {
    var shakeCount by remember { mutableStateOf(0) }
    var userWeapon by remember { mutableStateOf<String?>(null) }
    var computerWeapon by remember { mutableStateOf<String?>(null) }
    var result by remember { mutableStateOf<String?>(null) }
    var showWow by remember { mutableStateOf(false) }

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
                            val compChoice = getRandomWeapon()
                            userWeapon = userChoice
                            computerWeapon = compChoice
                            val res = compareWeapons(userChoice, compChoice)
                            result = res
                            if (res == "Gagné") {
                                val isRecord = ScoresManager.onWin()
                                if (isRecord) {
                                    showWow = true
                                    val tone = ToneGenerator(AudioManager.STREAM_ALARM, 100)
                                    tone.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
                                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                                    vibrator?.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
                                }
                            } else {
                                ScoresManager.onLose()
                                showWow = false
                            }
                            shakeCount = 0
                        }
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
    }

    DisposableEffect(Unit) {
        gyroSensor?.also {
            sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Version du shifumi contre l'ordinateur", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Nombre de tremblement : $shakeCount")
        Spacer(modifier = Modifier.height(24.dp))
        Text("Ton arme : ${userWeapon ?: "Vide"}", textAlign = TextAlign.Center)
        Text("Arme Ordi : ${computerWeapon ?: "Vide"}", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
        Text(if (result == null) "Secoue 3 fois pour jouer" else "Résultat : $result")
        if (showWow) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("WAOUHHHH! NOUVEAU RECORD!", textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Record actuel : ${ScoresManager.bestStreak}")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            userWeapon = null
            computerWeapon = null
            result = null
            showWow = false
            shakeCount = 0
        }) {
            Text("Rejouer")
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
