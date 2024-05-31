package com.syhdzn.capstoneapp.ui.process

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.syhdzn.capstoneapp.R
import com.syhdzn.capstoneapp.api_access.ApiClient
import com.syhdzn.capstoneapp.api_access.api_response.ApiResponse
import com.syhdzn.capstoneapp.databinding.ActivityProcessBinding
import com.syhdzn.capstoneapp.ui.dashboard.DashboardActivity
import com.syhdzn.capstoneapp.ui.result.ResultActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ProcessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProcessBinding
    private var imageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProcessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleImage()
        setupAction()
    }

    private fun handleImage() {
        val imageUriString = intent.getStringExtra("imageUri")
        val isBackCamera = intent.getBooleanExtra("isBackCamera", false)
        val pictureFile = intent.getSerializableExtra("picture") as? File

        imageFile = when {
            imageUriString != null -> {
                val imageUri = Uri.parse(imageUriString)
                handleGalleryImage(imageUri)
                null
            }
            isBackCamera && pictureFile != null -> {
                val rotatedBitmap = BitmapFactory.decodeFile(pictureFile.absolutePath)
                binding.ivItemProcess.setImageBitmap(rotatedBitmap)
                pictureFile
            }
            !isBackCamera && pictureFile != null -> {
                binding.ivItemProcess.setImageURI(Uri.fromFile(pictureFile))
                pictureFile
            }
            else -> null
        }

        if (imageFile == null) {
        }
    }

    private fun handleGalleryImage(imageUri: Uri) {
        binding.ivItemProcess.setImageURI(imageUri)
    }

    private fun setupAction() {
        binding.ivBgReplace.setOnClickListener {
            showDialogReplace()
        }
        binding.btnProcessImage.setOnClickListener {
            showDialogProcess()
        }
    }

    private fun uploadImage(imageFile: File?, type: String) {
        imageFile?.let {
            // Create the form data part for both file and type
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), it)
            val imageBody = MultipartBody.Part.createFormData("file", it.name, requestFile)

            val typeBody = RequestBody.create("text/plain".toMediaTypeOrNull(), type)

            // Make the API call with the combined form data
            val call = ApiClient.getApiService().uploadImage(imageBody, typeBody)

            call.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    hideLoading()
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        val resultIntent = Intent(this@ProcessActivity, ResultActivity::class.java)
                        resultIntent.putExtra("category", apiResponse?.data?.category)
                        resultIntent.putExtra("name", apiResponse?.data?.name)
                        resultIntent.putExtra("description", apiResponse?.data?.description)
                        resultIntent.putExtra("image", apiResponse?.data?.image)
                        startActivity(resultIntent)
                    } else {
                        showErrorDialog("Error uploading image")
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    hideLoading()
                    showErrorDialog("Failed to upload image")
                }
            })
        } ?: run {
            showErrorDialog("Image file is null")
        }
    }


    private fun showErrorDialog(message: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Error")
            .setContentText(message)
            .show()
    }

    private fun showDialogProcess() {

        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val customDialogView = inflater.inflate(R.layout.costum_dialog_process, null)

        builder.setView(customDialogView)
        val dialog = builder.create()

        val btnMakanan = customDialogView.findViewById<Button>(R.id.btn_makanan)
        val btnJajanan = customDialogView.findViewById<Button>(R.id.btn_jajanan)
        val btnExit = customDialogView.findViewById<ImageView>(R.id.btn_exit_process)

        btnMakanan.setOnClickListener {
            handleMakanan()
            dialog.dismiss()
            setupLoading()
        }

        btnJajanan.setOnClickListener {
            handleJajanan()
            dialog.dismiss()
            setupLoading()
        }

        btnExit.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_rounded_14)
        customDialogView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun handleMakanan() {
        val capturedImageFile = captureImageViewContent(binding.ivItemProcess)
        uploadImage(capturedImageFile, "MAKANAN")
    }

    private fun handleJajanan() {
        val capturedImageFile = captureImageViewContent(binding.ivItemProcess)
        uploadImage(capturedImageFile, "JAJANAN")
    }

    private fun captureImageViewContent(imageView: ImageView): File? {
        val drawable = imageView.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            val file = createImageFile()
            saveBitmapToFile(bitmap, file)
            return file
        }
        return null
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        if (storageDir != null) {
            val imageFileName = "captured_image_${System.currentTimeMillis()}.jpg"
            return File(storageDir, imageFileName)
        } else {
            showErrorDialog("External storage not available")
            return File("default_image.jpg")
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap, file: File) {
        try {
            val fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showDialogReplace() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val customDialogView = inflater.inflate(R.layout.costum_dialog_replace, null)

        builder.setView(customDialogView)
        val dialog = builder.create()

        val btnYes = customDialogView.findViewById<Button>(R.id.btn_yes)
        val btnNo = customDialogView.findViewById<Button>(R.id.btn_no)

        btnYes.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("switchToFragment", "DetectionFragment")
            intent.putExtra("selectMenuItem", R.id.cam)
            startActivity(intent)
        }

        btnNo.setOnClickListener {
            dialog.hide()
        }

        dialog.window?.setBackgroundDrawableResource(R.drawable.bg_rounded_14)
        customDialogView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun setupLoading() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#06283D")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(true)
        pDialog.show()
    }

    private fun hideLoading() {
        val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#06283D")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(true)
        pDialog.hide()
    }
}
