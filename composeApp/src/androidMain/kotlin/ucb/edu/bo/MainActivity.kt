package ucb.edu.bo

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
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
        setContent { App() }

        // Solo pide notificaciones si aún no las tiene (Android 13+)
        if (!hasNotificationPermission()) {
            checkNotificationPermission()
        }
    }

    // ─── Verificar si ya tiene el permiso ────────────────────────────────────
    private fun hasNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    // ─── Solicitar permiso de notificaciones ─────────────────────────────────
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Dexter.withContext(this)
                .withPermission(Manifest.permission.POST_NOTIFICATIONS)
                .withListener(object : PermissionListener {

                    // ✅ Permiso concedido → vibrar como confirmación
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        vibratePhone()
                    }

                    // ⚠️ Explicar por qué se necesita y volver a pedir
                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        Toast.makeText(
                            this@MainActivity,
                            "La app necesita permiso para enviarte notificaciones",
                            Toast.LENGTH_LONG
                        ).show()
                        token?.continuePermissionRequest()
                    }

                    // ❌ Permiso denegado
                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        Toast.makeText(
                            this@MainActivity,
                            "No se pueden enviar notificaciones",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
                .check()
        }
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
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}