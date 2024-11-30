package com.ruangtenun.app.view.main.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ruangtenun.app.databinding.MapsTypeBottomSheetBinding

class MapsTypeBottomSheet(private val mapTypeCallback: (Int) -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var binding: MapsTypeBottomSheetBinding
    private var selectedView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MapsTypeBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            tvDefault.setOnClickListener {
                updateSelectedView(it)
                mapTypeCallback(GoogleMap.MAP_TYPE_NORMAL)
            }

            tvSatellite.setOnClickListener {
                updateSelectedView(it)
                mapTypeCallback(GoogleMap.MAP_TYPE_SATELLITE)
            }

            tvTerrain.setOnClickListener {
                updateSelectedView(it)
                mapTypeCallback(GoogleMap.MAP_TYPE_TERRAIN)
            }

            tvHybrid.setOnClickListener {
                updateSelectedView(it)
                mapTypeCallback(GoogleMap.MAP_TYPE_HYBRID)
            }

            tvTitleMapType.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun updateSelectedView(newSelectedView: View) {
        selectedView?.isSelected = false

        newSelectedView.isSelected = true
        selectedView = newSelectedView
    }
}