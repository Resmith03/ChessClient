package com.client.app;

import java.io.IOException;

import com.client.models.transport.Client;
import com.client.ui.UI;

public class App {
    public static void main(String args[]) {
	try {
	    new UI(new Client());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
