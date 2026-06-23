package com.zx_tole.pocketpsychologist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zx_tole.pocketpsychologist.R
import com.zx_tole.pocketpsychologist.data.model.MoodRecord
import com.zx_tole.pocketpsychologist.data.model.MoodType
import com.zx_tole.pocketpsychologist.ui.home.HomeEvent
import com.zx_tole.pocketpsychologist.ui.home.HomeIntent
import com.zx_tole.pocketpsychologist.ui.home.HomeState
import com.zx_tole.pocketpsychologist.ui.home.HomeViewModel
import com.zx_tole.pocketpsychologist.ui.theme.MoodAnxious
import com.zx_tole.pocketpsychologist.ui.theme.MoodCalm
import com.zx_tole.pocketpsychologist.ui.theme.MoodExcited
import com.zx_tole.pocketpsychologist.ui.theme.MoodNeutral
import com.zx_tole.pocketpsychologist.ui.theme.MoodSad
import com.zx_tole.pocketpsychologist.ui.breathing.BreathingExerciseScreen
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val homeState by homeViewModel.state.collectAsState()
    val homeEvent by homeViewModel.events.collectAsState()
    
    // Handle events
    LaunchedEffect(homeEvent) {
        homeEvent?.let { event ->
            when (event) {
                is HomeEvent.RecordingStarted -> {
                    // Already handled by state update
                }
                is HomeEvent.RecordingStopped -> {
                    // Already handled by state update
                }
                is HomeEvent.AnalysisProgressChanged -> {
                    // Progress is handled by state update
                }
                is HomeEvent.MoodAnalyzed -> {
                    // Mood analyzed and saved
                }
                is HomeEvent.HistoryLoaded -> {
                    // History loaded
                }
                is HomeEvent.Error -> {
                    // Handle error
                }
            }
        }
    }
    
    var showBreathingDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Главная") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    label = { Text("Дыхание") },
                    selected = false,
                    onClick = { showBreathingDialog = true }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
                // Mood Today Widget
                MoodTodayWidget(homeState.lastMood)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Analysis Section
                AnalysisSection(
                    isRecording = homeState.isRecording,
                    analysisProgress = homeState.analysisProgress,
                    recordDuration = homeState.recordDuration,
                    onRecordClick = { homeViewModel.handleIntent(HomeIntent.RecordButtonClick) },
                    onStopClick = { homeViewModel.handleIntent(HomeIntent.StopButtonClick) }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Recent Mood History
                MoodHistorySection(homeState.moodHistory)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Breathing Exercise Card
                BreathingCard {
                    showBreathingDialog = true
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            if (showBreathingDialog) {
                BreathingExerciseDialog {
                    showBreathingDialog = false
                }
            }
        }
    }
}

@Composable
fun MoodTodayWidget(lastMood: MoodType?) {
    val mood = lastMood ?: MoodType.NEUTRAL
    val moodText = when (mood) {
        MoodType.CALM -> R.string.mood_calm
        MoodType.EXCITED -> R.string.mood_excited
        MoodType.ANXIOUS -> R.string.mood_anxious
        MoodType.SAD -> R.string.mood_sad
        else -> R.string.mood_neutral
    }
    val moodColor = when (mood) {
        MoodType.CALM -> MoodCalm
        MoodType.EXCITED -> MoodExcited
        MoodType.ANXIOUS -> MoodAnxious
        MoodType.SAD -> MoodSad
        else -> MoodNeutral
    }
    
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(moodColor.copy(alpha = 0.1f))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = moodColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.mood_today),
                    style = MaterialTheme.typography.titleMedium,
                    color = moodColor
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(moodText),
                style = MaterialTheme.typography.displaySmall,
                color = moodColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun AnalysisSection(
    isRecording: Boolean,
    analysisProgress: Float,
    recordDuration: Long,
    onRecordClick: () -> Unit,
    onStopClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.analyze_your_mood),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Visual recording indicator
            if (isRecording) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(MoodAnxious)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(MoodAnxious.copy(alpha = 0.5f))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MoodAnxious.copy(alpha = 0.3f))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.listening),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MoodAnxious
                )
                if (recordDuration > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${recordDuration / 1000}s",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(MoodCalm)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(MoodCalm.copy(alpha = 0.5f))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            tint = MoodCalm,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Progress bar during analysis
            if (analysisProgress > 0f && analysisProgress < 1f) {
                LinearProgressIndicator(
                    progress = { analysisProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = MoodCalm,
                    trackColor = Color.Gray.copy(alpha = 0.3f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when {
                        analysisProgress < 0.3f -> stringResource(R.string.analyzing)
                        analysisProgress < 0.6f -> stringResource(R.string.processing)
                        else -> stringResource(R.string.analyzing)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Record/Stop button
            Button(
                onClick = if (isRecording) onStopClick else onRecordClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = if (isRecording) stringResource(R.string.btn_stop) else stringResource(R.string.btn_record),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun MoodHistorySection(moodHistory: List<MoodRecord>) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.last_analysis),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            if (moodHistory.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_history),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            } else {
                moodHistory.take(5).forEach { record ->
                    MoodItem(record)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun MoodItem(record: MoodRecord) {
    val moodTextId = when (record.moodType) {
        MoodType.CALM -> R.string.mood_calm
        MoodType.EXCITED -> R.string.mood_excited
        MoodType.ANXIOUS -> R.string.mood_anxious
        MoodType.SAD -> R.string.mood_sad
        else -> R.string.mood_neutral
    }
    val moodText = stringResource(moodTextId)
    val moodColor = when (record.moodType) {
        MoodType.CALM -> MoodCalm
        MoodType.EXCITED -> MoodExcited
        MoodType.ANXIOUS -> MoodAnxious
        MoodType.SAD -> MoodSad
        else -> MoodNeutral
    }
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(moodColor)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = moodText,
                style = MaterialTheme.typography.bodyMedium,
                color = moodColor
            )
            Text(
                text = "${record.speechRate.toInt()} слов/мин",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${record.confidence * 100f.toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun BreathingCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MoodCalm)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = null,
                    tint = MoodCalm
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(R.string.take_a_deep_breath),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.start_breathing),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun BreathingExerciseDialog(onClose: () -> Unit) {
    Dialog(
        onDismissRequest = onClose
    ) {
        Card(
            modifier = Modifier.padding(16.dp)
        ) {
            BreathingExerciseScreen(onClose = onClose)
        }
    }
}
