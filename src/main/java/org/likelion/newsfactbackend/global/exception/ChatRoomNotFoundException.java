package org.likelion.newsfactbackend.global.exception;

public class ChatRoomNotFoundException extends NullPointerException{
    public ChatRoomNotFoundException(){
        super("chat room not found.");
    }
    public ChatRoomNotFoundException(String msg){
        super(msg);
    }
}
