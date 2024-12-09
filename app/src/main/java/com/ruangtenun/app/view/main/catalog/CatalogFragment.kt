package com.ruangtenun.app.view.main.catalog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.FragmentCatalogBinding
import com.ruangtenun.app.databinding.FragmentScannerBinding

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)


        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}