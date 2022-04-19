package com.example.homework30camera

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.homework30camera.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

private const val PICK_IMAGE_REQUEST = 71

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var storage: FirebaseStorage

    private var selectedPhotoUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()

        binding.gallery.setOnClickListener {
            galleryAddPic()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    selectedPhotoUri = data?.data
                    binding.imagePhoto.setImageURI(selectedPhotoUri)

                    val myData = Data.Builder()
                        .putString( "uri",selectedPhotoUri.toString())
                        .build()
                    val workRequest = OneTimeWorkRequestBuilder<NotificationManager>()
                        .setInputData(myData)
                        .build()
                    WorkManager.getInstance(this).enqueue(workRequest)
                }
            }
        }
    }

    private fun galleryAddPic() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
}

