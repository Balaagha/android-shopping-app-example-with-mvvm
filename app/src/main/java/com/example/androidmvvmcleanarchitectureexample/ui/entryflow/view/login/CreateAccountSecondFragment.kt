package com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentCreateAccountFirstBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentCreateAccountSecondBinding
import com.example.androidmvvmcleanarchitectureexample.helper.util.InputTextWatcherDelegate
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.viewmodel.EntryViewModel
import com.example.core.view.BaseMvvmFragment
import com.example.data.helper.manager.SharedPrefsManager
import com.example.data.helper.models.typedefs.SharedTypes
import com.example.data.utils.SharedConstant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountSecondFragment :
    BaseMvvmFragment<FragmentCreateAccountSecondBinding, EntryViewModel>(
        R.layout.fragment_create_account_second, EntryViewModel::class
    ) {

    private val inputTextWatcherDelegateList: ArrayList<InputTextWatcherDelegate> = ArrayList()
    private val sharedPrefsManager by lazy {
        SharedPrefsManager(context, SharedTypes.USER_DATA)
    }

    override val viewModelFactoryOwner: () -> ViewModelStoreOwner = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_entry)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
        binding.viewmodel = viewModel
    }

    override fun handleUiActionEvent(action: String?) {
        when (action) {
            EntryViewModel.GO_TO_LOGIN -> {
                Log.d("myTag","GO_TO_LOGIN in lower")
                findNavController().navigate(R.id.action_global_loginFragment)
            }
        }
    }

    private fun initClickListener() {
        with(binding) {
            navigationBackBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            tvSignIn.setOnClickListener {
                findNavController().navigate(R.id.action_global_loginFragment)
            }
            inputTextWatcherDelegateList.clear()
            btnContinue.setOnClickListener {
                viewModel.createUser()
            }
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