package com.example.twitter.presentation.viewModal

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.twitter.domain.model.AuthenticatedUser
import com.example.twitter.domain.model.ChatViewModel
import com.example.twitter.domain.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.ByteArrayInputStream
import java.io.InputStream
import javax.inject.Inject
import kotlin.jvm.java

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _chats = MutableStateFlow<List<ChatViewModel>>(emptyList())
    val chats = _chats.asStateFlow()

    private val _loadingState = MutableStateFlow<Boolean>(false)
    val loadingState = _loadingState.asStateFlow()

    val currentUser = auth.currentUser
    val firebaseDatabase = FirebaseDatabase.getInstance().getReference("users")

    fun searchUserByPhoneNumber(phoneNumber: String) {
        if (currentUser != null) {
            firebaseDatabase.orderByChild("phoneNumber").equalTo(phoneNumber)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val user =
                                snapshot.children.first().getValue(AuthenticatedUser::class.java)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("TAG", "Fetch user failed: ${error.message}")
                    }

                })
        }

    }


    fun getChatForUser(userId: String) {
        val firebaseDatabase = FirebaseDatabase.getInstance().getReference("users/${userId}/chats")
        firebaseDatabase.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatList = mutableListOf<ChatViewModel>()
                    for (child in snapshot.children) {
                        val chat = child.getValue(ChatViewModel::class.java)
                        chat?.let { chatList.add(it) }
                    }
                    _chats.value = chatList
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "Fetch chat failed: ${error.message}")
                }

            })

    }


    private val _chatList = MutableStateFlow<List<ChatViewModel>>(emptyList())
    val chatList = _chatList.asStateFlow()

    init {
        loadChatData()
    }

    private fun loadChatData() {
        val currentUser = auth.currentUser?.uid

        if (currentUser != null) {
            val firebaseDatabase = database.getReference("chats")
            firebaseDatabase.orderByChild("userId").equalTo(currentUser)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val chatList = mutableListOf<ChatViewModel>()
                        for (child in snapshot.children) {
                            val chat = child.getValue(ChatViewModel::class.java)
                            chat?.let { chatList.add(it) }
                        }
                        _chatList.value = chatList
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("TAG", "Fetch chat failed: ${error.message}")
                    }
                }
                )

        }
    }


    fun addChat(newChat: ChatViewModel) {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            val firebaseDatabase = database.getReference("chats")
            val newChatRef = firebaseDatabase.push()
            val newChatWithUser = newChat.copy(userId = currentUserId)
            newChatRef.setValue(newChatWithUser)
                .addOnSuccessListener {
                    Log.d("TAG", "Chat added successfully")
                }
                .addOnFailureListener {
                    Log.d("TAG", "Failed to add chat: ${it.message}")
                }
        } else {
            Log.d("TAG", "User not authenticated")
        }
    }

    private val createRef = database.reference
    fun senderMessage(
        senderPhone: String,
        receiverPhone: String,
        message: String
    ) {
        val messageId = createRef.push().key ?: return
        val message = Message(
            senderPhone = senderPhone,
            message = message,
            timestamp = System.currentTimeMillis()
        )

        createRef.child("messages").child(senderPhone).child(receiverPhone).child(messageId)
            .setValue(message)
        createRef.child("messages").child(receiverPhone).child(senderPhone).child(messageId)
            .setValue(message)
            .addOnSuccessListener {
                Log.d("TAG", "Message sent successfully")
            }
            .addOnFailureListener {
                Log.d("TAG", "Failed to send message: ${it.message}")
            }
    }


    fun getMessage(
        senderPhone: String,
        receiverPhone: String,
        onNewMessages: (Message) -> Unit
    ) {

        val messageRef = createRef.child("messages")
            .child(senderPhone)
            .child(receiverPhone)
        messageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.getValue(Message::class.java)
                if (messages != null) {
                    onNewMessages(messages)
                } else {
                    Log.d("TAG", "No messages found")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    fun fetchLastMessages(
        senderPhone: String,
        receiverPhone: String,
        onLastMessagesFetched: (String, String) -> Unit
    ) {

        val chatRef = FirebaseDatabase.getInstance().reference
            .child("messages")
            .child(senderPhone)
            .child(receiverPhone)

        chatRef.orderByChild("timestamp").limitToLast(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val lastMessage =
                            snapshot.children.firstOrNull()?.child("message")?.value as? String
                        val timestamp =
                            snapshot.children.firstOrNull()?.child("timestamp")?.value as? String
                        onLastMessagesFetched(lastMessage ?: "", timestamp ?: "")
                    } else {
                        onLastMessagesFetched("no messages", "")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onLastMessagesFetched("error", "")
                }
            }
            )
    }

    fun loadChatList(currentUserPhone: String, onChatListLoaded: (List<ChatViewModel>) -> Unit) {
        val chatList = mutableListOf<ChatViewModel>()
        val chatRef = FirebaseDatabase.getInstance().reference
            .child("chats")
            .orderByChild("phoneNumber")

        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (chatSnapshot in snapshot.children) {
                        val chat = chatSnapshot.getValue(ChatViewModel::class.java)
                        val phoneNumber = chat?.phoneNumber
                        val name = chat?.userName
                        val image = chat?.profilePictureUrl
                        val profileImageBitMap = image?.let { decodeBase64ToBitmap(it) }
                        fetchLastMessages(
                            currentUserPhone,
                            phoneNumber ?: "",
                        ) { lastMessage, timestamp ->
                            chatList.add(
                                ChatViewModel(
                                    profilePictureUrl = profileImageBitMap.toString(),
                                    userName = name ?: "",
                                    phoneNumber = phoneNumber ?: "",
                                    message = lastMessage,
                                    timestamp = timestamp
                                )
                            )
                            if (chatList.size == snapshot.childrenCount.toInt()) {
                                onChatListLoaded(chatList)
                            }

                        }
                    }

                } else {
                    onChatListLoaded(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onChatListLoaded(emptyList())
            }

        })


    }

    private fun decodeBase64ToBitmap(base64Image: String): Bitmap? {
        return try {
            val decodedByte = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
        } catch (e: Exception) {
            null
        }


    }

    fun bitmapToString(bitmap64Image: String): Bitmap? {
        return try {
            val decodedByte = android.util.Base64.decode(bitmap64Image, android.util.Base64.DEFAULT)
            val inputStream: InputStream = ByteArrayInputStream(decodedByte)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }

}