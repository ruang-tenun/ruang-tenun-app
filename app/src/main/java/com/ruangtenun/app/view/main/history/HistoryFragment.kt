package com.ruangtenun.app.view.main.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

}