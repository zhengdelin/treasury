package com.example.treasury.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.treasury.R
import com.example.treasury.databinding.SampleInputComponentBinding

class InputComponent(
    context: Context, attrs:AttributeSet
):LinearLayout(context, attrs){
    private var binding:SampleInputComponentBinding = SampleInputComponentBinding.inflate(
        LayoutInflater.from(context),this, true
    )
    private var label:TextView
    private var editText:EditText
    init {
        inflate(context, R.layout.sample_input_component, this)

        val attributes = context.obtainStyledAttributes(
            attrs, R.styleable.InputComponent, 0, 0
        )
        label = binding.label
        editText = binding.editText

        //label
        label.text = attributes.getString(R.styleable.InputComponent_labelText)
        editText.hint = attributes.getString(R.styleable.InputComponent_placeholderText)

    }

    fun getInputData():String{
        return editText.text.toString()
    }
}