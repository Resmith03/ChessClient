package com.client.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.client.models.ContentType;
import com.client.models.MessageType;
import com.client.models.Request;
import com.client.models.Response;
import com.client.util.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
    private final String SERVER_ADDRESS = "127.0.0.1";
    private final Integer SERVER_PORT = 1321;

    private SocketReader reader;
    private SocketWriter writer;
    private Socket socket;
    private RequestHandler requestHandler;
    private boolean connected;
    
    public Client() throws UnknownHostException, IOException {
	socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
	writer = new SocketWriter(socket.getOutputStream());
	reader = new SocketReader(socket.getInputStream());
	requestHandler = new RequestHandler();
	connected = true;
	new Thread(writer).start();
	new Thread(reader).start();
	new Thread(requestHandler).start();
    }    
    
    private class SocketWriter implements Runnable {
	private Integer MAX_ATTEMPTS = 10;
	private Integer REQUEST_PAUSE = 500;
	private PrintWriter writer;
	private volatile List<Request> requests;
	private volatile List<Response> responses;
	
	private ObjectMapper mapper;
	
	public SocketWriter(OutputStream outputStream){
	    writer = new PrintWriter(outputStream, true);
	    mapper = new ObjectMapper();
	    requests = new ArrayList<Request>();
	    responses = new ArrayList<Response>();
	}
	
	private void sendRequests() throws JsonProcessingException{
	    for(Request request:new ArrayList<Request>(requests)){
		writer.println(mapper.writeValueAsString(request));
		requests.remove(request);
	    }
	}
	
	private void sendResponses() throws JsonProcessingException{
	    for(Response response:new ArrayList<Response>(responses)){
		writer.println(mapper.writeValueAsString(response));
		responses.remove(response);
	    }
	}
	
	@Override
	public void run() {
	    while(connected){
		try {
		    sendRequests();
		    sendResponses();
		    Thread.sleep(500);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}		
	    }
	}
	
	public Response sendRequest(Request request){
	    Response response = null;
	    String requestId = request.getId();
	    
	    requests.add(request);

	    Integer attempts = 0;
	    while(response == null && attempts < 20){
		attempts++;
	   	response = reader.getResponseByRequestId(requestId);
	   	
	   	try {
		    Thread.sleep(1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	    
	    return response;
	}
	
	public void sendResponse(Response response){
	    responses.add(response);
	}
    }
    
    private class SocketReader implements Runnable {
	private BufferedReader reader;
	private List<Request> requests;
	private List<Response> responses;
	private ObjectMapper mapper;
	
	public SocketReader(InputStream inputStream) {
	    reader = new BufferedReader(new InputStreamReader(inputStream));
	    mapper = new ObjectMapper();
	    requests = new ArrayList<Request>();
	    responses = new ArrayList<Response>();
	}
	
	public Response getResponseByRequestId(String requestId) {
	    Response response = null;
	    
	    for(Response curResponse:new ArrayList<Response>(responses)){
		if(curResponse.getRequestId().equals(requestId)){
		    response = curResponse;
		    responses.remove(response);
		}
	    }
	    
	    return response;
	}

	private void sleep(){
	    try {
		Thread.sleep(10);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	@Override
	public void run() {
	    while (connected) {
		try {
		    String line = reader.readLine();
		    
		    try{
			Request request = mapper.readValue(line, Request.class);
			requests.add(request);
			Logger.info("Got Request: " + request.getId());
		    }catch(Exception ex){
			//TODO: find better way
		    }
		    
		    try{
			Response response = mapper.readValue(line, Response.class);
			Logger.info("Got response: " + response.getId() + " : " + response.getPayload());
			responses.add(response);
		    }catch(Exception ex){
			//TODO: find better way
		    }
		    
		    sleep();
		    
		} catch (IOException e) {
		    e.printStackTrace();
		    connected = false;
		} 
	    }
	}
	
	public Request getRequest(){
	    Request request = null;
	    
	    if(requests.size() > 0){
		request = requests.get(0);
		requests.remove(request);
	    }
	    
	    return request;
	}
    }   
    
    private class RequestHandler implements Runnable{
	public RequestHandler(){}
	
	@Override
	public void run() {
	    while(connected){
		Request request = reader.getRequest();
		
		if(request != null){
		    writer.sendResponse(process(request));
		}
		
		sleep();
	    }
	}


	private void sleep() {
	    try {
		Thread.sleep(100);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}


	private Response process(Request request) {
	    Response response = null;
	    
	    MessageType type = request.getMessageType();
	    
	    switch(type){
	    	case PING:
	    	    response = processPingRequest(request);
	    	    break;
	    	case GET:
	    	    response = processGetRequest(request);
	    	    break;
	    	case POST:
	    	    response = processPostRequest(request);
	    	    break;
	    	case INFO:
	    	    response = processInfoRequest(request);
	    	    break;
	    }
	    
	    return response;
	}


	private Response processInfoRequest(Request request) {
	    // TODO Auto-generated method stub
	    return null;
	}


	private Response processPostRequest(Request request) {
	    // TODO Auto-generated method stub
	    return null;
	}


	private Response processGetRequest(Request request) {
	    // TODO Auto-generated method stub
	    return null;
	}

	private Response processPingRequest(Request request) {
	    return new Response(request.getId(), ContentType.NONE, MessageType.PING, "ACTIVE");
	}
	
    }

    public Response sendRequest(Request request) {
	return writer.sendRequest(request);
    }

}
