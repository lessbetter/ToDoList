package com.example.todolist.data

import android.net.Uri
import java.nio.file.Path

class AttachmentContent(
    val fileUri: Uri,
    val filePath: Path,
    val fileName: String,
)