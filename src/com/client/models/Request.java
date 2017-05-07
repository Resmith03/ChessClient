package com.client.models;

import java.util.UUID;

public class Request {
    private static Integer counter = 0;
    private String id;
    private MessageType messageType;
    private ContentType contentType;
    private String payload;
    private ResponseType responseType;
    
    public Request(){
	id = UUID.randomUUID().toString();
    }
    
    public Request(ContentType contentType, MessageType messageType, ResponseType responseType, String payload){
	this();
	this.messageType = messageType;
	this.contentType = contentType;
	this.responseType = responseType;
	this.payload = payload;
    }
    
    public String getId() {
        return id;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public void setId(String id) {
        this.id = id;
    }
}
