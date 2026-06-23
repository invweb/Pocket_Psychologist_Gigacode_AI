package com.zx_tole.pocketpsychologist.ui

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zx_tole.pocketpsychologist.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        var permissionRequested by mutableStateOf(false)
        
        setContent {
            MaterialTheme {
                Surface {
                    val homeViewModel: HomeViewModel = viewModel()
                    
                    // Request RECORD_AUDIO permission
                    val permissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { granted ->
                            if (granted) {
                                Toast.makeText(this, "Разрешение на запись получено", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Разрешение на запись отклонено", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                    
                    LaunchedEffect(Unit) {
                        if (!permissionRequested) {
                            permissionRequested = true
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                    
                    HomeScreen(homeViewModel)
                }
            }
        }
    }
}
