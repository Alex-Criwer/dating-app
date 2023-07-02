package com.example.sonder_dating_app.ui

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderNavigationMainActivityBinding
import com.example.sonder_dating_app.firebase.FirebaseAuthProvider
import com.example.sonder_dating_app.presentation.command_handler.DashboardCommandHandler
import com.example.sonder_dating_app.presentation.event_handler.*
import com.example.sonder_dating_app.presentation.view_models.BaseViewModel
import com.example.sonder_dating_app.presentation.view_models.MatchesViewModel
import com.example.sonder_dating_app.presentation.view_models.SuggestedProfilesViewModel
import com.example.sonder_dating_app.presentation.view_models.UserProfileViewModel
import com.example.sonder_dating_app.presentation.view_models.chat.ChatViewModel
import com.example.sonder_dating_app.presentation.view_models.chat.ChatsViewModel
import com.example.sonder_dating_app.presentation.view_models.login.SignUpViewModel
import com.example.sonder_dating_app.service.video_call.VideoCallService
import com.example.sonder_dating_app.ui.fragments.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject


private const val REQUEST_LOCATION_PERMISSION = 1

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.sonder_navigation_main_activity) {

    private val binding by viewBinding(SonderNavigationMainActivityBinding::bind)
    private lateinit var navigationController: NavController

    private lateinit var dashboardEventHandler: DashboardEventHandler
    private lateinit var dashboardCommandHandler: DashboardCommandHandler

    private val signUpViewModel: SignUpViewModel by viewModels()
    private val userProfileViewModel: UserProfileViewModel by viewModels()
    private val suggestedProfilesViewModel: SuggestedProfilesViewModel by viewModels()
    private val matchesViewModel: MatchesViewModel by viewModels()
    private val chatsViewModel: ChatsViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()

    @Inject lateinit var firebaseAuthProvider: FirebaseAuthProvider

    @Inject lateinit var videoCallService: VideoCallService
    private var isVideoServiceStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuthProvider.initActivityContext(this)
        navigationController = findNavController(R.id.fragmentContainerView)
        binding.bottomNavigation.setupWithNavController(navigationController)

        requestLocationPermission()

        initDashboardEventHandler()
        initDashboardCommandHandler()

        lifecycleScope.launchWhenResumed {
            BaseFragment.tasksEventFlow.collect {
                dashboardEventHandler.handle(it)
            }
        }
        lifecycleScope.launchWhenResumed {
            BaseViewModel.commandsFlow.collect {
                dashboardCommandHandler.handle(it)
            }
        }

        observeUserForVideoService()
    }

    //TODO потом сделаем свою кастомную нотификашку
    fun showNotification(notificationMessage: String) {
        Toast.makeText(this, notificationMessage, Toast.LENGTH_LONG).show()
    }

    private fun initDashboardEventHandler() {
        dashboardEventHandler = DashboardEventHandler(
            signUpEventHandler = SignUpEventHandler(this, signUpViewModel, navigationController),
            userProfileEventHandler = UserProfileEventHandler(this,userProfileViewModel),
            suggestedProfilesEventHandler = SuggestedProfilesEventHandler(this, suggestedProfilesViewModel),
            chatsEventHandler = ChatsEventHandler(this, chatsViewModel ,navigationController),
            chatEventHandler = ChatEventHandler(this, chatViewModel, navigationController),
            matchesEventHandler = MatchesEventHandler(matchesViewModel, navigationController)
        )
    }

    private fun initDashboardCommandHandler() {
        dashboardCommandHandler = DashboardCommandHandler(
            signUpViewModel = signUpViewModel,
            userProfileViewModel = userProfileViewModel,
            suggestedProfilesViewModel = suggestedProfilesViewModel,
            matchesViewModel = matchesViewModel,
            chatsViewModel = chatsViewModel,
            chatViewModel = chatViewModel
        )
    }

    fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        videoCallService.stopVideoService()
        isVideoServiceStarted = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    fun requestLocationPermission() {
        val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (!EasyPermissions.hasPermissions(this, *perms)) {
            EasyPermissions.requestPermissions(
                this,
                "Please grant the location permission",
                REQUEST_LOCATION_PERMISSION,
                *perms
            )
        }
    }

    private fun observeUserForVideoService() {
//        userProfileViewModel.user.observe(this) { user ->
//            if (user?.uid != null && user.name.isNotBlank() && !isVideoServiceStarted) {
//                isVideoServiceStarted = true
//                videoCallService.startVideoService(user.uid!!, user.name, application)
//            }
//        }
        signUpViewModel._uid.observe(this) { uid ->
            if (uid != null) {
                isVideoServiceStarted = true
                videoCallService.startVideoService(uid, "You", application)
            }
        }
    }
}
