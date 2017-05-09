package com.client.DTO;

public class MoveDTO {
    private LocationDTO from;
    private LocationDTO to;
    
    public MoveDTO(){}
    
    public MoveDTO(LocationDTO from, LocationDTO to){
	this.from = from;
	this.to = to;
    }
    public LocationDTO getFrom() {
        return from;
    }
    public void setFrom(LocationDTO from) {
        this.from = from;
    }
    public LocationDTO getTo() {
        return to;
    }
    public void setTo(LocationDTO to) {
        this.to = to;
    }
    
    
}
