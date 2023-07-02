package com.example.sonder_domain.repository

import android.util.Log
import com.example.sonder_domain.GrpcService
import com.example.sonder_domain.mapper.*
import com.example.sonder_domain.models.*
import io.grpc.ManagedChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import misc.Misc
import server.DatingServerGrpc
import server.DatingServerOuterClass.SendMessageRequest
import server.DatingServerOuterClass.SendMessageReply
import server.DatingServerOuterClass.DownloadMediaReply
import server.DatingServerOuterClass.DownloadMediaRequest
import server.DatingServerOuterClass.GetReactionsRequest
import server.DatingServerOuterClass.GetReactionsReply
import server.DatingServerOuterClass.SetMessageTokenRequest
import server.DatingServerOuterClass.SetMessageTokenReply
import server.DatingServerOuterClass.SearchUsersRequest
import server.DatingServerOuterClass.SearchUsersReply
import server.DatingServerOuterClass.UpdateUserRequest
import server.DatingServerOuterClass.UpdateUserReply
import server.DatingServerOuterClass.SetReactionRequest
import server.DatingServerOuterClass.SetReactionReply
import server.DatingServerOuterClass.GetUserReply
import server.DatingServerOuterClass.RegisterUserRequest
import server.DatingServerOuterClass.GetUserRequest
import server.DatingServerOuterClass.RegisterUserReply
import server.DatingServerOuterClass.UploadMediaReply
import server.DatingServerOuterClass.UploadMediaRequest
import server.DatingServerOuterClass.GetLastMessagesReply
import server.DatingServerOuterClass.GetLastMessagesRequest
import server.DatingServerOuterClass.GetChatsRequest
import server.DatingServerOuterClass.GetChatsReply
import java.util.concurrent.TimeUnit

class SonderRemoteDataSource(private val grpcService: GrpcService) {
    private lateinit var channel: ManagedChannel
    private val grpcMapper = GrpcMapper()

    private var _token: String = ""

    fun setToken(token: String) {
        Log.d("a", "asca setToken to $token")
        _token = token
    }

    suspend fun setMessageToken(uid: String, messageToken: String): SetMessageTokenReply =
        withContext(Dispatchers.IO) {
            Log.d("a", "asca setMessageToken to $messageToken")
            channel = grpcService.createManagedChannel()
            val stub = DatingServerGrpc.newBlockingStub(channel)
            val request = SetMessageTokenRequest.newBuilder()
                .setUID(uid)
                .setToken(messageToken)
                .setAuth(Misc.TAuth.newBuilder().setToken(_token).build())
                .build()
            val response = stub.setMessageToken(request)
            channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
            return@withContext response
        }

    suspend fun registerUser(user: User): RegisterUserReply = withContext(Dispatchers.IO) {
        channel = grpcService.createManagedChannel()
        val stub = DatingServerGrpc.newBlockingStub(channel)
        val grpcUser = grpcMapper.userToGrpcUser(user)
        val request = RegisterUserRequest.newBuilder()
            .setUser(grpcUser)
            .setAuth(Misc.TAuth.newBuilder().setToken(_token).build())
            .build()
        val response = stub.registerUser(request)
        Log.d("a", "asca register $response")
        channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
        return@withContext response
    }


    suspend fun getUser(uid: String): GetUserReply = withContext(Dispatchers.IO) {
        val channel = grpcService.createManagedChannel()
        val stub = DatingServerGrpc.newBlockingStub(channel)
        val request = GetUserRequest.newBuilder()
            .setUID(uid)
            .setAuth(Misc.TAuth.newBuilder().setToken(_token).build())
            .build()
        val response = stub.getUser(request)
        Log.d("a", "asca get $response")
        channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
        return@withContext response
    }

    suspend fun updateUser(user: User): UpdateUserReply = withContext(Dispatchers.IO) {
        channel = grpcService.createManagedChannel()
        val stub = DatingServerGrpc.newBlockingStub(channel)
        val grpcUser = grpcMapper.userToGrpcUser(user)
        val request = UpdateUserRequest.newBuilder()
            .setUserDelta(grpcUser)
            .setAuth(Misc.TAuth.newBuilder().setToken(_token).build())
            .build()
        val response = stub.updateUser(request)
        channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
        return@withContext response
    }

    suspend fun searchUsers(user: User): SearchUsersReply = withContext(Dispatchers.IO) {
        val channel = grpcService.createManagedChannel()
        val stub = DatingServerGrpc.newBlockingStub(channel)
        val request = SearchUsersRequest.newBuilder()
            .setUID(user.uid)
            .setAuth(Misc.TAuth.newBuilder().setToken(_token).build())
            .setDistance(user.searchDistanceKm.toString())
            .setGeo(grpcMapper.geoToGrpcGeo(user.geo))
            .build()
        val response = stub.searchUsers(request)
        //Log.d("a", "asca search users $response")
        channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
        return@withContext response
    }

    suspend fun setReaction(fromUid: String, toUid: String, reaction: Reaction)
            : SetReactionReply = withContext(Dispatchers.IO) {
        Log.d("a", "asca set reaction $fromUid $toUid $reaction")
        channel = grpcService.createManagedChannel()
        val stub = DatingServerGrpc.newBlockingStub(channel)
        val request = SetReactionRequest.newBuilder()
            .setFromUID(fromUid)
            .setToUID(toUid)
            .setReaction(reaction.toGrpcReaction())
            .setAuth(Misc.TAuth.newBuilder().setToken(_token).build())
            .build()
        val response = stub.setReaction(request)
        Log.d("a", "asca set reaction $response")
        channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
        return@withContext response
    }

    suspend fun getReactions(toUid: String): GetReactionsReply = withContext(Dispatchers.IO) {
        Log.d("a", "asca get reactions to $toUid")
        channel = grpcService.createManagedChannel()
        val stub = DatingServerGrpc.newBlockingStub(channel)
        val request = GetReactionsRequest.newBuilder()
            .setToUID(toUid)
            .setAuth(Misc.TAuth.newBuilder().setToken(_token).build())
            .build()
        val response = stub.getReactions(request)
        Log.d("a", "asca get reactions ${response.reactionsList.size}")
        channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
        return@withContext response
    }

    suspend fun uploadMedia(loadingMedia: LoadingMedia): UploadMediaReply =
        withContext(Dispatchers.IO) {
            Log.d("a", "asca upload media $loadingMedia")
            val channel = grpcService.createManagedChannel()
            val stub = DatingServerGrpc.newBlockingStub(channel)
            val request = UploadMediaRequest.newBuilder()
                .setMedia(loadingMedia.toGrpcLoadingMedia())
                .setAuth(Misc.TAuth.newBuilder().setToken(_token).build())
                .build()
            Log.d("a", "asca upload media request ${request.media.data}")
            val response = stub.uploadMedia(request)
            Log.d("a", "asca upload media response $response")
            channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
            return@withContext response
        }

    suspend fun downloadMedia(media: Media): DownloadMediaReply = withContext(Dispatchers.IO) {
        val channel = grpcService.createManagedChannel()
        val stub = DatingServerGrpc.newBlockingStub(channel)
        val request = DownloadMediaRequest.newBuilder()
            .setMedia(media.toGrpcMedia())
            .setAuth(Misc.TAuth.newBuilder().setToken(_token).build())
            .build()
        val response = stub.downloadMedia(request)
        channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
        return@withContext response
    }

    suspend fun sendMessage(message: Message): SendMessageReply = withContext(Dispatchers.IO) {
        val channel = grpcService.createManagedChannel()
        val stub = DatingServerGrpc.newBlockingStub(channel)
        Log.d("a", "asca sendMessage $message")
        val request = SendMessageRequest.newBuilder()
            .addAllMessages(listOf(message.toGrpcMessage()))
            .setAuth(Misc.TAuth.newBuilder().setToken(_token).build())
            .build()
        val response = stub.sendMessage(request)
        Log.d("a", "asca sendMessage response $response")
        channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
        return@withContext response
    }

    suspend fun getLastMessagesRequest(
        fromUid: String,
        toUid: String
    ): GetLastMessagesReply = withContext(Dispatchers.IO) {
        val channel = grpcService.createManagedChannel()
        val stub = DatingServerGrpc.newBlockingStub(channel)
        Log.d("a", "asca getLastMessages fromUid $fromUid toUid $toUid")
        val request = GetLastMessagesRequest.newBuilder()
            .setFromUID(fromUid)
            .setToUID(toUid)
            .setAuth(Misc.TAuth.newBuilder().setToken(_token).build())
            .build()
        val response = stub.getLastMessages(request)
        Log.d("a", "asca getLastMessages response ${response.messagesList}")
        channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
        return@withContext response
    }

    suspend fun getChats(uid: String): GetChatsReply = withContext(Dispatchers.IO) {
        Log.d("a", "asca getChats for response $uid")
        val channel = grpcService.createManagedChannel()
        val stub = DatingServerGrpc.newBlockingStub(channel)
        val request = GetChatsRequest.newBuilder()
            .setUID(uid)
            .setAuth(Misc.TAuth.newBuilder().setToken(_token).build())
            .build()
        val response = stub.getChats(request)
        Log.d("a", "asca getChats response ${response.chatsList.size}")
        channel.shutdown().awaitTermination(1, TimeUnit.MINUTES)
        return@withContext response
    }
}
