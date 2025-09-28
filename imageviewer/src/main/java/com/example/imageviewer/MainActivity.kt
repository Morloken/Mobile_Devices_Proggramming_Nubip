package com.example.imageviewer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PICK_FOLDER = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnChooseFolder = findViewById<Button>(R.id.btnChooseFolder)
        val btnAuthor = findViewById<Button>(R.id.btnAuthor)

        btnChooseFolder.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(intent, REQUEST_CODE_PICK_FOLDER)
        }

        btnAuthor.setOnClickListener {
            startActivity(Intent(this, AuthorActivity::class.java))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_FOLDER && resultCode == Activity.RESULT_OK) {
            val folderUri: Uri? = data?.data
            if (folderUri != null) {
                val pickedDir = DocumentFile.fromTreeUri(this, folderUri)

                if (pickedDir != null && pickedDir.isDirectory) {
                    val imageUris = pickedDir.listFiles()
                        .filter { it.isFile && it.name?.matches(Regex(".*\\.(jpg|jpeg|png|gif|bmp|tiff)$", RegexOption.IGNORE_CASE)) == true }
                        .map { it.uri.toString() }

                    if (imageUris.isNotEmpty()) {
                        val intent = Intent(this, ImageViewerActivity::class.java)
                        intent.putStringArrayListExtra("imageList", ArrayList(imageUris))
                        startActivity(intent)
                    }
                }
            }
        }
    }
}
