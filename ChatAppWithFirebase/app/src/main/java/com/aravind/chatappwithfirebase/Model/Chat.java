package com.aravind.chatappwithfirebase.Model;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private String voiceMessage;


    public Chat(String sender, String receiver, String message, String voiceMessage) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.voiceMessage = voiceMessage;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getVoiceMessage() {
        return voiceMessage;
    }

    public void setVoiceMessage(String voiceMessage) {
        this.voiceMessage = voiceMessage;
    }


}
