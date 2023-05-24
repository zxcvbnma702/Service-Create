package com.example.servicecreate.ui.home.watch


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.base.activity.BaseFragment
import com.example.servicecreate.R
import com.example.servicecreate.databinding.FragmentWatchBinding
import com.example.servicecreate.logic.network.model.BraceletResponse
import com.example.servicecreate.ui.dialogCancelInfo
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.Utils
import com.kongzue.dialogx.dialogs.BottomDialog
import com.kongzue.dialogx.interfaces.OnBindView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.random.nextInt


class WatchFragment : BaseFragment<FragmentWatchBinding>(), WatchListener,
    SwipeRefreshLayout.OnRefreshListener, OnChartValueSelectedListener {

    private lateinit var bottomDialog: BottomDialog
    internal val mViewModel: WatchViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[WatchViewModel::class.java]
    }

    override fun FragmentWatchBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.watchListener = this@WatchFragment

        watchToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.watchCirProgressInner.progress = 88f
        binding.watchCirProgress.text = "88"

        watchSwipeLayout.apply {
            setColorSchemeColors(
                getColor(requireContext(), R.color.main_color),
                resources.getColor(R.color.main_color_primary_variant),
                resources.getColor(R.color.main_color_primary_secondary)
            )
            setOnRefreshListener(this@WatchFragment)
        }

        watch4.setOnClickListener {
            bottomDialog = BottomDialog.show(getString(R.string.watch_health_title),
                object : OnBindView<BottomDialog?>(R.layout.layout_dialog_health) {
                    override fun onBind(dialog: BottomDialog?, v: View) {
                        initDialogView(v)
                    }
                })
                .setRadius(30f)
                .setCancelTextInfo(dialogCancelInfo(requireContext()))
                .setOkButton { dialog, v ->
                    false
                }.setCancelButton { dialog, v ->
                    false
                }
        }


        mViewModel.braceLet()
    }

    @SuppressLint("SetTextI18n")
    private fun initDialogView(v: View) {
        val sleepTime = v.findViewById<TextView>(R.id.watch_health_sleeptime_text)
        sleepTime.text = mViewModel.getTimeText()
        val pie = v.findViewById<PieChart>(R.id.chart1)
        pie.apply {
            dragDecelerationFrictionCoef = 0.95f
            centerText = generateCenterSpannableText()
            isDrawHoleEnabled = true
            holeRadius = 58f
            transparentCircleRadius = 61f
            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true
            setUsePercentValues(true)
            setExtraOffsets(5f, 10f, 5f, 5f)
            setHoleColor(Color.WHITE)
            setCenterTextTypeface(Typeface.SERIF)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            setDrawCenterText(true)
            setOnChartValueSelectedListener(this@WatchFragment);
            animateY(1400, Easing.EaseInOutQuad);
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTypeface(Typeface.SANS_SERIF)
            setEntryLabelTextSize(12f)

            data = mViewModel.setData(2, 100f)

            description.text = "此数据仅供参考"

            highlightValues(null)
            invalidate()
        }
        val line = v.findViewById<LineChart>(R.id.chart2)
        line.apply {

            setBackgroundColor(Color.WHITE)

            description.isEnabled = true

            setTouchEnabled(true)

            setDrawGridBackground(false)

            isDragEnabled = true
            setScaleEnabled(true)

            setPinchZoom(true)

            xAxis.enableGridDashedLine(10f, 10f, 0f)

            axisRight.isEnabled = false

            axisLeft.enableGridDashedLine(10f, 10f, 0f)

            axisLeft.axisMaximum = 100f
            axisLeft.axisMinimum = 30f

            description.text = "此数据仅供参考"

        }

        setData(mViewModel.sleepTime, 100f, line)

    }

    override fun onBraceLet(result: LiveData<Result<BraceletResponse>>) {
        result.observe(this) { re ->
            val response = re.getOrNull()
            if (response != null) {
                binding.watchHeartText.text = "心率: ${response.data.heart} BPM"
                binding.watchStepsText.text = "步数: ${response.data.steps} S"
                binding.watchHiperText.text = "高压: ${response.data.higeper} mmHg"
                binding.watchLowerText.text = "低压: ${response.data.lowper} mmmHg"
            }
        }
    }

    override fun onRefresh() {
        mViewModel.braceLet()
        val job = Job()
        CoroutineScope(job).launch {
            withContext(Dispatchers.Main) {
                delay(3000)
                binding.watchSwipeLayout.isRefreshing = false
            }
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {

    }

    private fun generateCenterSpannableText(): SpannableString? {
        val s = SpannableString("今日睡眠数据\n by 智能云居")
        s.setSpan(RelativeSizeSpan(1.3f), 0, 6, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 0, 6, 0)
        s.setSpan(ForegroundColorSpan(Color.argb(100,140, 146,251)), 0, 6, 0)

        s.setSpan(ForegroundColorSpan(Color.GRAY), 6, s.length - 5, 0)
        s.setSpan(RelativeSizeSpan(.8f), 6, s.length - 5, 0)

        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 4, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 4, s.length, 0)
        return s
    }

    private fun setData(count: Int, range: Float, chart: LineChart) {
        val values = ArrayList<Entry>()
        for (i in 0 until count) {
            val heart = Random.nextInt(50 .. 65)
            values.add(Entry(i.toFloat(), heart.toFloat(), getDrawable(requireContext(), R.drawable.baseline_favorite_border_24)))
        }
        val set1: LineDataSet
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = LineDataSet(values, "心率数据")
            set1.setDrawIcons(false)

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set1.color = Color.argb(100,127, 133,249)
            set1.setCircleColor(Color.argb(100,127, 133,249))

            // line thickness and point size
            set1.lineWidth = 4f
            set1.circleRadius = 4f

            // draw points as solid circles
            set1.setDrawCircleHole(true)

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }

            set1.fillColor = Color.argb(100,140, 146,251)

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart.data = data
        }
    }


}

