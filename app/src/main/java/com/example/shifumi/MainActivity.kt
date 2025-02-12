package com.example.shifumi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.shifumi.navigation.ShifumiNavHost
import com.example.shifumi.ui.theme.ShifumiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShifumiTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    ShifumiNavHost(
                        navController = navController,
                        modifier = Modifier
                            .padding(innerPadding) // <= on applique le padding du Scaffold
                            .fillMaxSize()
                    )
                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    ShifumiTheme {
        // Pour un aperçu statique, on peut afficher juste HomeScreen:
        // HomeScreen {}
    }
}
