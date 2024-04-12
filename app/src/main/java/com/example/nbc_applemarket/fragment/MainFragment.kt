package com.example.nbc_applemarket.fragment

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nbc_applemarket.R
import com.example.nbc_applemarket.adapter.DataAdapter
import com.example.nbc_applemarket.data.DataSource
import com.example.nbc_applemarket.databinding.FragmentMainBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DataAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        recyclerViewLine()
        finishAlert()
        binding.ivMainNotification.setOnClickListener {
            showNotification()
        }
    }

    //알림 울리기 (권한 검사 후)
    private fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {
                // 알림 권한이 없다면, 사용자에게 권한 요청
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                }
                startActivity(intent)
            }else{
                setUpNotification()
            }
        }

    }


    private fun setUpAdapter() {
        val dataSource = DataSource.getDataSource()

        adapter = DataAdapter(dataSource.getDataList())
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        //반드시 생성해줘야한다. RecyclerView의 item 배치를 어떻게 표시할 지 모르기때문
        //LinearLayout, GridLayoutManager 등등

        adapter.itemClick = object : DataAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val selectedData = dataSource.getDataList()[position]
                val detailFragment = DetailFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("selectedData", selectedData)
                    }
                }
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainer, detailFragment)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    //RecyclerView item 사이에 구분선 만들기
    private fun recyclerViewLine() {
        binding.recyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        }
    }

    //뒤로가기 눌렀을 때 종료 물어보기
    private fun finishAlert() { //todo
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(requireContext())
                    .setIcon(R.drawable.chat_num_svg)
                    .setTitle("종료")
                    .setMessage("정말 종료하시겠습니까?")
                    .setPositiveButton("확인") { _, _ ->
                        requireActivity().finish()
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }

        })
    }

    //알림 울리기 기초 설정
    private fun setUpNotification() {
        val builder = NotificationCompat.Builder(requireContext(), "notification")
            .setSmallIcon(R.drawable.icon_a)
            .setContentTitle("키워드 알림")
            .setContentText("설정한 키워드에 대한 알림이 도착했습니다.")

        val notificationManager: NotificationManager =
            requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "notification"
            val channelName = "채널이름"
            val descriptionText = "설명글"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(1002, builder.build())
        } else {
            notificationManager.notify(1002, builder.build())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}