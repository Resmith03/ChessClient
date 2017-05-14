package com.client.models.transport;

import java.util.UUID;

public class Request {

    private String id;
    private MessageType messageType;
    private ContentType contentType;
    private String payload;
    private ResponseType responseType;
    private String ipAddress;
    
    public Request() {
	id = UUID.randomUUID().toString();
    }

    public Request(String ipAddress, ContentType contentType, MessageType messageType, ResponseType responseType, String payload) {
	this();
	this.ipAddress = ipAddress;
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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
}
