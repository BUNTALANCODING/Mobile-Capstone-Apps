package com.syhdzn.capstoneapp.ui.shop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.syhdzn.capstoneapp.api_access.ApiClient
import com.syhdzn.capstoneapp.api_access.api_response.ShopItem
import com.syhdzn.capstoneapp.api_access.api_response.ShopResponse
import com.syhdzn.capstoneapp.databinding.FragmentShopBinding
import com.syhdzn.capstoneapp.ui.detail_shop.DetailShopActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ShopFragment : Fragment(), ShopAdapter.OnItemClickListener {

    private lateinit var binding: FragmentShopBinding
    private lateinit var shopAdapter: ShopAdapter


    private var shopListFull: List<ShopItem> = emptyList()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        fetchShopData()
    }

    private fun setupRecyclerView() {
        shopAdapter = ShopAdapter(emptyList(), this)
        binding.rvShop.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = shopAdapter
        }
    }

    private fun fetchShopData() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiClient.getApiService().getShops()
                handleShopResponse(response)
            } catch (e: HttpException) {
                // Handle HTTP exception
            } catch (e: Throwable) {
                // Handle other exceptions
            }
        }
    }

    private fun handleShopResponse(response: retrofit2.Response<ShopResponse>) {
        if (response.isSuccessful) {
            val shopList = response.body()?.data ?: emptyList()
            shopListFull = shopList
            shopAdapter.updateData(shopList)
        } else {

        }
    }

    private fun setupSearchView() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handleSearch(newText.orEmpty())
                return true
            }
        })
    }

    private fun handleSearch(query: String) {
        val filteredList = shopListFull.filter { item ->
            when (item) {
                is ShopItem -> item.name.contains(query, ignoreCase = true)
                else -> false
            }
        }
        shopAdapter.updateData(filteredList)
    }

    override fun onItemClick(shopItem: ShopItem) {
        navigateToDetail(shopItem.id)
    }


    private fun navigateToDetail(shopId: Int) {
        val intent = Intent(requireContext(), DetailShopActivity::class.java)
        intent.putExtra("shopId", shopId)
        startActivity(intent)
    }
}
