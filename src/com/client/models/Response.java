package com.client.models;

import java.util.UUID;

public class Response {
    private static Integer counter = 0;
    private String id;
    private MessageType messageType;
    private ContentType contentType;
    private String payload;
    private String requestId;
    
    private Response(){
	id = UUID.randomUUID().toString();
    }
    
    private Response(String requestId){
	this();
	this.requestId = requestId;
    }
    
    public Response(String requestId, ContentType contentType, MessageType messageType, String payload){
	this(requestId);
	this.messageType = messageType;
	this.contentType = contentType;
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
}
