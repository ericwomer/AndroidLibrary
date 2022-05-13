package com.ewomer.happybirthday

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.result.ActivityResultCallback
import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import com.ewomer.happybirthday.R

class MainActivity : AppCompatActivity() {
    private var textviewName: TextView? = null
    private var textviewEmailAddress: TextView? = null
    private var imageviewPFP: ImageView? = null
    var getResults =
        registerForActivityResult(StartActivityForResult(), ActivityResultCallback { result ->
            if (result == null) return@ActivityResultCallback
            if (result.data == null) return@ActivityResultCallback
            if (result.resultCode == RESULT_CANCELED) return@ActivityResultCallback
            val name = result.data!!.getStringExtra("name")
            val email = result.data!!.getStringExtra("email")
            textviewName!!.text = name
            textviewEmailAddress!!.text = email
        })
    var chooseImage =
        registerForActivityResult(GetContent()) { result -> imageviewPFP!!.setImageURI(result) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWidgets()
    }

    private fun initWidgets() {
        imageviewPFP = findViewById(R.id.imageview_pfp)
        val buttonChooseImage = findViewById<Button>(R.id.button_browse)
        val buttonEditInformation = findViewById<Button>(R.id.button_edit)
        textviewName = findViewById(R.id.textview_name)
        textviewEmailAddress = findViewById(R.id.textview_email_address)
        buttonChooseImage.setOnClickListener(buttonChooseImageListener)
        buttonEditInformation.setOnClickListener(buttonEditInformationListener)
    }

    private val buttonEditInformationListener = View.OnClickListener { view ->
        val getResultsIntent = Intent(view.context, MainActivity2::class.java)
        getResults.launch(getResultsIntent)
    }
    private val buttonChooseImageListener = View.OnClickListener { chooseImage.launch("image/*") }
}