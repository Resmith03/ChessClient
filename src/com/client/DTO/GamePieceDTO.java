package com.client.DTO;

public class GamePieceDTO {
    private String playerColor;
    private String pieceType;
    private String symbol;
    
    public GamePieceDTO(){}
    
    public String getPlayerColor() {
        return playerColor;
    }
    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public String getPieceType() {
        return pieceType;
    }

    public void setPieceType(String pieceType) {
        this.pieceType = pieceType;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
}
