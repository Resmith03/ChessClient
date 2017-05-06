package com.client.app;

import java.io.IOException;

import com.client.ui.UI;

public class App {
    public static void main (String args[]){
	try {
	    new Thread(new Client(new UI())).start();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
