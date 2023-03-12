package com.example.docshub

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val imgButton = findViewById<ImageButton>(R.id.addDocument);
        imgButton.setOnClickListener(){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            val uri: Uri = Uri.parse(
                (Environment.getExternalStorageDirectory().getPath()
                        + File.separator).toString() + "myFolder" + File.separator
            )
            intent.setDataAndType(uri, "text/csv")
            startActivity(Intent.createChooser(intent, "Open folder"))
        }
    }
}