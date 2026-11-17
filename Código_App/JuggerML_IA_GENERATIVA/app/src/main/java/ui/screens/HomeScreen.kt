package com.example.juggerml_ia_generativa.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juggerml_ia_generativa.R

@Composable
fun HomeScreen(
    onLogout: () -> Unit = {},
    onSettings: () -> Unit = {},
    onCreateMatch: () -> Unit = {}
) {
    val context = LocalContext.current
    val url = "https://juggerliga.es/reglamento.php"
    val intentweb = Intent(Intent.ACTION_VIEW, Uri.parse(url))

    // Estado para la barra de navegación inferior (0: Social, 1: Home, 2: Settings)
    var selectedTab by remember { mutableIntStateOf(1) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { index ->
                    if (index == 0) {
                        // Botón Social de la barra inferior
                        Toast.makeText(context, "Por implementar", Toast.LENGTH_SHORT).show()
                    } else {
                        selectedTab = index
                        when (index) {
                            1 -> { /* Ya estamos en Home */ }
                            2 -> onSettings() // Conectado a ajustes
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // 1. CABECERA: Título "Home" y botón circular "Perfil"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Home",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedButton(
                    onClick = {Toast.makeText(context, "Por implementar", Toast.LENGTH_SHORT).show()},
                    shape = CircleShape,
                    modifier = Modifier.size(64.dp),
                    contentPadding = PaddingValues(0.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        text = "Perfil",
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. FILA DE ACCIONES RÁPIDAS (Aquí conectamos "Partidos" a onCreateMatch)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CircularMenuOption(
                    letter = "\uD83D\uDEA9",
                    label = "Recursos",
                    onClick = { context.startActivity(intentweb) }
                )
                CircularMenuOption(
                    letter = "\uD83C\uDFC6",
                    label = "Ranking",
                    onClick = {Toast.makeText(context, "Por implementar", Toast.LENGTH_SHORT).show()}
                )
                CircularMenuOption(
                    letter = "⚔\uFE0F",
                    label = "Partidos",
                    onClick = onCreateMatch // <-- Conectado al botón de tu clase
                )
                CircularMenuOption(
                    letter = "\uD83D\uDDE3\uFE0F\u200B",
                    label = "Clases",
                    onClick = {Toast.makeText(context, "Por implementar", Toast.LENGTH_SHORT).show()}
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. CONTENEDOR CENTRAL: Eventos / Anuncios y enlace de reglamento opcional
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Eventos/Anuncios",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(3) { index ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text(
                                    text = "Anuncio de prueba #${index + 1}",
                                    modifier = Modifier.padding(12.dp),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón extra de Cerrar Sesión que tenías en la clase
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = onLogout) {
                    Text(
                        text = stringResource(R.string.Boton_CerrarSesion),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun CircularMenuOption(
    letter: String,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = onClick,
            shape = CircleShape,
            modifier = Modifier.size(64.dp),
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Text(
                text = letter,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 13.sp
        )
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        val items = listOf(
            Triple("S", "Social", 0),
            Triple("H", "Home", 1),
            Triple("St", "Settings", 2)
        )

        items.forEach { item ->
            NavigationBarItem(
                selected = selectedTab == item.third,
                onClick = { onTabSelected(item.third) },
                icon = {
                    Text(
                        text = item.first,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                label = {
                    Text(text = item.second)
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}