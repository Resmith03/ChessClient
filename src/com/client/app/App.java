package com.client.app;

import java.io.IOException;

public class App {
    public static void main (String args[]){
	try {
	    new Thread(new Client()).start();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
