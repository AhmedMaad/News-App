package com.maad.newsadminmaad

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintsChangedListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.maad.newsadminmaad.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var picUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                picUri = it.data?.data
                binding.imageBtn.setImageURI(picUri)
            }
        }

        binding.imageBtn.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/*"
            resultLauncher.launch(i)
        }

        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if (motionLayout?.progress == 1f) {
                    binding.loadingProgress.isVisible = true
                    updateProgress(30, "Please wait...")
                    uploadPicture()
                }

            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }
        })

    }

    private fun uploadPicture() {
        val picName = "${('a'..'z').random()}${(1000..10000).random()}"
        val storage = Firebase.storage.reference.child(picName)
        storage.putFile(picUri!!).addOnSuccessListener {
            updateProgress(55, "Uploading Picture...")
            storage.downloadUrl.addOnSuccessListener {
                updateProgress(90, "Saving Data...")
                addArticle(it)
            }
        }
    }

    private fun addArticle(uri: Uri) {
        val title = binding.titleEt.text.toString()
        val description = binding.descriptionEt.text.toString()
        val a = Article(title, description, uri.toString())
        Firebase.firestore.collection("articles").add(a).addOnSuccessListener {
            updateProgress(95, "Saving Data...")
            a.id = it.id
            it.update("id", it.id).addOnSuccessListener {
                updateProgress(100, "Saved!")
            }
        }
    }

    private fun updateProgress(progress: Int, text: String) {
        binding.loadingProgress.progress = progress
        binding.progressTv.text = text
    }

}