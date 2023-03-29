package com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentCreateAccountFirstBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentUserProfileFillBinding
import com.example.androidmvvmcleanarchitectureexample.helper.util.InputTextWatcherDelegate
import com.example.androidmvvmcleanarchitectureexample.ui.MainActivity
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.viewmodel.EntryViewModel
import com.example.core.view.BaseMvvmFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFillFragment :
    BaseMvvmFragment<FragmentUserProfileFillBinding, EntryViewModel>(
        R.layout.fragment_user_profile_fill, EntryViewModel::class
    ) {

    private val inputTextWatcherDelegateList: ArrayList<InputTextWatcherDelegate> = ArrayList()

    private val storageRef by lazy {
        Firebase.storage.reference
    }

    private val activityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intentResult = result.data

            val profileImagesRef = storageRef.child("images/${viewModel.userProfileUiData.userNameForPath}.jpg")
            viewModel.loadingEvent.postValue(true)
            profileImagesRef.putFile(intentResult?.data!!).addOnSuccessListener { mTask ->
                mTask.metadata?.reference?.downloadUrl?.addOnSuccessListener {imageUrl ->
                    viewModel.userProfileUiData.avatarUrl.set(imageUrl.toString())
                    Log.d("myTag", " imageUrl.path => ${imageUrl.path} | downloadUrl: $imageUrl in profileImagesRef.putFile(intentResult?.data!!).addOnSuccessListener")
                    Glide.with(requireContext()).load("$imageUrl").into(binding.imgViewProfilePhoto)
                    viewModel.loadingEvent.postValue(false)
                }?.addOnFailureListener {
                    viewModel.loadingEvent.postValue(false)
                }
            }.addOnFailureListener{
                viewModel.loadingEvent.postValue(false)
            }
        }
    }

    override val viewModelFactoryOwner: () -> ViewModelStoreOwner = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_entry)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
        binding.viewmodel = viewModel
    }

    override fun performOnViewCreatedOnlyOnce() {
        viewModel.clearViewData()
    }

    override fun handleUiActionEvent(action: String?) {
        when (action) {
            EntryViewModel.GO_TO_DASHBOARD -> {
                activity?.apply {
                    finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
            }
        }
    }

    private fun initClickListener() {
        with(binding) {
            imgViewProfilePhoto.setOnClickListener {
                activityForResult.launch(getFileChooserIntentForImage())
            }
            navigationBackBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSkip.setOnClickListener {
                activity?.apply {
                    finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
            }
            btnContinue.setOnClickListener {
                viewModel.updateUserProfile()
            }
            inputTextWatcherDelegateList.clear()
            listOf(
                eTvFirstName,
                eTvLastName,
                eTvUserName,
                eTvEmail,
                eTvPhoneNumber,
                eTvGender
            ).forEach {
                val textWatcherDelegate = InputTextWatcherDelegate(
                    inputLayout = it,
                    minTextLength = if (it == eTvPhoneNumber) 7 else 2,
                    maxTextLength = if(it == eTvPhoneNumber) 7 else 50,
                    afterTextChangedBlock = { value, isValid ->
                        btnContinue.isEnabled = true
                        inputTextWatcherDelegateList.forEach { textWatcherDelegate ->
                            if (textWatcherDelegate.isValid().not()) {
                                btnContinue.isEnabled = false
                                return@forEach
                            }
                        }
                    },
                    mask = if(it == eTvPhoneNumber) "### ## ##" else null,
                    maskDecoderBlock = if (it == eTvPhoneNumber) { value ->
                        val data = value.replace(" ", "")
                        Log.d("InputTextWatcher", "maskDecoderBlock: $data")
                        value.replace(" ", "")
                    } else null,
                    deniedCharacters = if (it == eTvPhoneNumber) arrayListOf("-", "(", ")", "+","#",".","/") else null
                )
                inputTextWatcherDelegateList.add(textWatcherDelegate)
                it.editText?.addTextChangedListener(textWatcherDelegate)
            }
            checkInputsValidationState()
        }
    }

    private fun checkInputsValidationState() {
        var isValid = true
        inputTextWatcherDelegateList.forEach {
            if (it.isValid().not()) {
                isValid = false
                return@forEach
            }
        }
        binding.btnContinue.isEnabled = isValid
    }

    fun getFileChooserIntentForImage(): Intent {
        val STRING_FOR_INTENT_ONLY_IMAGE ="image/*"

        val mimeTypes = arrayOf(STRING_FOR_INTENT_ONLY_IMAGE)
        return Intent(Intent.ACTION_GET_CONTENT)
            .setType("${STRING_FOR_INTENT_ONLY_IMAGE}}")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    }
}