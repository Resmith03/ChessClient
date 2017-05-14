package com.client.models.player;

import java.util.UUID;

public class Player {
    private String username;
    private Color color;
    private String id;

    private Player() {
	id = UUID.randomUUID().toString();
    }

    public Player(String username) {
	this();
	this.username = username;
    }

    public Player(String username, Color color) {
	this(username);
	this.color = color;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public Color getColor() {
	return color;
    }

    public void setColor(Color color) {
	this.color = color;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    @Override
    public String toString() {
	return "Player " + username;
    }
}
