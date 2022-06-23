package com.langyage.treasury.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.langyage.treasury.ColorData
import com.example.treasury.R
import com.example.treasury.databinding.SampleColorSelectorBinding

class ColorSelector(
    context: Context, attrs: AttributeSet
) : LinearLayout(context, attrs) {

    private var rSeekBar:SeekBar
    private var gSeekBar:SeekBar
    private var bSeekBar: SeekBar
    private var aSeekBar: SeekBar

    private var rTextView:TextView
    private var gTextView:TextView
    private var bTextView:TextView
    private var aTextView:TextView


    private var binding:SampleColorSelectorBinding = SampleColorSelectorBinding.inflate(
        LayoutInflater.from(context), this,true
    )

    //滑動軸被滑動時的事件監聽器
    private var seekBarChangeListener:SeekBar.OnSeekBarChangeListener
    init {
        inflate(context, R.layout.sample_color_selector, this)


        // Load attributes
        val attributes = context.obtainStyledAttributes(
            attrs, R.styleable.ColorSelector, 0, 0
        )
        //標題label
//        findViewById<TextView>(R.id.color_selector_label).text = attributes.getString(R.styleable.ColorSelector_titleLabel)
        binding.colorSelectorLabel.text = attributes.getString(R.styleable.ColorSelector_titleLabel)
        attributes.recycle()


        //rgba
        rSeekBar = binding.colorRSeekbar
        gSeekBar = binding.colorGSeekbar
        bSeekBar = binding.colorBSeekbar
        aSeekBar = binding.colorASeekbar
        rTextView = binding.colorRValue
        gTextView = binding.colorGValue
        bTextView = binding.colorBValue
        aTextView = binding.colorAValue

        //滑動軸被滑動時的事件監聽器
        seekBarChangeListener = object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when(seekBar){
                    aSeekBar -> {
                        val str = String.format("%3d", progress * 100 / 255) + "%"
                        aTextView.text = str
                    }
                    rSeekBar -> {
                        rTextView.text = progress.toString()
                    }
                    gSeekBar -> {
                        gTextView.text = progress.toString()
                    }
                    bSeekBar -> {
                        bTextView.text = progress.toString()
                    }
                    else ->{}
                }
                colorChange()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        }

        //a
        aSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        rSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        gSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        bSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)

        setColor(ColorData(255, 0, 0, 0))
    }

    //滑動軸變動時
    fun colorChange(){
        val color = getCurColor()
        setCurColorImageView(color)
    }
    //設置顯示現在顏色
    fun setCurColorImageView(color:String){
        Log.i("setCurColorImageView", color)
        binding.curColorImageView.setBackgroundColor(Color.parseColor(color))
    }

    //取得目前滑動條的16進位顏色
    fun getCurColor():String{
        var a = Integer.toHexString(aSeekBar.progress)
        var r = Integer.toHexString(rSeekBar.progress)
        var g = Integer.toHexString(gSeekBar.progress)
        var b = Integer.toHexString(bSeekBar.progress)

        if(a.length == 1) a = "0$a"
        if(r.length == 1) r = "0$r"
        if(g.length == 1) g = "0$g"
        if(b.length == 1) b = "0$b"
        return "#$a$r$g$b"
    }
    //設置滑動條
    fun setColor(colorData: ColorData){
        aSeekBar.progress = colorData.a
        rSeekBar.progress = colorData.r
        gSeekBar.progress = colorData.g
        bSeekBar.progress = colorData.b
    }
    //設置滑動條 傳入16進位 #ffffffff
    fun setColor(color:String){
        val colorData = hex2Rgba(color)
        Log.i("setColor", color)
        setColor(colorData)
    }
    //將16進位(#ffffffff)轉為rgba
    fun hex2Rgba(color:String): ColorData {
        val a = Integer.valueOf(color.substring(1,3), 16)
        val r = Integer.valueOf(color.substring(3,5), 16)
        val g = Integer.valueOf(color.substring(5,7), 16)
        val b = Integer.valueOf(color.substring(7,9), 16)
        return ColorData(a, r, g, b)
    }

}