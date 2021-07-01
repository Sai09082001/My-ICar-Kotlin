package com.example.icarchecking.view.act

import android.view.View
import com.example.icarchecking.R
import com.example.icarchecking.databinding.ActHomeBinding
import com.example.icarchecking.view.MapManager
import com.example.icarchecking.view.fragment.M000SplashFrg


class HomeAct : BaseActivity<ActHomeBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.act_home
    }

    override fun initBinding(rootView: View): ActHomeBinding {
        return ActHomeBinding.bind(rootView)
    }

    override fun initViews() {
        MapManager.getInstance().mContext = this
        showFrg(TAG, M000SplashFrg.TAG, false)
    }

    companion object {
        private val TAG = HomeAct::class.java.name
    }
}