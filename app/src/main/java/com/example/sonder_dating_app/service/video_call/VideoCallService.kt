package com.example.sonder_dating_app.service.video_call

import android.app.Application
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService


private const val REQUEST_CAMERA_PERMISSION = 1

// there is application context
class VideoCallService {

    fun startVideoService(userID: String, userName: String, application: Application) {
        val appID: Long = 377029645  // yourAppID
        val appSign = "ae63ee25d81dcabb098f0127f3c05fda59282550a800306ce917f9287a0d9781"

        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()
        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true
        val notificationConfig = ZegoNotificationConfig()
        notificationConfig.sound = "zego_uikit_sound_call"
        notificationConfig.channelID = "CallInvitation"
        notificationConfig.channelName = "CallInvitation"
        ZegoUIKitPrebuiltCallInvitationService.init(application, appID, appSign, userID, userName, callInvitationConfig)
    }

    fun stopVideoService() {
        ZegoUIKitPrebuiltCallInvitationService.unInit()
    }
}
