package com.client.models.board;

public class BoardSpace {
    private GamePiece gamePiece;
    private Location location;
    public BoardSpace(){}
    public BoardSpace(Location location) {
	this.location = location;
    }

    public GamePiece getGamePiece() {
	return gamePiece;
    }

    public void setGamePiece(GamePiece gamePiece) {
	this.gamePiece = gamePiece;
    }

    public Location getLocation() {
	return location;
    }

    public void setLocation(Location location) {
	this.location = location;
    }
}
