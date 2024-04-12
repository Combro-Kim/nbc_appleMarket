package com.example.nbc_applemarket.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nbc_applemarket.R
import com.example.nbc_applemarket.data.Data
import com.example.nbc_applemarket.databinding.FragmentDetailBinding
import java.text.DecimalFormat


class DetailFragment : Fragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding =  FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //데이터 넘김
        arguments?.getParcelable("selectedData",Data::class.java)?.let {
            inputEachData(it)
        }
        //back Button 클릭 시
        goBack()
    }

    private fun goBack() {
        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }


    private fun inputEachData(data: Data){
        binding.apply {
            ivDetailImage.setImageResource(data.image)
            tvDetailName.text = data.name
            tvDetailLocation.text = data.location
            tvDetailMannerTemperature.text = data.mannerTemperature.toString()
            tvDetailTitle.text = data.title
            tvDetailDescription.text = data.description
            val decimal = DecimalFormat("#,###")
            tvDetailPrice.text = decimal.format(data.price.toString().toInt())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}