package com.example.upstoxassignment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.upstoxassignment.adapter.HoldingsListAdapter
import com.example.upstoxassignment.databinding.ActivityMainBinding
import com.example.upstoxassignment.models.UiState
import com.example.upstoxassignment.repository.MainRepository
import com.example.upstoxassignment.viewmodels.MainViewModel
import com.example.upstoxassignment.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: HoldingsListAdapter
    private lateinit var mainViewModel: MainViewModel
    private var mainRepository: MainRepository? = null
    private var expanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = HoldingsListAdapter(arrayListOf())
        val layoutManager = LinearLayoutManager(this)
        binding.rvHoldingItems.layoutManager = layoutManager
        binding.rvHoldingItems.adapter = adapter

        binding.tvArrow.setOnClickListener {
            if (expanded) {
                expanded = false
                binding.tvArrow.text = "▲"
                binding.clThreeRows.visibility = View.GONE
            } else {
                expanded = true
                binding.tvArrow.text = "▼"
                binding.clThreeRows.visibility = View.VISIBLE
            }
        }

        mainRepository = (application as MainApplication).mainRepository
        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(mainRepository)
        ).get(MainViewModel::class.java)

        initObservers()

    }

    private fun initObservers() {
        mainViewModel.holdingsData.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvHoldingItems.visibility = View.VISIBLE
                    binding.clBottomSheet.visibility = View.VISIBLE

                    binding.clBottomSheet.visibility = View.VISIBLE
                    if (expanded) {
                        binding.clThreeRows.visibility = View.VISIBLE
                    } else binding.clThreeRows.visibility = View.GONE

                    binding.tvCurrentValue.text =
                        "₹" + String.format("%.2f", mainViewModel.getCurrentValue())
                    binding.tvTotalInvValue.text =
                        "₹" + String.format("%.2f", mainViewModel.getTotalInvValue())

                    val todayPNLVal = mainViewModel.getTodaysPNLValue()
                    binding.tvTodaysPNLValue.text = "₹" + String.format("%.2f", todayPNLVal)
                    if (todayPNLVal < 0.0) {
                        binding.tvTodaysPNLValue.setTextColor(Color.RED)
                    }

                    val pnlVal = mainViewModel.getTotalPNLValue()
                    binding.tvProfitAndLossValue.text = "₹" + String.format("%.2f", pnlVal)
                    if (pnlVal < 0.0) {
                        binding.tvProfitAndLossValue.setTextColor(Color.RED)
                    }

                    adapter.updateList(it.data)
                }

                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvHoldingItems.visibility = View.GONE
                    binding.clBottomSheet.visibility = View.GONE
                }
            }
        }
    }
}