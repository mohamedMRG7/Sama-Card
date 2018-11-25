package com.dev.mohamed.samacard.chat;

public class Chat {

    private String sender;
    private String reciver;
    private String meassage;
    private boolean seen;
    private String messageId;
    private String dateAndTime;

    public Chat() {
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public Chat(String sender, String reciver, String meassage, boolean seen, String messageId, String dateAndTime) {
        this.sender = sender;
        this.reciver = reciver;
        this.meassage = meassage;
        this.seen = seen;
        this.messageId = messageId;
        this.dateAndTime = dateAndTime;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }


    public String getMessageId() {
        return messageId;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getSender() {
        return sender;
    }

    public String getReciver() {
        return reciver;
    }

    public String getMeassage() {
        return meassage;
    }

    public boolean isSeen() {
        return seen;
    }
}
