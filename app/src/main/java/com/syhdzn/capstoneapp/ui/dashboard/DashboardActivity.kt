package com.syhdzn.capstoneapp.ui.dashboard

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.syhdzn.capstoneapp.R
import com.syhdzn.capstoneapp.databinding.ActivityDashboardBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.syhdzn.capstoneapp.ui.detection.CameraFragment
import com.syhdzn.capstoneapp.ui.history.HistoryFragment
import com.syhdzn.capstoneapp.ui.home.HomeFragment
import com.syhdzn.capstoneapp.ui.shop.ShopFragment

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    private var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        firstSelectedItem()
        observeSelectedItem()
        setupBottomNavigation()
        handleIntent(intent)
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

    private fun firstSelectedItem(){
        binding.menuBottom.setItemSelected(R.id.home, true)
        supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment()).commit()
    }

    private fun observeSelectedItem() {
        viewModel.selectedItemId.observe(this, Observer { itemId ->
            when (itemId) {
                R.id.home -> fragment = HomeFragment()
                R.id.cart -> fragment = ShopFragment()
                R.id.cam -> fragment = CameraFragment()
                R.id.fav -> fragment = HistoryFragment()
            }

            fragment?.let {
                supportFragmentManager.beginTransaction().replace(R.id.container, it).commit()
            }
        })
    }

    private fun setupBottomNavigation() {
        binding.menuBottom.setOnItemSelectedListener(object : ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(i: Int) {
                viewModel.setSelectedItemId(i)
            }
        })
    }

    private fun handleIntent(intent: Intent) {
        val switchToFragment = intent.getStringExtra("switchToFragment")
        val selectMenuItem = intent.getIntExtra("selectMenuItem", -1)

        if (switchToFragment != null) {
            when (switchToFragment) {
                "DetectionFragment" -> switchToDetectionFragment()
                "ShopFragment" -> switchToDetectionFragment()
                "HistoryFragment" -> switchToDetectionFragment()
            }
        }

        if (selectMenuItem != -1) {
            binding.menuBottom.setItemSelected(selectMenuItem, true)
        }
    }

    private fun switchToDetectionFragment() {
        fragment = CameraFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment!!)
            .addToBackStack(null)
            .commit()
    }


    override fun onBackPressed() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
}