package com.example.sonder_domain.repository

import com.example.sonder_domain.models.*
import server.DatingServerOuterClass.UploadMediaReply
import server.DatingServerOuterClass.GetReactionsReply
import server.DatingServerOuterClass.SetMessageTokenReply
import server.DatingServerOuterClass.SetReactionReply
import server.DatingServerOuterClass.GetUserReply
import server.DatingServerOuterClass.RegisterUserReply
import server.DatingServerOuterClass.UpdateUserReply
import server.DatingServerOuterClass.SearchUsersReply
import server.DatingServerOuterClass.DownloadMediaReply
import server.DatingServerOuterClass.SendMessageReply
import server.DatingServerOuterClass.GetLastMessagesReply
import server.DatingServerOuterClass.GetChatsReply

interface SonderRepository {
    fun setToken(token: String)

    suspend fun setMessageToken(uid: String, messageToken: String): SetMessageTokenReply?

    suspend fun registerUser(user: User) : RegisterUserReply

    suspend fun updateUser(user: User) : UpdateUserReply

    suspend fun getUser(uid: String): GetUserReply

    suspend fun searchUsers(user: User): SearchUsersReply

    suspend fun setReaction(fromUid: String, toUid: String, reaction: Reaction): SetReactionReply

    suspend fun getReactions(toUid: String): GetReactionsReply

    suspend fun uploadMedia(loadingMedia: LoadingMedia): UploadMediaReply

    suspend fun downloadMedia(media: Media): DownloadMediaReply

    suspend fun sendMessage(message: Message): SendMessageReply

    suspend fun getLastMessagesRequest(fromUid: String, toUid: String): GetLastMessagesReply

    suspend fun getChats(uid: String): GetChatsReply
}
