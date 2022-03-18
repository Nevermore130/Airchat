package com.example.dengluzhuce
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.View.MeasureSpec.EXACTLY
class FlowLayout : ViewGroup {

    /**
     * 每行的View集合
     */
    private var mLinesView: MutableList<MutableList<View>> = mutableListOf()

    /**
     * 行高
     */
    private var mLineHeight: MutableList<Int> = mutableListOf()

    /**
     * 水平、垂直间距
     */
    private val mHorizontalSpacing = dp2px(10)//每个item横向间距
    private val mVerticalSpacing = dp2px(16) //每个item横向间距


    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    fun clearData() {
        mLinesView.clear()
        mLineHeight.clear()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        clearData()

        //孩子个数
        val childCount = childCount
        ll("childCount = $childCount")

        //解析 MeasureSpec
        val selfWidth = MeasureSpec.getSize(widthMeasureSpec)
        val selfWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        val selfHeight = MeasureSpec.getSize(widthMeasureSpec)
        val selfHeightMode = MeasureSpec.getMode(widthMeasureSpec)

        //当前行 使用宽度
        var lineUseWidth = 0
        //一行记录
        var lineViews = mutableListOf<View>()
        //行高
        var lineHeight = 0

        //该View 所需宽度 高度
        var parentNeedHeight = 0
        var parentNeedWidth = 0

        //遍历子View
        for (i in 0 until childCount) {

            val childView = getChildAt(i)

            val childLP = childView.layoutParams

            val childWidthMeasureSpec = getChildMeasureSpec(
                widthMeasureSpec,
                paddingLeft + paddingRight,
                childLP.width)

            val childHeightMeasureSpec = getChildMeasureSpec(
                heightMeasureSpec,
                paddingTop + paddingBottom,
                childLP.height
            )
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec)

            //子View 宽高
            val childMeasureWidth = childView.measuredWidth
            val childMeasureHeight = childView.measuredHeight
            ll("onMeasure childMeasureWidth = $childMeasureWidth")
            ll("onMeasure childMeasureHeight = $childMeasureHeight")

            if (childMeasureWidth + mHorizontalSpacing + lineUseWidth > selfWidth) {

                mLinesView.add(lineViews)
                mLineHeight.add(lineHeight)

                //存入
                parentNeedHeight += lineHeight + mVerticalSpacing
                parentNeedWidth = Integer.max(parentNeedWidth, lineUseWidth + mHorizontalSpacing)

                //重置换行
                lineViews = ArrayList()
                lineHeight = 0
                lineUseWidth = 0

            }

            lineViews.add(childView)
            lineUseWidth += childMeasureWidth + mHorizontalSpacing
            lineHeight = Integer.max(lineHeight, childMeasureHeight)


            if (i == childCount - 1) {
                mLinesView.add(lineViews)
                mLineHeight.add(lineHeight)
                parentNeedHeight += lineHeight + mVerticalSpacing
                parentNeedWidth = Integer.max(parentNeedWidth, lineUseWidth + mHorizontalSpacing)
            }

        }

        val realWidth = if (selfWidthMode == EXACTLY) selfWidth else parentNeedWidth
        val realHeight = if (selfHeightMode == EXACTLY) selfHeight else parentNeedHeight

        setMeasuredDimension(realWidth, realHeight)

    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        var curL: Int = paddingLeft
        var curT: Int = paddingTop

        for (i in 0 until mLinesView.size) {
            ll("onLayout line = $i")
            val curList: MutableList<View> = mLinesView[i]
            val lineHeight: Int = mLineHeight[i]

            curList.forEach {
                val left = curL
                val top = curT
                val right = left + it.measuredWidth
                val bottom = top + it.measuredHeight

                it.layout(left, top, right, bottom)

                curL = right + mHorizontalSpacing
            }
            curL = paddingLeft
            curT += lineHeight + mVerticalSpacing

        }


    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }


    fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    fun ll(msg: String) {
        Log.e("chenxh1", msg)
    }

}