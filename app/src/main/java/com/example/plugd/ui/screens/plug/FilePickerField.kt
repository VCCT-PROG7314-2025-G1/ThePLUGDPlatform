package com.example.plugd.ui.screens.plug

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun FilePickerField(supportDocs: String?, onFileSelected: (Uri) -> Unit) {
    val context = LocalContext.current

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onFileSelected(it) }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            filePickerLauncher.launch("*/*")
        } else {
            Toast.makeText(context, "Permission denied to access files", Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = supportDocs?.substringAfterLast("/") ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Choose file...") },
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_MEDIA_IMAGES
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            filePickerLauncher.launch("*/*")
                        } else {
                            requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }
                    } else {
                        filePickerLauncher.launch("*/*")
                    }
                }
        )
    }
}