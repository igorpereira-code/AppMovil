package ucb.edu.bo

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
        checkAndRequestCalendarPermission()
    }
    // ─── Dexter: verificar y solicitar permiso ───────────────────────────────
    private fun checkAndRequestCalendarPermission() {
        Dexter.withContext(this)
            .withPermission(Manifest.permission.WRITE_CALENDAR)
            .withListener(object : PermissionListener {

                // ✅ Permiso concedido
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    Toast.makeText(
                        this@MainActivity,
                        "Se puede escribir en el Calendario",
                        Toast.LENGTH_LONG
                    ).show()

                    vibratePhone() // Vibra al obtener el permiso
                }

                // ⚠️ Mostrar explicación al usuario y volver a pedir
                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "La app necesita acceso al Calendario",
                        Toast.LENGTH_LONG
                    ).show()
                    token?.continuePermissionRequest()
                }

                // ❌ Permiso denegado
                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    if (response?.isPermanentlyDenied == true) {
                        // Usuario marcó "No preguntar de nuevo" → abrir Configuración
                        goToAppSettings()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "No se puede escribir en el Calendario",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
            .check()
    }

    // ─── Vibración ───────────────────────────────────────────────────────────
    @Suppress("DEPRECATION")
    private fun vibratePhone() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                vibrator.vibrate(200)
            }
        } else {
            Toast.makeText(this, "Este dispositivo no soporta la Vibración", Toast.LENGTH_LONG).show()
        }
    }

    // ─── Ir a Configuración de la app ────────────────────────────────────────
    private fun goToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivityForResult(intent, 1)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}