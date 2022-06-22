package com.example.treasury.passwords

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.treasury.R
import com.example.treasury.databinding.FragmentPasswordUpdateBinding
import com.example.treasury.folders.FolderController
import com.example.treasury.folders.FolderData

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PasswordUpdateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordUpdateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var folderData:FolderData = FolderData("","")
    private var passwordData:PasswordData = PasswordData("","")

    private var _binding:FragmentPasswordUpdateBinding ?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val folderBundle:Bundle = it.getBundle("folderBundle")!!
            folderData = FolderController(requireContext()).bundleToFolderData(folderBundle)

            val passwordBundle:Bundle = it.getBundle("passwordBundle")!!
            passwordData = PasswordController(requireContext()).bundleToPasswordData(passwordBundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasswordUpdateBinding.inflate(inflater,container, false)

        val root = binding.root
        val passwordController = PasswordController(root.context)

        FolderController(root.context).setCurrentFolder(folderData, binding.currentFolderText, binding.currentFolderIcon)
        binding.currentFolderText.setText("${folderData.name} / ${passwordData.title}")

        val title = binding.passwordTitleInput
        val password = binding.passwordPasswordInput
        val note = binding.passwordNoteInput
        title.setText(passwordData.title)
        password.setText(passwordData.password)
        note.setText(passwordData.note)

        binding.passwordActionBtn.setOnClickListener {
            if(title.text.isBlank() || password.text.isBlank())
                Toast.makeText(root.context, "請輸入完整資訊", Toast.LENGTH_SHORT).show()
            else {
                passwordController.updatePassword(
                    PasswordData(
                        title.text.toString(),
                        password.text.toString(),
                        note.text.toString(),
                        passwordData.id
                    )
                )
                root.findNavController().navigateUp()
            }
        }
        // Inflate the layout for this fragment
        return root
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment PasswordUpdateFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            PasswordUpdateFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}