package com.example.treasury.passwords

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.collection.arrayMapOf
import androidx.navigation.findNavController
import com.example.treasury.R
import com.example.treasury.databinding.FragmentPasswordCreateBinding
import com.example.treasury.folders.FolderController
import com.example.treasury.folders.FolderData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = ""

/**
 * A simple [Fragment] subclass.
 * Use the [PasswordCreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordCreateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var folderData: FolderData = FolderData("","")

    private var _binding : FragmentPasswordCreateBinding?=null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        folderData = FolderController(requireContext()).bundleToFolderData(requireArguments())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasswordCreateBinding.inflate(inflater, container, false);
        val root = binding.root

        val passwordController = PasswordController(root.context, activity)
        val title = binding.passwordTitleInput
        val password = binding.passwordPasswordInput
        val note = binding.passwordNoteInput

        binding.passwordActionBtn.setOnClickListener {
            if(title.text.isBlank() || password.text.isBlank())
                Toast.makeText(root.context, "請輸入完整資訊", Toast.LENGTH_SHORT).show()
            else{
                passwordController.addPassword(
                    folderData.id,
                    PasswordData(title.text.toString(),password.text.toString(),note.text.toString())
                ){
                    root.findNavController().navigateUp()
                }
            }

        }

        FolderController(root.context).setCurrentFolder(folderData, binding.currentFolderText, binding.currentFolderIcon)

        return root
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment PasswordCreateFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            PasswordCreateFragment().apply {
//                arguments = Bundle().apply {
////                    putInt(ARG_PARAM1, param1)
//                }
//            }
//    }
}