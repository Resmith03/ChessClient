package com.client.DTO;

public class LocationDTO {

    private int x;
    private int y;

    public LocationDTO() {
    }

    public LocationDTO(int x, int y) {
	this.x = x;
	this.y = y;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public int[] toArray() {
	int[] array = { this.x, this.y };
	return array;
    }

}
