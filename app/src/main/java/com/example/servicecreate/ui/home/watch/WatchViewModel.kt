package com.example.servicecreate.ui.home.watch

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.TimeUtils
import androidx.annotation.RequiresApi
import androidx.core.util.toRange
import androidx.lifecycle.ViewModel
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.ui.home.person.PersonListener
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import java.time.LocalTime
import java.util.concurrent.ThreadLocalRandom
import kotlin.properties.Delegates
import kotlin.random.Random

/**
 * @author:SunShibo
 * @date:2023-05-21 23:18
 * @feature:
 */
class WatchViewModel: ViewModel() {
    internal var sleepTime by Delegates.notNull<Int>()
    internal var watchListener: WatchListener ?=null
    private val repository = Repository

    private var startMS: String = ""
    private var endMS: String = ""
    private var startSS: String = ""
    private var endSS: String = ""
    internal var timeSleep = ""

    private val token = ServiceCreateApplication.appSecret

    fun braceLet(){
        getTimeText()
        val result = repository.getBraceLet()
        watchListener?.onBraceLet(result)
    }

    private fun getTimeText(){
        val startM = arrayListOf(22, 23, 24).random()
        val startS = Random.nextInt(0, 59)
        val endM = arrayListOf(6, 7, 8).random()
        val endS = Random.nextInt(0, 59)
        sleepTime = (endM * 60 + endS + (24 - startM) * 60 - startS) / 60

        startMS = if(startM == 24){
            "00"
        }else{
            startM.toString()
        }

        startSS = if(startS < 10){
            "0${startS}"
        }else{
            startS.toString()
        }

        endSS = if(endS < 10){
            "0${endS}"
        }else{
            endS.toString()
        }

        endMS = endM.toString()
        timeSleep = "${startMS}:${startSS} -- ${endMS}:${endSS}"
    }

    internal fun setData(count: Int, range: Float): PieData {
        val times = (210.. 260).random().toFloat()
        val deepSleep = (times / 1000) * 100
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        val entries = arrayListOf(
            PieEntry(deepSleep, "深度睡眠"),
            PieEntry(range - deepSleep, "浅度睡眠"),
        )

        val dataSet = PieDataSet(entries, "睡眠类型")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
        val colorPies = arrayListOf<IntArray>(
            ColorTemplate.VORDIPLOM_COLORS,
            ColorTemplate.JOYFUL_COLORS,
            ColorTemplate.COLORFUL_COLORS,
            ColorTemplate.LIBERTY_COLORS,
            ColorTemplate.PASTEL_COLORS)
        for (c in colorPies.random()){
            colors.add(c)
        }
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        //set data for pie
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        data.setValueTypeface(Typeface.SANS_SERIF)
        return data
    }
}