package com.example.nbc_applemarket.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nbc_applemarket.R
import com.example.nbc_applemarket.adapter.DataAdapter
import com.example.nbc_applemarket.data.DataSource
import com.example.nbc_applemarket.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DataAdapter
    private var dataSource = DataSource.getDataSource()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        recyclerViewLine()
        finishAlert()
        scrollEvent()

        //알림
        binding.ivMainNotification.setOnClickListener {
            showNotification()
        }
    }


    //알림 울리기(권한 요청)
    private fun showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()) {
                // 알림 권한이 없다면, 사용자에게 권한 요청
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                }
                startActivity(intent)
            } else {
                setUpNotification()
            }
        }

    }


    private fun setUpLike() {
        val isLiked = arguments?.getBoolean("like") ?: false

        /*val dataAdapter = DataAdapter().apply {
            arguments = Bundle().apply {
                putBoolean("likeToAdapter",isLiked)
            }
        }*/
    }

    //adapter
    private fun setUpAdapter() {
        val isLiked = arguments?.getBoolean("like") ?: false

        adapter = DataAdapter(dataSource.getDataList(),isLiked)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        adapter.itemClick = object : DataAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val selectedData = dataSource.getDataList()[position]


                //Detail에 데이터 전달
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

        //아이템 삭제
        adapter.itemLongClick = object : DataAdapter.ItemLongClick {
            override fun onLongClick(view: View, position: Int) {
                AlertDialog.Builder(requireContext())
                    .setIcon(R.drawable.chat_num_svg)
                    .setTitle("상품 삭제")
                    .setMessage("상품을 정말로 삭제하시겠습니까?")
                    .setPositiveButton("확인") { _, _ ->

                        dataSource.removeItem(position)
                        adapter.notifyItemRemoved(position) //todo IndexOutOfBoundsException 원인 찾기
                        adapter.notifyItemRangeChanged(position, adapter.itemCount - position)
                        //notifyItemRangeChanged(삭제된 아이템의 위치, 업데이트할 아이템의 개수(삭제 후속 아이템들의 개수))
                        Snackbar.make(view, "선택한 상품을 삭제했습니다.", Snackbar.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("취소", null)
                    .show()
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
    private fun finishAlert() {
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

    //스크롤 최상단 올리기
    private fun scrollEvent() {
        val btnUp = binding.btnUp
        //fadeIn, fadeOut 투명 애니메이션
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 300 }
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 300 }

        //스크롤 이벤트 감지
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) { //OnScrolled vs onScrollStateChange
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // SCROLL_STATE_SETTLING, SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING
                    if (!recyclerView.canScrollVertically(-1)) { //최상단일 경우 -1 <> 최하단일 경우 0
                        btnUp.startAnimation(fadeOut)
                        btnUp.visibility = View.GONE
                    } else {
                        if (btnUp.visibility == View.GONE) {
                            btnUp.startAnimation(fadeIn)
                        }
                        btnUp.visibility = View.VISIBLE
                    }
                }
            }
        })
        btnUp.setOnClickListener {//최상단으로 이동
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}