package com.syhdzn.capstoneapp.ui.result

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.syhdzn.capstoneapp.R
import com.syhdzn.capstoneapp.databinding.ActivityResultBinding
import com.syhdzn.capstoneapp.ui.dashboard.DashboardActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val viewModel: ResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.name.observe(this, Observer { name ->
            binding.tvNameFood.text = name
        })

        viewModel.description.observe(this, Observer { description ->
            binding.tvDescriptionFood.text = description
        })

        viewModel.imageUrl.observe(this, Observer { imageUrl ->
            Glide.with(this)
                .load(imageUrl)
                .into(binding.ivItemResult)
        })

        setupAction()
        getDataFromIntent()
    }


    private fun setupAction() {
        binding.btnResult.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
        binding.btnShare.setOnClickListener {
            val name = viewModel.name.value
            val description = viewModel.description.value

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, name)
            val sharedText = "\n\n~ $name ~\n\n$description\n\n # Ayoo Makan !! #"
            shareIntent.putExtra(Intent.EXTRA_TEXT, sharedText)
            startActivity(Intent.createChooser(shareIntent, "Bagikan via"))
        }
    }

    private fun getDataFromIntent() {
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getStringExtra("image")

        if (name != null) {
            viewModel.setName(name)
        }
        if (description != null) {
            viewModel.setDescription(description)
        }
        if (imageUrl != null) {
            viewModel.setImageUrl(imageUrl)
        }
    }


    override fun onBackPressed() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("switchToFragment", "DetectionFragment")
        intent.putExtra("selectMenuItem", R.id.cam)
        startActivity(intent)
    }
}
