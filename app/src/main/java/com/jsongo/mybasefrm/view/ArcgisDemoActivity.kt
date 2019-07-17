package com.jsongo.mybasefrm.view

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.esri.android.map.GraphicsLayer
import com.esri.android.map.LocationDisplayManager
import com.esri.android.map.MapOnTouchListener
import com.esri.android.map.MapView
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer
import com.esri.android.map.event.OnStatusChangedListener
import com.esri.android.runtime.ArcGISRuntime
import com.esri.core.geometry.GeometryEngine
import com.esri.core.geometry.Point
import com.esri.core.io.UserCredentials
import com.esri.core.map.Graphic
import com.esri.core.symbol.PictureMarkerSymbol
import com.jsongo.mybasefrm.BaseActivity
import com.jsongo.mybasefrm.R
import com.safframework.log.L
import kotlinx.android.synthetic.main.activity_arcgis_demo.*
import kotlinx.android.synthetic.main.activity_base.*

class ArcgisDemoActivity : BaseActivity() {

    var mapscale = 5000.0
    var markPoint: Point? = null
    var clickPoint: Point? = null
    var graphicsLayer: GraphicsLayer? = null
    var locationDisplayManager: LocationDisplayManager? = null

    override var mainLayoutId = R.layout.activity_arcgis_demo
    override var useContainer2: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        topbar.setTitle("Arcgis")
        smart_refresh_layout.isEnabled = false
        setSwipeBackEnable(false)

        ArcGISRuntime.setClientId("L8zvYbPtXBtrHJ26")
        val creds = UserCredentials()
        creds.setUserAccount("tcdldzww", "tcdld12345")
        creds.tokenServiceUrl = "http://10.35.207.185:8080/RemoteTokenServer"

        val gisTiledMapServiceLayer = ArcGISTiledMapServiceLayer(
            "http://10.35.207.185:8080/OneMapServer/rest/services/tcsl_new/MapServer",
            creds
        )
        mapview.addLayer(gisTiledMapServiceLayer)

//        val gisTiledMapServiceLayer2 =
//            ArcGISTiledMapServiceLayer("http://58.211.227.180:8051/OneMapServer/rest/services/zjg2015/MapServer")
//        mapview.addLayer(gisTiledMapServiceLayer2)
        graphicsLayer = GraphicsLayer()

        mapview.addLayer(graphicsLayer)

        mapview.setOnTouchListener(MyTouchListener(this, mapview))

        //延迟加载
        mapview.setOnStatusChangedListener { any, status ->
            if (any == mapview && status == OnStatusChangedListener.STATUS.INITIALIZED) {
                mapview.postDelayed({
                    mapview.visibility = View.VISIBLE
                    //自动定位
                    setupLocationListener()
                }, 500L)
            }
        }
    }

    private fun setupLocationListener() {
        mapview?.let {
            if (it.isLoaded) {
                locationDisplayManager = it.locationDisplayManager
                locationDisplayManager?.locationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        location?.let {
                            L.i("定位：x: ${it.longitude},y: {${it.latitude}")

                            clickPoint = getAsPoint(location)
                            zoomToPoint(clickPoint!!, R.drawable.cgt_target_red)

                            //设置定位模式
                            locationDisplayManager?.autoPanMode =
                                LocationDisplayManager.AutoPanMode.LOCATION
                            //关闭定位
//                        locationDisplayManager?.stop()
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                    }

                    override fun onProviderEnabled(provider: String?) {
                    }

                    override fun onProviderDisabled(provider: String?) {
                    }
                }
                //开始定位
                locationDisplayManager?.start()
            }
        }
    }

    // 缩放到指定位置
    fun zoomToPoint(point: Point, imgid: Int) {
        graphicsLayer?.removeAll()
        mapview?.let {
            // 在地图上标注位置
            val pictureFillSymbol =
                PictureMarkerSymbol(it.getContext(), resources.getDrawable(imgid))
            val graphic = Graphic(point, pictureFillSymbol)
            graphicsLayer?.addGraphic(graphic)

            // 缩放到位置点
            it.zoomToScale(point, mapscale)
        }
    }

    // GPS坐标 转 Point
    private fun getAsPoint(loc: Location): Point? {
        mapview?.let {
            val wgsPoint = Point(loc.longitude, loc.latitude)
            return GeometryEngine.project(
                wgsPoint,
                it.spatialReference,
                it.spatialReference
            ) as Point
        }
        return null
    }

    inner class MyTouchListener(context: Context, mapview: MapView) :
        MapOnTouchListener(context, mapview) {
        override fun onSingleTap(point: MotionEvent?): Boolean {
            L.d("当前地图比例：${mapview.scale}")
            return true
        }

        //长按缩放
        override fun onLongPress(event: MotionEvent?) {
            super.onLongPress(event)
            event?.apply {
                L.d("long press x:${x},y:${y}")
                markPoint = mapview.toMapPoint(x, y)
                zoomToPoint(markPoint!!, R.drawable.cgt_target_red)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        mapview?.postDelayed({
            mapview?.apply {
                unpause()
            }
        }, 500)
    }

    override fun onPause() {
        super.onPause()
        mapview?.apply {
            pause()
        }
    }

    override fun onDestroy() {
        mapview?.apply {
            destroyDrawingCache()
            recycle()
        }
        super.onDestroy()
    }
}
