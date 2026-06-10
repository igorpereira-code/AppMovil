package ucb.edu.bo.todoApp.profile.presentation.composable

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import java.io.ByteArrayOutputStream

@Composable
actual fun rememberImagePicker(onImagePicked: (ByteArray?) -> Unit): ImagePicker {
    val context = LocalContext.current

    // 1. Lanzador para la galería
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            onImagePicked(bytes)
        }
    }

    // 2. Lanzador para la cámara (Miniatura para no requerir permisos extra en el Manifest)
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            onImagePicked(stream.toByteArray())
        }
    }

    return remember {
        object : ImagePicker {
            override fun takePhoto() {
                cameraLauncher.launch(null)
            }

            override fun pickFromGallery() {
                galleryLauncher.launch("image/*")
            }
        }
    }
}

actual fun ByteArray.toImageBitmap(): ImageBitmap? {
    return try {
        val bitmap = BitmapFactory.decodeByteArray(this, 0, this.size)
        bitmap.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}