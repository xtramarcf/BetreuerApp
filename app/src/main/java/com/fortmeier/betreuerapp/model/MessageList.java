package com.fortmeier.betreuerapp.model;

import java.util.List;

public class MessageList {

    private List<ChatMessage> messageList;

    public MessageList(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }

    public List<ChatMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<ChatMessage> messageList) {
        this.messageList = messageList;
    }
}
