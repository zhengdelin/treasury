package com.langyage.treasury.folders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.treasury.databinding.FragmentFolderUpdateBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FolderUpdateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FolderUpdateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var folderData: FolderData = FolderData("","")

    private var _binding:FragmentFolderUpdateBinding ?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        folderData = FolderController(requireContext()).bundleToFolderData(requireArguments())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFolderUpdateBinding.inflate(inflater,container,false)
        val root = binding.root

        val folderController = FolderController(root.context)
        val name = binding.folderNameInput
        val color = binding.colorSelector
        //初始化資料
        name.setText(folderData.name)
        color.setColor(folderData.color)
        //現在資料夾顯示
        folderController.setCurrentFolder(folderData, binding.currentFolderText, binding.currentFolderIcon)

        binding.folderActionBtn.setOnClickListener {
            val nameText = name.text.toString()
            val colorText = color.getCurColor()
            if(nameText.isBlank() || colorText.isBlank()){
                Toast.makeText(root.context, "請輸入完整資訊", Toast.LENGTH_SHORT).show()
            }else{
                val flag = folderController.updateFolder(
                    FolderData(nameText, colorText, folderData.id)
                )
                if(flag)
                    root.findNavController().navigateUp()
            }
        }
        return root
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment FolderUpdateFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            FolderUpdateFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}