package com.example.dengluzhuce
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import retrofit2.Callback
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_label.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response
import java.math.BigDecimal.ONE

import kotlin.math.log

class Label : BaseActivity() {
    private var contentList:MutableList<String>?= arrayListOf()
    private var idList :MutableList<String>?= arrayListOf()
    private var idMap :MutableMap<String,String> ?= mutableMapOf()
    private val mListGrade = ArrayList<EvaluateBean>()
    private val mListSkin = ArrayList<EvaluateBean>()
    private val mListHaircut = ArrayList<EvaluateBean>()
    private val mListFigure = ArrayList<EvaluateBean>()
    private val mListAdornment = ArrayList<EvaluateBean>()
    private var mListLabel =
        listOf(mListGrade, mListSkin, mListHaircut, mListFigure, mListAdornment)

//    private val userSex: String = intent.getStringExtra("userSex").toString()
    private val userSex="boy"
    private val femaleGrade = listOf("大一", "大二", "大三", "大四", "读研中", "已毕业")
    private val femaleSkin = listOf("正常", "黑", "偏黑", " 白", " 偏白", "亚洲黄")
    private val femaleHaircut = listOf(
        "都不是",
        "寸头自来卷",
        "卷刘海",
        "齐刘海",
        "没刘海",
        "小平头",
        "大背头",
        "头发很长",
        "狗啃刘海",
        "中分",
        "三七分",
        "锡纸烫",
        "脏辫",
        "黄发色",
        "稀缺发色"
    )
    private val femaleFigure = listOf("都不是", "精壮", "很高", "正常", "微胖", "瘦高", "偏瘦", "高壮")
    private val femaleAdornment = listOf("都没有", "戴眼镜", "有手表", "有耳钉", "有项链", "有纹身", "戴帽子", "戴口罩")

    private val maleGrade = listOf("大一", "大二", "大三", "大四", "读研中", "已毕业")
    private val maleSkin = listOf("正常", "不白", "白的发光", " 白白哒", " 偏白", " 亚洲黄")
    private val maleHaircut = listOf(
        "都不是",
        "烫过卷",
        "短发",
        "长发及腰",
        "披肩发",
        "双马尾",
        "马尾",
        "黑长直",
        "大波浪",
        "空气刘海",
        "卷刘海",
        "没刘海",
        "黄发色",
        "稀缺发色"
    )
    private val maleFigure = listOf("都不是", "偏瘦", "偏高", "正常", "很高", "微胖", "瘦高")
    private val maleAdornment = listOf("都没有", "戴眼镜", "有手表", "有耳钉", "有项链", "有纹身", "戴帽子", "戴口罩")

    private val maleLabel = listOf(maleGrade, maleSkin, maleHaircut, maleFigure, maleAdornment)
    private val femaleLabel =
        listOf(femaleGrade, femaleSkin, femaleHaircut, femaleFigure, femaleAdornment)
    private var labelList: List<List<String>>? = null
    private var mFlowLayout1: FlowLayout? = null
    private var mFlowLayout2: FlowLayout? = null
    private var mFlowLayout3: FlowLayout? = null
    private var mFlowLayout4: FlowLayout? = null
    private var mFlowLayout5: FlowLayout? = null
    private var mFlowLayoutList: List<FlowLayout>? = null
    private val thisActivity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_label)
        mFlowLayout1 = findViewById<FlowLayout>(R.id.flow1)
        mFlowLayout2 = findViewById<FlowLayout>(R.id.flow2)
        mFlowLayout3 = findViewById<FlowLayout>(R.id.flow3)
        mFlowLayout4 = findViewById<FlowLayout>(R.id.flow4)
        mFlowLayout5 = findViewById<FlowLayout>(R.id.flow5)
        mFlowLayoutList =
            listOf(mFlowLayout1!!, mFlowLayout2!!, mFlowLayout3!!, mFlowLayout4!!, mFlowLayout5!!)
        if (userSex == "male") {
            labelList = maleLabel
        } else {
            labelList = femaleLabel
        }

        initView()
        val spanable = SpannableStringBuilder(skip.text)
        skip.setMovementMethod(LinkMovementMethod.getInstance())
        spanable.setSpan(
            object : ClickableSpan() {

                override fun onClick(v: View) {
                    val intent = Intent(thisActivity, lifePhoto::class.java)
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    skip.highlightColor = resources.getColor(R.color.transparent)
                }

            }, 0, skip.text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spanable.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    skip.context,
                    R.color.gray888888
                )

            ),
            0,
            skip.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        skip.setText(spanable)
        getIdMap()
        goOnButton.setOnClickListener {

            val idString=setId()
            val intent = Intent(thisActivity,lifePhoto::class.java)
            intent.putExtra("idString",idString)
            startActivity(intent)
        }


    }

    private fun initView() {


        for (i in 0 until labelList!!.size) {
            for (j in 0 until labelList!![i].size) {
                mListLabel[i].add(EvaluateBean(labelList!![i][j]))

            }
            for (labelBean in mListLabel[i]) {
                if(i == 2 || i ==4) {addChildToFlowLayoutMore(labelBean, mListLabel[i], mFlowLayoutList!![i])}
                else{addChildToFlowLayout(labelBean, mListLabel[i], mFlowLayoutList!![i])}
            }
        }
    }


    private fun addChildToFlowLayout(
        bean: EvaluateBean,
        mList: ArrayList<EvaluateBean>,
        mFlowLayout: FlowLayout
    ) {
        val tv: TextView = LayoutInflater.from(this).inflate(
            R.layout.flow_item,
            mFlowLayout,
            false
        ) as TextView
        tv.text = bean.name
        tv.tag = bean

        if (bean.checked) {
            tv.setBackgroundResource(R.drawable.label_shape_check)
            addContent(bean.name)
        } else {
            tv.setBackgroundResource(R.drawable.label_shape)
        }
        tv.setOnClickListener {
            bean.checked = true

            for (labelBean in mList) {
                delContent(labelBean.name)
                if (labelBean != bean) {
                    labelBean.checked = false

                }
            }
            checkLabel(mList, mFlowLayout)
            if (contentList!=null){
                goOnButton.isEnabled=true
                goOnButton.background=resources.getDrawable(R.drawable.button_selector)
                goOnButton.setTextColor(resources.getColor(R.color.white))
            }
            else{
                goOnButton.isEnabled=false
                goOnButton.setBackgroundResource(R.drawable.button_shape2)
                goOnButton.setTextColor(resources.getColor(R.color.grayc1))
            }
        }
        mFlowLayout?.addView(tv)
    }

    private fun checkLabel(mList: ArrayList<EvaluateBean>, mFlowLayout: FlowLayout) {
        mFlowLayout!!.removeAllViews()
        for (labelBean in mList) {
            addChildToFlowLayout(labelBean, mList, mFlowLayout)
        }
    }

    private fun addChildToFlowLayoutMore(
        bean: EvaluateBean,
        mList: ArrayList<EvaluateBean>,
        mFlowLayout: FlowLayout
    ) {
        val tv: TextView = LayoutInflater.from(this).inflate(
            R.layout.flow_item,
            mFlowLayout,
            false
        ) as TextView
        tv.text = bean.name
        tv.tag = bean
        if (bean.checked) {
            tv.setBackgroundResource(R.drawable.label_shape_check)

        } else {
            tv.setBackgroundResource(R.drawable.label_shape)

        }
        tv.setOnClickListener {
            bean.checked = !bean.checked
            if(bean.checked){addContent(bean.name)}
            else{delContent(bean.name)}
            checkLabelMore(mList,mFlowLayout)
            if (contentList!!.size!=0){

                goOnButton.isEnabled=true
                goOnButton.background=resources.getDrawable(R.drawable.button_selector)
                goOnButton.setTextColor(resources.getColor(R.color.white))
            }
            else{

                goOnButton.isEnabled=false
                goOnButton.setBackgroundResource(R.drawable.button_shape2)
                goOnButton.setTextColor(resources.getColor(R.color.grayc1))
            }
        }
        mFlowLayout?.addView(tv)
    }

    private fun checkLabelMore(mList: ArrayList<EvaluateBean>, mFlowLayout: FlowLayout) {
        mFlowLayout!!.removeAllViews()
        for (labelBean in mList) {
            addChildToFlowLayoutMore(labelBean, mList, mFlowLayout)
        }
    }
    private fun addContent(content:String){
        contentList?.add(content)
    }
    private fun delContent(content:String){
        if (contentList!=null) {
            for (index in 0 until contentList!!.size) {
                if (contentList!![index] == content) {
                    contentList!!.remove(content)
                    break}
            }
        }
    }
    private fun getIdMap(){
        val getAllTagIdService = ServiceCreator.create(GetAllTagIdService::class.java)
        getAllTagIdService.getAllTagId(0).enqueue(object : Callback<GetAllTagIdReturn>{
            override fun onResponse(
                call: Call<GetAllTagIdReturn>,
                response: Response<GetAllTagIdReturn>
            ) {
                val returnData = response.body()
                if(returnData!=null && returnData.code==0)
                {
                    val data= returnData.data.getAsJsonArray("tags")
                    for (item in data) {
                        var tags:JsonArray = item.asJsonObject.get("tags") as JsonArray
                        for (items in tags)
                        {
                            var length =  items.asJsonObject.get("content").toString().length
                            idMap!!.put(items.asJsonObject.get("content").toString().substring(1,length-1),items.asJsonObject.get("tagId").toString())

                        }


                    }


                }

            }



                override fun onFailure(call: Call<GetAllTagIdReturn>, t: Throwable) {
                    Log.d("code","$******************************************************{item.code}")
                    t.printStackTrace()
            }
        }  )

    }
    private fun setId():String{
        for (item in contentList!!){

            var id:String? = idMap!![item]
            idList!!.add(id!!)
        }

     return idList.toString().replace("[","").replace("]","").replace(" ","")
    }
    }



