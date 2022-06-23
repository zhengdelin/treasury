package com.langyage.treasury.folders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.treasury.R
import com.example.treasury.databinding.FolderListRecyclerViewLayoutBinding

class FolderListRecyclerViewAdapter(private val data:MutableList<FolderData>, private var optionsMenuClickListener: OptionsMenuClickListener):
RecyclerView.Adapter<FolderListRecyclerViewAdapter.ViewHolder>(){

    private var curPosition = -1
    fun getCurPosition():Int = curPosition

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int, clickedIcon:ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val binding = FolderListRecyclerViewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bindModel(item)
    }
    inner class ViewHolder(binding:FolderListRecyclerViewLayoutBinding):RecyclerView.ViewHolder(binding.root)
    {
        private var binding:FolderListRecyclerViewLayoutBinding
        init {
            this.binding = binding
        }
        fun bindModel(item: FolderData){
            val pos = adapterPosition

            //  Log.i("color:", item.color)
            val folderController = FolderController(binding.root.context)
            //設置文件夾顏色
            folderController.setCurrentFolder(item, binding.foldersName, binding.foldersImage)

//          Log.i("rv:","name:${item.name}, id:${item.id} color${item.color}")
            binding.root.setOnClickListener {
                val bundle:Bundle = folderController.folderDataToBundle(item)
                binding.root.findNavController().navigate(R.id.action_folderListFragment_to_passwordListFragment, bundle)
            }
            //點擊三個點顯示context menu
            binding.folderListActionIcon.setOnClickListener {
                curPosition = pos
                optionsMenuClickListener.onOptionsMenuClicked(pos, binding.folderListActionIcon)
//            Log.i("click","pos$pos")
            }

            binding.root.setOnLongClickListener {
                curPosition = pos
                binding.root.showContextMenu()
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }



}