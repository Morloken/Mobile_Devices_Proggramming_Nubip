package com.example.imageviewer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PICK_FOLDER = 1001

    private val allExtensions = listOf("jpg", "png", "tiff")
    private var allowedExtensions = allExtensions.toMutableList()

    private val allImageUris = ArrayList<String>()
    private val selectedFolders = ArrayList<FolderItem>()
    private lateinit var folderAdapter: FolderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnChooseFolder = findViewById<Button>(R.id.btnChooseFolder)
        val btnAuthor = findViewById<Button>(R.id.btnAuthor)
        val btnViewAll = findViewById<Button>(R.id.btnViewAll)
        val rvFolders = findViewById<RecyclerView>(R.id.rvFolders)

        val cbJpg = findViewById<CheckBox>(R.id.cbJpg)
        val cbPng = findViewById<CheckBox>(R.id.cbPng)
        val cbTiff = findViewById<CheckBox>(R.id.cbTiff)

        // Spinner для сортування
        val spinnerSort = findViewById<Spinner>(R.id.spinnerSort)
        val sortOptions = listOf("Name", "Date", "Size")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSort.adapter = spinnerAdapter

        // динамічний фільтр через чекбокси
        val checkBoxes = mapOf(
            "jpg" to cbJpg,
            "png" to cbPng,
            "tiff" to cbTiff
        )
        checkBoxes.forEach { (ext, cb) ->
            cb.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!allowedExtensions.contains(ext)) allowedExtensions.add(ext)
                } else {
                    allowedExtensions.remove(ext)
                }
            }
        }

        // список вибраних папок
        folderAdapter = FolderAdapter(selectedFolders) { folder ->
            selectedFolders.remove(folder)
            allImageUris.removeAll(folder.imageUris)
            folderAdapter.notifyDataSetChanged()
        }

        rvFolders.layoutManager = LinearLayoutManager(this)
        rvFolders.adapter = folderAdapter

        btnChooseFolder.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(intent, REQUEST_CODE_PICK_FOLDER)
        }

        btnAuthor.setOnClickListener {
            startActivity(Intent(this, AuthorActivity::class.java))
        }

        btnViewAll.setOnClickListener {
            val filteredImages = allImageUris
                .mapNotNull { getImageMetadata(Uri.parse(it)) }
                .filter { allowedExtensions.any { ext -> it.name.endsWith(".$ext", ignoreCase = true) } }

            val sortedImages = when (spinnerSort.selectedItem.toString()) {
                "Name" -> filteredImages.sortedBy { it.name.lowercase() }
                "Date" -> filteredImages.sortedBy { it.dateModified }
                "Size" -> filteredImages.sortedBy { it.size }
                else -> filteredImages
            }

            if (sortedImages.isNotEmpty()) {
                val intent = Intent(this, ImageViewerActivity::class.java)
                intent.putStringArrayListExtra("imageList", ArrayList(sortedImages.map { it.uri.toString() }))
                startActivity(intent)
            }
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
                        .filter { it.isFile && it.name?.let { name ->
                            allowedExtensions.any { ext -> name.endsWith(".$ext", ignoreCase = true) }
                        } == true }
                        .map { it.uri.toString() }

                    if (imageUris.isNotEmpty()) {
                        val folderName = pickedDir.name ?: "Unnamed"
                        val folderItem = FolderItem(folderName, imageUris)

                        selectedFolders.add(folderItem)
                        allImageUris.addAll(imageUris)
                        folderAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun getImageMetadata(uri: Uri): ImageMetadata? {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                val name = if (nameIndex >= 0) cursor.getString(nameIndex) else "Unknown"
                val size = if (sizeIndex >= 0) cursor.getLong(sizeIndex) else 0L
                val dateModified = File(uri.path ?: "").lastModified()
                return ImageMetadata(uri, name, size, dateModified)
            }
        }
        return null
    }

    data class ImageMetadata(
        val uri: Uri,
        val name: String,
        val size: Long,
        val dateModified: Long
    )
}
