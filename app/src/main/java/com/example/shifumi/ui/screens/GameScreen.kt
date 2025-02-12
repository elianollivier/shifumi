package com.example.shifumi.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun GameScreen() {
    // État : nombre de "secouages" détectés
    var shakeCount by remember { mutableStateOf(0) }
    // État : arme choisie (null au départ)
    var chosenWeapon by remember { mutableStateOf<String?>(null) }
    // État : coordonnées brutes pour affichage (optionnel)
    var gyroX by remember { mutableStateOf(0f) }
    var gyroY by remember { mutableStateOf(0f) }
    var gyroZ by remember { mutableStateOf(0f) }

    val context = LocalContext.current

    // On récupère le SensorManager et le capteur Gyroscope
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val gyroSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    // Listener pour le gyroscope
    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = it.values[0]
                    val y = it.values[1]
                    val z = it.values[2]

                    gyroX = x
                    gyroY = y
                    gyroZ = z

                    // On calcule l'intensité du mouvement rotatif
                    val magnitude = sqrt(x * x + y * y + z * z)

                    // Seuil arbitraire pour considérer un "secouage"
                    val threshold = 2.0f

                    if (magnitude > threshold) {
                        // Incrémente le nombre de "secouages"
                        shakeCount++

                        // Quand on atteint 3 secouages, on choisit une arme aléatoire
                        if (shakeCount == 3) {
                            chosenWeapon = getRandomWeapon()
                            // On peut remettre le shakeCount à 0 pour un nouveau tour
                            shakeCount = 0
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }
    }

    // Enregistre/désenregistre le listener en fonction du cycle de vie du composable
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

    // UI : on affiche le nombre de secouages (temporaire) et l’arme choisie
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Secouages détectés : $shakeCount")
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = if (chosenWeapon == null) "Aucune arme encore tirée" else "Arme tirée : $chosenWeapon",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        // Affichage de debug : coordonnées gyroscope
        Text(
            text = "Gyro X: %.2f\nGyro Y: %.2f\nGyro Z: %.2f".format(gyroX, gyroY, gyroZ),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Retourne aléatoirement "Pierre", "Feuille" ou "Ciseaux".
 */
private fun getRandomWeapon(): String {
    val weapons = listOf("Pierre", "Feuille", "Ciseaux")
    return weapons[Random.nextInt(weapons.size)]
}
