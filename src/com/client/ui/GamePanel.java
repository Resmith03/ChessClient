package com.client.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.client.DTO.BoardSpaceDTO;
import com.client.DTO.GamePieceDTO;
import com.client.app.Client;
import com.client.models.ContentType;
import com.client.models.MessageType;
import com.client.models.Request;
import com.client.models.Response;
import com.client.models.ResponseType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GamePanel extends JPanel{
    private UI ui;
    private Client client;
    private JPanel grid;
    
    private int colCounter = 0;
    private int rowCounter = 0;
    private List<BoardSpaceDTO> spaces;
    
    private BoardSpaceDTO getBoardSpace(int x, int y){
	BoardSpaceDTO dto = new BoardSpaceDTO();
	
	if(spaces != null){
	for(BoardSpaceDTO space: spaces){
	    if(space.getX() == x && space.getY() == y){
		dto = space;
		break;
	    }
	}
	}
	return dto;
    }
    
    private String getImgUrl(int x, int y){
	String url = "";
	
	BoardSpaceDTO space = getBoardSpace(x, y);
	if(space != null){
	    GamePieceDTO piece = space.getGamePiece();
	    if(piece != null){
		url = getImageBySymbol(piece.getSymbol());
	    }
	}
	return url;
    }
    
    private String getImageBySymbol(String symbol){
	
	String url = "";
	if(symbol != null){
	switch(symbol){
	case "R": url = "rook_white.png";
	break;
	case "r": url = "rook.png";
	break;
	case "N": url = "knight_white.png";
	break;
	case "n": url = "knight.png";
	break;
	case "B": url = "bishop_white.png";
	break;
	case "b": url = "bishop.png";
	break;
	case "P": url = "pawn_white.png";
	break;
	case "p": url = "pawn.png";
	break;
	case "K": url = "king_white.png";
	break;
	case "k": url = "king.png";
	break;
	case "Q": url = "queen_white.png";
	break;
	case "q": url = "queen.png";
	}
	}
	return url;
    }
    private JToggleButton getButton(){
	
	JToggleButton button = new JToggleButton();
	button.addItemListener(new BoardSpaceListener());
	button.setSize(87,87);
	button.setLocation(colCounter * 87, rowCounter * 87);
	
	String imgUrl = getImgUrl(colCounter, rowCounter);
	
	if(imgUrl != null && !"".equals(imgUrl)){
	    button.setIcon(new ImageIcon(getClass().getResource(imgUrl)));
	}
	

	colCounter++;
	
	if((colCounter + rowCounter) % 2 == 0){
	    button.setBackground(Color.BLACK);
	    button.setForeground(Color.white);
	}else{
	    button.setBackground(Color.white);
	}
	
	if(colCounter > 7){
	    colCounter = 0;
	    rowCounter++;
	}
	
	return button;
    }
    public GamePanel(UI ui, Client client){
	spaces = new ArrayList<BoardSpaceDTO>();
	this.client = client;
	this.ui = ui;
	loadBoard();
	addTitle();
	setPanelProperties();
	addBoardGrid();
	
    }
    
    private void loadBoard(){
	
	try{
	    Response response = client.sendRequest(new Request(ContentType.NONE, MessageType.GET, ResponseType.BOARD, ""));
	    List<BoardSpaceDTO> spaces = new ObjectMapper().readValue(response.getPayload(), new TypeReference<List<BoardSpaceDTO>>(){});
	    
	    if(spaces != null && spaces.size() > 0){
		this.spaces = spaces;
	    }
	}catch(Exception ex){
	    ex.printStackTrace();
	}
    }
    
    private void addTitle(){
	JLabel label = new JLabel("Frostburg State University: Chess");
	label.setSize(300,20);
	label.setLocation(50,10);
	add(label);
    }
    
    private void setPanelProperties(){
	setOpaque(true);
	setBackground(Color.WHITE);
	setLayout(null);
	setSize(800,800);
    }
    
    private void addBoardGrid(){
	grid = new JPanel(new GridLayout(8,8));
	grid.setSize(696,696);
	grid.setLocation(50,50);
	grid.setBackground(Color.LIGHT_GRAY);
	grid.setOpaque(true);
	
	for(int i = 0; i < 64; i++){
	    grid.add(getButton());
	}
	
	add(grid);
    }
    
    private List<JToggleButton> selected = new ArrayList<JToggleButton>();
    private boolean clearing = false;
    
    private class BoardSpaceListener implements ItemListener{

	@Override
	public void itemStateChanged(ItemEvent ev) {
	    if(clearing){
		return;
	    }
	    if(ev.getStateChange()==ItemEvent.SELECTED){
		System.out.println("Adding Selected Button");
	        selected.add((JToggleButton) ev.getSource());
	        
	        if(selected.size() > 1){
	            clearing = true;
	            
	            for(JToggleButton button:selected){
	        	button.setSelected(false);
	            }
	            
	            //TODO: process move and update board
	            selected.get(1).setIcon(selected.get(0).getIcon());
	            selected.get(0).setIcon(null);
	            selected.clear();
	            clearing = false;
	        }
	      } else if(ev.getStateChange()==ItemEvent.DESELECTED){
	         selected.remove((JToggleButton)ev.getSource());
	      }
	}
	
    }
}
