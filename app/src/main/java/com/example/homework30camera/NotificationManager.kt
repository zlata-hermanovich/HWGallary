package com.example.homework30camera

import android.content.Context
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

private const val CHANNEL_ID = "14"
private const val NOTIFICATION_ID = 14

class NotificationManager(
    private val context: Context, workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    lateinit var storage:FirebaseStorage
    private lateinit var  reference:StorageReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {

        val value = inputData.getString("uri")
        val myUri=Uri.parse(value)
        val filename = UUID.randomUUID().toString()

        storage= FirebaseStorage.getInstance()
        reference=storage.reference
        if (myUri != null) {

            val ref: StorageReference = reference.child("images/$filename")

            ref.putFile(myUri)
                .addOnSuccessListener {
                    showNotification()
                }
        } else
            Toast.makeText(context,"Error",Toast.LENGTH_LONG).show()

        return Result.success()
    }

    private fun showNotification() {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentText("Picture been load")
            .setContentTitle("Wow")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
}
