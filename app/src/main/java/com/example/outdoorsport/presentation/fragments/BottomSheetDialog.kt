package com.example.outdoorsport.presentation.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.outdoorsport.R
import com.example.outdoorsport.databinding.BottomSheetDialogBinding
import com.example.outdoorsport.utils.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog : BottomSheetDialogFragment() {
    lateinit var binding: BottomSheetDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDialogBinding.inflate(inflater, container, false)
        val fragmentInstance =
            requireActivity().supportFragmentManager.findFragmentById(R.id.container_main)
        if (fragmentInstance is StartGameFragment) {
            binding.tvInfo.text = getString(R.string.info1)
        } else if (fragmentInstance is GameChoiceFragment) {
            binding.tvInfo.text = getString(R.string.info2)
        }
        binding.close.setOnClickListener {
            dismiss()
        }

        binding.tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("marcinpeg@gmail.com"))
            intent.data = Uri.parse("mailto:")
            try {
                requireActivity().startActivity(
                    Intent.createChooser(
                        intent,
                        "Choose Email Client..."
                    )
                )
            } catch (e: Exception) {
                toast { e.message.toString() }
            }
        }
        return binding.root
    }
}