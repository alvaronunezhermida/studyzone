package com.alvaronunez.studyzone.presentation.ui.createphotoitem

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.presentation.ui.common.PermissionRequester
import kotlinx.android.synthetic.main.activity_create_photo_item.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class CreatePhotoItemActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    private val cameraPermissionRequester =
        PermissionRequester(this, Manifest.permission.CAMERA)

    private val writePermissionRequester =
        PermissionRequester(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_photo_item)
        openCamera()
    }

    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        storageDir?.let {
            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                it /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
            }
        }?: run {
            return null
        }
    }

    private fun openCamera() {
        writePermissionRequester.request { writePermission ->
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile = createImageFile()
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI = FileProvider.getUriForFile(
                            this,
                            "com.example.android.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        //TODO: Hacer con corrutinas
                        cameraPermissionRequester.request { cameraPermission ->
                            if (cameraPermission && writePermission) {
                                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                                    takePictureIntent.resolveActivity(packageManager)?.also {
                                        startActivityForResult(
                                            takePictureIntent,
                                            REQUEST_IMAGE_CAPTURE
                                        )
                                    }
                                }
                            } else {
                                finish()
                            }

                        }
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            data.extras?.let {
                // Get the dimensions of the View
                val targetW: Int = photoItemImage.width
                val targetH: Int = photoItemImage.height

                val bmOptions = BitmapFactory.Options().apply {
                    val photoW: Int = outWidth
                    val photoH: Int = outHeight

                    // Determine how much to scale down the image
                    val scaleFactor: Int = min(photoW / targetW, photoH / targetH)

                    // Decode the image file into a Bitmap sized to fill the View
                    inJustDecodeBounds = false
                    inSampleSize = scaleFactor
                }
                BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also {bitmap ->
                    photoItemImage.setImageBitmap(bitmap)
                }
            }
        }
    }

}