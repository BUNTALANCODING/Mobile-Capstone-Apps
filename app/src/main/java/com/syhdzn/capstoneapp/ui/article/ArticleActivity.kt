package com.syhdzn.capstoneapp.ui.article

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.syhdzn.capstoneapp.api_access.api_response.FoodsItem
import com.syhdzn.capstoneapp.databinding.ActivityArticleBinding
import com.syhdzn.capstoneapp.ui.dashboard.DashboardActivity

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private val viewModel: ArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        viewModel.foodItem.observe(this, Observer { foodItem ->
            updateUI(foodItem)
        })

        val foodItem: FoodsItem? = intent.getParcelableExtra("foodItem")
        foodItem?.let { viewModel.setFoodItem(it) }
    }

    private fun updateUI(foodItem: FoodsItem) {
        with(binding) {
            tvTitleArticle.text = foodItem.name
            tvDescriptionArticle.text = foodItem.description

            Glide.with(this@ArticleActivity)
                .load(foodItem.image)
                .into(ivArticle)
        }
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.ivBackArticle.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
