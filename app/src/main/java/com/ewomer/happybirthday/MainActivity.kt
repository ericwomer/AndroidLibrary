package com.ewomer.happybirthday

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.ewomer.happybirthday.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var selectedFileUri : Uri = Uri.parse("http://www.example.com")
    private lateinit var imagePicker: com.ewomer.imagepicker.ImagePicker

    private val getPreviewImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            selectedFileUri = result.data?.data!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonRequestPermissions = binding.buttonRequestPermissions
        buttonRequestPermissions.setOnClickListener{
            requestPermissions()
        }

        val ivPhoto = binding.ivPhoto
        imagePicker = com.ewomer.imagepicker.ImagePicker(this, BuildConfig.APPLICATION_ID) { uri ->
            ivPhoto.setImageURI(uri)
        }

        val btChoosePhoto = binding.btChoosePhoto
        btChoosePhoto.setOnClickListener {
            imagePicker.show()
        }

    }

    private fun hasWriteExternalStoragePermissions() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun hasCoarseLocationPermissions() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun hasBackgroundLocationPermissions() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun hasCameraPermissions() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        var permissionsToRequest = mutableListOf<String>()
        if(!hasWriteExternalStoragePermissions()) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if(!hasCoarseLocationPermissions()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if(!hasBackgroundLocationPermissions() && hasCoarseLocationPermissions()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        if(!hasCameraPermissions()) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }

        if(permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val tvLog = binding.tvLog
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty()) {
            for(i in grantResults.indices) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    tvLog.setText("${permissions[i]} granted\n")
                }
            }
        }
    }

}