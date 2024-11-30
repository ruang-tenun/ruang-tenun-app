package com.ruangtenun.app.view.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.apply {
            cardProduct.setOnClickListener {
                findNavController().navigate(R.id.navigation_partner)
            }
        }

        return binding.root
    }

}