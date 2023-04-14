package com.example.servicecreate.ui.controller.camera

import android.content.res.Configuration
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.base.activity.BaseFragment
import com.example.servicecreate.databinding.FragmentCameraBinding
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils


class CameraFragment(id: Long, roomName: String) : BaseFragment<FragmentCameraBinding>() {

    private val mViewModel: CameraViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[CameraViewModel::class.java]
    }

    override fun FragmentCameraBinding.initBindingView() {
        this.viewModel = mViewModel

        mViewModel.orient = OrientationUtils(requireActivity(), gsyVideo)
        mViewModel.orient.isEnable = false

        with(gsyVideo){

            setUp(mViewModel.url, true, mViewModel.title)

            titleTextView.visibility = View.VISIBLE

            setIsTouchWiget(true)
            isRotateViewAuto = true
            isLockLand = true
            isShowFullAnimation = true
            setIsTouchWiget(false)

            setVideoAllCallBack(object: GSYSampleCallBack() {
                override fun onPrepared(url: String?, vararg objects: Any?) {
                    super.onPrepared(url, *objects)
                    mViewModel.orient.isEnable = true
                    mViewModel.isPlay = true
                }
            })

            fullscreenButton.setOnClickListener {
                this.startWindowFullscreen(requireActivity(), true, true)
            }

            setLockClickListener { _, lock ->
                mViewModel.orient.isEnable = !lock
            }
        }
    }

    override fun onPause() {
        binding.gsyVideo.currentPlayer.onVideoPause()
        super.onPause()
        mViewModel.isPause = true
    }

    override fun onResume() {
        binding.gsyVideo.currentPlayer.onVideoResume(false);
        super.onResume()
        mViewModel.isPause = false;
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mViewModel.isPlay) {
            binding.gsyVideo.currentPlayer.release();
        }
        mViewModel.orient.releaseListener();
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (mViewModel.isPlay && !mViewModel.isPause) {
            binding.gsyVideo.onConfigurationChanged(requireActivity(), newConfig, mViewModel.orient, true, true);
        }
    }

}