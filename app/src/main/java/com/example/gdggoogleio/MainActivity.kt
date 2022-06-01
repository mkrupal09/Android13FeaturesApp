package com.example.gdggoogleio

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.LocaleListCompat
import com.bumptech.glide.Glide
import com.example.gdggoogleio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        {
            createNotificationChannel()
            sendNotification()
        }

    private val photoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val currentUri: Uri = it.data?.data!!
                Glide.with(binding.ivProfile).load(currentUri).into(binding.ivProfile)
            }
        }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)


        //Per app language change programatically
        binding.btnChangeLanguage.setOnClickListener {
            val appLocale: LocaleListCompat =
                LocaleListCompat.forLanguageTags(arrayListOf("hi").joinToString(","))
            AppCompatDelegate.setApplicationLocales(appLocale)
            recreate()
        }


        //Notification runtime permision
        binding.btnSendNotification.setOnClickListener {
            requestSendNotification()
        }

        //Downgradable permission
        binding.btnRevokeNotificationPermission.setOnClickListener {
            revokeSelfPermissionOnKill(Manifest.permission.POST_NOTIFICATIONS)
            revokeSelfPermissionsOnKill(
                arrayListOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
            Toast.makeText(this, "Permission revoked", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnRestartApp.setOnClickListener {
            recreate()
        }

        binding.ivProfile.setOnClickListener {
            val pickerIntent = Intent(MediaStore.ACTION_PICK_IMAGES)
            //pickerIntent.type = "video/*"
            pickerIntent.type = "image/*"
            //pickerIntent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, 5) //you can set max limit
            //MediaStore.getPickImagesMaxLimit() // to get max limit
            photoPickerLauncher.launch(pickerIntent)
        }
    }

    private fun requestSendNotification() {
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            sendNotification()
        } else {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }


    private fun sendNotification() {
        val notification = NotificationCompat.Builder(this, "123")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Hello")
            .setContentText("Now i'm free")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()

        with(NotificationManagerCompat.from(this))
        {
            notify(123, notification)
        }
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("123", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(channel)
    }

    private fun predictiveBack() {
        /*if (BuildCompat.isAtLeastT()) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                */
        /**
         * onBackPressed logic goes here. For instance:
         * Prevents closing the app to go home screen when in the
         * middle of entering data to a form
         * or from accidentally leaving a fragment with a WebView in it
         *
         * Unregistering the callback to stop intercepting the back gesture:
         * When the user transitions to the topmost screen (activity, fragment)
         * in the BackStack, unregister the callback by using
         * OnBackInvokeDispatcher.unregisterOnBackInvokedCallback
         * (https://developer.android.com/reference/kotlin/android/view/OnBackInvokedDispatcher#unregisteronbackinvokedcallback)
         *//*
                }
            }*/
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}