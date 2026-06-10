package ucb.edu.bo.todoApp.profile.presentation.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap

@Composable
actual fun rememberImagePicker(onImagePicked: (ByteArray?) -> Unit): ImagePicker {
    return remember {
        object : ImagePicker {
            override fun takePhoto() {
                // Implementación para iOS (Por ahora vacía para que compile Android)
            }

            override fun pickFromGallery() {
                // Implementación para iOS (Por ahora vacía para que compile Android)
            }
        }
    }
}

actual fun ByteArray.toImageBitmap(): ImageBitmap? {
    // Implementación para iOS (Por ahora nula para que compile Android)
    return null
}