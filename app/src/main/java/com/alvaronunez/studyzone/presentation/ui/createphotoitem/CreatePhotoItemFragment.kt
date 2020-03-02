package com.alvaronunez.studyzone.presentation.ui.createphotoitem

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.alvaronunez.studyzone.R
import com.alvaronunez.studyzone.databinding.FragmentCreatePhotoItemBinding
import com.alvaronunez.studyzone.presentation.ui.common.EventObserver
import com.alvaronunez.studyzone.presentation.ui.common.PermissionRequester
import com.alvaronunez.studyzone.presentation.ui.common.bindingInflate
import kotlinx.android.synthetic.main.fragment_create_photo_item.*
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CreatePhotoItemFragment : Fragment() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    private val viewModel: CreatePhotoItemViewModel by currentScope.viewModel(this)
    private lateinit var cameraPermissionRequester: PermissionRequester
    private lateinit var navController: NavController

    private var currentPhotoPath: String? = null
    private var binding: FragmentCreatePhotoItemBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = container?.bindingInflate(R.layout.fragment_create_photo_item, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = view.findNavController()
        binding?.apply {
            viewmodel = viewModel
            lifecycleOwner = this@CreatePhotoItemFragment
        }
        cameraPermissionRequester = PermissionRequester(this.activity as Activity, Manifest.permission.CAMERA)
        setupObservers()
        openCamera()
    }

    private fun setupObservers() {
        viewModel.takePhoto.observe(this, EventObserver{
            openCamera()
        })
    }

    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = this.activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
        this.context?.let { context ->
            cameraPermissionRequester.request { cameraPermission ->
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    // Ensure that there's a camera activity to handle the intent
                    takePictureIntent.resolveActivity(context.packageManager)?.also {
                        // Create the File where the photo should go
                        val photoFile = createImageFile()
                        // Continue only if the File was successfully created
                        photoFile?.also {
                            val photoURI = FileProvider.getUriForFile(
                                context,
                                "com.example.android.fileprovider",
                                it
                            )
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            //TODO: Hacer con corrutinas
                            if (cameraPermission) {
                                takePictureIntent.resolveActivity(context.packageManager)?.also {
                                    startActivityForResult(
                                        takePictureIntent,
                                        REQUEST_IMAGE_CAPTURE
                                    )
                                }

                            } //else {
                            //TODO: finish()
                            //}
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
            BitmapFactory.decodeFile(currentPhotoPath)?.also { bitmap ->
                photoItemImage.setImageBitmap(rotateImage(bitmap))
            }

        }
    }

}