@file:Suppress("DEPRECATION")

package com.example.detectorapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private var imageView: ImageView? = null
    private var uploadButton: Button? = null
    private var resultTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply the AppCompat theme to the activity
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        imageView = findViewById(R.id.imageView)
        uploadButton = findViewById(R.id.uploadButton)
        resultTextView = findViewById(R.id.resultTextView)

        // Request external storage permissions for image upload
        if (isStoragePermissionGranted()) {
            setUploadButtonClickListener()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                101
            )
        }
    }

    private fun setUploadButtonClickListener() {
        uploadButton?.setOnClickListener {
            try {
                // Open a file picker or gallery to choose an image for upload
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, 102)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUploadButtonClickListener()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("MainActivity", "onActivityResult called")
        if (requestCode == 102 && resultCode == RESULT_OK && data != null) {
            // Handle the selected image URI
            val selectedImageUri = data.data
            // Process the selected image with TensorFlow Lite models
            // Update the resultTextView with the detected information
            resultTextView?.text = "Image uploaded: ${selectedImageUri.toString()}"
        }
    }
}