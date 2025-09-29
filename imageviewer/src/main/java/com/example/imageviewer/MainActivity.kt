package com.example.imageviewer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PICK_FOLDER = 1001

    // всі можливі формати
    private val allExtensions = listOf("jpg", "png", "tiff")
    private var allowedExtensions = allExtensions.toMutableList()

    // список усіх картинок з усіх папок
    private val allImageUris = ArrayList<String>()

    // список вибраних папок
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

        // налаштування списку вибраних папок
        folderAdapter = FolderAdapter(selectedFolders) { folder ->
            // видаляємо папку при натисканні на хрестик
            selectedFolders.remove(folder)
            allImageUris.removeAll(folder.imageUris)
            folderAdapter.notifyDataSetChanged()
        }

        rvFolders.layoutManager = LinearLayoutManager(this)
        rvFolders.adapter = folderAdapter

        // вибір папки
        btnChooseFolder.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            startActivityForResult(intent, REQUEST_CODE_PICK_FOLDER)
        }

        // відкриття автора
        btnAuthor.setOnClickListener {
            startActivity(Intent(this, AuthorActivity::class.java))
        }

        // перегляд усіх вибраних папок
        btnViewAll.setOnClickListener {
            val filteredImages = allImageUris.filter { uri ->
                allowedExtensions.any { ext -> uri.endsWith(".$ext", ignoreCase = true) }
            }

            if (filteredImages.isNotEmpty()) {
                val intent = Intent(this, ImageViewerActivity::class.java)
                intent.putStringArrayListExtra("imageList", ArrayList(filteredImages))
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
}
