package com.example.sonder_domain.repository

import android.util.Log
import com.example.sonder_domain.models.*
import server.DatingServerOuterClass.GetLastMessagesReply
import server.DatingServerOuterClass.DownloadMediaReply
import server.DatingServerOuterClass.UploadMediaReply
import server.DatingServerOuterClass.GetReactionsReply
import server.DatingServerOuterClass.SetMessageTokenReply
import server.DatingServerOuterClass.SetReactionReply
import server.DatingServerOuterClass.SearchUsersReply
import server.DatingServerOuterClass.UpdateUserReply
import server.DatingServerOuterClass.GetUserReply
import server.DatingServerOuterClass.RegisterUserReply
import server.DatingServerOuterClass.SendMessageReply
import server.DatingServerOuterClass.GetChatsReply

class SonderRepositoryImpl(
    private val sonderRemoteDataSource: SonderRemoteDataSource,
    private val sonderMessageTokenSharedPref: SonderMessageTokenSharedPref
) : SonderRepository {

    override fun setToken(token: String) {
        sonderRemoteDataSource.setToken(token)
    }

    override suspend fun setMessageToken(uid: String, messageToken: String): SetMessageTokenReply? {
        if (sonderMessageTokenSharedPref.setMessageToken(messageToken)) {
            Log.d("a", "asca setMessageToken first time")
            return sonderRemoteDataSource.setMessageToken(uid, messageToken)
        }
        Log.d("a", "asca setMessageToken not first time")
        return null
    }

    override suspend fun registerUser(user: User): RegisterUserReply {
        return sonderRemoteDataSource.registerUser(user)
    }

    override suspend fun updateUser(user: User): UpdateUserReply {
        return sonderRemoteDataSource.updateUser(user)
    }

    override suspend fun getUser(uid: String): GetUserReply {
        return sonderRemoteDataSource.getUser(uid)
    }

    override suspend fun searchUsers(user: User): SearchUsersReply {
        return sonderRemoteDataSource.searchUsers(user)
    }

    override suspend fun setReaction(fromUid: String, toUid: String, reaction: Reaction): SetReactionReply {
        return sonderRemoteDataSource.setReaction(fromUid, toUid, reaction)
    }

    override suspend fun getReactions(toUid: String): GetReactionsReply {
        return sonderRemoteDataSource.getReactions(toUid)
    }

    override suspend fun uploadMedia(loadingMedia: LoadingMedia): UploadMediaReply {
        return sonderRemoteDataSource.uploadMedia(loadingMedia)
    }

    override suspend fun downloadMedia(media: Media): DownloadMediaReply {
        return sonderRemoteDataSource.downloadMedia(media)
    }

    override suspend fun sendMessage(message: Message): SendMessageReply {
        return sonderRemoteDataSource.sendMessage(message)
    }

    override suspend fun getLastMessagesRequest(fromUid: String, toUid: String): GetLastMessagesReply {
        return sonderRemoteDataSource.getLastMessagesRequest(fromUid, toUid)
    }

    override suspend fun getChats(uid: String): GetChatsReply {
        return sonderRemoteDataSource.getChats(uid)
    }
}
