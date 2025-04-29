package com.example.proect23.ui.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.proect23.R
import com.example.proect23.databinding.FragmentProfileBinding
import com.example.proect23.ui.auth.RegisterActivity
import com.example.proect23.util.PrefsManager
import com.google.android.material.snackbar.Snackbar
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    private val PICK_IMAGE_REQUEST = 1001

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Проверка размеров и видимости ImageView
        binding.avatar.post {
            Log.d(
                "ProfileFragment",
                "ImageView size: ${binding.avatar.width}x${binding.avatar.height}, visibility: ${binding.avatar.visibility}"
            )
        }

        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                binding.progress.isVisible = state is ProfileState.Loading
                binding.cardProfile.isVisible = state is ProfileState.Success
                binding.error.isVisible = state is ProfileState.Error

                when (state) {
                    is ProfileState.Success -> {
                        binding.username.text = state.profile.username
                        binding.userId.text = "ID: ${state.profile.id}"

                        state.profile.avatarUrl?.let { url ->
                            val fullAvatarUrl = "http://10.0.2.2:8000${url}?t=${System.currentTimeMillis()}"
                            Log.d("ProfileFragment", "Loading avatar from: $fullAvatarUrl")

                            Glide.with(this@ProfileFragment)
                                .load(fullAvatarUrl)
                                .placeholder(R.drawable.ic_profile)
                                .error(R.drawable.ic_profile)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .circleCrop()
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Drawable>,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        Log.e("Glide", "Failed to load image: ${e?.message}", e)
                                        binding.error.text = "Ошибка загрузки изображения: ${e?.message}"
                                        binding.error.isVisible = true
                                        return false
                                    }

                                    override fun onResourceReady(
                                        resource: Drawable,
                                        model: Any,
                                        target: Target<Drawable>,
                                        dataSource: DataSource,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        Log.d("Glide", "Image loaded successfully")
                                        binding.error.isVisible = false
                                        return false
                                    }
                                })
                                .into(binding.avatar)
                        } ?: run {
                            Log.d("ProfileFragment", "No avatar URL, setting placeholder")
                            binding.avatar.setImageResource(R.drawable.ic_profile)
                        }
                    }

                    is ProfileState.Error -> {
                        binding.error.text = state.message
                        binding.error.isVisible = true
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                    }

                    is ProfileState.Unauthorized -> {
                        // Если токен протух, переходим на регистрацию
                        PrefsManager.clearTokens()
                        val intent = Intent(requireContext(), RegisterActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        requireActivity().finish()
                    }

                    else -> Unit
                }
            }
        }

        binding.avatarContainer.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.avatarOverlay.isVisible = true
                    binding.ivEdit.isVisible = true
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    binding.avatarOverlay.isVisible = false
                    binding.ivEdit.isVisible = false
                    if (event.action == MotionEvent.ACTION_UP) {
                        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "image/*"
                        }
                        startActivityForResult(intent, PICK_IMAGE_REQUEST)
                    }
                    true
                }
                else -> false
            }
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Подтверждение выхода")
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Да") { _, _ ->
                    PrefsManager.clearTokens()
                    val intent = Intent(requireContext(), RegisterActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("Нет", null)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { sourceUri ->
                val mimeType = requireContext().contentResolver.getType(sourceUri)
                if (mimeType !in listOf("image/jpeg", "image/png")) {
                    binding.error.text = "Только изображения JPEG или PNG"
                    binding.error.isVisible = true
                    Snackbar.make(
                        binding.root,
                        "Только изображения JPEG или PNG",
                        Snackbar.LENGTH_LONG
                    ).show()
                    return
                }
                val destUri =
                    Uri.fromFile(File(requireContext().cacheDir, "avatar_cropped.jpg"))
                UCrop.of(sourceUri, destUri)
                    .withAspectRatio(1f, 1f)
                    .withMaxResultSize(500, 500)
                    .start(requireContext(), this)
            }
        }
        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            UCrop.getOutput(data!!)?.let { uploadAvatarToServer(it) }
        }
        if (requestCode == UCrop.RESULT_ERROR) {
            UCrop.getError(data!!)?.printStackTrace()
            binding.error.text = "Ошибка обрезки изображения. Попробуйте снова."
            binding.error.isVisible = true
            Snackbar.make(
                binding.root,
                "Ошибка обрезки. Нажмите для повтора.",
                Snackbar.LENGTH_LONG
            ).setAction("Повторить") {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            }.show()
        }
    }

    private fun uploadAvatarToServer(uri: Uri) {
        val file = File(uri.path ?: return)
        Log.d("ProfileFragment", "Uploading avatar file: ${file.absolutePath}")
        viewModel.uploadAvatar(file)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
