package com.syhdzn.capstoneapp.ui.detection

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.syhdzn.capstoneapp.databinding.FragmentCameraBinding
import com.syhdzn.capstoneapp.ui.process.ProcessActivity
import com.syhdzn.capstoneapp.utils.createFile
import com.syhdzn.capstoneapp.utils.showToast
import com.syhdzn.capstoneapp.utils.uriToFile
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

const val CAMERA_PERMISSION_REQUEST = 123

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var cameraExecutor: ExecutorService

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var capturedFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupActions()
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermissionAndStart()
    }

    private fun setupViews() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun setupActions() {
        binding.apply {
            btnCamera.setOnClickListener { takePhoto() }
            btnGallery.setOnClickListener { startGallery() }
            btnSwitch.setOnClickListener {
                cameraSelector =
                    if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                    else CameraSelector.DEFAULT_BACK_CAMERA
                startCamera()
            }
        }
    }

    private fun checkCameraPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(binding.viewFinder.surfaceProvider) }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(requireActivity(), cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                requireActivity().showToast("Failed to Show Camera")
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                requireActivity().showToast("Izin kamera ditolak.")
            }
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createFile(requireContext().applicationContext)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        setupLoading()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    requireActivity().showToast("Failed To Take Image.")
                    hideLoading()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    handleImageSaved(photoFile)
                }
            }
        )
    }

    private fun handleImageSaved(photoFile: File) {
        hideLoading()

        val intent = Intent(requireContext(), ProcessActivity::class.java)
        intent.putExtra("picture", photoFile)
        intent.putExtra("isBackCamera", cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
        startActivity(intent)
    }

    private fun startGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri? = result.data?.data
            selectedImage?.let {
                setupLoading()
                capturedFile = uriToFile(selectedImage, requireContext())
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(requireContext(), ProcessActivity::class.java)
                    intent.putExtra("imageUri", selectedImage.toString())
                    startActivity(intent)

                    hideLoading()
                }, 1000)
            }
        } else {
            hideLoading()
        }
    }

    private fun setupLoading() {
        val pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#06283D")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(true)
        pDialog.show()
    }

    private fun hideLoading() {
        val pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#06283D")
        pDialog.titleText = "Loading"
        pDialog.setCancelable(true)
        pDialog.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseResources()
    }

    private fun releaseResources() {
        cameraExecutor.shutdown()
    }
}
