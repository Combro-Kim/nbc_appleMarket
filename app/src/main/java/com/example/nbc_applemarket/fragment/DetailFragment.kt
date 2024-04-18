package com.example.nbc_applemarket.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.example.nbc_applemarket.R
import com.example.nbc_applemarket.adapter.DataAdapter
import com.example.nbc_applemarket.data.Data
import com.example.nbc_applemarket.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var isHeart = false
    private lateinit var adapter: DataAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //데이터 넘김
        arguments?.getParcelable("selectedData", Data::class.java)?.let {
            inputEachData(it)
            if (it.isHeart){
                binding.ivHeart.setImageResource(R.drawable.icon_red_heart)
            }else{
                binding.ivHeart.setImageResource(R.drawable.heart_num_svg)
            }
        }
        //back Button 클릭 시
        binding.btnBack.setOnClickListener {
            goBack()
        }
        //기기의 back버튼 클릭 시 main의 종료 AlertDialog 발생 -> 해결
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            goBack()
        }

        //좋아요 기능
        binding.ivHeart.setOnClickListener {
            if (!isHeart) {
                binding.ivHeart.setImageResource(R.drawable.icon_red_heart)
                Snackbar.make(view, "관심 목록에 추가되었습니다.", Snackbar.LENGTH_SHORT).show()
            } else {
                binding.ivHeart.setImageResource(R.drawable.heart_num_svg)
            }
            isHeart = !isHeart
        }
    }



    private fun goBack() {
        requireActivity().supportFragmentManager.popBackStack()
        //todo Main으로 데이터 전달
/*        val mainFragment = MainFragment().apply {
            arguments = Bundle().apply {
                putBoolean("like",isHeart)
            }
        }*/
    }


    private fun inputEachData(data: Data) {
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