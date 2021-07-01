package com.example.icarchecking.view.fragment

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import com.example.icarchecking.CommonUtils
import com.example.icarchecking.R
import com.example.icarchecking.databinding.FrgM003MenuMainBinding
import com.example.icarchecking.view.MapManager
import com.example.icarchecking.view.viewmodel.BaseViewModel
import com.example.icarchecking.view.viewmodel.M003MenuViewModel
import com.google.android.gms.maps.SupportMapFragment


class M003MenuFrg : BaseFragment<FrgM003MenuMainBinding, M003MenuViewModel>() {
    companion object {
        val TAG: String = M003MenuFrg::class.java.name
    }

    override fun initViews() {
        checkPermission()
        val phone = CommonUtils.getInstance().getPref(BaseViewModel.PHONE)
        val name = CommonUtils.getInstance().getPref(BaseViewModel.USER_NAME)

        binding?.includeMenu?.tvUserName?.text = name ?: "Chưa xác định"
        binding?.includeMenu?.tvPhone?.text = phone
        binding?.drawer?.addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                binding?.includeActionbar?.ivMenu?.setImageResource(R.drawable.ic_menu_open_24)
            }

            override fun onDrawerClosed(drawerView: View) {
                binding?.includeActionbar?.ivMenu?.setImageResource(R.drawable.ic_menu_24)
            }
        })

        val mapFrg = fragmentManager?.findFragmentById(R.id.frg_map) as SupportMapFragment
        mapFrg.getMapAsync {
            MapManager.getInstance().mMap = it
            MapManager.getInstance().initMap()
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                mContext as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 101
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        for (rs in grantResults) {
            if (rs != PackageManager.PERMISSION_GRANTED) {
                showNotify("Hãy cho phép quyền truy cập vị trí để sử dụng ứng dụng")
            }
        }
    }

    override fun initBinding(mRootView: View): FrgM003MenuMainBinding {
        return FrgM003MenuMainBinding.bind(mRootView)
    }

    override fun getViewModelClass(): Class<M003MenuViewModel> {
        return M003MenuViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m003_menu_main
    }
}
