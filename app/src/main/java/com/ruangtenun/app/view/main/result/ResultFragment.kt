package com.ruangtenun.app.view.main.result

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ruangtenun.app.data.api.response.PredictResponse
import com.ruangtenun.app.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null

    private val binding get() = _binding!!

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
            predictResult?.confidenceScore!! < 0.3 -> "Rendah"
            predictResult.confidenceScore in 0.3..0.6 -> "Cukup"
            predictResult.confidenceScore in 0.6..0.85 -> "Bagus"
            else -> "Sangat Bagus"
        }

        predictResult.let {
            binding.resultClass.text = it.result
            binding.resultDesc.text = "Tingkat Keyakinan: $confidenceLevel"
        }

        imageUri?.let {
            binding.resultImage.setImageURI(it)
        }

        return binding.root
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