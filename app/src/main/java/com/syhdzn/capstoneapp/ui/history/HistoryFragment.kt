package com.syhdzn.capstoneapp.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.syhdzn.capstoneapp.api_access.api_response.Datahistori
import com.syhdzn.capstoneapp.databinding.FragmentHistoryBinding
import com.syhdzn.capstoneapp.ui.detail.DetailActivity

class HistoryFragment : Fragment(), HistoryAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historiAdapter: HistoryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvFavorite
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        setupRecyclerView()
        observeViewModel()

        viewModel.fetchHistory()
    }

    private fun observeViewModel() {
        viewModel.historiList.observe(viewLifecycleOwner) { historiList ->
            historiAdapter.updateData(historiList)
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        historiAdapter = HistoryAdapter(emptyList(), this)
        recyclerView.adapter = historiAdapter
    }

    override fun onItemClick(histori: Datahistori) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("historiItem", histori)
        startActivity(intent)
    }
}
