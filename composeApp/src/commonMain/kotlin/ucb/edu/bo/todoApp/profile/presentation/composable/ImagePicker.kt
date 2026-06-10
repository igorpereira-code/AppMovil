package ucb.edu.bo.todoApp.profile.presentation.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

interface ImagePicker {
    fun takePhoto()
    fun pickFromGallery()
}

@Composable
expect fun rememberImagePicker(onImagePicked: (ByteArray?) -> Unit): ImagePicker

// Función para convertir los bytes en una imagen visible en Compose
expect fun ByteArray.toImageBitmap(): ImageBitmap?