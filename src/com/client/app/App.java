package com.client.app;

import com.client.models.transport.Client;
import com.client.ui.UI;

public class App {
    public static void main(String args[]) {
	new UI(new Client());
    }
}
