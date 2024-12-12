package com.ruangtenun.app.view.main.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ruangtenun.app.R
import com.ruangtenun.app.data.model.ClassificationHistory
import com.ruangtenun.app.databinding.FragmentHistoryBinding
import com.ruangtenun.app.utils.ResultState
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.viewmodel.history.AdapterHistory
import com.ruangtenun.app.viewmodel.history.HistoryViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterHistory: AdapterHistory

    private val historyViewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.apply {
            val verticalLayout = LinearLayoutManager(requireContext())
            rvHistory.layoutManager = verticalLayout
            val itemDecoration = DividerItemDecoration(requireContext(), verticalLayout.orientation)
            rvHistory.addItemDecoration(itemDecoration)

            adapterHistory = AdapterHistory(
                onItemClick = { historyId ->
                    val bundle = Bundle().apply {
                        historyId?.let { putString("historyId", historyId) }
                    }
                    findNavController().navigate(R.id.navigation_detail, bundle)
                },
                onDeleteClick = { historyId ->
                    historyId?.let { id ->
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(resources.getString(R.string.delete_history))
                            .setMessage(resources.getString(R.string.supporting_text))
                            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                                val historyToDelete = ClassificationHistory(id = id)
                                historyViewModel.deleteHistory(historyToDelete)
                            }
                            .show()
                    }
                }
            )

            rvHistory.adapter = adapterHistory
        }
    }

    private fun observeViewModel() {
        historyViewModel.allHistories.observe(viewLifecycleOwner) { data ->
            setHistory(data)
        }

        historyViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoading(true)
            } else {
                showLoading(false)
            }
        }

        historyViewModel.message.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setHistory(listClassificationHistory: List<ClassificationHistory>) {
        adapterHistory.submitList(listClassificationHistory)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
