package com.example.sonder_dating_app.ui.fragments.suggested_profiles

import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderProfilesFragmentBinding
import com.example.sonder_dating_app.presentation.commands.SuggestedProfilesCommand.RemoveFirstUser
import com.example.sonder_dating_app.presentation.commands.SuggestedProfilesCommand.SearchSuggestedUsers
import com.example.sonder_dating_app.presentation.events.SuggestedProfilesEvent.*
import com.example.sonder_dating_app.presentation.view_models.SuggestedProfilesViewModel
import com.example.sonder_dating_app.presentation.view_models.UserProfileViewModel
import com.example.sonder_dating_app.ui.adapter.SuggestedProfilesAdapter
import com.example.sonder_dating_app.ui.fragments.BaseFragment
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import com.example.sonder_dating_app.presentation.events.UserProfileEvent.OnInitialize as OnInitializeUser
import com.example.sonder_dating_app.presentation.view_models.BaseViewModel.Companion.state as CommonState

@AndroidEntryPoint
internal class SuggestedProfilesFragment : BaseFragment(R.layout.sonder_profiles_fragment) {

    private val binding by viewBinding(SonderProfilesFragmentBinding::bind)
    private val suggestedProfilesViewModel: SuggestedProfilesViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    private var suggestedProfilesAdapter: SuggestedProfilesAdapter? = null
    private var stackCardManager: CardStackLayoutManager? = null

    override fun initFragment() {
        initUi()
        initSubscribe()
        CommonState.value?.user?.uid?.let { dispatchEvent(OnInitializeUser(it)) }
        suggestedProfilesViewModel.dispatchCommand(SearchSuggestedUsers)
    }

    private fun initSubscribe() {
        suggestedProfilesViewModel.suggestedUsers.observe(viewLifecycleOwner) {
            if (it.size == 0) {
                suggestedProfilesViewModel.dispatchCommand(SearchSuggestedUsers)
            }
            suggestedProfilesAdapter?.setItems(it)
        }
    }

    private fun initUi() {
        suggestedProfilesAdapter = SuggestedProfilesAdapter(
                requireContext(),
                viewLifecycleOwner,
                userProfileViewModel
        )
        setUpStackCardManager()
        binding.profiles.adapter = suggestedProfilesAdapter
        binding.profiles.layoutManager = stackCardManager
        binding.likeButton.setOnClickListener {
            if (suggestedProfilesAdapter?.itemCount != 0) swipeCard(Direction.Right)
        }

        binding.dislikeButton.setOnClickListener {
            if (suggestedProfilesAdapter?.itemCount != 0) swipeCard(Direction.Left)
        }
        binding.suggestedProfilesSettings.setOnClickListener {
            dispatchEvent(OnSettingsClicked)
        }
    }

    private fun setUpStackCardManager() {
        stackCardManager = CardStackLayoutManager(requireContext(), object: CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {}
            override fun onCardSwiped(direction: Direction?) {
                cardSwiped(direction)
            }
            override fun onCardRewound() { }
            override fun onCardCanceled() { }
            override fun onCardAppeared(view: View?, position: Int) { }
            override fun onCardDisappeared(view: View?, position: Int) { }
        })
        stackCardManager!!.setStackFrom(StackFrom.Top)
        stackCardManager!!.setVisibleCount(3)
        stackCardManager!!.setTranslationInterval(8.0f)
    }

    private fun swipeCard(direction: Direction) {
        val swipeAnimationSetting = SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(200)
            .setInterpolator(AccelerateInterpolator())
            .build()
        stackCardManager!!.setSwipeAnimationSetting(swipeAnimationSetting)
        binding.profiles.swipe()
    }

    private fun cardSwiped(direction: Direction?) {
        val swipedPosition = stackCardManager!!.topPosition - 1
        val swipedUser = suggestedProfilesAdapter!!.getItem(swipedPosition)
        suggestedProfilesViewModel.dispatchCommand(RemoveFirstUser)
        Log.d("a", "asca swiped $swipedUser")
        when(direction) {
            Direction.Left -> {
                CommonState.value?.user?.uid?.let { fromUid -> swipedUser.uid?.let {
                        toUid -> OnSetDislikeToUserClicked(fromUid, toUid)
                } }?.let { dispatchEvent(it) }
            }
            Direction.Right -> {
                CommonState.value?.user?.uid?.let { fromUid -> swipedUser.uid?.let {
                        toUid -> OnSetLikeToUserClicked(fromUid, toUid)
                } }?.let { dispatchEvent(it) }
            }
            else -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        suggestedProfilesViewModel.clearUsers()
    }
}
