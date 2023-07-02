package com.example.sonder_dating_app.ui.fragments.likes

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderMatchesFragmentBinding
import com.example.sonder_dating_app.presentation.events.MatchesEvent.OnInitialize
import com.example.sonder_dating_app.presentation.events.MatchesEvent.OnMatchUserClicked
import com.example.sonder_dating_app.presentation.view_models.MatchesViewModel
import com.example.sonder_dating_app.presentation.view_models.UserProfileViewModel
import com.example.sonder_dating_app.ui.adapter.MatchesAdapter
import com.example.sonder_dating_app.ui.fragments.BaseFragment
import com.example.sonder_dating_app.presentation.view_models.BaseViewModel.Companion.state as CommonState

internal class MatchesFragment : BaseFragment(R.layout.sonder_matches_fragment) {

    private val binding by viewBinding(SonderMatchesFragmentBinding::bind)

    private val matchesViewModel: MatchesViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    private lateinit var matchesAdapter: MatchesAdapter

    override fun initFragment() {
        initUi()
        initSubscribe()
        CommonState.value?.user?.uid?.let { dispatchEvent(OnInitialize(it)) }
    }

    private fun initUi() {
        matchesAdapter = MatchesAdapter(requireContext(), viewLifecycleOwner, userProfileViewModel) {
            user -> dispatchEvent(OnMatchUserClicked(user))
        }
        binding.matchesList.apply {
            adapter = matchesAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun initSubscribe() {
        matchesViewModel.matchesUsers.observe(viewLifecycleOwner) {
            matchesAdapter.updateData(it)
        }
    }
}
