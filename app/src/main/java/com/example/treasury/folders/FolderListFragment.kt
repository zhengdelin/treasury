package com.example.treasury.folders

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.treasury.AlertDialogController
import com.example.treasury.R
import com.example.treasury.databinding.FragmentFolderListBinding
import com.example.treasury.passwords.PasswordListRecyclerViewAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FoldersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FolderListFragment : Fragment() {
    private var _binding:FragmentFolderListBinding? = null
    private val binding get() = _binding!!

    private lateinit var folderListRecyclerViewAdapter:FolderListRecyclerViewAdapter
    private lateinit var folderController: FolderController
    private lateinit var folderListRecyclerView: RecyclerView
    private var folderList:ArrayList<FolderData> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolderListBinding.inflate(inflater, container, false)
        val root:View= binding.root
        folderController = FolderController(root.context, activity)
        //取得recyclerView
        folderListRecyclerView = binding.folderListRecyclerView
//        同步recycler view資料
//        syncFolderList()

        binding.toFolderCreateBtn.setOnClickListener {
            root.findNavController().navigate(R.id.action_folderListFragment_to_folderCreateFragment)
        }

//        註冊上下文菜單
        registerForContextMenu(folderListRecyclerView)

        Log.i("FolderListFragment:","onCreateView")
        return root
    }

    override fun onResume() {
        super.onResume()
        Log.i("onResume","")
        syncFolderList()
    }
    //實作點擊彈出上下文菜單監聽器
    private val optionsMenuClickListener = object : FolderListRecyclerViewAdapter.OptionsMenuClickListener{
        override fun onOptionsMenuClicked(position: Int, clickedIcon:ImageView) {
            //popupMenu
            val popupMenu = PopupMenu(context,clickedIcon)
            //填充menu
            popupMenu.menuInflater.inflate(R.menu.folder_list_context_menu, popupMenu.menu)
            //設置監聽器
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    return onMenuItemClick(item, position)
                }
            })
            popupMenu.show()
        }
    }
    private fun onMenuItemClick(item:MenuItem?, position:Int) : Boolean {
        val folderData = folderList[position]
        when(item?.itemId){
            R.id.action_edit_folder->{
                val bundle:Bundle = folderController.folderDataToBundle(folderData)
                binding.root.findNavController().navigate(R.id.action_folderListFragment_to_folderUpdateFragment, bundle)
            }
            R.id.action_delete_folder->{
                AlertDialogController(binding.root.context).showSingleCheckBoxDialog(
                    "確定要刪除資料夾嗎?(將會連同資料夾下的密碼一同刪除)",
                    "連同雲端資料一起刪除",
                    "刪除"
                ){
                    folderController.deleteFolder(folderData, it){
                        syncFolderList()
                    }
                }
            }
        }
        return false
    }
//    上下文菜單被點擊時
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return onMenuItemClick(item, folderListRecyclerViewAdapter.getCurPosition())
    }

    //常按的上下文菜單
    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val menuInflater = MenuInflater(v.context)
        menuInflater.inflate(R.menu.folder_list_context_menu, menu)
    }

    fun syncFolderList(){
        //生成要放入recyclerView的資料
        folderList = folderController.getFolders()
        if(folderList.size == 0){
            Log.i("syncFolderList:","syncFolderList")
            folderController.addFolder(FolderData("預設資料夾", "#ff000000")){
                folderList = folderController.getFolders()
                handleAdaptRecyclerView()
            }
            return
        }
        handleAdaptRecyclerView()
    }
    private fun handleAdaptRecyclerView(){
        //產生adapter
        folderListRecyclerViewAdapter = FolderListRecyclerViewAdapter(folderList, optionsMenuClickListener)
        folderListRecyclerView.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = folderListRecyclerViewAdapter
        }
    }


    override fun onDestroy() {
        unregisterForContextMenu(binding.folderListRecyclerView)
        _binding = null
        super.onDestroy()
    }
}