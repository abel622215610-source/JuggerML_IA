package com.example.juggerml.matchs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchsScreen(
    onBack: () -> Unit = {},
    onMatchCreated: (matchId: String) -> Unit = {},
    viewModel: MatchsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumeMessage()
        }
    }
    LaunchedEffect(state.createdMatchId) {
        state.createdMatchId?.let { id ->
            snackbarHostState.showSnackbar("Partido creado correctamente")
            onMatchCreated(id)
            viewModel.consumeMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear partido", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Button(
                    onClick = viewModel::createMatch,
                    enabled = !state.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp)
                        .height(52.dp)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Crear partido", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TeamCard(
                    title = "EQUIPO A",
                    accent = MaterialTheme.colorScheme.primary,
                    form = state.teamA,
                    enabled = !state.isSaving,
                    onNameChange = { viewModel.onTeamNameChange(TeamSide.A, it) },
                    onStarterChange = { i, v -> viewModel.onStarterChange(TeamSide.A, i, v) },
                    onSubstituteChange = { i, v -> viewModel.onSubstituteChange(TeamSide.A, i, v) }
                )
            }
            item { VersusBadge() }
            item {
                TeamCard(
                    title = "EQUIPO B",
                    accent = MaterialTheme.colorScheme.tertiary,
                    form = state.teamB,
                    enabled = !state.isSaving,
                    onNameChange = { viewModel.onTeamNameChange(TeamSide.B, it) },
                    onStarterChange = { i, v -> viewModel.onStarterChange(TeamSide.B, i, v) },
                    onSubstituteChange = { i, v -> viewModel.onSubstituteChange(TeamSide.B, i, v) }
                )
            }
        }
    }
}

@Composable
private fun VersusBadge() {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    "VS",
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
private fun TeamCard(
    title: String,
    accent: Color,
    form: TeamForm,
    enabled: Boolean,
    onNameChange: (String) -> Unit,
    onStarterChange: (Int, String) -> Unit,
    onSubstituteChange: (Int, String) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.5.dp, accent.copy(alpha = 0.6f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = accent
            )

            OutlinedTextField(
                value = form.name,
                onValueChange = onNameChange,
                label = { Text("Nombre del equipo") },
                singleLine = true,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                )
            )

            SectionHeader(
                text = "Titulares",
                count = form.starters.count { it.isNotBlank() },
                max = MatchRules.MAX_STARTERS,
                accent = accent
            )
            form.starters.forEachIndexed { index, value ->
                PlayerField(
                    value = value,
                    label = "Jugador ${index + 1}",
                    enabled = enabled,
                    onValueChange = { onStarterChange(index, it) }
                )
            }

            Spacer(Modifier.height(4.dp))
            HorizontalDivider()
            Spacer(Modifier.height(4.dp))

            SectionHeader(
                text = "Reservas",
                count = form.substitutes.count { it.isNotBlank() },
                max = MatchRules.MAX_SUBSTITUTES,
                accent = accent
            )
            form.substitutes.forEachIndexed { index, value ->
                PlayerField(
                    value = value,
                    label = "Reserva ${index + 1}",
                    enabled = enabled,
                    onValueChange = { onSubstituteChange(index, it) }
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String, count: Int, max: Int, accent: Color) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        AssistChip(
            onClick = {},
            enabled = false,
            label = { Text("$count / $max") },
            colors = AssistChipDefaults.assistChipColors(
                disabledLabelColor = accent,
                disabledContainerColor = accent.copy(alpha = 0.12f)
            ),
            border = null
        )
    }
}

@Composable
private fun PlayerField(
    value: String,
    label: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
        singleLine = true,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        )
    )
}