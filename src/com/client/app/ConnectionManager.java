package com.client.app;

import java.io.IOException;
import java.util.Scanner;

public class ConnectionManager implements Runnable{
    private Client client;
    
    public ConnectionManager(Client client) throws IOException {
	this.client = client;
    }

    @Override
    public void run() {
	new Thread(new SocketReader(client)).start();
	new Thread(new ConsoleManager(client)).start();
    }

    private class SocketReader implements Runnable {
	private Client client;
	
	public SocketReader(Client client){
	    this.client = client;
	}
	
	@Override
	public void run() {
	    String message = null;
	    
	    while (true) {
		try {
		    message = client.getReader().readLine();
		    if(message != null && !"".equals(message)){
			client.getUI().addMessage(message);
			System.out.println(message);
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}

    }
    
    private class ConsoleManager implements Runnable{
	private Client client;
	
	public ConsoleManager(Client client){
	    this.client = client;
	}
	
	@Override
	public void run() {
	    Scanner scanner = new Scanner(System.in);
	    
	    while(true){
		String input = scanner.nextLine();
		client.getWriter().println(input);
	    }
	}
    }
}
