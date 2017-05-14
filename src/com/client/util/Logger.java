package com.client.util;

public class Logger {
    public synchronized static void info(String message) {
	System.out.println(message);
    }
}
