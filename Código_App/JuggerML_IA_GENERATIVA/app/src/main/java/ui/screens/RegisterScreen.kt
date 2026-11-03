package ui.screens

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.juggerml_ia_generativa.R


@Preview(showSystemUi = true)
@Composable
fun RegisterScreen(
    onRegisterOut: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    onRegisterFailed: () -> Unit = {}
) {
    var userName by rememberSaveable { mutableStateOf("")}
    var userLastName by rememberSaveable { mutableStateOf("")}
    var userBirthday by rememberSaveable { mutableStateOf("")}
    var userGmail by rememberSaveable { mutableStateOf("") }
    var userPassword by rememberSaveable { mutableStateOf("") }
    var userRePassword by rememberSaveable { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Si se muestra el diálogo, renderizamos el DatePickerDialog de Material 3
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            // Convertir los milisegundos a un formato de fecha legible (ej: DD/MM/YYYY)
                            val formatter = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                            userBirthday = formatter.format(java.util.Date(millis))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Image(
            painterResource(R.drawable.icono_juggerml),
            contentDescription = "icono app",
            modifier = Modifier.size(130.dp)
        )
        Spacer(Modifier.height(30.dp))
        TextField(
            value = userName,
            label = {Text(stringResource(R.string.CampoNombre))},
            singleLine = true,
            onValueChange = {userName = it.trim()})

        Spacer(Modifier.height(20.dp))

        TextField(
            value = userLastName,
            label = {Text(stringResource(R.string.CampoApellido))},
            singleLine = true,
            onValueChange = {userLastName = it.trim()})

        Spacer(Modifier.height(20.dp))

        Box {
            TextField(
                value = userBirthday,
                label = { Text(stringResource(R.string.CampoFechaNac)) },
                singleLine = true,
                readOnly = true, // Evita que escriban con el teclado directamente
                onValueChange = {}
            )
            // Superficie transparente para capturar el clic en todo el TextField y abrir el calendario
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePicker = true }
            )
        }

        Spacer(Modifier.height(20.dp))

        TextField(
            value = userGmail,
            label = { Text(stringResource(R.string.CampoGmail)) },
            singleLine = true,
            onValueChange = { userGmail = it.trim() })

        Spacer(Modifier.height(20.dp))

        TextField(
            value = userPassword,
            label = { Text(stringResource(R.string.CampoContraseña)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { userPassword = it })

        Spacer(Modifier.height(20.dp))

        TextField(
            value = userRePassword,
            label = { Text(stringResource(R.string.CampoRepetirContraseña)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { userRePassword = it })

        Spacer(Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                onRegisterSuccess()
            }) {
                Text(
                    stringResource(R.string.Boton_RegistrarCuenta),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Button(
                modifier = Modifier.padding(start = 10.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                onClick = {
                    onRegisterOut()
                }) {
                Text(
                    text = stringResource(R.string.Boton_SalirRegistrarCuenta),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }


        Spacer(Modifier.height(15.dp))

    }
}