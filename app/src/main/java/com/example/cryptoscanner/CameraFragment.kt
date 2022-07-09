package com.example.cryptoscanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.cryptoscanner.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {

    private val TAG = "CameraFragment"
    private lateinit var binding: FragmentCameraBinding
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all { it.value }
            if (granted) {
                //TODO: Open Camera
            }
            else {
                val showRationale = REQUIRED_PERMISSIONS.map {
                    ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), it)
                }.reduce { a, b -> a && b }
                if (showRationale) {
                    Toast.makeText(requireContext(), "Camera permission required to scan QR code", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigateUp()
                } else {
                    // TODO: Show Permission Denied Permanently Layout
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when {
            allPermissionsGranted() -> {} // TODO: Start Camera
            else -> requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}