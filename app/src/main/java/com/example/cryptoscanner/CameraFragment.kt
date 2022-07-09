package com.example.cryptoscanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cryptoscanner.databinding.FragmentCameraBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.log

class CameraFragment : Fragment() {

    private val TAG = "CameraFragment"
    private lateinit var binding: FragmentCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private val viewModel: CameraViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all { it.value }
            if (granted) startCamera()
            else {
                val showRationale = REQUIRED_PERMISSIONS.map {
                    ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), it)
                }.reduce { a, b -> a && b }
                if (showRationale) {
                    Toast.makeText(requireContext(), "Camera permission required to scan QR code", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigateUp()
                } else showCameraPermissionDeniedLayout()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (!allPermissionsGranted()) requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        else startCamera()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeScannedAddress()
        binding.cameraPermissionDeniedLayout.openSettingsBtn.setOnClickListener { openSettingsPage() }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun observeScannedAddress() {
        lifecycleScope.launch {
            viewModel.scannedAddress.collectLatest {
                //  TODO: Navigate to validation screen
            }
        }
    }

    private fun startCamera() {
        showCameraLayout()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val preview = Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(binding.cameraLayout.viewFinder.surfaceProvider) }

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QRCodeAnalyzer {
                        lifecycleScope.launch { viewModel.setScannedAddress(it) }
                    })
                }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview, imageAnalyzer)
            } catch (e: Exception) {
                Log.e(TAG, "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun openSettingsPage() {
        val uri: Uri = Uri.fromParts("package", activity?.packageName, null)
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also { it.data = uri }
        startActivity(intent)
    }

    private fun showCameraLayout() {
        binding.apply {
            cameraLayout.root.visibility = View.VISIBLE
            cameraPermissionDeniedLayout.root.visibility = View.GONE
        }
    }

    private fun showCameraPermissionDeniedLayout() {
        binding.apply {
            cameraLayout.root.visibility = View.GONE
            cameraPermissionDeniedLayout.root.visibility = View.VISIBLE
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}