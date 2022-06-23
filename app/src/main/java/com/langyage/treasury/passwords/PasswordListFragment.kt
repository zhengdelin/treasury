package com.langyage.treasury.passwords

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.langyage.treasury.AlertDialogController
import com.example.treasury.R
import com.example.treasury.databinding.FragmentPasswordListBinding
import com.langyage.treasury.folders.FolderController
import com.langyage.treasury.folders.FolderData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAMS = ""
/**
 * A simple [Fragment] subclass.
 * Use the [PasswordListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var folderData: FolderData = FolderData("","")

    private var _binding:FragmentPasswordListBinding ?= null
    private val binding get() = _binding!!

    private lateinit var passwordController: PasswordController
    private lateinit var passwordListRecyclerViewAdapter: PasswordListRecyclerViewAdapter
    private lateinit var recyclerView:RecyclerView
    private var passwordList:ArrayList<PasswordData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        folderData = FolderController(requireContext()).bundleToFolderData(requireArguments())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasswordListBinding.inflate(inflater, container, false)

        val root = binding.root

        //前往新增頁面
        binding.toPasswordCreateBtn.setOnClickListener {
            root.findNavController().navigate(R.id.action_passwordListFragment_to_passwordCreateFragment, arguments)
        }

        passwordController = PasswordController(root.context, activity)
        //recyclerView
        recyclerView = binding.passwordListRecyclerView


        syncPasswordList()

        //cur folder
        FolderController(root.context).setCurrentFolder(folderData, binding.currentFolderText, binding.currentFolderIcon)

        return root
    }

    private fun syncPasswordList(){
        passwordList = passwordController.getPasswords(folderData.id)
        passwordListRecyclerViewAdapter = PasswordListRecyclerViewAdapter(passwordList, optionsMenuClickListener)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = passwordListRecyclerViewAdapter
        }
    }

    //實作點擊彈出上下文菜單監聽器
    private val optionsMenuClickListener = object :
        PasswordListRecyclerViewAdapter.OptionsMenuClickListener {
        override fun onOptionsMenuClicked(position: Int, clickedIcon:ImageView) {
            //popupMenu
            val popupMenu = PopupMenu(context,clickedIcon)
            //填充menu
            popupMenu.menuInflater.inflate(R.menu.password_list_context_menu, popupMenu.menu)
            //設置監聽器
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    return onMenuItemClick(item, position)
                }
            })
            popupMenu.show()
        }
    }
   private fun onMenuItemClick(item:MenuItem?, position:Int):Boolean{
//       Log.i("onMenuItemClick","pos:$position" )
       val data = passwordList[position]
       val folderController = FolderController(binding.root.context)
       return when(item?.itemId){
           R.id.action_edit_password -> {
               val folderBundle:Bundle = folderController.folderDataToBundle(folderData)
               val passwordBundle:Bundle = passwordController.passwordDataToBundle(data)
               val bundle = bundleOf(
                   "folderBundle" to folderBundle,
                   "passwordBundle" to passwordBundle)
               binding.root.findNavController().navigate(R.id.action_passwordListFragment_to_passwordUpdateFragment, bundle)
               true
           }
           R.id.action_delete_password -> {
               AlertDialogController(binding.root.context).showSingleCheckBoxDialog(
                   "確定要刪除密碼",
                   "連同雲端資料一起刪除",
                   "刪除"
               ){
                   passwordController.deletePassword(data, it){
                       syncPasswordList()
                   }
               }
               true
           }
           else -> false
       }
   }


}