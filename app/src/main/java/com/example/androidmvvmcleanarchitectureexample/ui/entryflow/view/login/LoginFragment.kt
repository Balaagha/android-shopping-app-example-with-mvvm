package com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentLoginBinding
import com.example.androidmvvmcleanarchitectureexample.helper.util.InputTextWatcherDelegate
import com.example.androidmvvmcleanarchitectureexample.ui.MainActivity
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.viewmodel.EntryViewModel
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.viewmodel.EntryViewModel.Companion.GO_TO_DASHBOARD
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.viewmodel.EntryViewModel.Companion.GO_TO_PROFILE_SCREEN
import com.example.core.view.BaseMvvmFragment
import com.example.data.helper.manager.SharedPrefsManager
import com.example.data.helper.models.typedefs.SharedTypes
import com.example.data.utils.SharedConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseMvvmFragment<FragmentLoginBinding, EntryViewModel>(
    R.layout.fragment_login, EntryViewModel::class
) {

    private val inputTextWatcherDelegateList: ArrayList<InputTextWatcherDelegate> = ArrayList()

    override val viewModelFactoryOwner: () -> ViewModelStoreOwner = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_entry)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
        binding.viewmodel = viewModel
    }

    override fun performOnViewCreatedOnlyOnce() {
        setViewData()
    }

    override fun handleUiActionEvent(action: String?) {
        when (action) {
            GO_TO_PROFILE_SCREEN -> {
                findNavController().navigate(R.id.action_global_userProfileFillFragment)
            }
            GO_TO_DASHBOARD -> {
                Log.d("myTag","GO_TO_DASHBOARD in lower")
                activity?.apply {
                    finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
            }
        }
    }

    private fun setViewData() {
        viewModel.clearViewData()
        val sharedPrefsManager = SharedPrefsManager(context, SharedTypes.USER_DATA)
        viewModel.loginUserUiData.email.set(sharedPrefsManager.get(SharedConstant.USER_EMAIL, "",true))
        viewModel.loginUserUiData.password.set(
            sharedPrefsManager.get(
                SharedConstant.USER_PASSWORD,
                "",
                true
            )
        )
    }

    private fun initClickListener() {
        with(binding) {
            tvSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_global_createAccountFirstFragment)
            }
            navigationBackBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            btnContinue.setOnClickListener {
                viewModel.loginUser()
            }
            inputTextWatcherDelegateList.clear()
            listOf(
                eTvEmail,
                eTvPassword
            ).forEach {
                val textWatcherDelegate = InputTextWatcherDelegate(
                    inputLayout = it,
                    minTextLength = 8,
                    maxTextLength = 50,
                    afterTextChangedBlock = { value, isValid ->
                        btnContinue.isEnabled = true
                        inputTextWatcherDelegateList.forEach { textWatcherDelegate ->
                            if (textWatcherDelegate.isValid().not()) {
                                btnContinue.isEnabled = false
                                return@forEach
                            }
                        }
                    }
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

}