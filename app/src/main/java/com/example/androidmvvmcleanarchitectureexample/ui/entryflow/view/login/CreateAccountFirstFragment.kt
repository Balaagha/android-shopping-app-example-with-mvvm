package com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentCreateAccountFirstBinding
import com.example.androidmvvmcleanarchitectureexample.helper.util.InputTextWatcherDelegate
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.viewmodel.EntryViewModel
import com.example.core.view.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFirstFragment :
    BaseMvvmFragment<FragmentCreateAccountFirstBinding, EntryViewModel>(
        R.layout.fragment_create_account_first, EntryViewModel::class
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
        viewModel.clearViewData()
    }

    private fun initClickListener() {
        with(binding) {
            navigationBackBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            tvSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_global_loginFragment)
            }
            btnContinue.setOnClickListener {
                findNavController().navigate(R.id.action_createAccountFirstFragment_to_createAccountSecondFragment)
            }
            inputTextWatcherDelegateList.clear()
            listOf(
                eTvFirstName,
                eTvLastName,
                eTvUserName
            ).forEach {
                val textWatcherDelegate = InputTextWatcherDelegate(
                    inputLayout = it,
                    minTextLength = 3,
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