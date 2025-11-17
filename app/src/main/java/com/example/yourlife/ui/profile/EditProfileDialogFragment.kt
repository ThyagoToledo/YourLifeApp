package com.example.yourlife.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.yourlife.R
import com.example.yourlife.databinding.DialogEditProfileBinding

class EditProfileDialogFragment : DialogFragment() {

    private var _binding: DialogEditProfileBinding? = null
    private val binding get() = _binding!!

    private var onSave: ((name: String, bio: String, avatarUrl: String, coverUrl: String, interests: String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_YourLife_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { dismiss() }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
                    val name = binding.etName.text.toString()
                    val bio = binding.etBio.text.toString()
                    val avatarUrl = binding.etAvatarUrl.text.toString()
                    val coverUrl = binding.etCoverUrl.text.toString()
                    val interests = binding.etInterests.text.toString()
                    onSave?.invoke(name, bio, avatarUrl, coverUrl, interests)
                    dismiss()
                    true
                }
                else -> false
            }
        }
    }

    fun setOnSaveClickListener(listener: (name: String, bio: String, avatarUrl: String, coverUrl: String, interests: String) -> Unit) {
        onSave = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
