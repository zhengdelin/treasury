package com.langyage.treasury.passwords

import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.langyage.treasury.R
import com.langyage.treasury.databinding.PasswordListRecyclerViewLayoutBinding


class PasswordListRecyclerViewAdapter(
    private var data:ArrayList<PasswordData>,
    private var optionsMenuClickListener: OptionsMenuClickListener
     ):RecyclerView.Adapter<PasswordListRecyclerViewAdapter.ViewHolder>() {

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int, clickedIcon:ImageView)
    }

    private var showPassword:Boolean = false
    private lateinit var binding:PasswordListRecyclerViewLayoutBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = PasswordListRecyclerViewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bindModel(item)
    }

    override fun getItemCount() = data.size

    inner class ViewHolder(binding: PasswordListRecyclerViewLayoutBinding):RecyclerView.ViewHolder(binding.root){
        private var _binding:PasswordListRecyclerViewLayoutBinding?=null
        private val binding get() = _binding!!
        init {
            _binding = binding
        }
        fun bindModel(item: PasswordData){
            binding.passwordListTitle.text = item.title
            binding.passwordListNote.text = item.note
            Log.i("click","pos${binding.passwordListTitle.text}")

            togglePassword(item)

            //顯示密碼事件監聽器
            binding.passwordListPasswordShowIcon.setOnClickListener {
                showPassword = !showPassword
                togglePassword(item)
            }

            //點擊三個點顯示context menu
            binding.passwordListActionIcon.setOnClickListener {
//                curPosition = adapterPosition
                optionsMenuClickListener.onOptionsMenuClicked(adapterPosition, binding.passwordListActionIcon)
            }
        }
        private fun togglePassword(item: PasswordData){
            //密碼長度
            val passwordLength = item.password.length
            val passwordListPassword = binding.passwordListPassword
            val passwordListPasswordShowIcon = binding.passwordListPasswordShowIcon
            Log.i("togglePassword","title:${item.title}, password:${item.password}, icon")

            if(showPassword){
                passwordListPassword.text = item.password
                passwordListPasswordShowIcon.setImageResource(R.drawable.ic_eye_slash_solid)
                return
            }
            passwordListPassword.text = "*".repeat(passwordLength)
            passwordListPasswordShowIcon.setImageResource(R.drawable.ic_eye_solid)
        }

    }

}
