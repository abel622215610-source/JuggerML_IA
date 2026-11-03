package ui.screens

import models.UserData
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun SettingsScreen(userData: UserData, onVolverHome: () -> Unit = {}) {

        Column() {
            Spacer(modifier = Modifier.height(20.dp))
            Row() {
                Text(text = "Nombre:", fontSize = 18.sp ,modifier = Modifier.weight(1f))
                Text(text = "${userData.nombre}", fontSize = 18.sp,modifier = Modifier.weight(1f))
                Text(text = "Apellidos:", fontSize = 18.sp,modifier = Modifier.weight(1f))
                Text(text = "${userData.apellidos}", fontSize = 18.sp,modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row() {
                Text(text = "Fecha de nacimiento:", fontSize = 18.sp,modifier = Modifier.weight(1f))
                Text(text = "${userData.fecha_nac}", fontSize = 18.sp,modifier = Modifier.weight(1f))
                Text(text = "Email:", fontSize = 18.sp,modifier = Modifier.weight(1f))
                Text(text = "${userData.email}", fontSize = 18.sp,modifier = Modifier.weight(1f))
            }
        }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Button(modifier = Modifier.padding(bottom = 15.dp), onClick = {
            onVolverHome()
        }) {
            Text(text = "Volver a home")
        }
    }
}