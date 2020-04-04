package com.alvaronunez.studyzone.presentation.ui.createphotoitem

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.exifinterface.media.ExifInterface
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.databinding.ActivityCreatePhotoItemBinding
import com.alvaronunez.studyzone.presentation.ui.common.EventObserver
import com.alvaronunez.studyzone.presentation.ui.common.PermissionRequester
import kotlinx.android.synthetic.main.activity_create_photo_item.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CreatePhotoItemActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }


    private val viewModel: CreatePhotoItemViewModel by currentScope.viewModel(this)
    private val cameraPermissionRequester =
        PermissionRequester(this, Manifest.permission.CAMERA)

    private var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCreatePhotoItemBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_photo_item)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        setupObservers()
        openCamera()
    }

    private fun setupObservers() {
        viewModel.takePhoto.observe(this, EventObserver{
            openCamera()
        })

        viewModel.addItem.observe(this, EventObserver{
            Toast.makeText(this, "Add item clicked!", Toast.LENGTH_LONG).show()
        })
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
        cameraPermissionRequester.request { cameraPermission ->
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
                        if (cameraPermission) {
                            takePictureIntent.resolveActivity(packageManager)?.also {
                                startActivityForResult(
                                    takePictureIntent,
                                    REQUEST_IMAGE_CAPTURE
                                )
                            }

                        } else {
                            finish()
                        }
                    }
                }
            }
        }

    }

    private fun rotateImage(bitmap: Bitmap) =
        currentPhotoPath?.let {
            var rotate = 0
            val exif = ExifInterface(it)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
            val matrix = Matrix()
            matrix.postRotate(rotate.toFloat())
            Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width,
                bitmap.height, matrix, true
            )
        }?: run {
            bitmap
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            BitmapFactory.decodeFile(currentPhotoPath)?.also {bitmap ->
                photoItemImage.setImageBitmap(rotateImage(bitmap))
            }

        }
    }

}