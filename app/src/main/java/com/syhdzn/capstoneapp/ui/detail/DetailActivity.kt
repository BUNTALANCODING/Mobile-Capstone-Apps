package com.syhdzn.capstoneapp.ui.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.syhdzn.capstoneapp.R
import com.syhdzn.capstoneapp.api_access.api_response.Datahistori
import com.syhdzn.capstoneapp.databinding.ActivityDetailBinding
import com.syhdzn.capstoneapp.ui.dashboard.DashboardActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        viewModel.historiItem.observe(this) { historiItem ->
            updateUI(historiItem)
        }

        val historiItem: Datahistori? = intent.getParcelableExtra("historiItem")
        historiItem?.let { viewModel.setHistoriItem(it) }
    }

    private fun updateUI(historiItem: Datahistori) {
        with(binding) {
            tvDetailName.text = historiItem.name
            tvDetailDescription.text = historiItem.description

            Glide.with(this@DetailActivity)
                .load(historiItem.image)
                .into(ivDetail)
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
        binding.ivBackDetail.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("switchToFragment", "HistoryFragment")
            intent.putExtra("selectMenuItem", R.id.fav)
            startActivity(intent)
        }
        binding.btnHomeDetail.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("switchToFragment", "CameraFragment")
            intent.putExtra("selectMenuItem", R.id.cam)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("switchToFragment", "HistoryFragment")
        intent.putExtra("selectMenuItem", R.id.fav)
        startActivity(intent)
    }
}
