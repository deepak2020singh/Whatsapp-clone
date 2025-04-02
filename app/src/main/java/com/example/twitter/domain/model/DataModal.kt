package com.example.twitter.domain.model

data class ChatUI(
    val userId: String,
    val image: String,
    val name: String,
    val message: String,
    val time: String,
)

// From real time
data class ChatViewModel(
    val profilePictureUrl: String? = null,
    val userName: String = "",
    val timestamp: String = 0L.toString(),
    val message: String = "",
    val userId: String = "",
    val phoneNumber: String = ""
){
    constructor() : this(null, "", "", "", "", "")
}


data class Message(
    val senderPhone: String,
    val message: String,
    val timestamp: Long = 0L
)

fun provideChatList(): List<ChatUI> {
    return listOf(
        ChatUI(
            userId = "1",
            image = "https://farm9.staticflickr.com/8505/8441256181_4e98d8bff5_z_d.jpg",
            name = "Samson",
            message = "Hi There",
            time = "5:00"
        ),
        ChatUI(
            userId = "2",
            image = "https://farm2.staticflickr.com/1449/24800673529_64272a66ec_z_d.jpg",
            name = "Prem singh",
            message = "Good night",
            time = "10:00"
        ),
        ChatUI(
            userId = "3",
            image = "https://farm3.staticflickr.com/2220/1572613671_7311098b76_z_d.jpg",
            name = "Deepak",
            message = "NOt anyway",
            time = "10:00"
        ),
        ChatUI(
            userId = "4",
            image = "https://farm7.staticflickr.com/6089/6115759179_86316c08ff_z_d.jpg",
            name = "Radha singh",
            message = "Hi There",
            time = "10:00"
        ),
        ChatUI(
            userId = "5",
            image = "https://farm9.staticflickr.com/8505/8441256181_4e98d8bff5_z_d.jpg",
            name = "Security",
            message = "Hi There",
            time = "9:00"
        ),
        ChatUI(
            userId = "6",
            image = "https://farm2.staticflickr.com/1449/24800673529_64272a66ec_z_d.jpg",
            name = "Prem singh",
            message = "Good night",
            time = "5:40"
        ),
        ChatUI(
            userId = "7",
            image = "https://farm9.staticflickr.com/8295/8007075227_dc958c1fe6_z_d.jpg",
            name = "Deepak",
            message = "NOt anyway",
            time = "11:00"
        ),
        ChatUI(
            userId = "8",
            image = "https://farm4.staticflickr.com/3752/9684880330_9b4698f7cb_z_d.jpg",
            name = "Security",
            message = "Hi There",
            time = "6:45"
        ),
        ChatUI(
            userId = "9",
            image = "https://farm2.staticflickr.com/1449/24800673529_64272a66ec_z_d.jpg",
            name = "Prem singh",
            message = "Good night",
            time = "6:10"
        ),
        ChatUI(
            userId = "10",
            image = "https://farm7.staticflickr.com/6089/6115759179_86316c08ff_z_d.jpg",
            name = "Deepak",
            message = "NOt anyway",
            time = "7:00"
        ),


        )
}