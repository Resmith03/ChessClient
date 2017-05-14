package com.client.models.board;

import com.client.models.player.Color;

public class GamePiece {
    private Color color;
    private PieceType type;
    private String symbol;
    
    public GamePiece() {
	symbol = "UNKNOWN";
    }

    public GamePiece(Color color) {
	this.color = color;
    }

    public String getSymbol(){
	return symbol;
    }

    public Color getColor() {
	return color;
    }

    public void setColor(Color color) {
	this.color = color;
    }

    public PieceType getType() {
	return type;
    }

    public void setType(PieceType type) {
	this.type = type;
    }
}
