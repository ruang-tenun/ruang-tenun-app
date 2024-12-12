package com.ruangtenun.app.view.main.result

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ruangtenun.app.R
import com.ruangtenun.app.data.model.ClassificationHistory
import com.ruangtenun.app.data.remote.response.PredictResponse
import com.ruangtenun.app.databinding.FragmentResultBinding
import com.ruangtenun.app.utils.FileUtils
import com.ruangtenun.app.utils.ResultState
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.viewmodel.history.HistoryViewModel

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val predictResult = arguments?.getParcelable<PredictResponse>(PREDICT_RESULT)
        val imageUri = arguments?.getParcelable<Uri>(PREDICT_IMAGE)

        val confidenceLevel = when {
            predictResult?.confidenceScore!! < 0.3 -> getString(R.string.low)
            predictResult.confidenceScore in 0.3..0.6 -> getString(R.string.enough)
            predictResult.confidenceScore in 0.6..0.85 -> getString(R.string.good)
            else -> getString(R.string.very_good)
        }

        predictResult.let {
            binding.resultClass.text = it.result
            binding.resultDesc.text = getString(R.string.accuracy_desc, confidenceLevel)
        }

        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }

        historyViewModel.saveHistoryState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Idle -> {
                }

                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), result.data, Toast.LENGTH_SHORT).show()
                }

                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSave.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.save_image))
                .setMessage(getString(R.string.are_you_sure_want_to_save))
                .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                .setPositiveButton("Save") { _, _ ->
                    saveToHistory(imageUri, predictResult)
                }
                .show()
        }

        return binding.root
    }

    private fun saveToHistory(
        imageUri: Uri?,
        predictResult: PredictResponse?
    ) {
        imageUri?.let {
            val filename = "image_${System.currentTimeMillis()}.jpg"
            val savedImagePath =
                FileUtils.saveImageToInternalStorage(requireContext(), it, filename)

            if (savedImagePath == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.failed_save_image_to_storage),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            val history = ClassificationHistory(
                weavingName = predictResult?.result ?: "Unknown",
                confidenceScore = predictResult?.confidenceScore ?: 0.0,
                imageUrl = savedImagePath,
                createdAt = System.currentTimeMillis().toString()
            )
            historyViewModel.savePhotoAndHistory(history)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val PREDICT_RESULT = "predict_result"
        const val PREDICT_IMAGE = "predict_image"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
