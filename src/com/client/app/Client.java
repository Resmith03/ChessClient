package com.client.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 1321;
    private BufferedReader reader;
    private PrintWriter writer;
    
    private Socket socket;
    
    public boolean isConnected(){
	boolean connected = false;
	
	if(socket != null && socket.isConnected() && !socket.isClosed()){
	    connected = true;
	}
	
	return connected;
    }
    
    public Client() throws UnknownHostException, IOException{
	this.socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
	this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	this.writer = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public void run() {
	try {
	    new Thread(new ConnectionManager(this)).start();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public BufferedReader getReader() {
	return reader;
    }

    public PrintWriter getWriter() {
	return writer;
    }
}
