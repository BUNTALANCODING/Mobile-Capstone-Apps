package com.syhdzn.capstoneapp.ui.detail_shop

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.syhdzn.capstoneapp.R
import com.syhdzn.capstoneapp.api_access.ApiClient
import com.syhdzn.capstoneapp.api_access.api_response.ShopDetail
import com.syhdzn.capstoneapp.databinding.ActivityDetailShopBinding
import com.syhdzn.capstoneapp.ui.dashboard.DashboardActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailShopActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailShopBinding
    private lateinit var detailShopAdapter: DetailShopAdapter
    private val viewModel: DetailShopViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shopId = intent.getIntExtra("shopId", -1)

        if (shopId != -1) {
            fetchShopDetails(shopId)
        }

        setupView()
        setupAction()

        viewModel.shopDetail.observe(this, Observer { shopDetail ->
            shopDetail?.let { setupUI(it) }
        })
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
        binding.ivExit.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("switchToFragment", "ShopFragment")
            intent.putExtra("selectMenuItem", R.id.cart)
            startActivity(intent)
        }
    }

    private fun fetchShopDetails(shopId: Int) {
        val apiService = ApiClient.getApiService()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getShopDetailsById(shopId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val shopDetail = response.body()?.data

                        if (shopDetail != null) {
                            viewModel.setShopDetail(shopDetail)
                        } else {

                        }
                    } else {

                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun setupUI(shopDetail: ShopDetail) {
        binding.tvDetailShopName.text = shopDetail.name
        binding.tvLocationStoreDetail.text = shopDetail.location

        Glide.with(this)
            .load(shopDetail.image)
            .into(binding.ivItemDetail)

        val latitude = shopDetail.latitude
        val longitude = shopDetail.longitude

        binding.btnMaps.setOnClickListener {
            val uri = "https://maps.google.com/?q=$latitude,$longitude  "
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Aplikasi Google Maps tidak terinstall", Toast.LENGTH_SHORT).show()
            }
        }

        detailShopAdapter = DetailShopAdapter(shopDetail.foods)
        binding.rvDetailShop.layoutManager = LinearLayoutManager(this)
        binding.rvDetailShop.adapter = detailShopAdapter
    }
}
