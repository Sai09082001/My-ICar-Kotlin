package com.example.icarchecking.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.icarchecking.R
import com.example.icarchecking.Storage
import com.example.icarchecking.view.api.model.CarInfoModelRes
import com.example.icarchecking.view.api.model.entities.CarInfoEntity
import com.example.icarchecking.view.callback.OnActionCallBack
import com.example.icarchecking.view.dialog.CarInfoDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*


class MapManager : LocationCallback() {
    var mMap: GoogleMap? = null
    var mMapTracking: GoogleMap? = null
    lateinit var mContext: Context
    private var myLocation: Marker? = null
    private lateinit var fusedLPC: FusedLocationProviderClient
    private val listCarMarker = ArrayList<Marker>()
    private var carInfoDialog: CarInfoDialog? = null
    lateinit var callBack: OnActionCallBack
    private var startMarker: Marker? = null
    private var endMarker: Marker? = null
    private var polyLine: Polyline? = null
    private val polyOp = PolylineOptions()

    @SuppressLint("VisibleForTests")
    fun initMap(mMap: GoogleMap?) {
        mMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap?.uiSettings?.isZoomControlsEnabled = true
        mMap?.uiSettings?.setAllGesturesEnabled(true)
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap?.isMyLocationEnabled = true
        mMap?.uiSettings?.isMyLocationButtonEnabled = false
        mMap?.setOnMarkerClickListener {
            if (it.tag != null) {
                showCarInfo(it.tag as CarInfoEntity)
            }
            true
        }

        //update my location
        fusedLPC = FusedLocationProviderClient(mContext)
        val locationReq = LocationRequest.create()
        locationReq.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationReq.interval = 2000
        fusedLPC.requestLocationUpdates(locationReq, this, Looper.getMainLooper())
    }

    private fun showCarInfo(tag: CarInfoEntity) {
        if (carInfoDialog == null) {
            carInfoDialog = CarInfoDialog(mContext, tag, object : OnActionCallBack {
                override fun callBack(key: String, data: Any?) {
                    if (key == CarInfoDialog.KEY_SHOW_TRACKING) {
                        callBack.callBack(CarInfoDialog.KEY_SHOW_TRACKING, data)
                    } else if (key == CarInfoDialog.KEY_SHOW_HISTORY) {
                        callBack.callBack(CarInfoDialog.KEY_SHOW_HISTORY, data)
                    }
                }
            })
        } else {
            carInfoDialog?.reload(tag)
        }
        carInfoDialog?.show()
    }

    private fun updateMyLocation(rs: LocationResult) {
        if (Storage.getInstance().myPos == null) {
            Storage.getInstance().myPos = rs.locations[0]

            if (mMap == null) return
            showMyLocation()
        } else {
            Storage.getInstance().myPos = rs.locations[0]
        }
        Log.d(TAG, "updateMyLocation: ${Storage.getInstance().myPos}}")
    }

    fun showMyLocation() {
        if (Storage.getInstance().myPos == null) return
        val pos = LatLng(
            Storage.getInstance().myPos!!.latitude,
            Storage.getInstance().myPos!!.longitude
        )

        if (myLocation == null) {
            val myLocationOp = MarkerOptions()
            myLocationOp.title("Vị trí của tôi")
            myLocationOp.position(pos)
            myLocationOp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            myLocation = mMap?.addMarker(myLocationOp)
        } else {
            myLocation?.position = pos
        }
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f))
    }

    fun showListCar(carInfoModelRes: CarInfoModelRes) {
        Log.i(TAG, "carInfoModelRes= $carInfoModelRes")
        if (carInfoModelRes.data == null) return
        for (car in listCarMarker) {
            car.remove()
        }

        listCarMarker.clear()
        for ((index, car) in (carInfoModelRes.data!!).withIndex()) {
            showCarOnMap(car, index)
        }
    }

    private fun showCarOnMap(car: CarInfoEntity, index: Int) {
        val op = MarkerOptions()
        op.title(car.carNumber)
        val pos = LatLng((car.lastLat ?: "0").toDouble(), (car.lastLng ?: "0").toDouble())
        op.snippet(car.carNumber)
        op.position(pos)
        op.icon(
            if (car.activeStatus.equals("online"))
                BitmapDescriptorFactory.fromResource(R.drawable.ic_car_64_active)
            else BitmapDescriptorFactory.fromResource(R.drawable.ic_car_64)
        )
        val marker = mMap?.addMarker(op)
        marker?.tag = car
        listCarMarker.add(marker!!)
        if (index == 0) {
            mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12f))
        }
    }

    override fun onLocationResult(rs: LocationResult) {
        updateMyLocation(rs)
    }

    fun stopHandleLocation() {
        fusedLPC.removeLocationUpdates(this)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: MapManager? = null
        private var TAG = MapManager.javaClass.name

        fun getInstance(): MapManager {
            if (INSTANCE == null) {
                INSTANCE = MapManager()
            }
            return INSTANCE!!
        }
    }

    fun updateStatusCar(carInfo: CarInfoEntity) {
        for (item in listCarMarker) {
            val tag = item.tag ?: continue
            val carItem = tag as CarInfoEntity
            if (carInfo.id == carItem.id) {
                carInfo.phoneManager = carItem.phoneManager
                carInfo.passwordManager = carItem.passwordManager
                item.tag = carInfo

                item.title = carInfo.carNumber
                val pos =
                    LatLng(
                        (carInfo.lastLat ?: "0").toDouble(),
                        (carInfo.lastLng ?: "0").toDouble()
                    )
                item.snippet = carInfo.carNumber
                item.position = pos
                item.setIcon(
                    if (carInfo.activeStatus.equals("online"))
                        BitmapDescriptorFactory.fromResource(R.drawable.ic_car_64_active)
                    else BitmapDescriptorFactory.fromResource(R.drawable.ic_car_64)
                )
                break
            }
        }
    }

    fun updateTrackingCar(carInfo: CarInfoEntity) {
        if (endMarker == null || endMarker!!.tag == null) return

        val carItem = endMarker!!.tag as CarInfoEntity

        carInfo.phoneManager = carItem.phoneManager
        carInfo.passwordManager = carItem.passwordManager
        endMarker!!.tag = carInfo

        endMarker!!.title = carInfo.carNumber
        val pos =
            LatLng(
                (carInfo.lastLat ?: "0").toDouble(),
                (carInfo.lastLng ?: "0").toDouble()
            )
        endMarker!!.snippet = carInfo.carNumber
        endMarker!!.position = pos
        endMarker!!.setIcon(
            if (carInfo.activeStatus.equals("online"))
                BitmapDescriptorFactory.fromResource(R.drawable.ic_car_64_active)
            else BitmapDescriptorFactory.fromResource(R.drawable.ic_car_64)
        )

        polyOp.add(pos)
        polyLine!!.remove()
        polyLine = mMapTracking!!.addPolyline(polyOp)
    }

    fun showCarTracking(car: CarInfoEntity) {
        if (startMarker != null) {
            startMarker!!.remove()
            endMarker!!.remove()
        }
        val op = MarkerOptions()
        op.title(car.carNumber)
        val pos = LatLng((car.lastLat ?: "0").toDouble(), (car.lastLng ?: "0").toDouble())
        op.snippet(car.carNumber)
        op.icon(
            BitmapDescriptorFactory.fromResource(R.drawable.ic_flag_64)
        )
        op.position(pos)

        startMarker = mMapTracking?.addMarker(op)
        startMarker!!.tag = car
        op.icon(
            if (car.activeStatus.equals("online"))
                BitmapDescriptorFactory.fromResource(R.drawable.ic_car_64_active)
            else BitmapDescriptorFactory.fromResource(R.drawable.ic_car_64)
        )
        endMarker = mMapTracking?.addMarker(op)
        endMarker!!.tag = car

        polyOp.color(Color.RED)
            .width(10F)
            .add(pos, pos)
            .geodesic(true)
        polyLine = mMapTracking?.addPolyline(polyOp)
        mMapTracking?.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12f))
    }
}