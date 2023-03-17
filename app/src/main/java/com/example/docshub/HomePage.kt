package com.example.docshub

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class HomePage : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_PICK_PDF = 101
        const val REQUEST_CODE_PICK_DIRECTORY = 100
    }

    private lateinit var selectedFileUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val pickPdfButton = findViewById<ImageButton>(R.id.addDocument)
        pickPdfButton.setOnClickListener {
            showDirectoryPicker()
        }
    }
    private fun showDirectoryPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, REQUEST_CODE_PICK_DIRECTORY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_DIRECTORY && resultCode == Activity.RESULT_OK && data != null) {
            val directoryUri = data.data!!
            val documentUri = directoryUri.buildUpon().appendPath("document").build()

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "application/pdf"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, documentUri)
            startActivityForResult(intent, REQUEST_CODE_PICK_PDF)
        } else if (requestCode == REQUEST_CODE_PICK_PDF && resultCode == Activity.RESULT_OK && data?.data != null) {
            selectedFileUri = data.data!!
            showUploadDialog()
        }
    }

    private fun showUploadDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.upload_dialog, null)
        val uploadButton = dialogView.findViewById<Button>(R.id.upload_file_button)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancel_dialog_button)
        val fileNameTextView = dialogView.findViewById<TextView>(R.id.file_name_textview)
        fileNameTextView.text = getFileName(selectedFileUri)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        uploadButton.setOnClickListener {
            uploadPdfFile(selectedFileUri)
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun Context.getFileName(uri: Uri): String? = when(uri.scheme) {
        ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
        else -> uri.path?.let(::File)?.name
    }
    private fun Context.getContentFileName(uri: Uri): String? = runCatching {
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            cursor.moveToFirst()
            return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME).let(cursor::getString)
        }
    }.getOrNull()
    private fun uploadPdfFile(uri: Uri) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val pdfRef: StorageReference = storageRef.child("pdfs/${getFileName(uri)}")

        val inputStream = contentResolver.openInputStream(selectedFileUri)
        val buffer = ByteArray(inputStream!!.available())
        inputStream.read(buffer)

        pdfRef.putBytes(buffer)
            .addOnSuccessListener {
                val downloadUrlTextView = findViewById<TextView>(R.id.download_url_textview)
                pdfRef.downloadUrl.addOnSuccessListener { uri ->
                    downloadUrlTextView.text = uri.toString()
                }
                Toast.makeText(applicationContext, "File uploaded successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(applicationContext, "Error uploading file", Toast.LENGTH_SHORT).show()
            }
    }
}

