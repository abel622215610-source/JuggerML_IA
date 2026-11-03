package ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.juggerml_ia_generativa.R

@Preview(showBackground = true)
@Composable
fun HomeScreen(onLogout: () -> Unit = {}, onSettings: () -> Unit = {}) {
    // Usamos una Column principal que ocupe toda la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Column(
            modifier = Modifier
                .weight(1f) // Esto hace que la lista ceda espacio abajo para los botones
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            var context = LocalContext.current


            // 2. LOS BOTONES (Fijos en la parte inferior, fuera de la zona de scroll)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly // Distribuye los botones ordenadamente
            ) {
                Button(onClick = { onLogout() }) {
                    Text(
                        stringResource(R.string.Boton_CerrarSesion),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Button(onClick = { onSettings() }) {
                    Text(
                        stringResource(R.string.Boton_onSettings),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

            }
        }
    }
}
