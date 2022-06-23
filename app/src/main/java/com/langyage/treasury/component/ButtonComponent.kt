package com.langyage.treasury.component

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import com.example.treasury.R
import com.example.treasury.databinding.SampleButtonComponentBinding

class ButtonComponent(
    context: Context, attrs:AttributeSet
):LinearLayout(context, attrs){
    private var binding:SampleButtonComponentBinding = SampleButtonComponentBinding.inflate(
        LayoutInflater.from(context),this, true
    )
    private var button:Button

    private var text = "按鈕"
    private var textSize = resources.getDimension(R.dimen.defaultTextSize)
    private var textColor = Color.WHITE
    private var bgColor = Color.BLACK
    init {
        inflate(context, R.layout.sample_button_component, this)

        val attributes = context.obtainStyledAttributes(
            attrs, R.styleable.ButtonComponent, 0, 0
        )

        button = binding.button

        text = attributes.getString(R.styleable.ButtonComponent_text) ?: text
//        textSize = attributes.getDimension(R.styleable.ButtonComponent_textSize)
        textColor = attributes.getColor(R.styleable.ButtonComponent_textColor, textColor)
        bgColor = attributes.getColor(R.styleable.ButtonComponent_backgroundColor, bgColor)

        button.text = text
//        button.textSize = textSize
        button.setTextColor(textColor)
        button.setBackgroundColor(bgColor)
    }

    fun getButton():Button = binding.button

}